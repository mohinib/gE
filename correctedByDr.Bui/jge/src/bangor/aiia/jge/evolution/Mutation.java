/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	2 April 2006
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

package bangor.aiia.jge.evolution;

import bangor.aiia.jge.core.Core;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.util.MathUtil;

/**
 * The class <code>Mutation</code> is a utility class which provides
 * a collection of Evolutionary Computation Mutation operators.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 08/04/06
 * @see 	Core
 * @since	JavaGE 0.1
 */
public class Mutation {
	
	/**
	 * Default Private Constructor.
	 */
	private Mutation() {
		super();
	}
	
	/**
	 * This method performs the Point Mutation operation in the 
	 * binary genome <code>binaryGenome</code> with probability
	 * <code>mutationProbability</code>.
	 * <br><br>
	 * The mutation operator acts by testing 
	 * each bit locus of a genome and mutating each with a 
	 * pre-specified probability.
	 * <br>
	 * Namely, each bit will be changed (from 0 to 1 or from 1 to 0) 
	 * with the given mutation probability.
	 * 
	 * @param binaryGenome The binary genome.
	 * @param mutationProbability The probability of mutation to occur for each bit of the binary genome (from 0.0 to 1.0).
	 */
	public static void pointMutation(StringBuilder binaryGenome, double mutationProbability) {
	
		char current = '\0';
		
		for (int i = 0; i < binaryGenome.length(); i++) {	
			
			current = binaryGenome.charAt(i);
			
			// Invert the current character
			if (MathUtil.flip(mutationProbability)) {
				binaryGenome.setCharAt(i, MathUtil.invertBinary(current));								
			}			
			
		}	
		
	}
	
	/**
	 * This method performs the Point Mutation operation in the 
	 * binary string genome of the individual <code>individual</code> with probability
	 * <code>mutationProbability</code>.
	 * <br><br>
	 * The mutation operator acts by testing 
	 * each bit locus of a genome and mutating each with a 
	 * pre-specified probability.
	 * <br>
	 * Namely, each bit will be changed (from 0 to 1 or from 1 to 0) 
	 * with the given mutation probability.
	 * 
	 * @see Mutation#pointMutation(StringBuilder, double)
	 * @param <T>  The type of the value of the phenotype of the individual.
	 * @param individual The Individual object.
	 * @param mutationProbability The probability of mutation to occur for each bit of the binary genome (from 0.0 to 1.0).
	 */
	public static <T>void pointMutation(Individual<String, T> individual, double mutationProbability) {
	
		StringBuilder genome = new StringBuilder(individual.getGenotype().value());		
		pointMutation(genome, mutationProbability);
		individual.getGenotype().setValue(genome.toString());		
		
	}	
	

	
	
	

}
