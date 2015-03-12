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
 * The class <code>DefaultMapper&lt;T&gt;</code> implements the interface <code>Mapper&ltT, T&gt;</code>.
 * It implements the default mapping process where the genotype of the
 * individual is equal to its phenotype. This means that there is no 
 * genotype-phenotype distinction and can be used by the classical implementations
 * of Evolutionary Algorithms like Genetic Algorithms.<br>
 * The type &lt;T&gt; specifies the type of the value of both the <code>Genotype</code>
 * and the <code>Phenotype</code> objects. 
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 03/03/06
 * @see 	Core
 * @see 	Mapper
 * @see 	Genotype
 * @see 	Phenotype
 * @since	JavaGE 0.1
 * @param	<T> The type of the Genotype and the Phenotype.
 */
public class DefaultMapper<T> implements Mapper<T, T> {

	
	/**
	 * Default Constructor.
	 */
	public DefaultMapper() {
		
	}
	
	/**
	 * Returns a <code>Phenotype</code> object with the same value 
	 * with the passed <code>Genotype</code> object.
	 * 
	 * @param genotype The genotype to be processed.
	 * @return The corresponding phenotype.
	 * @throws InvalidPhenotypeException when an invalid <code>Phenotype</code> is resulted
	 * 									 by the mapping process.
	 */
	public Phenotype<T> getPhenotype(Genotype<T> genotype) throws InvalidPhenotypeException {
		return new Phenotype<T>(genotype.value());
	}

	/**
	 * Returns a collection with only one <code>Genotype</code> object which
	 * has the same value with the passed <code>Phenotype</code> object.
	 * 
	 * @param phenotype The phenotype to be processed.
	 * @return The candidate genotypes.
	 */
	public Vector<Genotype<T>> getGenotype(Phenotype<T> phenotype) {
		Vector<Genotype<T>> genotypes = new Vector<Genotype<T>>();
		genotypes.add(new Genotype<T>(phenotype.value()));
		return genotypes;
	}
	

}
