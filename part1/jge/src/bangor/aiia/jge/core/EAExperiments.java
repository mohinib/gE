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

import java.io.File;
import java.io.IOException;

import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.ps.HammingDistance;
import bangor.aiia.jge.ps.SymbolicRegression;
import bangor.aiia.jge.util.ConfigurationSettings;
import bangor.aiia.jge.util.JikesCompiler;
import bangor.aiia.jge.util.LogFile;
import bangor.aiia.jge.bnf.BNFGrammar;
import bangor.aiia.jge.core.GrammaticalEvolution;
import bangor.aiia.jge.evolution.EvolutionaryAlgorithm;
import bangor.aiia.jge.evolution.StandardGA;
import bangor.aiia.jge.evolution.SteadyStateGA;


/**
 * The class <code>EAExperiments</code> is a collection of some
 * Grammatical Evolution, Standard GA, and Steady-State GA experiments,
 * which demonstrate the use and application of the Evolutionary Algorithms
 * of the jGE library.
 * <br><br>
 * 
 * @author 	Loukas Georgiou 
 * @version	1.1, 12/06/06
 * @see 	GrammaticalEvolution
 * @see 	EvolutionaryAlgorithm
 * @see		StandardGA
 * @see 	SteadyStateGA
 * @since	JavaGE 0.1
 */
public class EAExperiments  {
	

	public static void main(String[] args) {

		EAExperiments eae = new EAExperiments();
		eae.doExperiments();
			
	}
	
	/**
	 * Default Constructor.
	 */
	public EAExperiments() {
		super();

	}
	
	public void doExperiments() {
		
		System.out.println("EC Experiments Demonstration\n");
	
		// Symbolic Regression Problems
		//srExperimentGE();		
		
		// Trigonometric Identity Problems
		//tiExperimentGE();			
		
		
		// Hamming Distance Problems		
		//hdExperimentGA();		
		//hdExperimentSSGA();
		//hdExperimentGE();
			
		
	}	
	
	
	/**
	 * This method demonstrates the use of Grammatical Evolution
	 * in a Symbolic Regression problem.
	 * 
	 * @param populationSize The size of the initial population.
	 * @param maxGenerations The maximum number of generations to be evolved.
	 * @return The solution of the Symbolic Regression experiment.
	 */
	public Individual<String, String> srExperimentGE(int populationSize, int maxGenerations) {
		
		Individual<String, String> solution = null;
		String expression = "x * x * x * x + x * x * x + x * x + x";
		
		// Java Compiler
		JikesCompiler compiler = new JikesCompiler(
									new File(ConfigurationSettings.getInstance().getJikesCompiler()), 
									new File(ConfigurationSettings.getInstance().getJikesBootClassPath()));
		// Problem Specification
		SymbolicRegression sr = new SymbolicRegression(expression, 20, -1, 1, true, compiler, new File(ConfigurationSettings.getInstance().getWorkingDirectoryPath()));
		// Log File
		LogFile log = new LogFile(new File(ConfigurationSettings.getInstance().getWorkingDirectoryPath() + "\\EAExperiments_GE_SR.txt"));
		log = null;
		
		// BNF Grammar
		BNFGrammar bnf = null;
		try {
			bnf = new BNFGrammar(new File(ConfigurationSettings.getInstance().getRootPath() + "\\res\\bnf\\ArithmeticExpression.bnf"));
		}
		catch(IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		// Grammatical Evolution
		GrammaticalEvolution ge = new GrammaticalEvolution(bnf, sr, populationSize, 8, 20, 30);
		ge.setCrossoverRate(0.9);
		ge.setMutationRate(0.01);
		ge.setDuplicationRate(0.01);
		ge.setPruningRate(0.01);
		ge.setGenerationGap(0.9);
		ge.setMaxGenerations(maxGenerations);
		ge.setLogger(log);	
		
		System.out.println("GE Symbolic Regression Experiment");
		System.out.println("Expression: " + expression);
		solution = ge.run();
		System.out.println("Generation " + ge.lastRunGenerations());
		System.out.println("Solution: " + solution.getPhenotype().value() + " (" + solution.rawFitness() + ")\n");
				
		return solution;
		
	}
	
	/**
	 * This method demonstrates the use of Grammatical Evolution
	 * in a Trigonometric Identity problem.
	 * 
	 * @param populationSize The size of the initial population.
	 * @param maxGenerations The maximum number of generations to be evolved.
	 * @return The solution of the Trigonometric Identity experiment.
	 */
	public Individual<String, String> tiExperimentGE(int populationSize, int maxGenerations) {
		
		Individual<String, String> solution = null;
		String expression = "Math.cos(2 * x)";
		
		// Java Compiler
		JikesCompiler compiler = new JikesCompiler(
									new File(ConfigurationSettings.getInstance().getJikesCompiler()), 
									new File(ConfigurationSettings.getInstance().getJikesBootClassPath()));
		// Problem Specification
		SymbolicRegression sr = new SymbolicRegression(expression, 20, 0, 2 * Math.PI, true, compiler, new File(ConfigurationSettings.getInstance().getWorkingDirectoryPath()));
		// Log File
		LogFile log = new LogFile(new File(ConfigurationSettings.getInstance().getWorkingDirectoryPath() + "\\EAExperiments_GE_TI.txt"));
		log = null;
		
		// BNF Grammar
		BNFGrammar bnf = null;
		try {
			bnf = new BNFGrammar(new File(ConfigurationSettings.getInstance().getRootPath() + "\\res\\bnf\\TrigonometricIdentityRestricted.bnf"));
		}
		catch(IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		// Grammatical Evolution
		GrammaticalEvolution ge = new GrammaticalEvolution(bnf, sr, populationSize, 8, 20, 30);
		ge.setCrossoverRate(0.9);
		ge.setMutationRate(0.01);
		ge.setDuplicationRate(0.01);
		ge.setPruningRate(0.01);
		ge.setGenerationGap(0.9);
		ge.setMaxGenerations(maxGenerations);
		ge.setLogger(log);		
		
		System.out.println("GE Trigonometric Identity Experiment");
		System.out.println("Expression: " + expression);
		solution = ge.run();
		System.out.println("Generation " + ge.lastRunGenerations());
		System.out.println("Solution: " + solution.getPhenotype().value() + " (" + solution.rawFitness() + ")\n");
			
		return solution;
	}
	
	

	/*
	 * HAMMING DISTANCE PROBLEMS: GE, SGA, SSGA
	 */	
		
	/**
	 * This method demonstrates the use of Grammatical Evolution
	 * in a Hamming Distance problem (finding a specific string).
	 * 
	 * @return The solution of the Hamming Distance experiment.
	 */
	public Individual<String, String> hdExperimentGE() {
		
		Individual<String, String> solution = null;
		String target = "111000111000101010101010101010";
		
		// Problem Specification
		HammingDistance hd = new HammingDistance(target);
		// Log File
		LogFile log = new LogFile(new File(ConfigurationSettings.getInstance().getWorkingDirectoryPath() + "\\EAExperiments_GE_HD.txt"));
		log = null;
		
		// BNF Grammar
		BNFGrammar bnf = null;
		try {
			bnf = new BNFGrammar(new File(ConfigurationSettings.getInstance().getRootPath() + "\\res\\bnf\\BinaryGrammarFixedLength.bnf"));
		}
		catch(IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		// Grammatical Evolution
		GrammaticalEvolution ge = new GrammaticalEvolution(bnf, hd, 50, 8, 20, 40);
		ge.setCrossoverRate(0.9);
		ge.setMutationRate(0.01);
		ge.setDuplicationRate(0.01);
		ge.setPruningRate(0.01);
		ge.setGenerationGap(0.9);
		ge.setMaxGenerations(100);
		ge.setLogger(log);
				
		System.out.println("GE Hamming Distance Experiment");
		System.out.println("Target: " + target);
		solution = ge.run();
		System.out.println("Generation " + ge.lastRunGenerations());
		System.out.println("Solution: " + solution.getPhenotype().value() + " (" + solution.rawFitness() + ")\n");
					
		return solution;
	}	
	
	/**
	 * This method demonstrates the use of the Standard Genetic Algorithm
	 * in a Hamming Distance problem (finding a specific string).
	 * 
	 * @return The solution of the Hamming Distance experiment.
	 */
	public Individual<String, String> hdExperimentGA() {
		
		Individual<String, String> solution = null;
		String target = "111000111000101010101010101010";
				
		// Problem Specification
		HammingDistance hd = new HammingDistance(target);
		// Log File
		LogFile log = new LogFile(new File(ConfigurationSettings.getInstance().getWorkingDirectoryPath() + "\\EAExperiments_GA_HD.txt"));
		log = null;
		
		// Standard GA
		StandardGA ga = new StandardGA(50, 1, 30, 30, hd);
		
		ga.setFixedSizeGenome(true);
		ga.setCrossoverRate(0.9);
		ga.setMutationRate(0.01);
		ga.setDuplicationRate(0.01);
		ga.setPruningRate(0.01);
		ga.setMaxGenerations(100);
		ga.setLogger(log);

		
		System.out.println("GA Hamming Distance Experiment");
		System.out.println("Target: " + target);
		solution = ga.run();
		System.out.println("Generation " + ga.lastRunGenerations());
		System.out.println("Solution: " + solution.getPhenotype().value() + " (" + solution.rawFitness() + ")\n");
				
		return solution;
	}
	
	/**
	 * This method demonstrates the use of the Steady-State Genetic Algorithm
	 * in a Hamming Distance problem (finding a specific string).
	 * 
	 * @return The solution of the Hamming Distance experiment.
	 */
	public Individual<String, String> hdExperimentSSGA() {
		
		Individual<String, String> solution = null;
		String target = "111000111000101010101010101010";
				
		// Problem Specification
		HammingDistance hd = new HammingDistance(target);
		// Log File
		LogFile log = new LogFile(new File(ConfigurationSettings.getInstance().getWorkingDirectoryPath() + "\\EAExperiments_SSGA_HD.txt"));
		log = null;
		
		// Steady-State GA
		SteadyStateGA ssga = new SteadyStateGA(50, 1, 30, 30, hd);
		
		ssga.setFixedSizeGenome(true);
		ssga.setCrossoverRate(0.9);
		ssga.setMutationRate(0.01);
		ssga.setDuplicationRate(0.01);
		ssga.setPruningRate(0.01);
		ssga.setGenerationGap(0.9);
		ssga.setMaxGenerations(100);
		ssga.setLogger(log);
		
		System.out.println("SSGA Hamming Distance Experiment");
		System.out.println("Target: " + target);
		solution = ssga.run();
		System.out.println("Generation " + ssga.lastRunGenerations());
		System.out.println("Solution: " + solution.getPhenotype().value() + " (" + solution.rawFitness() + ")\n");
				
		return solution;
		
	}	


}
