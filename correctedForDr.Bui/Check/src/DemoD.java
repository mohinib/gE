

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;


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
public class DemoD{

	private List<Bin> target = new ArrayList<Bin>();
	private int min, max, binSize = 0;
	private double avg = 0;
	private List<Integer> bins = null;
	private List<Bin> targetBins = null;
	private File file;
	FileWriter fw;
	private BufferedWriter bw;


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
	 * @throws IOException 
	 */
	public double evaluate(BufferedWriter bw) throws IOException {
		
		InputStream input = new FileInputStream("/Users/Borse/Graduation/Evolutionary_Algorithms/jGE/Check/src/heuristicTest.txt");
		 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		String current[] = new String[30];
		int index = 0;
		while ((line = reader.readLine()) != null) {
		current[index] = line;
		index++;
		}
		double sum_30 = 0;
		int ctr = 0;
		for(int a=0; a < 30; a ++){
			
		bw.write(current[a] + "\r\n");
			if (current[a].contains("<")) {
				//individual.setValid(false);
				System.out.println("false");
			} else {
				
				 List<Bin> objBin = new ArrayList<Bin>(); 
				// for(int a= 0 ; a<100; a++){
				String[] currently = current[a].split("\\s");
				int j = 0;

				// objBin = this.deepCopy(target);
				// List<Bin> temp = new ArrayList<Bin>();
				//bw.write("Initial FFD: \r \n");
				/*for(Bin bins_obj : target){
					bw.write(bins_obj.toString() + " Size= " +bins_obj.currentSize +"\r\n");
				}*/
				int k = 0;
				//bins = new ArrayList<Integer>();
				targetBins = new ArrayList<Bin>();
				
				List<List<Bin>> temp = new ArrayList<List<Bin>>();
				while (k < 10) {
					targetBins = this.deepCopy(target);
					//bw.write("target bin size " + targetBins.size() + " ");
					bins = new ArrayList<Integer>();
					while (j < currently.length) {
						if (targetBins.size() > 0) {
							objBin = ExecPhenotype(currently[j++]);
						} else {
							objBin = ExecPhenotype(currently[currently.length - 1]);
						}
					}
					temp.add(objBin);
					for(Bin i : objBin){
						for(Integer z : i.items){
							bw.write(z +" ");
						}
						bw.write("\r\n");
					}
					//targetBins = this.deepCopy(objBin);
					j = 0;
					k++;
					double fitness, sum = 0;
					if (objBin != null) {
						
						for (Bin bins : objBin) {
							sum += Math.pow((bins.currentSize / binSize), 2);
						}
						fitness = 1 - (sum / objBin.size());
						//bw.write(" Fitness = " + 1 / (1 + fitness) + "\r\n");
						System.out.println("Fitness = " + 1 / (1 + fitness));
					}
				}
				double avg = 0;
				double sum =0;
				for(List<Bin> obj : temp){
					sum+=obj.size();
				}
				avg = sum/temp.size();
				bw.write(" Average " + avg + "\r \n");
				sum_30 += avg;
				
				/*int var = Integer.MAX_VALUE;
				int c = -1;
				for (int z = 0; z < temp.size(); z++) {
					// System.out.println( temp.get(a).size());
					if (var > temp.get(z).size()) {
						var = temp.get(z).size();
						c = z;
					}
				}
				if (c != -1)
					objBin = this.deepCopy(temp.get(c));

				bw.write("Bin size " + objBin.size());*/
				// Assign Raw Fitness and set Individual as Valid
				
				// Check if a solution is found
			}
			}
		double avg_arr = sum_30/30;
		//bw.write(" Avg of one instance " + avg_arr + "\r\n");
			//bw.close();
		return avg_arr;
		}

	//}

	private List<Bin> ExecPhenotype(String Pheno) throws IOException {
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
			String remove) throws IOException {
		Random randomizer = new Random();
		int k = 0;
		int counter = 0;
		int size_bin = targetBins.size();
		while (k < targetBins.size() && counter < num) {
			int i = randomizer.nextInt(size_bin);
			if ((binSize - targetBins.get(i).currentSize) < threshold
					&& targetBins.get(i).currentSize < ignore * binSize) {
				if (targetBins.get(i).visited == false) {
					//System.out.println(targetBins.get(i).toString() + "\r\n");	
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
			String remove) throws IOException {
		int counter = 0;
		int i = 0;
		while (i < targetBins.size() && counter < num) {
			if (targetBins.get(i).numberOfItems() == numpieces
					&& targetBins.get(i).currentSize < ignore * binSize) {

				if (targetBins.get(i).visited == false) {
					//System.out.println(targetBins.get(i).toString() + "\r\n");	
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

	private void highest_filled(int num, double ignore, String remove) throws IOException {
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
					//System.out.println(targetBins.get(i).toString() + "\r\n");	
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

	private void lowest_filled(int num, double ignore, String remove) throws IOException {
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
					//System.out.println(targetBins.get(i).toString() + "\r\n");	
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

	private void random_bin(int num, double ignore, String remove) throws IOException {
		Random randomizer = new Random();
		int k = 0;
		int counter = 0;
		int size_bin = targetBins.size();
		while (k < targetBins.size() && counter < num) {
			int i = randomizer.nextInt(size_bin);
			if (targetBins.get(i).currentSize < ignore * binSize) {
				if (targetBins.get(i).visited == false) {
					//System.out.println(targetBins.get(i).toString() + "\r\n");	
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
