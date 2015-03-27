/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	2 April 2006
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

import bangor.aiia.jge.core.Core;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.util.MathUtil;

/**
 * The class <code>Duplication</code> is a utility class which provides
 * a collection of Evolutionary Computation Duplication operators.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 08/04/06
 * @see 	Core
 * @since	JavaGE 0.1
 */
public class Duplication {
	
	/**
	 * Default Private Constructor.
	 */
	private Duplication() {
		super();
	}
		
	/**
	 * This methods performs the Duplication operation in the given binary genome
	 * <code>binaryGenome</code> of codon size <code>codonSize</code> with
	 * probability <code>duplicationProbability</code>.
	 * <br><br>
	 * The duplicated codons are placed at the end of the chromosome. 
	 * Duplication involves randomly selection of<br>
	 * a) the number of codons to be duplicated, and <br>
	 * b) the starting position of the first codon in this set.<br>
	 * The position of the new duplicated codons is the penultimate codon
	 * position at the end of the chromosome.
	 * 
	 * @param binaryGenome The binary genome.
	 * @param codonSize The size in bits of each codon of the binary genome.
	 * @param duplicationProbability The probability of duplication to occur (from 0.0 to 1.0).
	 */
	public static void duplication(StringBuilder binaryGenome, int codonSize, double duplicationProbability) {
	
		if (MathUtil.flip(duplicationProbability)) {
		
			int codonsNum = 0;	// The number of codons in the binary string
			int start = 0;		// The first codon to be duplicated
			int duplicate = 0;	// The number of codons to duplicate
			int offset = 0;		// The position of the inserted codons
			String insert = "";	// The duplicated codons to insert
			
			// Calculate the number of codons in the genotype string
			// Note: It is possible the last codon to have less than codonSize bits
			codonsNum = (int) Math.ceil((double) binaryGenome.length() / (double) codonSize);
			
			if (codonsNum > 1) { // Duplication can be performed
				
				start = MathUtil.randomInteger(1, codonsNum - 1);			
				duplicate = MathUtil.randomInteger(1, codonsNum - start);
								
				offset = ((codonsNum - 1) * codonSize);				
				insert = binaryGenome.substring((start - 1) * codonSize, (start + duplicate - 1) * codonSize);
				
				binaryGenome.insert(offset, insert);			
				
			}	
		
		}			
		
	}
	
	/**
	 * This methods performs the Duplication operation in the binary genome
	 * of the given individual <code>individual</code> of codon size <code>codonSize</code> with
	 * probability <code>duplicationProbability</code>.
	 * 
	 * @see Duplication#duplication(StringBuilder, int, double)
	 * @param <T>  The type of the value of the phenotype of the individual.
	 * @param individual The individual.
	 * @param codonSize The size in bits of each codon of the binary genome of the individual.
	 * @param duplicationProbability The probability of duplication to occur (from 0.0 to 1.0).
	 */
	public static <T>void duplication(Individual<String, T> individual, int codonSize, double duplicationProbability) {
		
		StringBuilder genome = new StringBuilder(individual.getGenotype().value());
		duplication(genome, codonSize, duplicationProbability);
		individual.getGenotype().setValue(genome.toString());				
		
	}
	
	
	

}
