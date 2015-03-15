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
 * The class <code>HammnigDistance</code> implements a problem specification of
 * finding a target string. Namely, it compares the Hamming Distance between the
 * phenotypes of the individuals of the population and the target string.<br>
 * The phenotypes must be fixed-length strings. <br>
 * <br>
 * In information theory, the Hamming distance between two strings of equal
 * length is the number of positions for which the corresponding symbols are
 * different.<br>
 * Put another way, it measures the number of substitutions required to change
 * one into the other, or the number of errors that transformed one string into
 * the other.
 * 
 * @author Loukas Georgiou
 * @version 1.0, 15/04/06
 * @see Evaluator
 * @see Population
 * @see Individual
 * @see Core
 * @since JavaGE 0.1
 */
public class DemoD implements Evaluator<String, String> {

	private List<Bin> target = new ArrayList<Bin>();
	private int min, max, binSize = 0;
	private double avg = 0;
	private List<Integer> bins = null;
	private List<Bin> targetBins = null;

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
	 * 
	 * @param target
	 *            The target string that must be found.
	 */
	public DemoD(List<Bin> target, int min, int max, double avg, int size) {
		this();
		this.target = target;
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.binSize = size;
	}

	public List<Bin> deepCopy(List<Bin> bins) {
		ArrayList<Bin> copy = new ArrayList<Bin>();
		for (int i = 0; i < bins.size(); i++) {
			copy.add(bins.get(i).deepCopy());
		}
		return copy;
	}

	/**
	 * Evaluates the phenotype (which must be a fixed-length string) of each
	 * individual of the population and assigns the Raw Fitness Value according
	 * to the Hamming Distance between the phenotype and the target string.<br>
	 * If an individual's phenotype has not the same length with the target
	 * string then the individual is invalid (sets valid to false).<br>
	 * <br>
	 * The Raw Fitness is calculated with the following formula:<br>
	 * <code>Raw Fitness = Phenotype Length - Hamming Distance.</code>
	 * 
	 * @param population
	 *            The population to be evaluated.
	 */
	public void evaluate(Population<String, String> population) {

		solutionFound = false;
		int size = population.size();
		// int length = target.size();
		Individual<String, String> individual;
		String current = null;
		System.out.println(size);

		// Iterate the population
		for (int i = 0; i < size; i++) {
			System.out.println("i= " + i);

			// Collections.copy(targetBins, target);
			individual = population.getIndividual(i);
			current = individual.getPhenotype().value();
			System.out.println(current);
			if (current.contains("<")) {
				individual.setValid(false);
				System.out.println("false");
			} else {
				
				 List<Bin> objBin = new ArrayList<Bin>(); 
				 /* for(int k=0; k< 150;
				 * k++){ objBin.add(null); }
				 */
				// for(int a= 0 ; a<100; a++){
				String[] currently = current.split("\\s");
				int j = 0;

				// objBin = this.deepCopy(target);
				// List<Bin> temp = new ArrayList<Bin>();

				bins = new ArrayList<Integer>();
				targetBins = new ArrayList<Bin>();
				targetBins = this.deepCopy(target);
				while (j < currently.length) {
					if (targetBins.size() > 0) {
						objBin=ExecPhenotype(currently[j++]);
						
					} else {
						objBin=ExecPhenotype(currently[currently.length - 1]);
					}

				}
				// temp = this.deepCopy(targetBins);
				/*
				 * if(targetBins.size() < objBin.size()){ objBin = new
				 * ArrayList<Bin>(); objBin = this.deepCopy(targetBins); } }
				 */

				System.out.println();
				// Assign Raw Fitness and set Individual as Valid
				double fitness, sum = 0;
				System.out.println("Final Target Bin Size = " +objBin.size());
				if (objBin != null) {
					for (Bin bins : objBin) {
						sum += Math.pow((bins.currentSize / binSize), 2);
					}
					fitness = 1 - (sum / objBin.size());
					System.out.println("Fitness = " + 1 / (1 + fitness));
					fitness = 1 / (1 + fitness);
					individual.setRawFitnessValue(fitness);
					individual.setValid(true);
					individual.setNumberBins(objBin.size());
					//int numpieces = 0;
					// remove after testing
					/*
					 * for(Bin bins_obj : targetBins){ numpieces +=
					 * bins_obj.numberOfItems(); }
					 * System.out.println("num of pieces = " + numpieces +
					 * " bins = " + targetBins.size());
					 */
				}
				// Check if a solution is found

			}

		}

	}

	private List<Bin> ExecPhenotype(String Pheno) {
		System.out.println("Pheno = " + Pheno);
		List<Bin> output = new ArrayList<Bin>();
		if (Pheno.contains("=")) {
			String[] vals = Pheno.split("=");
			String[] values = vals[1].split(",");
			int number = 0;
			String removeVal = "";
			if (Pheno.contains("filled") || Pheno.contains("random")) {
				number = Integer.parseInt(values[0]);
				double ignoreVal = Double.parseDouble(values[1]);
				removeVal = values[2];
				if (Pheno.contains("highest")) {
					highest_filled(number, ignoreVal, removeVal);
				} else if (Pheno.contains("lowest")) {
					lowest_filled(number, ignoreVal, removeVal);
				} else {
					random_bin(number, ignoreVal, removeVal);
				}
			} else if (Pheno.contains("gap")) {
				number = Integer.parseInt(values[0]);
				String threshold = values[1];
				double ignoreVal = Double.parseDouble(values[2]);
				removeVal = values[3];
				double thres = 0;
				switch (threshold) {
				case "average": {
					thres = avg;
					break;
				}
				case "maximum": {
					thres = max;
					break;
				}
				case "minimum": {
					thres = min;
					break;
				}
				default: {
					break;
				}
				}
				gap_less_than(number, thres, ignoreVal, removeVal);
			} else if (Pheno.contains("num_of_pieces")) {
				number = Integer.parseInt(values[0]);
				int numpieces = Integer.parseInt(values[1]);
				double ignoreVal = Double.parseDouble(values[2]);
				removeVal = values[3];
				num_of_pieces(number, numpieces, ignoreVal, removeVal);
			}
		} else {
			Random randomizer = new Random();
			Iterator<Bin> iter = targetBins.iterator();
			while(iter.hasNext()){
				Bin obj = iter.next();
				if(obj.visited){
					if(obj.all==true){
						//System.out.println("n= " +obj.numberOfItems());
						
						for(Integer item_obj : obj.items){
						//System.out.println("value " + item_obj);
						
						bins.add(item_obj);
					}
						iter.remove();
				}else{
							int i=randomizer.nextInt(obj.numberOfItems());
							int item = obj.items.get(i);
							bins.add(item);
							obj.items.remove(i);
							obj.currentSize=obj.currentSize-item;
						}
				}
			}
			if (Pheno.contains("best")) {
				output = best_fit_decreasing(bins);
				//System.out.println("BFD Target Bin Size = " +output.size());
			} else if (Pheno.contains("worst")) {
				output = worst_fit_decreasing(bins);
				//System.out.println("WFD Target Bin Size = " +output.size());
			} else if (Pheno.contains("first")) {
				output = first_fit_decreasing(bins);
				//System.out.println("FFD Target Bin Size = " +output.size());
			}
		}
		return output;
	}

	private List<Bin> first_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
		FirstFit ffd = new FirstFit(bins, binSize);
		List<Bin> obj = new ArrayList<Bin>();
		obj = ffd.addBin(targetBins);
		//System.out.println("FFD Target Bin Size = " +obj.size());
		return obj;
	}

	private List<Bin> worst_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
		WorstFit wfd = new WorstFit(bins, binSize);
		List<Bin> obj = new ArrayList<Bin>();
		obj = wfd.addBin(targetBins);
		//System.out.println("WFD Target Bin Size = " +obj.size());
		return obj;
	}

	private List<Bin> best_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
		BestFit bfd = new BestFit(bins, binSize);
		List<Bin> obj = new ArrayList<Bin>();
		obj = bfd.addBin(targetBins);
		//System.out.println("BFD Target Bin Size = " +obj.size());
		return obj;
	}

	private void gap_less_than(int num, double threshold,
			double ignore, String remove) {
		Random randomizer = new Random();
		int k = 0;
		int counter = 0; 
		while (k < targetBins.size() && counter < num) {
			int i=randomizer.nextInt(targetBins.size());
			if ((binSize - targetBins.get(i).currentSize) < threshold && targetBins.get(i).currentSize < ignore* binSize) {
				if(targetBins.get(i).visited==false){
					targetBins.get(i).visited=true;	
					if (remove.equals("ALL")) {
							targetBins.get(i).all=true;
						}
				}
				counter++;
		}
			k++;
		}
	}

	private void num_of_pieces(int num, int numpieces, double ignore,
			String remove) {
		int counter = 0;
		int i=0;
		while(i < targetBins.size() && counter < num) {
			if (targetBins.get(i).numberOfItems() == numpieces && targetBins.get(i).currentSize<ignore*binSize) {
				
				if(targetBins.get(i).visited==false){
					targetBins.get(i).visited=true;	
					if (remove.equals("ALL")) {
						targetBins.get(i).all=true;
					}
			}
					counter++;
				}
			i++;	
			}
	
	}

	private void highest_filled(int num, double ignore, String remove) {
		Collections.sort(targetBins, new Comparator<Bin>() {
			public int compare(Bin bin1, Bin bin2) {
				return bin1.currentSize < bin2.currentSize ? 1
						: bin1.currentSize > bin2.currentSize ? -1 : 0;
			}
		}); // sort input by current bin size in non-increasing order.		

		int i=0;
		int counter = 0; 
		while(i<targetBins.size() && counter<num) {
				
				if (targetBins.get(i).numberOfItems() < ignore*binSize) {
					if(targetBins.get(i).visited==false){
						targetBins.get(i).visited=true;	
						if (remove.equals("ALL")) {
							targetBins.get(i).all=true;
							
						}
					}
						counter++;
					}
				i++;
		}	
		}

	private void lowest_filled(int num, double ignore, String remove) {
		Collections.sort(targetBins, new Comparator<Bin>() {
			public int compare(Bin bin1, Bin bin2) {
				return bin1.currentSize < bin2.currentSize ? -1
						: bin1.currentSize > bin2.currentSize ? 1 : 0;
			}
		}); // sort input by bin size in non-decreasing order
		int i=0;
		int counter = 0; 
		while(i<targetBins.size() && counter<num) {
				
				if (targetBins.get(i).numberOfItems() < ignore*binSize) {
					if(targetBins.get(i).visited==false){
						targetBins.get(i).visited=true;	
						if (remove.equals("ALL")) {
							targetBins.get(i).all=true;
							
						}
					}
						counter++;
					}
				i++;
		}	
		}

	private void random_bin(int num, double ignore, String remove) {
		Random randomizer = new Random();
		int k = 0;
		int counter = 0; 
		while (k < targetBins.size() && counter < num) {
			int i=randomizer.nextInt(targetBins.size());
			if (targetBins.get(i).numberOfItems() < ignore*binSize) {
				if(targetBins.get(i).visited==false){
					targetBins.get(i).visited=true;	
					if (remove.equals("ALL")) {
						targetBins.get(i).all=true;
						
					}
				}
					counter++;
				}
			k++;
		}
	}

	/**
	 * Returns true if the solution has been found.<br>
	 * For the Hamming Distance problem a solution is found when the current
	 * population contains an individual with phenotype equal to the target
	 * string.
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
	 * @param target
	 *            The target string to be found.
	 */
	public void setTarget(List<Bin> target) {
		this.target = target;
	}

}
