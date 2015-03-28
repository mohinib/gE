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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import bangor.aiia.jge.core.Core;

/**
 * The class <code>ConfigurationSettings</code> implements the Singleton
 * pattern and provides an abstraction of the configuration settings of the jGE
 * library.
 * 
 * @author 	Loukas Georgiou
 * @version 1.01, 26/03/06
 * @see 	Core
 * @since 	JavaGE 0.1
 */

public class ConfigurationSettings {

	/**
	 * The ConfigurationSettings instance.
	 */
	private static ConfigurationSettings configuration = null;

	/**
	 * The name of this product.
	 */
	public static final String productName = "jGE";
	
	/**
	 * The version of this product.
	 */
	public static final String productVersion = "1.0";
	
	/**
	 * The root path of the jGE library installation.
	 */
	private String rootPath;

	/**
	 * The working directory path of the jGE library installation.
	 */
	private String workingDirectoryPath;

	/**
	 * The path of the Sun's Java Compiler (javac.exe).
	 */
	private String sunJavaCompiler;

	/**
	 * The path of the Sun's Java Runtime (java.exe).
	 */
	private String sunJavaRuntime;

	/**
	 * The path of the IBM's Jikes Compiler (jikes.exe).
	 */
	private String jikesCompiler;

	/**
	 * The path of the Java Library Class file which is needed by the Jikes
	 * compiler (Option: -bootclasspath path).<br>
	 * It is usually set to [java_home]/lib/classes.zip or
	 * [java_home]/jre/lib/rt.jar.
	 */
	private String jikesBootClassPath;
	
	/**
	 * The logs directory of the application
	 */
	private String logsDirectoryPath;

	
	/**
	 * Private Constructor.
	 */
	private ConfigurationSettings() {

		rootPath = "/Users/Borse/Graduation/Evolutionary_Algorithms/jGE";
		workingDirectoryPath = rootPath + "//jge"; 
		sunJavaCompiler = "C:\\Java\\jdk1.6.0\\bin\\javac.exe"; 
		sunJavaRuntime = "C:\\Java\\jdk1.6.0\\bin\\java.exe"; 
		jikesCompiler = rootPath + "\\xtras\\jikes\\bin\\jikes.exe";
		jikesBootClassPath = "C:\\Java\\jdk1.6.0\\jre\\lib\\rt.jar";
		logsDirectoryPath = rootPath + "\\logs"; 		
		
	}

	/**
	 * The first time a <code>getInstance()</code> method is invoked 
	 * it creates, initialises, and returns a ConfigurationSettings instance. 
	 * In every subsequent invocation it returns
	 * always the reference to the already existing instance (Singleton
	 * pattern).
	 * 
	 * @see ConfigurationSettings#getInstance(File)
	 * @return An instance of the ConfigurationSettings class.
	 */
	public static ConfigurationSettings getInstance() {
		if (configuration == null) {
			configuration = new ConfigurationSettings();
		}
		return configuration;
	}
	
	/**
	 * The first time a <code>getInstance()</code> method is invoked 
	 * it creates, initialises, and returns a ConfigurationSettings instance. 
	 * In every subsequent invocation it returns
	 * always the reference to the already existing instance (Singleton
	 * pattern).
	 * 
	 * @see ConfigurationSettings#getInstance()
	 * @param config The configuration file with the settings.
	 * @return An instance of the ConfigurationSettings class.
	 */
	public static ConfigurationSettings getInstance(File config) {
		
		if (configuration == null) {					

			try {

				Properties prop = new Properties();
				FileInputStream is = new FileInputStream(config);
				prop.loadFromXML(is);
							
				configuration = new ConfigurationSettings();
				configuration.rootPath = null;
				configuration.workingDirectoryPath = null;
				configuration.sunJavaCompiler = null;
				configuration.sunJavaRuntime = null;
				configuration.jikesCompiler = null;
				configuration.jikesBootClassPath = null;
				configuration.logsDirectoryPath = null;
				
				configuration.rootPath = prop.getProperty("rootPath");
				configuration.workingDirectoryPath = prop.getProperty("workingDirectoryPath"); 
				configuration.sunJavaCompiler = prop.getProperty("sunJavaCompiler"); 
				configuration.sunJavaRuntime = prop.getProperty("sunJavaRuntime"); 
				configuration.jikesCompiler = prop.getProperty("jikesCompiler");
				configuration.jikesBootClassPath = prop.getProperty("jikesBootClassPath");
				configuration.logsDirectoryPath = prop.getProperty("logsDirectoryPath"); 	
				
				if (configuration.rootPath == null)
					throw new IOException("Property rootPath not found");
				if (configuration.workingDirectoryPath == null)
					throw new IOException("Property workingDirectoryPath not found");
				if (configuration.sunJavaCompiler == null)
					throw new IOException("Property sunJavaCompiler not found");
				if (configuration.sunJavaRuntime == null)
					throw new IOException("Property sunJavaRuntime not found");
				if (configuration.jikesCompiler == null)
					throw new IOException("Property jikesCompiler not found");
				if (configuration.jikesBootClassPath == null)
					throw new IOException("Property jikesBootClassPath not found");
				if (configuration.logsDirectoryPath == null)
					throw new IOException("Property logsDirectoryPath not found");
				
								
			}
			catch(IOException ioe) {
				throw new RuntimeException("Exception during the Configuration file loading: " + ioe.getMessage());
			}
			
		}
		
		return configuration;
		
	}
	 	 
	/**
	 * This method destroys (sets to null) the current 
	 * global (singleton) configuration instance.<br>
	 * It is used whether a new global configuration instance is needed
	 * in the next invocation of <code>getInstance()</code>
	 * or <code>getInstance(File)</code> method.
	 */
	public static void destroyInstance() {
		configuration = null;
	}	
	
	/**
	 * Prints in the standard output the 
	 * current configuration settings.
	 */
	public static void printSettings() {
		System.out.println("productName: " + ConfigurationSettings.productName);
		System.out.println("productVersion: " + ConfigurationSettings.productVersion);
		System.out.println("rootPath : " + (configuration == null?"null":configuration.rootPath));
		System.out.println("workingDirectoryPath : " + (configuration == null?"null":configuration.workingDirectoryPath));
		System.out.println("sunJavaCompiler      : " + (configuration == null?"null":configuration.sunJavaCompiler));
		System.out.println("sunJavaRuntime       : " + (configuration == null?"null":configuration.sunJavaRuntime));
		System.out.println("jikesCompiler        : " + (configuration == null?"null":configuration.jikesCompiler));
		System.out.println("jikesBootClassPath   : " + (configuration == null?"null":configuration.jikesBootClassPath));
		System.out.println("logsDirectoryPath    : " + (configuration == null?"null":configuration.logsDirectoryPath));		
	}

	/**
	 * Returns the root path of the jGE library installation.
	 * 
	 * @return The root path of the jGE library installation.
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * Returns the working directory path of the jGE library installation.
	 * 
	 * @return The working directory path of the jGE library installation.
	 */
	public String getWorkingDirectoryPath() {
		return workingDirectoryPath;
	}

	/**
	 * Returns the path of the Sun's Java Compiler (javac.exe).
	 * 
	 * @return The path of the Sun's Java Compiler (javac.exe).
	 */
	public String getSunJavaCompiler() {
		return sunJavaCompiler;
	}

	/**
	 * Returns the path of the Sun's Java Runtime (java.exe).
	 * 
	 * @return The path of the Sun's Java Runtime (java.exe).
	 */
	public String getSunJavaRuntime() {
		return sunJavaRuntime;
	}

	/**
	 * Returns the path of the IBM's Jikes Compiler (jikes.exe).
	 * 
	 * @return The path of the IBM's Jikes Compiler (jikes.exe).
	 */
	public String getJikesCompiler() {
		return jikesCompiler;
	}

	/**
	 * Returns the path of the Java Library Class file which is needed by the
	 * Jikes compiler (Option: -bootclasspath path).<br>
	 * It is usually set to [java_home]/lib/classes.zip or
	 * [java_home]/jre/lib/rt.jar.
	 * 
	 * @return The path of the Java Library Class file (classes.zip or rt.jar).
	 */
	public String getJikesBootClassPath() {
		return jikesBootClassPath;
	}
	
	/**
	 * Returns the log files directory path of the jGE library installation.
	 * 
	 * @return The log files directory path of the jGE library installation.
	 */
	public String getLogsDirectoryPath() {
		return logsDirectoryPath;
	}

}
