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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;

import bangor.aiia.jge.core.Core;

/**
 * The class <code>JikesCompiler</code> represents a Java Virtual Machine. 
 * It provides methods for the compilation of Java Source code and the execution 
 * of Java Bytecode (Java Classes) based on the IBM's implementation of the 
 * Java compiler (jikes.exe) and using the Dynamic Class Loading and Introspection 
 * Techniques for the execution of the <code>main</code> method of the compiled 
 * program (Java Class) and the capturing of the standard output of the later 
 * (<code>System.out.print()</code>). 
 * <br><br> 
 * About the Jikes Compiler (http://jikes.sourceforge.net)<br> 
 * Jikes&trade; is a compiler that translates Java&trade; source files as defined in
 * The Java Language Specification into the bytecoded instruction set and binary
 * format defined in The Java Virtual Machine Specification.<br> 
 * Jikes has five advantages that make it a valuable contribution to 
 * the Java community: 
 * <ul>
 *  <li>Open source</li>
 *   <li>Strictly Java compatible</li>
 *   <li>High performance</li>
 *   <li>Dependency analysis</li> 
 *   <li>Constructive Assistance</li> 
 *  </ul>
 *  
 * @author	Loukas Georgiou
 * @version	1.0, 01/04/06
 * @see		JavaCompiler
 * @see		AbstractCompiler
 * @see		Core
 * @since	JavaGE 0.1
 */
public class JikesCompiler extends JavaCompiler {

	/**
	 * The Java Library Class file which is needed by the Jikes compiler
	 * (Option: -bootclasspath path).<br>
	 * It is usually set to [java_home]/lib/classes.zip or
	 * [java_home]/jre/lib/rt.jar.
	 */
	private File jikesBootClassPathFile;

	/**
	 * JikesCompiler Constructor. Creates an instance of the JikesCompiler
	 * class.
	 * 
	 * @param compiler The Jikes Compiler file (jikes.exe).
	 * @param bootclasspath The Java Library Class file which is needed by the Jikes
	 *            			compiler (Option: -bootclasspath path).<br>
	 *            			It is usually set to [java_home]/lib/classes.zip or	[java_home]/jre/lib/rt.jar.
	 */
	public JikesCompiler(File compiler, File bootclasspath) {
		super(compiler, null);
		this.jikesBootClassPathFile = bootclasspath;
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
			ProcessBuilder builder = new ProcessBuilder(
										compiler.getCanonicalPath(), 
										"-bootclasspath", 
										jikesBootClassPathFile.getCanonicalPath(), 
										source.getCanonicalPath());
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
	 * Executes the given Java Class file (Java Program) and returns the
	 * standard output (command line screen) of the execution.<br>
	 * <code>JikesCompiler</code> implements this method using Dynamic Class
	 * Loading for the loading of the class and Introspection for the execution of
	 * its <code>main</code> method. Then it captures the standard output and
	 * returns it.<br>
	 * The Java program must have a
	 * <code>public static void main(String[] args)</code> method which will
	 * be executed and will print in the standard output the result of the
	 * execution.
	 * 
	 * @param file The Java Class (program) file to be executed.
	 * @return If the execution was successful, it returns the data which where
	 *         printed in the standard output during the execution, otherwise
	 *         it returns null.
	 */
	public String execute(File file) {

		StringBuilder result = new StringBuilder(); 	// The standard output of the execution
		
		String className = file.getName().substring(0, file.getName().lastIndexOf('.')); // The name of the Java Class to be executed
		Class<?> myClass = null; 							// The class to be loaded
		
		FileClassLoader loader = new FileClassLoader(file); // The Custom Class Loader
		CapturedOutputStream capturedOutputStream = new CapturedOutputStream(new ByteArrayOutputStream(), result); // The stream used to capture the standard output
		
		PrintStream out = System.out; // The Standard Output Stream		

		// Create an Output Stream to capture the standard ouput (System.out, System.err) of the Java Runtime
		PrintStream capture = new PrintStream(capturedOutputStream);

		try {

			// Load the Java Class File
			myClass = loader.loadClass(className);

			// Create the arguments for the invocation of the main(String[]
			// args) method
			Class<?>[] myArgsType = { (new String[0]).getClass() };

			// Get the main(String[] args) method of the loaded class
			Method myMain = myClass.getMethod("main", myArgsType);

			// Create the arguments for the method invocation
			Object[] myArgs = new String[1];

			// Set a Temporary Output Stream in order to be captured the Standard Output
			System.setOut(capture);			

			// Invoke the main method
			myMain.invoke(null, myArgs);		
			
			// Close the Temporary Output Stream
			capture.close();

			// Check the Result
			if (result.length() == 0) { // Unsuccessful Execution
				result = null;
			}

		} catch (ClassNotFoundException cle) {
			System.out.println(cle.getMessage());
			result = null;
		} catch (NoSuchMethodException nme) {
			System.out.println(nme.getMessage());
			result = null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = null;
		}
		finally {
			// Set back the default standard ouput (System.out) of the Java Runtime
			System.setOut(out);
		}
		
		return (result != null) ? result.toString() : null;

	}

}
