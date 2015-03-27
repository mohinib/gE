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
import bangor.aiia.jge.population.Genotype;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.Phenotype;
import bangor.aiia.jge.population.Population;
import bangor.aiia.jge.util.MathUtil;

/**
 * The class <code>Genesis</code> is a utility class which provides
 * a collection of Evolutionary Computation Genotypes Initialisation 
 * operations for the creation of a new population (Genesis).
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 09/04/06
 * @see 	Core
 * @since	JavaGE 0.1
 */
public class Genesis {
	
	/**
	 * Default Private Constructor.
	 */
	private Genesis() {
		super();
	}
	
	
	/**
	 * This method creates and returns a collection of binary strings with size <code>size</size>.
	 * The binary strings will be of variable-length in the specified range.
	 * Namely, each binary string will have a variable number of codons 
	 * (each one of <code>codonSize</code> bits) in the range between 
	 * <code>minCodons</code> and <code>maxCodons</code>.
	 * 
	 * @param size The size of the new collection of binary strings.
	 * @param codonSize The codon size in bits of the binary strings.
	 * @param minCodons The minimum number of codons of a binary string.
	 * @param maxCodons The maximum number of codons of a binary string.
	 * @return The created collection of binary strings.
	 */
	public static String[] randomBinaryGenotypes(int size, int codonSize, int minCodons, int maxCodons) {

		int length = 0;
		String[] population = new String[size];
		char[] binaryString = null;
				
		// Create the population
		for (int i = 0; i < size; i++) {
			length = MathUtil.randomInteger(minCodons, maxCodons) * codonSize;
			binaryString = new char[length];
			for (int y = 0; y < length; y++) 
				binaryString[y] = MathUtil.flipBinaryChar(0.5);
			population[i] = new String(binaryString);			
		}	
		
		return population;
		
	}
	

	/**
	 * This method creates and returns a new population with size <code>populationSize</size>.
	 * The inviduals will have variable-length binary encoded genotypes in the specified range.
	 * Namely, each individual will have a variable number of codons 
	 * (each one of <code>codonSize</code> bits) in the range between 
	 * <code>minCodons</code> and <code>maxCodons</code>.
	 * 
	 * @param <T>  The type of the value of the phenotype of the individuals of the population.
	 * @param populationSize The size of the new population (number of individuals).
	 * @param codonSize The codon size in bits of the genotype of the individuals.
	 * @param minCodons The minimum number of codons of the genotype of an individual.
	 * @param maxCodons The maximum number of codons of the genotype of an individual.
	 * @return The created population.
	 */
	public static <T>Population<String, T> randomPopulation(int populationSize, int codonSize, int minCodons, int maxCodons) {

		Population<String, T> population = new Population<String, T>(populationSize);
		Individual<String, T> individual = null;		
		Phenotype<T> phenotype = null;
		Genotype<String> genotype = null;
		String[] binaryStrings = null;
		
		// Create the collection of the binary strings for the genotype of 
		// the new population
		binaryStrings = randomBinaryGenotypes(populationSize, codonSize, minCodons, maxCodons);
				
		// Create the population
		for (int i = 0; i < populationSize; i++) {
			genotype = new Genotype<String>(binaryStrings[i]);
			phenotype = new Phenotype<T>();
			individual = new Individual<String, T>(genotype, phenotype);
			population.setIndividual(i, individual);			
		}	
		
		return population;
		
	}
	
	
	

}
