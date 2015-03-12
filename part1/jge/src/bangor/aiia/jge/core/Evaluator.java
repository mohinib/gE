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

package bangor.aiia.jge.core;

import bangor.aiia.jge.population.Population;

/**
 * The interface <code>Evaluator</code> specifies the required
 * functionality of any object which implements the problem specification
 * of an Evolutionary Computation process and which estimates the fitness
 * of the individuals (<code>Individual</code>) of a population (<code>Population</code>).
 * <br>
 * It is a Generic interface which means that the implementation classes must
 * specify the actual types of the Genotype&lt;T&gt; and the Phenotype&lt;S&gt;
 * of the individuals of the population which will be used by the methods of this interface.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 18/03/06
 * @see 	Core
 * @since	JavaGE 0.1
 * @param 	<T> The type of the value of the Genotype of the individuals of the population
 * @param 	<S> The type of the value of the Phenotype of the individuals of the population
 */
public interface Evaluator <T, S> {
	
	/**
	 * Evaluates the given population and calculates / assigns the fitness
	 * value of/to each individual of the population.
	 * 
	 * @param population The population to be evaluated.
	 */
	public void evaluate(Population<T, S> population);
	
	/**
	 * Returns true if the solution has been found.
	 * 
	 * @return True, if the solution has been found.
	 */
	public boolean solutionFound();

}
