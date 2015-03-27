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
	private int bestBinSize = 0; // to replace with best bin size for fitness

	// Flag whether a solution has be found or not
	//private boolean solutionFound = false;

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

		//solutionFound = false;
		int size = population.size();
		Individual<String, String> individual;
		String current = null;
		System.out.println(size);

		// Iterate the population
		for (int i = 0; i < size; i++) {
			individual = population.getIndividual(i);
			current = individual.getPhenotype().value();
			System.out.println(current);
			if (current.contains("<")) {
				individual.setValid(false);
				System.out.println("false");
			} else {

				List<Bin> objBin = new ArrayList<Bin>();
				String[] currently = current.split("\\s");
				int k = 0;
				int j = 0;
				targetBins = new ArrayList<Bin>();
				targetBins = this.deepCopy(target);
				bestBinSize = targetBins.size();
				List<List<Bin>> temp = new ArrayList<List<Bin>>();
				while (k < 100) {
					bins = new ArrayList<Integer>();
					while (j < currently.length) {
						if (targetBins.size() > 0) {
							objBin = ExecPhenotype(currently[j++]);
						} else {
							objBin = ExecPhenotype(currently[currently.length - 1]);
						}
					}
					temp.add(objBin);
					if(objBin.size() < 	bestBinSize){
						targetBins = this.deepCopy(objBin);
						bestBinSize = objBin.size();
					}
					j = 0;
					k++;
				}
				int var = Integer.MAX_VALUE;
				int index = -1;
				for (int a = 0; a < temp.size(); a++) {
					// System.out.println( temp.get(a).size());
					if (var > temp.get(a).size()) {
						var = temp.get(a).size();
						index = a;
					}
				}
				if (index != -1)
					objBin = this.deepCopy(temp.get(index));
				// Assign Raw Fitness and set Individual as Valid
				double fitness, sum = 0;
				if (objBin != null) {
					for (Bin bins : objBin) {
						sum += Math.pow((bins.currentSize / binSize), 2);
					}
					fitness = 1 - (sum / objBin.size());
					System.out.println("Fitness = " + 1 / (1 + fitness));
					double set = 1 / (1 + fitness);
					individual.setRawFitnessValue(set);
					individual.setValid(true);
					individual.setNumberBins(objBin.size());
				}
				// Check if a solution is found

			}

		}

	}

	private List<Bin> ExecPhenotype(String Pheno) {
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
			while (iter.hasNext()) {
				Bin obj = iter.next();
				if (obj.visited) {
					if (obj.all == true) {
						for (Integer item_obj : obj.items) {
							bins.add(item_obj);
						}
						iter.remove();
					} else {
						if (obj.numberOfItems() > 0) {
							int i = randomizer.nextInt(obj.numberOfItems());
							int item = obj.items.get(i);
							bins.add(item);
							obj.items.remove(i);
							obj.currentSize = obj.currentSize - item;
							if (obj.numberOfItems() == 0) {
								iter.remove();
							}
						}

					}
				}
			}
			if (Pheno.contains("best")) {
				output = best_fit_decreasing(bins);
			} else if (Pheno.contains("worst")) {
				output = worst_fit_decreasing(bins);
			} else if (Pheno.contains("first")) {
				output = first_fit_decreasing(bins);
			}
		}
		return output;
	}

	private List<Bin> first_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
		FirstFit ffd = new FirstFit(bins, binSize);
		List<Bin> obj = new ArrayList<Bin>();
		obj = ffd.addBin(targetBins);
		return obj;
	}

	private List<Bin> worst_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
		WorstFit wfd = new WorstFit(bins, binSize);
		List<Bin> obj = new ArrayList<Bin>();
		obj = wfd.addBin(targetBins);
		return obj;
	}

	private List<Bin> best_fit_decreasing(List<Integer> bins) {
		Collections.sort(bins, Collections.reverseOrder()); // sort
		BestFit bfd = new BestFit(bins, binSize);
		List<Bin> obj = new ArrayList<Bin>();
		obj = bfd.addBin(targetBins);
		return obj;
	}

	private void gap_less_than(int num, double threshold, double ignore,
			String remove) {
		Random randomizer = new Random();
		int k = 0;
		int counter = 0;
		int size_bin = targetBins.size();
		while (k < targetBins.size() && counter < num) {
			int i = randomizer.nextInt(size_bin);
			if ((binSize - targetBins.get(i).currentSize) < threshold
					&& targetBins.get(i).currentSize < ignore * binSize) {
				if (targetBins.get(i).visited == false) {
					targetBins.get(i).visited = true;
					if (remove.equals("ALL")) {
						targetBins.get(i).all = true;
					}
				}
				counter++;
			}
			k++;
			Collections.swap(targetBins, size_bin-1, i);
			size_bin--;
		}
	}

	private void num_of_pieces(int num, int numpieces, double ignore,
			String remove) {
		int counter = 0;
		int i = 0;
		while (i < targetBins.size() && counter < num) {
			if (targetBins.get(i).numberOfItems() == numpieces
					&& targetBins.get(i).currentSize < ignore * binSize) {

				if (targetBins.get(i).visited == false) {
					targetBins.get(i).visited = true;
					if (remove.equals("ALL")) {
						targetBins.get(i).all = true;
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

		int i = 0;
		int counter = 0;
		while (i < targetBins.size() && counter < num) {

			if (targetBins.get(i).currentSize < ignore * binSize) {
				if (targetBins.get(i).visited == false) {
					targetBins.get(i).visited = true;
					if (remove.equals("ALL")) {
						targetBins.get(i).all = true;

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
		int i = 0;
		int counter = 0;
		while (i < targetBins.size() && counter < num) {

			if (targetBins.get(i).currentSize < ignore * binSize) {
				if (targetBins.get(i).visited == false) {
					targetBins.get(i).visited = true;
					if (remove.equals("ALL")) {
						targetBins.get(i).all = true;

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
		int size_bin = targetBins.size();
		while (k < targetBins.size() && counter < num) {
			int i = randomizer.nextInt(size_bin);
			if (targetBins.get(i).currentSize < ignore * binSize) {
				if (targetBins.get(i).visited == false) {
					targetBins.get(i).visited = true;
					if (remove.equals("ALL")) {
						targetBins.get(i).all = true;

					}
				}
				counter++;
			}
			k++;
			Collections.swap(targetBins, size_bin-1, i);
			size_bin--;
		}
	}
	

}
