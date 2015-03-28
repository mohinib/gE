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
 * The class <code>Crossover</code> is a utility class which provides
 * a collection of Evolutionary Computation Crossover operators.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 08/04/06
 * @see 	Core
 * @since	JavaGE 0.1
 */
public class Crossover {
	
	/**
	 * Default Private Constructor.
	 */
	private Crossover() {
		super();
	}
	
	
	/**
	 * This method performs the Standard One Point Crossover operation to the
	 * given fixed-length binary genomes of the parents <code>binaryGenomeA</code> and <code>binaryGenomeA</code>
	 * with probability <code>crossoverProbability</code>.<br>
	 * After the execution of the crossover operation the binary genomes of the parents
	 * will be replaced with the binary genomes of the offspring.<br>
	 * Both parent's genomes must have the same length otherwise a RuntimeException will be thrown.
	 * Also, the offspring's genomes will have the same length with the parent's genomes.
	 * <br><br>
	 * The Standard One Point Crossover acts as follows:<br>
	 * <ul>
	 * <li>The Crossover point (same for both fixed-length parents) is selected at random</li>
	 * <li>The segments on the right hand side of each individual are then swapped.</li>
	 * <li>The probability of the crossover to happen is specified with the given
	 *     crossover probability.<br>
	 *     If a pair of genomes does not crossover then genome cloning takes place,
	 *     and the offspring are created as exact copies of their parents.</li>
	 * </ul>
	 * 
	 * @param binaryGenomeA The binary genome of the first parent which will become 
	 * 						the first offspring after the execution of the operation).
	 * @param binaryGenomeB The binary genome of the second parent which will become 
	 * 						the second offspring after the execution of the operation).
	 * @param crossoverProbability The probability of crossover to occur (from 0.0 to 1.0).
	 * @throws RuntimeException If the parent's genomes have not the same length.
	 */
	public static void standardOnePoint(StringBuilder binaryGenomeA, StringBuilder binaryGenomeB, double crossoverProbability) {
		
		int length = binaryGenomeA.length();
		
		if (length != binaryGenomeB.length())
			throw new RuntimeException("Parent's genomes are not of the same length");
		
		if (MathUtil.flip(crossoverProbability)) {	
					
			// STEP 1:
			// Get a random crossover point
			
			// Get Randomly how many bits of the left part of the offspring will be retained from their parents
			// Range [0, genomeLength]			
			int crossPoint = MathUtil.randomInteger(0, length); 
			//System.out.println("Crosspoint: " + crossPoint);
								
			// STEP 2:		
			// Perform the Crossover
			StringBuilder childA = new StringBuilder();
			StringBuilder childB = new StringBuilder();
			
			// Add the left parts of the offspring
			if (crossPoint > 0) {
				childA.append(binaryGenomeA.substring(0, crossPoint));				
				childB.append(binaryGenomeB.substring(0, crossPoint));
			}
			// Swap and add the right parts of the offspring
			if (crossPoint < length) {
				childA.append(binaryGenomeB.substring(crossPoint, length));
				childB.append(binaryGenomeA.substring(crossPoint, length));
			}
			 
			// STEP 3:
			// Replace Parent's genome with the offspring
			binaryGenomeA.delete(0, length);
			binaryGenomeA.append(childA);
			binaryGenomeB.delete(0, length);
			binaryGenomeB.append(childB);
			
											
		}					
				
	}		
	
	/**
	 * This method performs the Standard One Point Crossover operation to the
	 * fixed-length binary genomes of the parent individuals <code>individualA</code> and <code>individualB</code>
	 * with probability <code>crossoverProbability</code>.
	 * 
	 * @see Crossover#standardOnePoint(StringBuilder, StringBuilder, double)
	 * @param <T>  The type of the value of the phenotype of the individuals.
	 * @param individualA The first parent individual which will become 
	 * 					  the first offspring individual after the execution of the operation).
	 * @param individualB The second parent individual which will become 
	 * 					  the second offspring individual after the execution of the operation).
	 * @param crossoverProbability The probability of crossover to occur (from 0.0 to 1.0).
	 * @throws RuntimeException If the parent's genomes have not the same length.
	 */
	public static <T>void standardOnePoint(Individual<String, T> individualA, Individual<String, T> individualB, double crossoverProbability) {
		
		StringBuilder genomeA = new StringBuilder(individualA.getGenotype().value());
		StringBuilder genomeB = new StringBuilder(individualB.getGenotype().value());
		
		standardOnePoint(genomeA, genomeB, crossoverProbability);
		
		individualA.getGenotype().setValue(genomeA.toString());
		individualB.getGenotype().setValue(genomeB.toString());
		
	}	
	
	

	/**
	 * This method performs the Standard One Point Crossover operation to the
	 * given variable-length binary genomes of the parents <code>binaryGenomeA</code> and <code>binaryGenomeA</code>
	 * with probability <code>crossoverProbability</code>.<br>
	 * After the execution of the crossover operation the binary genomes of the parents
	 * will be replaced with the binary genomes of the offspring.
	 * <br><br>
	 * The Standard One Point Crossover acts as follows:<br>
	 * <ul>
	 * <li>Two Crossover points are selected at random, one on each individual.</li>
	 * <li>The segments on the right hand side of each individual are then swapped.</li>
	 * <li>In case the one crossover point is 0 and the other is the length of the other genome 
	 *     (which means that one offspring gets the genome from both parents and the other nothing)
	 *     then the empty offspring will be replaced by the valid genome of the other offspring.</li>
	 * <li>The probability of the crossover to happen is specified with the given
	 *     crossover probability.<br>
	 *     If a pair of genomes does not crossover then genome cloning takes place,
	 *     and the offspring are created as exact copies of their parents.</li>
	 * </ul>
	 * 
	 * @param binaryGenomeA The binary genome of the first parent which will become 
	 * 						the first offspring after the execution of the operation).
	 * @param binaryGenomeB The binary genome of the second parent which will become 
	 * 						the second offspring after the execution of the operation).
	 * @param crossoverProbability The probability of crossover to occur (from 0.0 to 1.0).
	 */
	public static void standardOnePointVariableLength(StringBuilder binaryGenomeA, StringBuilder binaryGenomeB, double crossoverProbability) {
		
		if (MathUtil.flip(crossoverProbability)) {	
					
			// STEP 1:
			// Get a random crossover point for each parent
			
			// Get Randomly how many bits of the left part of the first offspring will be retained from its parent
			// Range [0, genomeLength]
			int crossPointA = MathUtil.randomInteger(0, binaryGenomeA.length());
			//System.out.println("Crosspoint A: " + crossPointA);
						
			// Get Randomly how many bits of the left part of the first offspring will be retained from its parent
			// Range [0, genomeLength]
			int crossPointB = MathUtil.randomInteger(0, binaryGenomeB.length()); 
			//System.out.println("Crosspoint B: " + crossPointB);
						
					
			// STEP 2:		
			// Perform the Crossover
			StringBuilder childA = new StringBuilder();
			StringBuilder childB = new StringBuilder();
			
			// Add the left parts of the offspring
			if (crossPointA > 0)
				childA.append(binaryGenomeA.substring(0, crossPointA));				
			if (crossPointB > 0)
				childB.append(binaryGenomeB.substring(0, crossPointB));
			// Swap and add the right parts of the offspring
			if (crossPointB < binaryGenomeB.length())
				childA.append(binaryGenomeB.substring(crossPointB, binaryGenomeB.length()));
			if (crossPointA < binaryGenomeA.length())
				childB.append(binaryGenomeA.substring(crossPointA, binaryGenomeA.length()));
			 
			// Check if some offspring is empty.
			if (childB.length() == 0)
				childB.append(childA);
			if (childA.length() == 0)
				childA.append(childB);
			
			// STEP 3:
			// Replace Parent's genome with the offspring
			binaryGenomeA.delete(0, binaryGenomeA.length());
			binaryGenomeA.append(childA);
			binaryGenomeB.delete(0, binaryGenomeB.length());
			binaryGenomeB.append(childB);
			
											
		}			
					
	}	
	
	/**
	 * This method performs the Standard One Point Crossover operation to the
	 * variable-length binary genomes of the parents <code>individualA</code> and <code>individualB</code>
	 * with probability <code>crossoverProbability</code>.
	 * 
	 * @see Crossover#standardOnePointVariableLength(StringBuilder, StringBuilder, double)
	 * @param <T>  The type of the value of the phenotype of the individuals.
	 * @param individualA The first parent individual which will become 
	 * 					  the first offspring individual after the execution of the operation).
	 * @param individualB The second parent individual which will become 
	 * 					  the second offspring individual after the execution of the operation).
	 * @param crossoverProbability The probability of crossover to occur (from 0.0 to 1.0).
	 */
	public static <T>void standardOnePointVariableLength(Individual<String, T> individualA, Individual<String, T> individualB, double crossoverProbability) {
		
		StringBuilder genomeA = new StringBuilder(individualA.getGenotype().value());
		StringBuilder genomeB = new StringBuilder(individualB.getGenotype().value());
		
		standardOnePointVariableLength(genomeA, genomeB, crossoverProbability);
		
		individualA.getGenotype().setValue(genomeA.toString());
		individualB.getGenotype().setValue(genomeB.toString());
					
	}	
	

}
