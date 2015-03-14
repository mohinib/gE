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

package bangor.aiia.jge.core;

import bangor.aiia.jge.bnf.BNFGrammar;
import bangor.aiia.jge.evolution.EvolutionaryAlgorithm;
import bangor.aiia.jge.evolution.Genesis;
import bangor.aiia.jge.evolution.StandardGA;
import bangor.aiia.jge.evolution.SteadyStateGA;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.Population;
import bangor.aiia.jge.util.Logger;

/**
 * The class <code>GrammaticalEvolution</code> implements
 * the default version (with a minor exception regarding
 * the steady state replacement mechanism as mentioned below) 
 * of the Grammatical Evolution algorithm
 * which has been invented by O'Neill and Ryan.
 * <br><br>
 * The default implementation of Grammatical Evolution, as it is described 
 * in the papers and the book authored by O'Neill and Ryan, uses a 
 * Steady-State replacement mechanism such that two parents produce two
 * children the best of which replace the worst individual in the population
 * only if the child has a greater fitness than the individual to be replaced.
 * <br>
 * The class <code>GrammaticalEvolution</code> uses a slightly different replacement
 * mechanism, this one implemented by the class <code>SteadyStateGA</code>.<br>
 * Also, there is the option to use a Generational replacement mechanism
 * like in Standard GA (this one which is implemented by the class <code>StandardGA</code>).
 * <br><br> 
 * Regarding the configuration of a Grammatical Evolution run, 
 * O'Neill and Ryan suggest the following:
 * <ul>
 *  <li>A typical wrapping threshold is 10</li>
 *  <li>Size of the Codon: 8-bit codons</li>
 *  <li>Crossover: Typical probability 0.9.</li>
 *  <li>Mutation: Typical probability 0.01</li>
 *  <li>Duplication: Typical probability 0.01</li>
 *  <li>Pruning: Typically 0.01. </li>
 *  <li>Generation of the Initial Population:<br>
 *  	Randomly generation of variable-length binary strings within a pre-specified range of codons: 
 *      1 to 10 codons and each codon is a group of 8 bits (O’Neill and Ryan).<br>
 *      In the libGE implementation the size of the Initial Random Genotypes
 *      is 15-25 codons / integers from 0 to 255 (Nicolau).
 *  </li>
 * </ul>
 * The above configuration is the default of the <code>GrammaticalEvolution</code> class.<br>
 * Indeed, this implementation uses also the following default values:
 * <ul>
 *  <li>Max Generations: 50</li>
 *  <li>Searching Mechanism: Steady-State GA</li>
 *  <li>Generational Gap of the Steady-State GA, G = 2/n (n = the population size)</li>
 * </ul>
 *
 * @author 	Loukas Georgiou 
 * @version	1.1, 12/06/06
 * @see 	EvolutionaryAlgorithm
 * @see		StandardGA
 * @see 	SteadyStateGA
 * @since	JavaGE 0.1
 */
public class GrammaticalEvolution {
	
	// The types of evolutionary algorithms supported by this class
	public static enum EAType {
		StandardGA, SteadyStateGA
	}
	
	// The evolutionary algorithm to be used
	private EAType eaType = null;

	// The Genotype Crossover Probability
	private double crossoverRate = 0.0;
	
	// The Genotype Mutation Probability	
	private double mutationRate = 0.0;	
	
	// The Genotype Duplication Probability
	private double duplicationRate = 0.0;
	
	// The Genotype Pruning Probability
	private double pruningRate = 0.0;
	
	// The Generation Gap of a Steady-State GA
	private double generationGap = 0.0;

	// The Maximum Number of Generations to be created
	private int maxGenerations = 0;
	
	// The wrapping threshold
	private int wrapping = 0;
	
	
	// The Logger component which monitors and logs the Grammatical Evolution process
	private Logger logger = null;

	// The BNF Grammar
	private BNFGrammar bnfGrammar = null;
	
	// The Genotype to Phenotype Mapping Mechanism
	private GEMapper mapper = null;

	// The Evaluation Mechanism for the Individuals
	private Evaluator<String, String> evaluator = null;
	
	// The Population to be evolved
	private Population<String, String> population = null;
	
	// The codon size in bits
	private int codonSize = 0;	
	
	// Fixed-Length genomes
	private boolean fixedSizeGenome = false;
	
	// The generations created in the last run of the Evolutionary Algorithm
	protected int generationsCreated = 0;
	
	
	/**
	 * Default Constructor.
	 */
	private GrammaticalEvolution() {
		
		crossoverRate = 0.9;
		mutationRate = 0.01;
		duplicationRate = 0.01;
		pruningRate = 0.01;
		generationGap = 0.0;

		wrapping = 10;
		maxGenerations = 50;
		eaType = EAType.SteadyStateGA;
		fixedSizeGenome = false;
		
		generationsCreated = 0;
		
	}	

	/**
	 * Constructor.
	 * 
	 * @param bnfGrammar	The BNF Grammar.
	 * @param evaluator		The problem specification.
	 * @param population	The initial population.
	 * @param codonSize		The codon size in bits of the genotype of each individual.
	 */
	public GrammaticalEvolution(BNFGrammar bnfGrammar, Evaluator<String, String> evaluator, Population<String, String> population, int codonSize) {
		this();
		this.bnfGrammar = bnfGrammar;
		this.evaluator = evaluator;
		this.population = population;
		this.codonSize = codonSize;
		//generationGap = 2.0 / (double) population.size();
		generationGap = 0.9;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param bnfGrammar		The BNF Grammar.
	 * @param evaluator			The problem specification.
	 * @param populationSize	The size of the randomly created initial population.
	 * @param codonSize			The codon size in bits of the genotype of each individual.
	 * @param minCodons			The minimum codons of the genotype of the randomly created individuals.
	 * @param maxCodons			The maximum codons of the genotype of the randomly created individuals.
	 */
	public GrammaticalEvolution(BNFGrammar bnfGrammar, Evaluator<String, String> evaluator, int populationSize, int codonSize, int minCodons, int maxCodons) {
		this(bnfGrammar, evaluator, Genesis.<String>randomPopulation(populationSize, codonSize, minCodons, maxCodons), codonSize);		
	}
	
	
	/**
	 * Returns the solution of the Grammatical Evolution Algorithm.<br>
	 * Namely, the best individual in the current popupation after 
	 * the finishing of the Grammatical Evolution process.
	 * 
	 * @return The Solution. The best individual in the current population 
	 * 		   after the Grammatical Evolution process.
	 */
	public Individual<String, String> run() {
		
		Individual<String, String> solution = null;
		generationsCreated = 0;
		
		// Setup the Mapper 
		mapper = new GEMapper(bnfGrammar, codonSize, wrapping);
			
		// Setup the Searching Mechanism
		EvolutionaryAlgorithm<String, String> ea = null;
		if (eaType == EAType.StandardGA) {
			ea = new StandardGA(population, evaluator);
		}
		else if (eaType == EAType.SteadyStateGA) {
			ea = new SteadyStateGA(population, evaluator);
			((SteadyStateGA)ea).setGenerationGap(generationGap);
		}
		ea.setFixedSizeGenome(fixedSizeGenome);
		ea.setCodonSize(codonSize);
		ea.setCrossoverRate(crossoverRate);
		ea.setMutationRate(mutationRate);
		ea.setDuplicationRate(duplicationRate);
		ea.setPruningRate(pruningRate);
		ea.setMaxGenerations(maxGenerations);
						
		ea.setMapper(mapper);
		ea.setEvaluator(evaluator);		
		ea.setLogger(logger);		
		
		// Log the current Grammatical Evolution Setup
		if (logger != null) {
			logger.write("********** GRAMMATICAL EVOLUTION SETUP **********", true);
			logger.write(this.toString() + "\n", false);
		}
				
		// Execute the Evolutionary Algorithm		
		solution = ea.run();
		System.out.println("I am solution: " +solution);
		generationsCreated = ea.lastRunGenerations();
		population = ea.getPopulation();
		
		return solution;
		
	}
		
	
	/**
	 * Sets the Genotype Crossover Probability.
	 * 
	 * @param crossoverRate The Genotype Crossover Probability.
	 */
	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}
	
	/**
	 * Sets the Genotype Mutation Probability.
	 * 
	 * @param mutationRate The Genotype Mutation Probability.
	 */
	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}
	
	/**
	 * Sets the Genotype Duplication Probability.
	 * 
	 * @param duplicationRate The Genotype Duplication Probability.
	 */	
	public void setDuplicationRate(double duplicationRate) {
		this.duplicationRate = duplicationRate;
	}
	
	/**
	 * Sets the Genotype Pruning Probability.
	 * 
	 * @param pruningRate The Genotype Pruning Probability.
	 */
	public void setPruningRate(double pruningRate) {
		this.pruningRate = pruningRate;
	}
	
	/**
	 * Sets the &quot;Generation Gap&quot; which
	 * is used by a Steady-State GA.<br><br>
	 * Default value:<br>
	 * G = 2/n  (n is the size of the population).
	 * 
	 * @param generationGap The value of the &quot;Generation Gap&quot; of the Steady-State GA.
	 */
	public void setGenerationGap(double generationGap) {
		this.generationGap = generationGap;
	}
	
	/**
	 * Sets the Maximum Number of Generations to be created.
	 * 
	 * @param maxGenerations The Maximum Number of Generations to be created.
	 */
	public void setMaxGenerations(int maxGenerations) {
		this.maxGenerations = maxGenerations;
	}
	
	/**
	 * Sets the Wrapping threshold of the genome.
	 * 
	 * @param wrapping The Wrapping threshold of the genome.
	 */
	public void setWrapping(int wrapping) {
		this.wrapping = wrapping;
	}
	
	/**
	 * Sets the Evolutionary Algorithm to be used as the searching mechanism.
	 * 
	 * @param eaType The Evolutionary Algorithm to be used as the searching mechanism.
	 */
	public void setEAType(EAType eaType) {
		this.eaType = eaType;
	}
	
	/**
	 * Sets the Logger component which monitors and logs 
	 * the Grammatical Evolution process.
	 * 
	 * @param logger The Logger component which monitors and logs 
	 * 		  the Grammatical Evolution process.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Returns the number of the generations which were created 
	 * during the last run of the Grammatical Evolution algorithm.
	 * 
	 * @return The number of the generations of the last run.
	 */
	public int lastRunGenerations() {
		return generationsCreated;
	}
	
	/**
	 * Returns the current population.
	 * 
	 * @return The current population.
	 */
	public Population<String, String> getPopulation() {
		return population;
	}
	
	/**
	 * This methods returns a string description of the current
	 * state of the object.
	 * 
	 * @return The state of the object.
	 */
	public String toString() {

		StringBuffer state = new StringBuffer();
		
		state.append(this.getClass() + "\n");
		state.append("Population      : " + (population == null?"null":population.getClass()) + "\n");
		if (population != null)
			state.append("Population Size : " + population.size() + "\n");
		state.append("Codon Size      : " + codonSize + "\n");
		state.append("Genome Length   : " + (fixedSizeGenome == true?"fixed":"variable") + "\n");
		state.append("Max Wraps       : " + wrapping + "\n");
		state.append("BNF Grammar     : " + (bnfGrammar == null?"null":bnfGrammar.getClass()) + "\n");
		state.append("Mapper          : " + (mapper == null?"null":mapper.getClass()) + "\n");
		state.append("Evaluator       : " + (evaluator == null?"null":evaluator.getClass()) + "\n");
		state.append("Logger          : " + (logger == null?"null":logger.getClass()) + "\n");
		state.append("EA Type         : " + eaType + "\n");
		state.append("Crossover       : " + crossoverRate + "\n");
		state.append("Mutation        : " + mutationRate + "\n");
		state.append("Duplication     : " + duplicationRate + "\n");
		state.append("Pruning         : " + pruningRate + "\n");
		state.append("Generation Gap  : " + generationGap + "\n");
		state.append("Generations     : " + maxGenerations + "\n");			
		
		return state.toString();

	}
}