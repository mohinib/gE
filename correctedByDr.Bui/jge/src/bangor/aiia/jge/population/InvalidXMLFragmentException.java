/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	24 Feb 2006
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

package bangor.aiia.jge.population;


/** 
 * The class <code>InvalidXMLFragmentException</code> is a form of 
 * <code>Throwable</code> that indicates an invalid format or schema
 * of an XML Fragment. 
 * 
 * @author 	Loukas Georgiou 
 * @version	1.01, 05/03/06
 * @see 	Individual
 * @see 	Population
 * @since	JavaGE 0.1
 */
public class InvalidXMLFragmentException extends Exception {
	
	/**
	 * The Serial Version UID of the <class>InvalidBNFException</code> class.
	 */
	private static final long serialVersionUID = -6716782383713657536L;

	/**
	 * Constructs a new BNF exception with a defautl message and cause.
	 */
	public InvalidXMLFragmentException() {
		super("An InvalidXMLFragementException occured.");
	}

	
	/**
	 * Constructs a new BNF exception with the specified detail 
	 * message and cause.
	 * 
	 * @param message The exception detail and cause message.
	 */
	public InvalidXMLFragmentException(String message) {
		super(message);
	}

	
}

