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
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import bangor.aiia.jge.core.Core;

/**
 * The class <code>LogFile</code> implements the <code>Logger</code>
 * interface and represents a text log file of the file system.
 * 
 * @author 	Loukas Georgiou
 * @version 1.0, 20/03/06
 * @see 	Logger
 * @see 	Core
 * @since 	JavaGE 0.1
 */

public class LogFile implements Logger {

	/**
	 * The text file where the log messages will be written.
	 */
	private File logFile;

	/**
	 * Default Constructor. Should not be used
	 */
	@SuppressWarnings("unused")
	private LogFile() {
	}

	/**
	 * LogFile Constructor.
	 * 
	 * @param logFile The text file where the log messages will be written.
	 */
	public LogFile(File logFile) {
		this.logFile = logFile;
	}

	/**
	 * LogFile Constructor.
	 * 
	 * @param path The path of the text file where the log messages 
	 * 			   will be written.
	 */
	public LogFile(String path) {
		this.logFile = new File(path);
	}

	/**
	 * Appends the message in the text file of the LogFile object.
	 * If the argument <code>timestamp</code> is true then the date/time stamp
	 * will be logged as well.
	 * 
	 * @param msg The message to be written in the logfile.
	 * @param timestamp If true the current date/time stamp will be logged with the
	 *            		message as well.
	 * @return Returns true if the log writting successed otherwise returns false.
	 */
	public boolean write(String msg, boolean timestamp) {

		boolean success = false;

		try {
			FileWriter writer = new FileWriter(logFile, true);

			if (timestamp) {
				Date date = Calendar.getInstance().getTime();
				SimpleDateFormat fd = new SimpleDateFormat("#dd/MM/yyyy HH:mm:ss#");
				String text = fd.format(date);
				writer.append(text + "\n");
			}
			writer.append(msg + "\n");
			writer.close();
			success = true;
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			success = false;
		}

		return success;

	}

}
