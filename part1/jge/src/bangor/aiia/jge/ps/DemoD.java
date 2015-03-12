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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import bangor.aiia.jge.core.Core;
import bangor.aiia.jge.core.Evaluator;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.Population;
import bangor.aiia.jge.ps.BestFit;
import bangor.aiia.jge.ps.Bin;
import bangor.aiia.jge.ps.FirstFit;
import bangor.aiia.jge.ps.WorstFit;



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
public class DemoD implements Evaluator <String, String> {

private List<Bin> target = new ArrayList<Bin>();
private int min, max, binSize = 0;
private double avg = 0;
private List<Integer> bins = null;
private List<Bin> targetBins = new ArrayList<Bin>();
	
	// Flag whether a solution has be found or not
	private boolean solutionFound = false;
	
	/**
	 * Default Constructor. Should not be used.
	 */
	private DemoD() {
		super();
	}
	
	/**
	 * Constructor.
	 * @param target The target string that must be found.
	 */
	public DemoD(List<Bin> target, int min, int max, double avg, int size) {
		this();
		this.target = target;	
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.binSize = size;
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
		int length = target.size();
		Individual<String, String> individual;
		String current = null;
		System.out.println(size);
	
		// Iterate the population
		for (int i = 0; i < size; i++) {
			System.out.println("i= " +i);
			targetBins = target;
			individual = population.getIndividual(i);
			current = individual.getPhenotype().value();
			System.out.println(current);
			if(current.contains("<")){
				individual.setValid(false);
				System.out.println("false");
			}
			else {
			String[] currently = current.split("\\s");
			int j=0;
			List<Bin> objBin = new ArrayList<Bin>();
			
			//List<Bin> temp = new ArrayList<Bin>();
			//for(int a= 0 ; a<100; a++){
				bins=new ArrayList<Integer>();
			while(j < currently.length)
			{
				ExecPhenotype(currently[j++], targetBins, objBin);
				
			}
			/*if(temp.size() < objBin.size()){
				temp = objBin;
			}
			}*/
				
			System.out.println();
				// Assign Raw Fitness and set Individual as Valid
			double fitness,sum = 0;
			for(Bin bins: targetBins){
				sum += (bins.currentSize/bins.maxSize);
			}
			fitness = 1 - Math.pow((sum/length), 2);
			System.out.println("Fitness = " +fitness);		
				individual.setRawFitnessValue(fitness);
				individual.setValid(true);
				
				// Check if a solution is found
				
			}
			
		}
		
	}
	
	private void ExecPhenotype(String Pheno, List<Bin> targetBins,List<Bin> objBins) {
		System.out.println("Pheno = " +Pheno);		
		
		List<Integer> temp = new ArrayList<Integer>();
		List<Bin> output = new ArrayList<Bin>();
		if(Pheno.contains("=")){
		String[] vals = Pheno.split("=");
		String[] values=vals[1].split(",");
		int number=0;
		String removeVal = "";
		if(Pheno.contains("filled")||Pheno.contains("random")){
			number=Integer.parseInt(values[0]);
			double ignoreVal=Double.parseDouble(values[1]);
			removeVal=values[2];
			if(Pheno.contains("highest")){
				temp = highest_filled(number, ignoreVal, removeVal);
			}
			else if(Pheno.contains("lowest")){
				temp = lowest_filled(number, ignoreVal, removeVal);
			}
			else{
				temp = random_bin(number, ignoreVal, removeVal);
			}
		}
		else if(Pheno.contains("gap")){
			number=Integer.parseInt(values[0]);
			String threshold=values[1];			
			double ignoreVal=Double.parseDouble(values[2]);
			removeVal=values[3];
			double thres = 0;
			switch(threshold){
			case "average": {
				thres = avg;
				break;
			}
			case "maximum":{
				thres = max;
				break;
			}
			case "minimum":{
				thres = min;
				break;
			}
			default:{
				break;
			}
			}
			temp = gap_less_than(number, thres, ignoreVal, removeVal);
		}
		else if(Pheno.contains("num_of_pieces")){
			number=Integer.parseInt(values[0]);
			int numpieces=Integer.parseInt(values[1]);
			double ignoreVal=Double.parseDouble(values[2]);
			removeVal=values[3];
			temp = num_of_pieces(number, numpieces, ignoreVal, removeVal);
		}
	for(Integer el : temp){
		bins.add(el);
	}
		}
		else{
			if(Pheno.contains("best")){
				output = best_fit_decreasing(bins);
			}
			else if(Pheno.contains("worst")){
				output = worst_fit_decreasing(bins);
			}
			else if(Pheno.contains("first")){
				output = first_fit_decreasing(bins);
			}
			
			if(output != null && output.size() < objBins.size()){
				objBins = output;
			}
		}
		
	}

	private List<Bin> first_fit_decreasing(List<Integer> bins) {	
		Collections.sort(bins, Collections.reverseOrder()); // sort
        FirstFit ffd = new FirstFit(bins, binSize);
        List<Bin> obj = new ArrayList<Bin>();
        obj = ffd.getResult();
        for(Bin binobj : obj){
        	targetBins.add(binobj);
        }
        return targetBins;
        //ffd.printBestBins();
	}

	private List<Bin> worst_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
        WorstFit wfd = new WorstFit(bins, binSize);
        List<Bin> obj = new ArrayList<Bin>();
        obj = wfd.getResult();
        for(Bin binobj : obj){
        	targetBins.add(binobj);
        }
        //wfd.printBestBins();
        return targetBins;
	}

	private List<Bin> best_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
        BestFit bfd = new BestFit(bins, binSize);
        List<Bin> obj = new ArrayList<Bin>();
        obj = bfd.getResult();
        for(Bin binobj : obj){
        	targetBins.add(binobj);
        }
        return targetBins;
        // bfd.printBestBins();
	}

	private List<Integer> gap_less_than(int num, double threshold, double ignore,String remove) {
		List<Integer> returnBins = new ArrayList<Integer>();
		Random randomizer = new Random();
		int i=0;
		int sizee=0;
		while(i<num){
			if(targetBins.size() >= num){	
			int k = randomizer.nextInt(targetBins.size());
			sizee= targetBins.get(k).items.size();
			if(ignore != 1.1){
			if((targetBins.get(k).maxSize - targetBins.get(k).currentSize) < threshold && targetBins.get(k).currentSize < ignore*targetBins.get(k).maxSize){
				if(remove.equals("ONE")){
					returnBins.add(targetBins.get(k).items.get(randomizer.nextInt(sizee)));
				}
				else if(remove.equals("ALL")){
					for(int j=0; j < targetBins.get(k).numberOfItems(); j++){
						returnBins.add(targetBins.get(k).items.get(j));
					}
				}
				targetBins.remove(k);
			}
			}
			else if(ignore == 1.1){
				if(remove.equals("ONE")){
					returnBins.add(targetBins.get(k).items.get(randomizer.nextInt(sizee)));
				}
				else if(remove.equals("ALL")){
					for(int j=0; j < targetBins.get(k).numberOfItems(); j++){
						returnBins.add(targetBins.get(k).items.get(j));
					}
				}
				targetBins.remove(k);
			}
			i++;
		}
			}
		return returnBins;
		//System.out.println("I am gap_less_than(" +num +"," +threshold +"," +ignore +"," +remove +")");
		
	}
	
	private List<Integer> num_of_pieces(int num, int numpieces, double ignore,String remove) {
		List<Integer> returnBins = new ArrayList<Integer>();
		List<Bin> toRemoveBins = new ArrayList<Bin>();
		int counter= 0;
		Random randomizer = new Random();
		for(Bin bins: targetBins){
			if(targetBins.size() >= num){
			if(ignore != 1.1){
			if(counter < num && bins.numberOfItems() == numpieces && bins.currentSize < ignore*bins.maxSize){
				if(remove.equals("ONE")){
					int sizee= bins.items.size();
					returnBins.add(bins.items.get(randomizer.nextInt(sizee)));
				}
				else if(remove.equals("ALL")){
					for(int i = 0; i < bins.numberOfItems(); i++){
						returnBins.add(bins.items.get(i));
					}
				}
				counter++;
				toRemoveBins.add(bins);
				//targetBins.remove(bins);
			}			
			}
			else if(ignore == 1.1){ 
				if(counter < num && bins.numberOfItems() == numpieces){
					if(remove.equals("ONE")){
						int sizee=bins.items.size();
						returnBins.add(bins.items.get(randomizer.nextInt(sizee)));
					}
					else if(remove.equals("ALL")){
						for(int i = 0; i < bins.numberOfItems(); i++){
							returnBins.add(bins.items.get(i));
						}
					}
					counter++;
					toRemoveBins.add(bins);
				}
			}
			}
		}
			for(Iterator<Bin> itr = targetBins.iterator(); itr.hasNext();){
				Bin obj = itr.next();
				for(Bin binR : toRemoveBins){
				if(obj.equals(binR)){itr.remove();}
				}
			}
		
		return returnBins;
		//System.out.println("I am num_of_pieces(" +num +"," +numpieces +"," +ignore +"," +remove +")");
		
	}

	private List<Integer> highest_filled(int num, double ignore, String remove) {
		Collections.sort(targetBins, new Comparator<Bin>(){
			  public int compare(Bin bin1, Bin bin2) {
				    return bin1.currentSize < bin2.currentSize ? 1
				         : bin1.currentSize > bin2.currentSize ? -1
				         : 0;
				  }
		}); // sort input by bin size // sort input by size (big to small)
		List<Integer> returnBins = new ArrayList<Integer>();
		Random randomizer = new Random();
		
    	for(int i=0; i < num; i++){
    		if(targetBins.size() >= num){
    		//int size= targetBins.get(i).items.size();
    		if(ignore != 1.1){
    			if(targetBins.get(i).currentSize < ignore*targetBins.get(i).maxSize){
    				if(remove.equals("ONE")){
    					
    					returnBins.add(targetBins.get(i).items.get(randomizer.nextInt(targetBins.get(i).items.size())));
    				}
    				else if(remove.equals("ALL")){
    					for(int j=0; j < targetBins.get(i).numberOfItems(); j++){
    						returnBins.add(targetBins.get(i).items.get(j));
    					}
    				}
    				targetBins.remove(i);
    			}    		
    		}
    		else if(ignore == 1.1){    			
    			if(remove.equals("ONE")){
					returnBins.add(targetBins.get(i).items.get(randomizer.nextInt(targetBins.get(i).items.size())));
				}
				else if(remove.equals("ALL")){
					for(int j=0; j < targetBins.get(i).numberOfItems(); j++){
						returnBins.add(targetBins.get(i).items.get(j));
					}
				}
    			targetBins.remove(i);
    		}
    	}
		}
    	return returnBins;
		//System.out.println("I am highest_filled(" +num +"," +ignore +"," +remove +")");
	}
	
	private List<Integer> lowest_filled(int num, double ignore, String remove) {
		Collections.sort(targetBins, new Comparator<Bin>(){
			  public int compare(Bin bin1, Bin bin2) {
				    return bin1.currentSize < bin2.currentSize ? -1
				         : bin1.currentSize > bin2.currentSize ? 1
				         : 0;
				  }
		}); // sort input by bin size 
		List<Integer> returnBins = new ArrayList<Integer>();
    	Random randomizer = new Random();
    	
    	for(int i=0; i < num; i++){
    		if(targetBins.size() >= num){
    		
    		if(ignore != 1.1){
    			if(targetBins.get(i).currentSize < ignore*targetBins.get(i).maxSize){
    				if(remove.equals("ONE")){
    					System.out.println("targetBins.get(i).numberOfItems() = " +targetBins.get(i).numberOfItems());
    					returnBins.add(targetBins.get(i).items.get(randomizer.nextInt(targetBins.get(i).numberOfItems())));
    						
    				}
    				else if(remove.equals("ALL")){
    					for(int j=0; j < targetBins.get(i).numberOfItems(); j++){
    						returnBins.add(targetBins.get(i).items.get(j));
    					}
    				}
    			}  
    			targetBins.remove(i);
    		}
    		else if(ignore == 1.1){    			
    			/*if(remove.equals("ONE")){
					returnBins.add(targetBins.get(i).items.get(randomizer.nextInt(targetBins.get(i).numberOfItems())));
				}
				else if(remove.equals("ALL")){
					for(int j=0; j < targetBins.get(i).numberOfItems(); j++){
						returnBins.add(targetBins.get(i).items.get(j));
					}
				}
    			targetBins.remove(i);*/
    			if(targetBins.get(i).currentSize < ignore*targetBins.get(i).maxSize){
    				if(remove.equals("ONE")){
    					
    					returnBins.add(targetBins.get(i).items.get(randomizer.nextInt(targetBins.get(i).numberOfItems())));
    				}
    				else if(remove.equals("ALL")){
    					for(int j=0; j < targetBins.get(i).numberOfItems(); j++){
    						returnBins.add(targetBins.get(i).items.get(j));
    					}
    				}
    			}  
    			targetBins.remove(i);
    		}
    		}
    	}
    	
    	return returnBins;
		//System.out.println("I am lowest_filled(" +num +"," +ignore +"," +remove +")");
	}
	
	private List<Integer> random_bin(int num, double ignore, String remove) {
		List<Integer> returnBins = new ArrayList<Integer>();
    	Random randomizer = new Random();
    	
    	for(int i=0; i < num; i++){
    		if(targetBins.size() > num){
    		//int size= targetBins.get(i).items.size();
    		int k = randomizer.nextInt(targetBins.size());
    		if(ignore != 1.1){    			
    			if(targetBins.get(k).currentSize < ignore*targetBins.get(k).maxSize){
    				if(remove.equals("ONE")){
    					returnBins.add(targetBins.get(k).items.get(randomizer.nextInt(targetBins.get(k).items.size())));
    				}
    				else if(remove.equals("ALL")){
    					for(int j=0; j < targetBins.get(k).numberOfItems(); j++){
    						returnBins.add(targetBins.get(k).items.get(j));
    					}
    				}
    				targetBins.remove(k);
    			}    		
    		}
    		else if(ignore == 1.1){    			
    			if(remove.equals("ONE")){
					returnBins.add(targetBins.get(k).items.get(randomizer.nextInt(targetBins.get(k).items.size())));
				}
				else if(remove.equals("ALL")){
					for(int j=0; j < targetBins.get(k).numberOfItems(); j++){
						returnBins.add(targetBins.get(k).items.get(j));
					}
				}
    			targetBins.remove(k);
    		}
    	}
    	}
    	return returnBins;
		//System.out.println("I am random_bin(" +num +"," +ignore +"," +remove +")");
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
	public List<Bin> getTarget() {
		return target;
	}

	/**
	 * Sets the target string to be found.
	 * 
	 * @param target The target string to be found.
	 */
	public void setTarget(List<Bin> target) {
		this.target = target;
	}

}
