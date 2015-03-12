/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	15 April 2006
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
 * The class <code>HammnigDistance</code> implements a problem specification
 * of finding a target string. Namely, it compares the Hamming Distance
 * between the phenotypes of the individuals of the population and the target string.<br>
 * The phenotypes must be fixed-length strings.
 * <br><br>
 * In information theory, the Hamming distance between two strings 
 * of equal length is the number of positions for which 
 * the corresponding symbols are different.<br>
 * Put another way, it measures the number of substitutions required 
 * to change one into the other, or the number of errors that 
 * transformed one string into the other.
 * 
 * @author 	Loukas Georgiou
 * @version 1.0, 15/04/06
 * @see 	Evaluator
 * @see 	Population
 * @see 	Individual
 * @see 	Core
 * @since 	JavaGE 0.1
 */
public class HammingDistance implements Evaluator <String, String> {

	// The target string to be found.
	private String target = null;
	
	// Flag whether a solution has be found or not
	private boolean solutionFound = false;
	
	/**
	 * Default Constructor. Should not be used.
	 */
	private HammingDistance() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param target The target string that must be found.
	 */
	public HammingDistance(String target) {
		this();
		this.target = target;
	}
	
	/**
	 * Evaluates the phenotype (which must be a fixed-length string)
	 * of each individual of the population and assigns the Raw Fitness Value 
	 * according to the Hamming Distance between the phenotype and the target string.<br>
	 * If an individual's phenotype has not the same length with the target string
	 * then the individual is invalid (sets valid to false).<br><br> 
	 * The Raw Fitness is calculated with the following formula:<br>
	 * <code>Raw Fitness = Phenotype Length - Hamming Distance.</code>
	 *  
	 * @param population The population to be evaluated.
	 */
	public void evaluate(Population<String, String> population) {
		
		solutionFound = false;
		int size = population.size();
		int length = target.length();
		int hammingDistance = 0;
		Individual<String, String> individual;
		String current = null;
		
		
		// Iterate the population
		for (int i = 0; i < size; i++) {
			
			hammingDistance = 0;
			individual = population.getIndividual(i);
			current = individual.getPhenotype().value();
			
			if (current.length() != length) {	// Invalid Individual
				individual.setValid(false);
			}
			else {								// Valid Individual
								
				// Calculate Hamming Distance
				for (int y = 0; y < length; y++) {
					if (current.charAt(y) != target.charAt(y))
						hammingDistance++;
				}				
				
				// Assign Raw Fitness and set Individual as Valid
				individual.setRawFitnessValue((double) (length - hammingDistance));
				individual.setValid(true);
				
				// Check if a solution is found
				if (hammingDistance == 0)
					solutionFound = true;
				
			}
			
		}
		
	}
	
	/**
	 * Returns true if the solution has been found.<br>
	 * For the Hamming Distance problem a solution is found
	 * when the current population contains an individual with
	 * phenotype equal to the target string.
	 * 
	 * @return True, if the solution has been found.
	 */
	public boolean solutionFound() {
		return solutionFound;
	}

	
	/**
	 * Returns the target string to be found.
	 * 
	 * @return The target string to be found.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Sets the target string to be found.
	 * 
	 * @param target The target string to be found.
	 */
	public void setTarget(String target) {
		this.target = target;
	}

}
