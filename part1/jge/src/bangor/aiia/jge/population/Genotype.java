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

package bangor.aiia.jge.population;

import bangor.aiia.jge.core.Core;

/**
 * The class <code>Genotype&ltT&gt;</code> represents the genetic code (DNA) of an
 * individual (<code>Individual</code>) of a population (<code>Population</code>).<br>
 * It is a Generic class (parameterised type) which means that its actual value can be of any type.
 * The type parameter &lt;T&gt; specifies the type of the value of the <code>Genotype</code> object.<br>
 * Any <code>T</code> object must override the <code>toString()</code> method
 * in order to return a linear String representation of its actual value.<br>
 * Also, the value type <code>T</code> must be immutable (either a primitive type or an immutable object).
 * 
 * @author	Loukas Georgiou
 * @version 1.1, 10/04/06
 * @see 	Core
 * @see 	Individual
 * @see 	Population
 * @see		java.lang.Cloneable
 * @since	JavaGE 0.1
 * @param	<T> The type of the value of the Genotype
 * 			The value must be immutable (either a primitive type or a immutable object).
 */
public class Genotype<T> implements Cloneable {

	/**
	 * The value of the genotype.
	 */
	private T value;

	
	/**
	 * Default constructor.
	 */
	public Genotype() {
		this.value = null;
	}

	/**
	 * Genotype constructor.
	 * 
	 * @param value The value of the genotype.
	 */
	public Genotype(T value) {
		this();
		this.value = value;
	}

	/**
	 * Getter of the property <tt>value</tt>.
	 * Use the method value() instead of this one for better performance.
	 * 
	 * @return Returns the value of the genotype.
	 */
	public T getValue() {
		return value();
	}

	/**
	 * Setter of the property <tt>value</tt>.
	 * 
	 * @param value The value of the genotype to set.
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * Returns the value of the genotype. 
	 * Any object which needs to get the value of the genotype should
	 * use this method.
	 * 
	 * @return Returns the value of the genotype.
	 */
	public T value() {
		return value;
	}

	/**
	 * Returns the linear string representation of the value of the genotype. 
	 * Any object which needs to get a linear string representation of the value
	 * of the genotype must use this method. It calls and returns the <code>toString()</code>
	 * method of the <code>T value</code> object.
	 * 
	 * @return Returns the linear string representation of the value of the genotype.
	 */
	public String toString() {
		if (value != null)
			return value.toString();
		else
			return "";
	}
	

	/**
	 * Creates and returns a copy of this object.<br>
	 * The object returned by this method is independent of this object (which is being cloned). 
	 * 
	 * @return	The clone of this object.
	 */
	public Genotype<T> clone() {		
		return new Genotype<T>(value());		
	}
	

}
