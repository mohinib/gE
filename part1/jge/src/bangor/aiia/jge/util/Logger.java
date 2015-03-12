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

import bangor.aiia.jge.core.Core;

/**
 * The <code>Logger</code> interface abstracts the functionality
 * of a logging object. Logging objects are used for the logging
 * of messages, actions, events, and any other information
 * which must be stored for later processing and use (e.g. reports, statistics, data mining, etc.).
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 18/03/06
 * @see 	Core
 * @since	JavaGE 0.1
 */
public interface Logger {
	
	/**
	 * Stores (logs) the given information for later processing.
	 * If the argument <code>timestamp</code> is true then the date/time stamp
	 * will be logged as well.
	 * 
	 * @param msg The message to be logged / saved.
	 * @param timestamp If true the current date/time stamp will be logged with the message as well.
	 * @return Returns true if the log writting successed otherwise returns false.
	 */
	public boolean write(String msg, boolean timestamp);

}
