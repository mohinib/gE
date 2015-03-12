package main;

import java.util.Vector;

import bangor.aiia.jge.bnf.BNFGrammar;
import bangor.aiia.jge.bnf.BNFParser;
import bangor.aiia.jge.bnf.InvalidBNFException;
import bangor.aiia.jge.core.GrammaticalEvolution;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.ps.HammingDistance;
import bangor.aiia.jge.util.ConfigurationSettings;
import bangor.aiia.jge.util.LogFile;

public class HD {
	static Vector<String> rules = null;
	 static String bnf1=null;
	 static String rootPath = ConfigurationSettings.getInstance().getRootPath() + "/bnf/";

	public static Individual<String, String> hdExperiment() throws InvalidBNFException { 
	bnf1 = BNFParser.loadBNFGrammar(rootPath + "HDGrammar.bnf");
	Individual<String, String> solution = null;
	String target = "111000111000101010101010101010"; 
	LogFile log = null;
	HammingDistance hd = new HammingDistance(target);
	BNFGrammar bnf = new BNFGrammar(bnf1); 
	GrammaticalEvolution ea = new GrammaticalEvolution(bnf, hd, 50, 8, 20, 40);
	ea.setCrossoverRate(0.9);
	ea.setMutationRate(0.01);
	ea.setDuplicationRate(0.01);
	ea.setPruningRate(0.01);
	ea.setMaxGenerations(100);
	ea.setLogger(log);
	solution = ea.run();
	// Also the following information can be retrieved:
	//Number_of_Generations_created = ea.lastRunGenerations(); 
	double Sol_Fitness_Value = solution.rawFitness();
	System.out.println("Solution's raw fitness is: " +Sol_Fitness_Value);
	String Sol_phenotype = solution.getPhenotype().value();
	System.out.println("Solution's phenotype: " +Sol_phenotype);
	return solution;
	}
	public static void main(String[] args) throws Exception
	{
		Individual<String, String> solutionM = hdExperiment();
		System.out.println("Solution is: " +solutionM);
		
	}
}
