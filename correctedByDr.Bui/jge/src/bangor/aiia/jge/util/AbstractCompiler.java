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

import java.io.File;

import bangor.aiia.jge.core.Core;

/**
 * The abstract class <code>AbstractCompiler</code> abstracts the
 * functionality of both a programming language compiler and runtime.
 * 
 * @author 	Loukas Georgiou
 * @version 1.0, 20/03/06
 * @see 	Core
 * @since 	JavaGE 0.1
 */
public abstract class AbstractCompiler {

	/**
	 * The programming language's compiler executable file.
	 */
	protected File compiler;

	/**
	 * The programming language's runtime executable file.
	 */
	protected File runtime;

	
	/**
	 * Default Constructor. Should not be used.
	 */
	@SuppressWarnings("unused")
	private AbstractCompiler() {

	}

	/**
	 * AbstractCompiler Constructor.
	 * 
	 * @param compiler The programming language's compiler executable file.
	 * @param runtime The programming language's runtime executable file.
	 */
	public AbstractCompiler(File compiler, File runtime) {
		this.compiler = compiler;
		this.runtime = runtime;
	}

	/**
	 * Compiles the given source code and saves file that resulted in the given
	 * location.
	 * 
	 * @param source The source code to be compiled.
	 * @param target The resulted file of the compilation.
	 * @return Returns true if the compilation was successful otherwise it
	 *         returns false.
	 */
	public abstract boolean compile(File source, File target);

	/**
	 * Compiles the given source code and returns the file that results. If the
	 * compilation was unsuccessful then <code>null</code> is returned.
	 * 
	 * @param source The source code to be compiled.
	 * @return Returns the compiled file if the compilation was successful
	 *         otherwise returns null.
	 */
	public abstract File compile(File source);

	/**
	 * Executes the given file and creates a new file with the output of the
	 * execution. If the output file already exists then the output of the
	 * execution will be appended.
	 * 
	 * @param file The file to be executed.
	 * @param output The file where the ouput of the execution will be saved.
	 * @return Returns true if the execution was successful, otherwise returns
	 *         null.
	 */
	public abstract boolean execute(File file, File output);

	/**
	 * Executes the given file and returns the string representation of the
	 * output of the execution.
	 * 
	 * @param file The file to be executed.
	 * @return Returns the string representation of the execution if it was
	 *         successful, otherwise returns null.
	 */
	public abstract String execute(File file);

}