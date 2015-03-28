/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	14 Jan 2006
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

package bangor.aiia.jge.core;

import java.util.Vector;

import bangor.aiia.jge.population.Genotype;
import bangor.aiia.jge.population.InvalidPhenotypeException;
import bangor.aiia.jge.population.Phenotype;

/**
 * The interface <code>Mapper&ltT, S&gt;</code> specifies the required interface which
 * should be implemented by any object which is responsible for the mapping of
 * the genotype of an individual to its corresponding phenotype, and reversely,
 * the mapping of the phenotype of an individual to the collection of genotypes 
 * which could produce the given phenotype (candidate genotypes).<br>
 * It is a Generic interface which means the implementation classes must
 * specify the actual types of the Genotype&lt;T&gt; and the Phenotype&lt;S&gt;
 * objects which will be used by the methods of this interface.
 * 
 * @author 	Loukas Georgiou
 * @version 1.0, 01/03/06
 * @see 	Core
 * @since 	JavaGE 0.1
 * @param 	<T> The type of the value of the Genotype
 * @param 	<S> The type of the value of the Phenotype
 */
public interface Mapper<T, S> {

	/** 
	 * Maps the given <code>Genotype</code> object to its corresponding
	 * <code>Phenotype</code> object. Depending to the specific implementation
	 * of this method, the <code>Genotype</code> is processed and the corresponding
	 * <code>Phenotype</code> is created and returned.
	 * @param genotype The genotype to be processed.
	 * @return The corresponding phenotype.
	 * @throws InvalidPhenotypeException when an invalid <code>Phenotype</code> is resulted
	 * 									 by the mapping process.
	 */
	public Phenotype<S> getPhenotype(Genotype<T> genotype) throws InvalidPhenotypeException;
						
	
	/**
	 * Maps the given <code>Phenotype</code> object to a collection
	 * of candidate <code>Phenotype</code> objects. 
	 * Depending to the specific implementation
	 * of this method, the <code>Phenotype</code> is processed and its
	 * candidate <code>Genotype</code> objects are created and returned.
	 * 
	 * @param phenotype The phenotype to be processed.
	 * @return The candidate genotypes.
	 */
	public Vector<Genotype<T>> getGenotype(Phenotype<S> phenotype);

}
