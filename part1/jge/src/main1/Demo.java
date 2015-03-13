package main1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import bangor.aiia.jge.bnf.BNFGrammar;
import bangor.aiia.jge.bnf.BNFParser;
import bangor.aiia.jge.bnf.InvalidBNFException;
import bangor.aiia.jge.core.GrammaticalEvolution;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.ps.DemoD;
import bangor.aiia.jge.ps.FirstFit;
import bangor.aiia.jge.util.ConfigurationSettings;
import bangor.aiia.jge.util.LogFile;

public class Demo {
	static Vector<String> rules = null;
	 static String bnf1=null;
	 static String rootPath = ConfigurationSettings.getInstance().getRootPath();

	public static Individual<String, String> hdExperiment(FirstFit ff, int min_elem, int max_elem, double average_elem, int size) throws InvalidBNFException, IOException { 
	bnf1 = BNFParser.loadBNFGrammar(rootPath + "/bnf/HDGrammar11.bnf");
	Individual<String, String> solution = null;
	//String target = "111000111000101010101010101010"; 
	
	LogFile log = null;
	DemoD hd = new DemoD(ff.getResult(), min_elem, max_elem, average_elem, size);
	BNFGrammar bnf = new BNFGrammar(bnf1); 
	System.out.println(bnf);
	GrammaticalEvolution ea = new GrammaticalEvolution(bnf, hd, 50, 8, 10, 50);
	ea.setCrossoverRate(0.9);
	ea.setMutationRate(0.01);
	ea.setDuplicationRate(0.01);
	ea.setPruningRate(0.01);
	ea.setMaxGenerations(50);
	ea.setLogger(log);
	solution = ea.run();

	
	// Also the following information can be retrieved:
	//Number_of_Generations_created = ea.lastRunGenerations(); 
	
  String Sol_phenotype = solution.getPhenotype().value();
	System.out.println("Solution's phenotype: " +Sol_phenotype);
	double Sol_Fitness_Value = solution.rawFitness();
	System.out.println("Solution's raw fitness is: " +Sol_Fitness_Value);
	int Sol_Number_Bins = solution.numBins();
	System.out.println("Solution's number of bins is: " +Sol_Number_Bins);
	return solution;
	}
	  private static double calculateAverage(List<Integer> in) {
    	  Integer sum = 0;
    	  if(!in.isEmpty()) {
    	    for (Integer el : in) {
    	        sum += el;
    	    }
    	    return sum.doubleValue() / in.size();
    	  }
    	  return sum;
    	}
	public static void main(String[] args) throws Exception
	{
		//Individual<String, String> solutionM = hdExperiment();
		//int iterate=0;
		//while(iterate<5)
		//{
			//System.out.println(iterate);
			//solutionM=hdExperiment();
			//iterate++;
			//System.out.println("Solution is: " +solutionM);
		//}
		int size = 0;
		int count = 0;
		String content=""; 
		Scanner kb = new Scanner(System.in);       
	    System.out.println(" Please enter the file name. " );
	    String filename = kb.nextLine();
	    InputStream input = new FileInputStream(rootPath +"/jge/src/bangor/aiia/jge/ps/" + filename);
	    List<Integer> in = new ArrayList<Integer>(); 
	    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    Individual<String, String> obj = new Individual<String, String>();
	    
	    String line; 
	    while((line = reader.readLine()) != null){
	    	if(count < 2){        		
	    		if(count == 0){count++;}
	    		else{
	    			String[] splited = line.split("\\s+");
	    			content = line;
	    			size = Integer.parseInt(splited[0]);
	    			count++;
	    		}
	    	}
	    	else{
	    	in.add(Integer.parseInt(line));
	    	}
	    }
	    reader.close();
	    int best_sol = Integer.MAX_VALUE;
	    int min_elem = Collections.min(in);
	    int max_elem = Collections.max(in);
	    double average_elem = calculateAverage(in);
	 for(int a=0; a< 50; a++){
	    Collections.sort(in, Collections.reverseOrder()); 
	    FirstFit ff = new FirstFit(in, size);
	    System.out.println(" a = " + a);
		obj = hdExperiment(ff, min_elem, max_elem, average_elem, size);
		int temp=obj.numBins();
		if(temp<best_sol){
			best_sol=temp;
		}
	 }
	 File file = new File("filename.txt");
	 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("Best solution for" + content +  ":" + best_sol);
		bw.close();

	}
}
