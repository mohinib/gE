/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:    14 Jan 2006
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
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The class <code>FileClassLoader</code> extends the manner in which the Java
 * virtual machine dynamically loads classes. <br>
 * It adds the functionality of loading Java Classes from the local or network
 * file system by overwriting the method <code>findClass(String name)</code>
 * of its superclass <code>ClassLoader</code>. <br>
 * <br>
 * The <code>FileClassLoader</code> class exploits the delegation model of its <code>ClassLoader</code> superclass 
 * of searching for classes and resources. <br>
 * Namely, each instance of <code>ClassLoader</code> has an associated parent class loader. 
 * When requested to find a class or resource, a ClassLoader instance will delegate 
 * the search for the class or resource to its parent class loader before attempting 
 * to find the class or resource itself. <br>
 * The virtual machine's built-in class loader, called the "bootstrap class loader", 
 * does not itself have a parent but may serve as the parent of a ClassLoader instance.
 *
 * @author 	Loukas Georgiou
 * @version 1.0, 30/03/06  
 * @see 	ClassLoader
 * @since 	JavaGE 0.1
 */
public class FileClassLoader extends ClassLoader {

	/*
	 * The Java Class file to be loaded.
	 */
	File file = null;

	/**
	 * Default Constructor.<br>
	 * Creates a new class loader by searching for the given
	 * Java Class file. The parameter <code>file</code> must be an existing
	 * and valid Java Class file.
	 * 
	 * @param file The Java Class file to be loaded.
	 */
	public FileClassLoader(File file) {
		this.file = file;
	}

	/**
	 * Reads the data of the Java Class file from the local or network file
	 * system and returns a Class object with the specified type (<code>String name</code>).
	 * 
	 * @param name The binary name of the class (class type) which will be loaded.
	 * @return The resulting Class object.
	 * @throws ClassNotFoundException If the class file could not be found or read.
	 */
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] b = loadClassData(file);
		if (b == null)
			throw new ClassNotFoundException("Could not read file " + file.getName());
		return defineClass(name, b, 0, b.length);
	}

	/**
	 * Returns the data of the given file as an array of bytes.
	 * 
	 * @param The file to be read.
	 * @return The data of the file.
	 */
	private byte[] loadClassData(File file) {

		byte[] bytes = null;

		try {

			FileInputStream input = new FileInputStream(file);
			long fileBytes = file.length();

			// Check if the file size is not to long
			if (fileBytes > Integer.MAX_VALUE) {
				throw new IOException("File Size to Long");
			}

			bytes = new byte[(int) fileBytes];

			// Read the bytes from the file and fill the bytes arrary
			int result = input.read(bytes);

			// Check if all the bytes have been read in
			if (result != fileBytes) {
				throw new IOException("Could not completely read file " + file.getName());
			}

			// Close the input stream
			input.close();

		} catch (IOException ioe) {
			bytes = null;
			System.out.println(ioe.getMessage());
		}

		return bytes;

	}

}
