/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	1 Nov 2007
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

package bangor.aiia.jge.ps;

import bangor.aiia.jge.core.Core;
import bangor.aiia.jge.core.Evaluator;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.Population;

/**
 * The class <code>TextCategorisation</code> represents the specification of
 * a Text Categorisation problem with one dependent variable (x) 
 * and many independent variable (one for each Text Categorisation algorithm).
 * <br>
 * 
 * TODO Class Comments
 * ADD HERE:
 * A. DESCRIPTION OF THIS TEXT CATEGORISATION PROBLEM AND THE PROPOSED SOLUTION
 * B. DESCRIPTION OF THE INDIVIDUALS EVALUATION ALGORITHM
 * 
 * @author 	Loukas Georgiou
 * @version 1.0, 01/11/07
 * @see 	Evaluator
 * @see 	Population
 * @see 	Individual
 * @see 	Core
 * @since 	JavaGE 1.0
 */
public class TextCategorisation implements Evaluator <String, String> {

	// Flag whether a solution has be found or not
	private boolean solutionFound = false;
	
	
	/**
	 * Default Constructor. Should not be used.
	 */
	@SuppressWarnings("unused")
	private TextCategorisation() {
		super();
	}
	
	
	/**
	 * TODO Constructor Comments
	 * ADD JAVADOC COMMENTS HERE
	 */
	public TextCategorisation(String args) {
		// TODO Implement Constructor
		
	}
	
	
	/**
	 * TODO Evaluate Method Comments
	 * ADD JAVADOC COMMENTS HERE
	 */
	public void evaluate(Population<String, String> population) {
		// TODO Implement Evaluate Method

	}

	/**
	 * Returns true if the solution has been found.<br>
	 * TODO SolutionFound Method Comments
	 * WRITE HERE WHEN A SOLUTION HAS BEEN FOUND
	 * 
	 * @return True, if the solution has been found.
	 */
	public boolean solutionFound() {
		return solutionFound;
	}

}
