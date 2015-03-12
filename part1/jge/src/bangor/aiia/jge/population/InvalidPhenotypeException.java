/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	11 Feb 2006
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

import bangor.aiia.jge.core.Mapper;

/** 
 * The class <code>InvalidPhenotypeException</code> is a form of 
 * <code>Throwable</code> that indicates an invalid format of a 
 * Phenotype during the mapping process of a <code>Genotype</code>
 * into the corresponding <code>Phenotype</code>.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 05/03/06
 * @see		Mapper
 * @see 	Phenotype
 * @see 	Genotype
 * @since	JavaGE 0.1
 */
public class InvalidPhenotypeException extends Exception {
	
	/**
	 * The Serial Version UID of the <class>InvalidBNFException</code> class.
	 */
	private static final long serialVersionUID = 1471217360869418966L;


	/**
	 * Constructs a new Phenotype exception with a defautl message and cause.
	 */
	public InvalidPhenotypeException() {
		super("An InvalidPhenotypeException occured.");
	}

	
	/**
	 * Constructs a new Phenotype exception with the specified detail 
	 * message and cause.
	 * 
	 * @param message The exception detail and cause message.
	 */
	public InvalidPhenotypeException(String message) {
		super(message);
	}

	
}
