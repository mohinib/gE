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

import bangor.aiia.jge.core.Evaluator;
import bangor.aiia.jge.core.Mapper;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.Population;
import bangor.aiia.jge.util.Logger;

/**
 * The class <code>EvolutionaryAlgorithm</code> 
 * defines common properties and behaviours for evolutionary
 * algorithms like Genetic Algorithms, Genetic Programming,
 * Grammatical Evolution, etc.<br><br>
 * An Evolutionary Algorithm simulates the biological process
 * of evolution. The <code>evolution unit</code> of this process
 * is the population (Darwinism).
 * <br><br>
 * The basic strategy of an Evolutionary Algorithm is the following:<br>
 * <ul>
 *  <li>Initial Population Creation<br>
 *      An initial Population is randomly created 
 *      in case an already initialised population is not given to the algorithm. 
 *  </li>
 *  <li>In each Generation the following actions are executed:  
 * 		<ul>
 *  		<li>Competition (Evaluation of the Individuals of the Population)</li>
 *  		<li>Selection (The individuals to mate)</li>
 *  		<li>Variation (Crossover, Mutation, Duplication, Pruning, etc.)</li>
 *  		<li>Reproduction (Creation of the new population, the offspring, which replaces the old population)</li> 
 * 		</ul>
 *  </li> 
 * </ul>
 * The subclasses of this class must implement the concrete
 * steps of the above strategy in order to provide
 * specific versions of Evolutionary Algorithms.
 * <br><br>
 * The class <code>EvolutionaryAlgorithm</code> is a Generic abstract 
 * class which means that the subclasses which implement 
 * a specific evolutionary algorithm can specify the actual types 
 * of the Genotype&lt;T&gt; and the Phenotype&lt;S&gt;
 * of the individuals of the evolved population.
 * <br><br>
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 15/04/06
 * @since	JavaGE 0.1
 * @param 	<T> The type of the value of the Genotype of the evolved population.
 * @param 	<S> The type of the value of the Phenotype of the evolved population.
 */
public abstract class EvolutionaryAlgorithm<T, S> {
	
	// The Population to be evolved
	protected Population<T, S> population = null;
	
	// The codon size in bits
	protected int codonSize = 1;
	
	// The length-type of the genomes of the individuals of the population
	protected boolean fixedSizeGenome = false;
		
	// The Genotype to Phenotype Mapping Mechanism
	protected Mapper<T, S> mapper = null;
	
	// The Evaluation Mechanism for the Individuals
	protected Evaluator<T, S> evaluator = null;
	
	// The Logger component which monitors and logs the Evolutionary process
	protected Logger logger = null;

	// The Genotype Crossover Probability
	protected double crossoverRate = 0.0;
	
	// The Genotype Mutation Probability	
	protected double mutationRate = 0.0;	
	
	// The Genotype Duplication Probability
	protected double duplicationRate = 0.0;
	
	// The Genotype Pruning Probability
	protected double pruningRate = 0.0;

	// The Maximum Number of Generations to be created
	protected int maxGenerations = 10;
	
	// The Termination Condition of the Evolutionary Algorithm
	protected boolean terminate = false;
	
	// The generations created in the last run of the Evolutionary Algorithm
	protected int generationsCreated = 0;
	

	/**
	 * Returns the solution of the evolutionary algorithm.<br>
	 * Namely, the best individual in the current popupation 
	 * after the finishing of the evolutionary process.
	 * 
	 * @return The Solution. The best individual in the current 
	 * 		   population after the finishing of the evolutionary process.
	 */
	public abstract Individual<T, S> run(); 
	
	
	/**
	 * Returns the Population to be evolved.
	 * 
	 * @return The Population to be evolved.
	 */
	public Population<T, S> getPopulation() {
		return population;
	}	

	/**
	 * Sets the Population to be evolved.
	 * 
	 * @param population The Population to be evolved.
	 */
	public void setPopulation(Population<T, S> population) {
		this.population = population;
	}
	
	/**
	 * Returns the codon size in bits of the genome.<br>
	 * The default value is <code>1</code>.
	 * 
	 * @return The codon size in bits of the genome.
	 */
	public int getCodonSize() {
		return codonSize;
	}

	/**
	 * Sets the codon size in bits of the genome.
	 * 
	 * @param codonSize The codon size in bits of the genome.
	 */
	public void setCodonSize(int codonSize) {
		this.codonSize = codonSize;
	}
	
	/**
	 * Returns true if the genomes of the individuals of the population
	 * have a specific fixed length.<br>
	 * Otherwise, if the gemones have a variable length, it returns false.<br>
	 * The default value is <code>false</code> (variable-length).
	 * 
	 * @return True, in case of fixed-length genomes.
	 */
	public boolean isFixedSizeGenome() {
		return fixedSizeGenome;
	}

	/**
	 * Sets whether the sizes of the genomes of the individuals 
	 * will have a fixed or variable length.
	 * 
	 * @param fixedSizeGenome True for fixed-length genomes, 
	 * 		  false for variable-length genomes.
	 */
	public void setFixedSizeGenome(boolean fixedSizeGenome) {
		this.fixedSizeGenome = fixedSizeGenome;
	}
	
	
	/**
	 * Returns the Genotype to Phenotype Mapping Mechanism.
	 * 
	 * @return The Genotype to Phenotype Mapping Mechanism.
	 */
	public Mapper<T, S> getMapper() {
		return mapper;
	}

	/**
	 * Sets the Genotype to Phenotype Mapping Mechanism.
	 * 
	 * @param mapper The Genotype to Phenotype Mapping Mechanism.
	 */
	public void setMapper(Mapper<T, S> mapper) {
		this.mapper = mapper;
	}
	
	
	/**
	 * Returns the Evaluation Mechanism for the Individuals.
	 * 
	 * @return The Evaluation Mechanism for the Individuals.
	 */
	public Evaluator<T, S> getEvaluator() {
		return evaluator;
	}

	/**
	 * Sets the Evaluation Mechanism for the Individuals.
	 * 
	 * @param evaluator The Evaluation Mechanism for the Individuals.
	 */
	public void setEvaluator(Evaluator<T, S> evaluator) {
		this.evaluator = evaluator;
	}
	
	
	/**
	 * Returns the Logger component which monitors and logs 
	 * the Evolutionary process.
	 * 
	 * @return The Logger component which monitors and logs 
	 * 		   the Evolutionary process.
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the Logger component which monitors and logs 
	 * the Evolutionary process.
	 * 
	 * @param logger The Logger component which monitors and logs 
	 * 		  the Evolutionary process.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	
	/**
	 * Returns the Genotype Crossover Probability.<br>
	 * The default value is <code>0.0</code>.
	 * 
	 * @return The Genotype Crossover Probability.
	 */
	public double getCrossoverRate() {
		return crossoverRate;
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
	 * Returns the Genotype Mutation Probability.<br>
	 * The default value is <code>0.0</code>.
	 * 
	 * @return The Genotype Mutation Probability.
	 */
	public double getMutationRate() {
		return mutationRate;
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
	 * Returns the Genotype Duplication Probability.<br>
	 * The default value is <code>0.0</code>.
	 * 
	 * @return The Genotype Duplication Probability.
	 */
	public double getDuplicationRate() {
		return duplicationRate;
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
	 * Returns the Genotype Pruning Probability.<br>
	 * The default value is <code>0.0</code>.
	 * 
	 * @return The Genotype Pruning Probability.
	 */
	public double getPruningRate() {
		return pruningRate;
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
	 * Returns the Maximum Number of Generations to be created.<br>
	 * The default value is <code>10</code>.
	 * 
	 * @return The Maximum Number of Generations to be created.
	 */
	public int getMaxGenerations() {
		return maxGenerations;
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
	 * The Termination Condition of the Evolutionary Algorithm.<br>
	 * If true then the evolutionary process has been finished.<br>
	 * The default value is <code>false</code>.
	 * 
	 * @return The Termination Condition of the Evolutionary Algorithm.
	 */
	synchronized public boolean isTerminate() {
		return terminate;
	}

	/**
	 * Sets the Termination Condition of the Evolutionary Algorithm.<br>
	 * If true then the evolutionary process will finish.
	 * 
	 * @param terminate The Termination Condition of the Evolutionary Algorithm.
	 */
	synchronized public void setTerminate(boolean terminate) {
			this.terminate = terminate;
	}
	
	/**
	 * Returns the number of the generations which were created 
	 * during the last run of the Evolutionary Algorithm.
	 * 
	 * @return The number of the generations of the last run.
	 */
	public int lastRunGenerations() {
		return generationsCreated;
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
		state.append("Mapper          : " + (mapper == null?"null":mapper.getClass()) + "\n");
		state.append("Evaluator       : " + (evaluator == null?"null":evaluator.getClass()) + "\n");
		state.append("Logger          : " + (logger == null?"null":logger.getClass()) + "\n");
		state.append("Crossover       : " + crossoverRate + "\n");
		state.append("Mutation        : " + mutationRate + "\n");
		state.append("Duplication     : " + duplicationRate + "\n");
		state.append("Pruning         : " + pruningRate + "\n");
		state.append("Generations     : " + maxGenerations + "\n");	
		state.append("Termination     : " + terminate + "\n");
		
		return state.toString();
	}
	
}
