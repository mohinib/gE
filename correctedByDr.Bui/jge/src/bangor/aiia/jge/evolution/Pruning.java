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
import bangor.aiia.jge.core.GEMapper;
import bangor.aiia.jge.population.Genotype;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.InvalidPhenotypeException;
import bangor.aiia.jge.util.MathUtil;

/**
 * The class <code>Pruning</code> is a utility class which provides
 * a collection of Evolutionary Computation Pruning operators.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 08/04/06
 * @see 	Core
 * @since	JavaGE 0.1
 */
public class Pruning {
	
	/**
	 * Default Private Constructor.
	 */
	private Pruning() {
		super();
	}
	
	
	/**
	 * This method performs the Pruning operation in the binary genome
	 * <code>binaryGenome</code> with probability <code>duplicationProbability</code>
	 * according to the mapping function <code>mapper</code><br>.
	 * Pruning discards codons that are not used in the mapping process.
	 * <br><br>
	 * The prune operator discards genes not used in the genotype to phenotype process.
	 * The effect of pruning is a dramatic faster and better crossover.
	 * But later research (O'Neill and Ryan, [1999]) showed that gene introns are
	 * beneficial in other genetic algorithms.<br>
	 * For this reason, if this operator is used at all it should
	 * be applied with a very small probability, typically 0.01.  
	 * 
	 * @param binaryGenome The binary genome.
	 * @param mapper The Grammatical Evolution Mapping object.
	 * @param pruningProbability The probability of pruning to occur (from 0.0 to 1.0).
	 */
	public static void pruning(StringBuilder binaryGenome, GEMapper mapper, double pruningProbability) {
		
		Genotype<String> genotype = new Genotype<String>(binaryGenome.toString());
		
		if (MathUtil.flip(pruningProbability)) {	
			
			try {
				
				mapper.getPhenotype(genotype);
				
				String usable = null;
				int codonSize = mapper.getCodonSize();
				int lastRunCodonIndex = mapper.lastRunCodonIndex();
				int lastRunWraps = mapper.lastRunWraps();			
				// Calculate the number of codons in the genotype string
				// Note: It is possible the last codon to have less than codonSize bits
				int codonsNum = (int) Math.ceil((double) binaryGenome.length() / (double) codonSize);
				
				if (lastRunWraps > 0) // Genotype Wrapping occured which means that all codons where used
					return;
				if (lastRunCodonIndex == codonsNum - 1) // Last codon of the genome was used
					return;
				
				// Keep only the used codons
				usable = binaryGenome.substring(0, (lastRunCodonIndex + 1) * codonSize);
				
				binaryGenome.delete(0, binaryGenome.length());
				binaryGenome.append(usable);
				
			}
			catch(InvalidPhenotypeException ipe) {
				//System.out.println(ipe.getMessage());				
			}								
			
		}
		
		
	}
	
	/**
	 * This method performs the Pruning operation in the binary genome
	 * of the individual <code>individual</code> with probability <code>duplicationProbability</code>
	 * according to the mapping function <code>mapper</code><br>.
	 * Pruning discards codons that are not used in the mapping process.
	 *
	 * @see Pruning#pruning(StringBuilder, GEMapper, double)
	 * @param <T>  The type of the value of the phenotype of the individual.
	 * @param individual The individual.
	 * @param mapper The Grammatical Evolution Mapping object.
	 * @param pruningProbability The probability of pruning to occur (from 0.0 to 1.0).
	 */
	public static <T>void pruning(Individual<String, T> individual, GEMapper mapper, double pruningProbability) {
		
		StringBuilder genome = new StringBuilder(individual.getGenotype().value());
		pruning(genome, mapper, pruningProbability);
		individual.getGenotype().setValue(genome.toString());			
		
	}

	
	
	
	

}
