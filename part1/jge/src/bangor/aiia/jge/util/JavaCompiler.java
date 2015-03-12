/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	14 Jan 2006
 * 
 * Copyright 2006, 2008 Loukas Georgiou.
 * This file is part of JavaGE (jGE) Library.
 * 
 * jGE Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * jGE Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with jGE Library.  If not, see <http://www.gnu.org/licenses/>.
 */ 

package bangor.aiia.jge.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import bangor.aiia.jge.core.Core;

/**
 * The class <code>JavaCompiler</code> represents a Java Virtual Machine. It
 * provides methods for the compilation of Java Source code and the execution of
 * Java Bytecode (Java Classes) based on the Sun's implementation of the Java
 * compiler (javac.exe) and runtime (java.exe). <br>
 * <br>
 * <code>JavaCompiler</code> is a simple implementation of the execution of
 * the <code>javac.exe</code> and <code>java.exe</code> programs without
 * keeping in mind the execution time efficiency.<br>
 * For much better execution time efficieny of the compilation and execution of
 * Java classes use the <code>JikesCompiler</code> class.
 * 
 * @author 	Loukas Georgiou
 * @version 1.0, 24/03/06
 * @see 	AbstractCompiler
 * @see 	JikesCompiler
 * @see 	Core
 * @since 	JavaGE 0.1
 */
public class JavaCompiler extends AbstractCompiler {

	/**
	 * JavaCompiler Constructor.
	 * 
	 * @param compiler The Java Compiler file.
	 * @param runtime The Java Runtime file.
	 */
	public JavaCompiler(File compiler, File runtime) {
		super(compiler, runtime);
	}

	/**
	 * Compiles the given Java Source code and saves the resulting Java Class
	 * file in the given location.
	 * 
	 * @param source The Java Source code to be compiled.
	 * @param target The resulted Java Class file of the compilation.
	 * @return Returns true if the compilation was successful otherwise it
	 *         returns false.
	 */
	public boolean compile(File source, File target) {

		boolean result = false; // If true then the compilation was successful
		File output = null; // The compilation resulting file

		if ((output = compile(source)) != null) { // Successful Compilation

			try {

				result = true;

				// Move the target file if necessary
				if (!target.getCanonicalPath().equals(output.getCanonicalPath())) {
					if (!output.renameTo(target)) {
						result = false;
					}
				}

			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}

		}

		return result;

	}

	/**
	 * Compiles the given Java Source code and returns the resulting Java Class
	 * file. If the compilation was unsuccessful then <code>null</code> is
	 * returned.
	 * 
	 * @param source The Java Source code to be compiled.
	 * @return Returns the compiled Java Class file if the compilation was
	 *         successful otherwise returns null.
	 */
	public File compile(File source) {

		File result = null; 	// The compiled class file
		int exit = -1; 			// The exit status of the compilation
		StringBuilder msg = new StringBuilder(); // The ouput of the compilation
		String line = null;

		try {

			// Create the Process
			ProcessBuilder builder = new ProcessBuilder(compiler.getCanonicalPath(), source.getCanonicalPath());
			builder.redirectErrorStream(true);
			Process child = builder.start();

			// Capture the output
			BufferedReader in = new BufferedReader(new InputStreamReader(child.getInputStream()));
			while ((line = in.readLine()) != null) {
				msg.append(line);
				msg.append("\n");
			}

			// Wait the process to exit
			child.waitFor();

			// Get Process Execution Status. Value 0 indicates normal termination
			exit = child.exitValue();

			if (exit == 0) { 	// Successful Compilation

				// Create the Default Ouput of the Java Compiler
				String targetPath = source.getCanonicalPath().substring(0, source.getCanonicalPath().lastIndexOf('.')) + ".class";
				result = new File(targetPath);

			} else { 			// Unsuccessful Compilation
				result = null;
				//System.out.println(msg);
			}

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		}

		return result;

	}

	/**
	 * Executes the given Java Class file (Java Program) and creates a new text
	 * file with the output of the execution. If the output text file already
	 * exists then the output of the execution will be appended.<br>
	 * The Java Class must have a
	 * <code>public static void main(String[] args)</code> method which will
	 * be executed and will print in the standard output the result of the
	 * execution.
	 * <br>
	 * The output text of the execution will always end with the
	 * line-termination character <code>\n</code> regardless
	 * of whether the original output ends or not with such a character.
	 * If the original output ends already with a line-termination character
	 * then a new one such character will not be appended in the output.
	 * 
	 * @param file The Java Class (program) file to be executed.
	 * @param output The text file where the ouput of the execution will be saved.
	 * @return Returns true if the execution was successful, otherwise returns
	 *         null.
	 */
	public boolean execute(File file, File output) {

		boolean result = false;
		String programOutput = null;

		// Get the program's output and save it to the given text file
		if ((programOutput = execute(file)) != null) {

			result = true;
			FileWriter writer;

			try {
				writer = new FileWriter(output, true);
				writer.append(programOutput + "\n");
				writer.close();
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
				result = false;
			}

		}

		return result;

	}

	/**
	 * Executes the given Java Class file (Java Program) and returns the
	 * standard output (command line screen) of the execution.<br>
	 * The Java Class must have a <code>public static void main(String[] args)</code> 
	 * method which will be executed and will print in the standard output the result of the
	 * execution.
	 * <br>
	 * The output text of the execution will always end with the
	 * line-termination character <code>\n</code> regardless
	 * of whether the original output ends or not with such a character.
	 * If the original output ends already with a line-termination character
	 * then a new one such character will not be appended in the output.
	 * 
	 * @param file The Java Class (program) file to be executed.
	 * @return If the execution was successful, it returns the data which where
	 *         printed in the standard output during the execution , otherwise
	 *         it returns null.
	 */
	public String execute(File file) {

		int exit = -1; 				// The exit status of the compilation
		StringBuilder result = new StringBuilder(); // The standard and error output of the execution
		String classPath = null; 	// The execution classpath
		String className = null; 	// The name of the Java Class to be executed
		String line = null;

		classPath = file.getParent();
		className = file.getName().substring(0, file.getName().lastIndexOf('.'));

		try {

			// Create the Process
			ProcessBuilder builder = new ProcessBuilder(runtime.getCanonicalPath(), "-cp", classPath, className);
			builder.redirectErrorStream(true);
			Process child = builder.start();

			// Capture the output
			BufferedReader in = new BufferedReader(new InputStreamReader(child.getInputStream()));
			while ((line = in.readLine()) != null) {
				result.append(line);
				result.append("\n");
			}			

			// Wait the process to exit
			child.waitFor();

			// Get Process Execution Status. Value 0 indicates normal termination
			exit = child.exitValue();

			if (exit != 0) { 	// Unsuccessful Execution
				result = null;
			}

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		}

		return (result != null) ? result.toString() : null;

	}

}
