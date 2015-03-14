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
 * The class <code>SteadyStateGA</code> implements a
 * version of a Steady-State Genetic Algorithm.<br>
 * The main idea of a Steady-State Genetic Algorithm is that
 * a portion of the population P survives in the new population P'
 * and that only the worst individuals are replaced.<br>
 * Namely, a few good individuals will mate and their offspring
 * will replace the worst individuals. The rest of the population will survive.
 * <br><br>
 * The portion of the population P that will be replaced in P'
 * is known as the Generation Gap and is a fraction in the interval (0,1).<br>
 * The default implementation of SSGA uses a fraction, G = 2/n (where n the size of the population).
 * Namely, 2 individuals will mate and their offspring will replace the 2 worst individuals.<br>
 * In general, the number of the individuals which will be replaced in each generation is <code>G * n</code>.
 * <br><br>
 * The SSGA process implemented by this class is the following:
 * <ol>
 *  <li>Random Initialisation of population, P (if not one given)</li>
 *  <li>Fitness Evaluation of the initial individuals in P</li>
 *  <li>Creation of an empty population, P'</li>
 *  <li>Until new offspring = G * n</li>  
 *   <ul>       
 *  	<li>Selection of two individuals from P to mate with Roulette Wheel Selection</li>
 *  	<li>Producing of two offspring using standard one-point crossover with probability Pc</li>
 *  	<li>Point Mutation with probability Pm on the two offspring</li>
 *  	<li>Duplication with probability Pd on the two offspring</li>
 *  	<li>Pruning with probability Pp  on the two offspring</li>
 *  	<li>Addition of the two offspring into P'</li>
 *   </ul> 
 *  </li> 
 *  <li>Addition of the best <code>n - (G * n)</code> individuals of P into the offspring P'</li>
 *  <li>Replacement of P with P'</li>
 *  <li>Fitness Evaluation of the individuals in P</li>
 *  <li>Repetition of steps 3 until 7 until termination criteria are met (solution found or max generations exceeded)</li>
 *  <li>Returning of best the individual (solution) in current population, P</li>
 * </ol>
 * Note:<br>
 * In case where G * n is not an even integer then it will be normalised.<br>
 * The larger even integer less than G * n and larger than 0 will be used.
 * 
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 15/04/06
 * @see 	EvolutionaryAlgorithm
 * @since	JavaGE 0.1
 */
public class SteadyStateGA extends EvolutionaryAlgorithm<String, String> {

	// The fraction in the interval [0.0, 1.0] of the subset of the population
	// to be replaced in every generation
	private double generationGap;
	
	/**
	 * Default Constructor.
	 */
	private SteadyStateGA() {
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
	public SteadyStateGA(Population<String, String> population, Evaluator<String, String> evaluator) {
		this();
		setPopulation(population);
		setEvaluator(evaluator);
		//setGenerationGap( 0.9 / (double)population.size());
		setGenerationGap( 0.9);
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
	public SteadyStateGA(int populationSize, int codonSize, int minCodons, int maxCodons, Evaluator<String, String> evaluator) {
		this(Genesis.<String>randomPopulation(populationSize, codonSize, minCodons, maxCodons), evaluator);
		setCodonSize(codonSize);		
	}
	
	/**
	 * Returns the solution of the Steady-State Genetic Algorithm.<br>
	 * Namely, the best individual in the current popupation after 
	 * the finishing of the Steady-State Genetic Algorithm.
	 * 
	 * @return The Solution. The best individual in the current population 
	 * 		   after the finishing of the Steady-State Genetic Algorithm.
	 */
	public Individual<String, String> run() {
			
		Individual<String, String> solution = null;		// The problem solution		
		ArrayList<Individual<String, String>> survivors = null; // The individuals who survive to the next generation
		ArrayList<Individual<String, String>> parents = null; // The selected parents
		Individual<String, String> parentA = null;		// The first parent
		Individual<String, String> parentB = null;		// The second parent
		Individual<String, String> childA = null;		// The first child
		Individual<String, String> childB = null;		// The second child
		Population<String, String> offspring;			// The new generation's population
		int size = population.size();					// The population size
		int newborns = 0;								// The offspring indviduals created so far
		int currentGeneration = 0;						// The current generation's number
		int replacements = 0;							// The individuals to be replaced in every generation
		generationsCreated = 0;							// Initialise the created so far generations of this run
		terminate = false;								// Initialise the termination condition
		//int terminate=0;
		// Calculate and Normalise the replacements
		replacements = (int) (generationGap * size);
		if (replacements % 2 != 0) {
			replacements--;
		}
		System.out.println("Generation Gap =" +generationGap +"Replacements =" +replacements);
		if (replacements <= 0)
			throw new RuntimeException("SSGA Run: No replacment will occur in the population");
		
		// Log the current SGA Setup
		if (logger != null) {
			logger.write("NEW SSGA RUN", true);
			logger.write("Replacements in each generation: " + replacements, false);
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
		//while (terminate<50) {
			// Creation of an empty population, P' 
			offspring = new Population<String, String>(size);
			newborns = 0;
			currentGeneration++;
			System.out.println("Generation Gap =" +generationGap +"Replacements =" +replacements);
			//System.out.print(" " + currentGeneration + " ");
					
			// Create the new population
			while (newborns < replacements) {
							
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
				offspring.setIndividual(newborns, childB);								
				newborns++;
				
			}
			
			// Addition of the best n - (G * n) individuals to P' 
			survivors = Selection.selectBestAndWorst(population, size - replacements, 0);
			for (int i = 0; i < survivors.size(); i++)
				offspring.setIndividual(newborns++, survivors.get(i));
						
			// Replacement of P with P' 
			population = offspring;		
			
			// Population Evaluation
			evaluatePopulation();
			
			/*
			//TEMPORARY LOGGING PART STARTS HERE
			Individual<String,String> bestSoFar = null;
			bestSoFar = Selection.selectBestAndWorst(population, 1, 0).get(0);
			System.out.println("Generation " + currentGeneration);
			System.out.println("Best individual: " + bestSoFar.getPhenotype().toString());
			System.out.println("Raw Fitness: " + bestSoFar.getRawFitnessValue());
			System.out.println();
			//TEMPORARY LOGGING PART ENDS HERE
			*/
			
			// Log the current population
			if (logger != null) {
				logger.write("GENERATION: " + currentGeneration, true);
				logger.write(population.toString() + "\n", false);				
			}
			//terminate++;
			// Evolutionary Process Termination criteria
			if (currentGeneration == maxGenerations){
				if (logger != null) 
					logger.write("SSGA TERMINATION: Max Generations Reached (" + currentGeneration + ")", true);
				terminate = true;		
			}	
			/*if (evaluator.solutionFound()) {
				if (logger != null) 
					logger.write("SSGA TERMINATION: Solution Found in Generation " + currentGeneration, true);
				terminate = true;		
			}	*/
			
		}
		
		solution = Selection.selectBestAndWorst(population, 1, 0).get(0);
		generationsCreated = currentGeneration;
		
		if (logger != null) 
			logger.write("\nSSGA SOLUTION:\n" + solution.toString(), true);
		
		return solution;
		
	}

	/**
	 * Returns the fraction in the interval [0.0, 1.0] of the subset 
	 * of the population to be replaced in every generation (Generation Gap).<br><br>
	 * Default value:<br>
	 * G = 2/n (n = the population size).
	 * 
	 * @return The &quot;Generation Gap&quot; of the Steady-State GA.
	 */
	public double getGenerationGap() {
		return generationGap;
	}

	/**
	 * Sets the fraction in the interval [0.0, 1.0] of the subset 
	 * of the population to be replaced in every generation (Generation Gap).<br><br>
	 * Default value:<br>
	 * G = 2/n (n = the population size).
	 * 
	 * @param generationGap The value of the &quot;Generation Gap&quot; of the Steady-State GA.
	 */
	public void setGenerationGap(double generationGap) {
		this.generationGap = generationGap;
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
		state.append("Generation Gap  : " + generationGap + "\n");
		
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
			//System.out.println("Init Population #" +i);
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
