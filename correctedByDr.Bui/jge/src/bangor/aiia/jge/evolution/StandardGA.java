/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	10 April 2006
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

import bangor.aiia.jge.core.DefaultMapper;
import bangor.aiia.jge.core.Evaluator;
import bangor.aiia.jge.core.GEMapper;
import bangor.aiia.jge.core.Mapper;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.InvalidPhenotypeException;
import bangor.aiia.jge.population.Phenotype;
import bangor.aiia.jge.population.Population;

/**
 * The class <code>StandardGA</code> implements the 
 * classical Genetic Algorithm.<br>
 * Namely, the following process is implemented:
 * <ol>
 *  <li>Random Initialisation of population, P (if not one given)</li>
 *  <li>Fitness Evaluation of the initial individuals in P</li>
 *  <li>Creation of an empty population, P'</li>
 *  <li>Until P' is full
 *   <ul>      
 *  	<li>Selection of two individuals from P to mate with Roulette Wheel Selection</li>
 *  	<li>Producing of two offspring using standard one-point crossover with probability Pc</li>
 *  	<li>Point Mutation with probability Pm on the two offspring</li>
 *  	<li>Duplication with probability Pd on the two offspring</li>
 *  	<li>Pruning with probability Pp  on the two offspring</li>
 *  	<li>Addition of the two offspring into P'</li>
 *   </ul> 
 *  </li> 
 *  <li>Replacement of P with P'</li>
 *  <li>Fitness Evaluation of the individuals in P</li>
 *  <li>Repetition of steps 3 until 6 until termination criteria are met (solution found or max generations exceeded)</li>
 *  <li>Returning of the best individual (solution) in current population, P</li>
 * </ol>
 * 
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 15/04/06
 * @see 	EvolutionaryAlgorithm
 * @since	JavaGE 0.1
 */
public class StandardGA extends EvolutionaryAlgorithm<String, String> {

	/**
	 * Default Constructor.
	 */
	private StandardGA() {	
		Mapper<String, String> mapper = new DefaultMapper<String>();
		setMapper(mapper);
		generationsCreated = 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param population	The population to be evolved.
	 * @param evaluator		The problem specification.
	 */
	public StandardGA(Population<String, String> population, Evaluator<String, String> evaluator) {
		this();
		setPopulation(population);
		setEvaluator(evaluator);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param populationSize	The size of the population to be randomly created.
	 * @param codonSize			The size of codon in bits.
	 * @param minCodons			The lower limit of codons of an individual.
	 * @param maxCodons			The upper limit of codons of an individual.
	 * @param evaluator			The problem specification.
	 */
	public StandardGA(int populationSize, int codonSize, int minCodons, int maxCodons, Evaluator<String, String> evaluator) {
		this(Genesis.<String>randomPopulation(populationSize, codonSize, minCodons, maxCodons), evaluator);
		setCodonSize(codonSize);
	}
	

	/**
	 * Returns the solution of the Standard Genetic Algorithm.<br>
	 * Namely, the best individual in the current popupation after 
	 * the finishing of the Standard Genetic Algorithm.
	 * 
	 * @return The Solution. The best individual in the current population 
	 * 		   after the finishing of the Standard Genetic Algorithm.
	 */
	public Individual<String, String> run() {
		
		Individual<String, String> solution = null;				// The problem solution		
		ArrayList<Individual<String, String>> parents = null; 	// The selected parents		
		Individual<String, String> parentA = null;				// The first parent
		Individual<String, String> parentB = null;				// The second parent
		Individual<String, String> childA = null;				// The first child
		Individual<String, String> childB = null;				// The second child
		Population<String, String> offspring;					// The new generation's population
		int size = population.size();							// The population size
		int newborns = 0;										// The offspring indviduals created so far
		int currentGeneration = 0;								// The current generation's number
		generationsCreated = 0;									// Initialise the created so far generations of this run
		terminate = false;										// Initialise the termination condition
		
		// Log the current SGA Setup
		if (logger != null) {
			logger.write("NEW SGA RUN", true);
			logger.write(this.toString() + "\n", false);
		}
		
		// Initial Population Evaluation
		evaluatePopulation();

		
		// Log the current population
		if (logger != null) {
			logger.write("GENERATION: " + currentGeneration, true);
			logger.write(population.toString() + "\n", false);				
		}		
		
		// Start Evolutionary Process (Generations)
		while (!terminate) {
			
			// Creation of an empty population, P' 
			offspring = new Population<String, String>(size);
			newborns = 0;
			currentGeneration++;	
			
			// Create the new population
			while (newborns < size) {
							
				// Selection of 2 individuals from P to mate with Roulette Wheel Selection 
				parents = Selection.rouletteWheel(population);
				parentA = parents.get(0);
				parentB = parents.get(1);
				
				// Producing of 2 offspring using standard one-point crossover with probability Pc 
				childA = parentA.cloneGenomeOnly();
				childB = parentB.cloneGenomeOnly();
				if (fixedSizeGenome)
					Crossover.standardOnePoint(childA, childB, crossoverRate);
				else
					Crossover.standardOnePointVariableLength(childA, childB, crossoverRate);
								
				// Point Mutation with probability Pm on the 2 offspring 
				Mutation.pointMutation(childA, mutationRate);
				Mutation.pointMutation(childB, mutationRate);
				
				// Duplication with probability Pd on the 2 offspring 
				Duplication.duplication(childA, codonSize, duplicationRate);
				Duplication.duplication(childB, codonSize, duplicationRate);
				
				// Pruning with probability Pp on the 2 offspring 
				if (mapper instanceof GEMapper) {
					Pruning.pruning(childA, (GEMapper)mapper, pruningRate);
					Pruning.pruning(childB, (GEMapper)mapper, pruningRate);					
				}
				
				// Addition of the 2 offspring to P'				
				offspring.setIndividual(newborns, childA);
				newborns++;
				if (newborns < size) { // In case the population size is an odd number
					offspring.setIndividual(newborns, childB);								
					newborns++;
				}
				
			}
			
			// Replacement of P with P' 
			population = offspring;
			
			// Population Evaluation
			evaluatePopulation();
			
			// Log the current population
			if (logger != null) {
				logger.write("GENERATION: " + currentGeneration, true);
				logger.write(population.toString() + "\n", false);				
			}			
			
			// Evolutionary Process Termination criteria
			if (currentGeneration == maxGenerations){
				if (logger != null) 
					logger.write("SGA TERMINATION: Max Generations Reached (" + currentGeneration + ")", true);
				terminate = true;		
			}	
		/*	if (evaluator.solutionFound()) {
				if (logger != null) 
					logger.write("SGA TERMINATION: Solution Found in Generation " + currentGeneration, true);
				terminate = true;		
			}	*/
		
		}
		
		solution = Selection.selectBestAndWorst(population, 1, 0).get(0);
		generationsCreated = currentGeneration;
		
		if (logger != null) 
			logger.write("\nSGA SOLUTION:\n" + solution.toString(), true);
		
		return solution;
		
	}
	
	
	/**
	 * This methods returns a string description of the current
	 * state of the object.
	 * 
	 * @return The state of the object.
	 */
	public String toString() {
		
		StringBuffer state = new StringBuffer();
		state.append(super.toString());	
		
		return state.toString();
	}
	
	/**
	 * This method creates the phenotypes of the individuals
	 * according the given mapper and evaluates their validity
	 * and raw fitness according the given evaluator (problem specification).
	 */
	private void evaluatePopulation() {
		
		Individual<String, String> individual = null;	// The current individual
		Phenotype<String> phenotype = null;				// The current individual's phenotype
		int size = population.size();					// The population size
		
		// Create the Phenotypes of the Individuals
		for (int i = 0; i < size; i++) {
			individual = population.getIndividual(i);
			try {				
				phenotype = mapper.getPhenotype(individual.getGenotype());					
				individual.setPhenotype(phenotype);
				individual.setValid(true);
			}
			catch(InvalidPhenotypeException ipe) {
				if (mapper instanceof GEMapper) {
					individual.setValid(false);
					phenotype = new Phenotype<String>(((GEMapper) mapper).lastRunPhenotypeValue());	
					individual.setPhenotype(phenotype);
				}
				if (logger != null) {
					logger.write("INVALID PHENOTYPE EXCEPTION: " + ipe.getMessage(), false);
				}
			}				
		}		
		
		// Fitness Evalution of the individuals in P 
		evaluator.evaluate(population);
		
	}	

}