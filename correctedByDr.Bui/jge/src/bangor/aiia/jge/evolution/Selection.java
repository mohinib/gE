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

import java.util.ArrayList;

import bangor.aiia.jge.core.Core;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.Population;
import bangor.aiia.jge.util.MathUtil;

/**
 * The class <code>Selection</code> is a utility class which provides
 * a collection of Evolutionary Computation Selection operators.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 08/04/06
 * @see 	Core
 * @since	JavaGE 0.1
 */
public class Selection {
	
	/**
	 * Default Private Constructor.
	 */
	private Selection() {
		super();
	}
	
	/**
	 * This method implements the Roulette Wheel Selection mechanism
	 * and returns a randomly selected pair of individuals from the given population <code>population</code>.
	 * <br><br>
	 * Description of the selection process:<br>
	 * <ol>
	 *  <li>Sums the total fitness of the individuals in the population (sum = T).</li>
	 *  <li>Generates a random decimal number r, 0 <= r < T</li>
	 *  <li>Steps through the individuals of the population and sums the fitness value.</li>
	 *  <li>The step 3 is repeated until the current sum is >= r.<br>
	 *  	The individual whose fitness value shifted the sum over the limit is the one selected</li>
	 *  <li>Repeats steps 1 - 4 in order to be drawn the second individual
	 * </ol>
	 * 
	 * @param <T>  The type of the value of the phenotype of the individuals of the population.
	 * @param population The population from which the two individuals will be drawn.
	 * @return A randomly selected pair of individuals.
	 */
	public static <T>ArrayList<Individual<String, T>> rouletteWheel(Population<String, T> population) {
		
		ArrayList<Individual<String, T>> chosen = new ArrayList<Individual<String, T>>(2);
	
		double totalFitness = 0.0;		// The sum of the fitness of all individuals of the population
		int size = population.size();	// The size of the population
		double random = 0.0;			// A random number in the range 0 and totalFitness
		double temp = 0.0;				// The temporary sum of the fitness of the individuals
		
		// Calcuate the Total sum		
		for (int i = 0; i < size; i++)
			totalFitness += population.getIndividual(i).rawFitness();
		//System.out.println("Total Fitness: " + totalFitness);
		
		// Get the first individual		
		random = MathUtil.randomDouble(0, totalFitness);
		temp = 0.0;
		//System.out.println("Random 1: " + random);
		for (int i = 0; i < size; i++) {
			//System.out.println("Add individual: " + i);
			temp += population.getIndividual(i).rawFitness();
			//System.out.println("Current temp: " + temp);
			if (temp >= random) {
				chosen.add(population.getIndividual(i));
				break;
			}				
		}		
		
		// Get the second individual
		random = MathUtil.randomDouble(0, totalFitness);
		temp = 0.0;
		//System.out.println("Random 2: " + random);
		for (int i = 0; i < size; i++) {
			//System.out.println("Add individual: " + i);
			temp += population.getIndividual(i).rawFitness();
			//System.out.println("Current temp: " + temp);
			if (temp >= random) {
				chosen.add(population.getIndividual(i));				
				break;
			}				
		}		
		
		return chosen;
		
	}
	
	
	/**
	 * This method implements the Rank Selection mechanism
	 * and returns a randomly selected pair of individuals from the given population <code>population</code>.
	 * <br><br>
	 * Description of the selection process:
	 * <ol>
	 *  <li>Individuals N are sorted according to their fitness value</li>
	 *  <li>A rank N is assigned to the best individual and rank of 1 to the worst individual.</li>
	 *  <li>The selection probability is linearly assigned to each individual according to the rank value.</li>
	 * </ol> 
	 * Rank selection first ranks the population and then every chromosome
	 * receives fitness from this ranking. The worst will have fitness 1,
	 * second worst 2 etc. and the best will have fitness N (number of chromosomes in population). 
	 * 
	 * @param <T>  The type of the value of the phenotype of the individuals of the population.
	 * @param population The population from which the two individuals will be drawn.
	 * @return A randomly selected pair of individuals.
	 */
	public static <T>ArrayList<Individual<String, T>> rankSelection(Population<String, T> population) {
		
		ArrayList<Individual<String, T>> chosen = new ArrayList<Individual<String, T>>(2);
		
		// Sort the population into ascending order
		population.sort(true);
				
		int totalRank = 0;				// The sum of the ranks of all individuals of the population
		int size = population.size();	// The size of the population
		int random = 0;					// A random number in the range 1 and totalRank
		int temp = 0;					// The temporary sum of the fitness of the individuals
		
		// Calcuate the Total sum of Ranks		
		for (int i = 0; i < size; i++)
			totalRank += i + 1;
		//System.out.println("Total Rank: " + totalRank);
		
		// Get the first individual		
		random = MathUtil.randomInteger(1, totalRank);
		temp = 0;
		//System.out.println("Random 1: " + random);
		for (int i = 0; i < size; i++) {
			//System.out.println("Add individual: " + i);
			temp += i + 1;
			//System.out.println("Current temp: " + temp);
			if (temp >= random) {
				chosen.add(population.getIndividual(i));
				break;
			}				
		}		
		
		// Get the second individual
		random = MathUtil.randomInteger(1, totalRank);
		temp = 0;
		//System.out.println("Random 2: " + random);
		for (int i = 0; i < size; i++) {
			//System.out.println("Add individual: " + i);
			temp += i + 1;
			//System.out.println("Current temp: " + temp);
			if (temp >= random) {
				chosen.add(population.getIndividual(i));
				break;
			}				
		}		
		
		return chosen;
		
	}
	
	
	
	/**
	 * This methods returns <code>numOfBest + numOfWorst</code> individuals from the
	 * given population <code>population</code> in respect to their ranking according their fitness value.<br>
	 * In the returned collection of individuals the first <code>numOfBest</code> individuals
	 * are the best <code>numOfBest</code> individuals of the given population <code>population</code>
	 * and the last <code>numOfWorst</code> individuals are the worst <code>numOfWorst</code> 
	 * of the given population <code>population</code>.
	 * 
	 * @param <T> The type of the value of the phenotype of the individuals of the population.
	 * @param population The population from which the two individuals will be drawn.
	 * @param numOfBest The number of the best individuals in the population which will be selected and returned.
	 * @param numOfWorst The number of the worst individuals in the population which will be selected and returned.
	 * @return The best <code>numOfBest</code> and worst <code>numOfWorst</code> individuals in the population.
	 * @throws IndexOutOfBoundsException Whether the sum <code>numOfBest + numOfWorst</code> is greater than the size of the population;
	 */
	public static <T>ArrayList<Individual<String, T>> selectBestAndWorst(Population<String, T> population, int numOfBest, int numOfWorst) {
		
		int size = population.size();
		
		// Throw an exception if the total of the requested individuals
		// is larger than the population's size
		if (numOfBest + numOfWorst > size) 
			throw new IndexOutOfBoundsException("The population is smaller than ther total of the requested individuals");
		
		ArrayList<Individual<String, T>> chosen = new ArrayList<Individual<String, T>>(numOfBest + numOfWorst);
		
		// Sort the population into descending order
		population.sort(false);
		
		// Get the best individuals
		for (int i = 0; i < numOfBest; i++)
			chosen.add(population.getIndividual(i));
		
		// Get the worst individuals
		for (int i = 0; i < numOfWorst; i++)
			chosen.add(population.getIndividual(size - numOfWorst + i));
		
		return chosen;
		
	}
	
	
	
}