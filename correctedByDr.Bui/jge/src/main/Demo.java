package main;

import java.io.BufferedReader;
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
import bangor.aiia.jge.ps.Bin;
import bangor.aiia.jge.ps.DemoD;
import bangor.aiia.jge.ps.FirstFit;
import bangor.aiia.jge.util.ConfigurationSettings;
import bangor.aiia.jge.util.LogFile;

public class Demo {
	static Vector<String> rules = null;
	static String bnf1 = null;
	static String rootPath = ConfigurationSettings.getInstance().getRootPath();

	public static Individual<String, String> hdExperiment(List<Bin> newobj,
			int min_elem, int max_elem, double average_elem, int size)
			throws InvalidBNFException, IOException {
		bnf1 = BNFParser.loadBNFGrammar(rootPath + "/bnf/HDGrammar11.bnf");
		Individual<String, String> solution = null;
		LogFile log = null;
		DemoD hd = new DemoD(newobj, min_elem, max_elem, average_elem, size);
		BNFGrammar bnf = new BNFGrammar(bnf1);
		System.out.println(bnf);
		GrammaticalEvolution ea = new GrammaticalEvolution(bnf, hd, 50, 8, 10,
				50);
		ea.setCrossoverRate(0.9);
		ea.setMutationRate(0.01);
		ea.setDuplicationRate(0.01);
		ea.setPruningRate(0.01);
		ea.setMaxGenerations(50);
		ea.setLogger(log);
		/*File file = new File("30RunFile.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		*/
		//for(int i=0;i<30;i++){
			//fw.write("\r\n i=" +i +"\r\n");
			solution = ea.run();
			//fw.write("Phenotype: " +solution.getPhenotype().value() +" \r\n Number of bins:" +solution.numBins() +"\r\n");
			
		//}
		// Also the following information can be retrieved:
		// Number_of_Generations_created = ea.lastRunGenerations();

		String Sol_phenotype = solution.getPhenotype().value();
		System.out.println("Solution's phenotype: " + Sol_phenotype);
		double Sol_Fitness_Value = solution.rawFitness();
		System.out.println("Solution's raw fitness is: " + Sol_Fitness_Value);
		int Sol_Number_Bins = solution.numBins();
		System.out.println("Solution's number of bins is: " + Sol_Number_Bins);
		return solution;
	}

	private static double calculateAverage(List<Integer> in) {
		Integer sum = 0;
		if (!in.isEmpty()) {
			for (Integer el : in) {
				sum += el;
			}
			return sum.doubleValue() / in.size();
		}
		return sum;
	}

	public static void main(String[] args) throws Exception {
		String[] content = null;
		Scanner kb = new Scanner(System.in);
		System.out.println(" Please enter the file name. ");
		String filename = kb.nextLine();
		InputStream input = new FileInputStream(rootPath
				+ "/jge/src/bangor/aiia/jge/ps/" + filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		
		//Individual<String, String> obj = new Individual<String, String>();
		List<List<Integer>> group = null;
		int[] size = null;
		String line, t;
		int instances = 0;
		while ((line = reader.readLine()) != null) {
			instances = Integer.parseInt(line);
			size = new int[instances];
			group = new ArrayList<List<Integer>>(instances);
			content = new String[instances];
			List<Integer> in = null;
			line = reader.readLine();
			for (int a = 0; a < instances; a++) {
				int index = 0;
				String con;
				if (line != null && (line.charAt(0) == 't' || line.charAt(0) == 's' || line.charAt(0) == 'u')) {
					in = new ArrayList<Integer>();
					con = reader.readLine();
					content[a] = con;
					String[] splited = con.split("\\s+");
					if(line.charAt(0) == 's'){
						size[a] = Integer.parseInt(splited[0]);
					}else {
					size[a] = (int) (10*Double.parseDouble(splited[0]));
					}
					while (index < Integer.parseInt(splited[1])) {
						if((t = reader.readLine())!= null){
							if(line.charAt(0) == 's'){
								in.add(Integer.parseInt(t));
							}
							else{
								in.add((int) (10*Double.parseDouble(t)));
							}
						}
						index++;
					}
					group.add(in);
					line = reader.readLine();
				} else if (line == null) {
					break;
				}

			}
		}
		reader.close();
		System.out.println("group size " + group.size());
		/*
		 * file file 1 is for testing purpose only. It is recommended to clean
		 * all the code associated with it for the final submission.
		 */
		/*
		 * File file1 = new File("filenameN.txt");
		 * 
		 * // if file doesnt exists, then create it if (!file1.exists()) {
		 * file1.createNewFile(); }
		 * 
		 * FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
		 * BufferedWriter bw1 = new BufferedWriter(fw1);
		 */
		File file = new File("filename_updated2day.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		for (int b = 0; b < group.size(); b++) {
			//for (int a = 0; a < 30; a++) {
			Individual<String, String> obj = new Individual<String, String>();
			int temp = 0;
			//int best_sol = Integer.MAX_VALUE;
			int min_elem = Collections.min(group.get(b));
			int max_elem = Collections.max(group.get(b));
			double average_elem = calculateAverage(group.get(b));
			//Collections.sort(group.get(b), Collections.reverseOrder());
			//FirstFit ff = new FirstFit(group.get(b), size[b]);
			//List<Bin> newobj = new ArrayList<Bin>();
			//newobj = ff.getResult();
			fw.write("\r \n Solution for " + content[b] + ": ");
			for (int a = 0; a < 30; a++) {
				
				FirstFit ff = new FirstFit(group.get(b), size[b]);
				List<Bin> newobj = new ArrayList<Bin>();
				newobj = ff.getResult();
				
				// bw1.write("\n First Fit " + ":" + newobj.size());
				obj = hdExperiment(newobj, min_elem, max_elem, average_elem,size[b]);
				
				temp = obj.getNumberBins();
				Collections.shuffle(group.get(b));
				//if (temp < best_sol) {
					//best_sol = temp;
					// bw1.write("\n Best solution " + content[b] + ":" +
					// best_sol);

				//}
				fw.write("\r\n" + obj.getPhenotype().value());
			}
			
		}

		fw.close();
	}
}