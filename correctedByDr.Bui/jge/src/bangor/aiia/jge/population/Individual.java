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

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import bangor.aiia.jge.core.Core;

/**
 * The class <code>Individual&lt;T, S&gt;</code> represents a member of a real world
 * population. It is composed from its genotype (<code>Genotype</code> class)
 * and the corresponding phenotype (<code>Phenotype</code> class).<br>
 * It is a Generic class (parameterised type) which means that the classes which
 * instantiate an <code>Individual</code> object must specify the actual types 
 * of the <code>Genotype&lt;T&gt;</code> and the <code>Phenotype&lt;S&gt;</code> objects of the <code>Individual</code> object.
 * <br><br>
 * <b>XML Representation of the <code>Individual</code> object</b><br>
 * The <code>Individual</code> class supports XML representation (XML Fragment)
 * and XML creation and initialisation (XML Fragment) of an <code>Individual</code> object.
 * In this way the state of the object can be exchanged between different systems,
 * programming platforms, and networks without compatibility issues.<br>
 * WARNING:<br>
 * The XML representation functions support currently only objects
 * of type <code>Individual&lt;String, String&gt;</code>
 * namely, containing objects of the type <code>Genotype&lt;String&gt;</code> and <code>Phenotype&lt;String&gt;</code>.
 * <br><br>
 * The following methods provide the XML support:
 * <ul>
 * <li>getXMLFragment(): String</li>
 * <li>createInstance(xmlFragment : String)</li>
 * </ul>
 * The valid format of an Individual's XML Fragment is the following:
 * <br>
 * <code>
 *  &lt;genotype&gt;[String representation of the genotype]&lt;/genotype&gt;<br>
 * 	&lt;phenotype&gt;[String representation of the phenotype]&lt;/phenotype&gt;<br>
 *  &lt;fitness&gt;[double value]&lt;/fitness&gt;<br>
 *  &lt;valid&gt;[true|false]&lt;/valid&gt;
 * </code> 
 * <br><br> 
 * For example:
 * <br>
 * <code>
 *  &lt;genotype&gt;00101001101010011100001011001&lt;/genotype&gt;<br>
 * 	&lt;phenotype&gt;move();goleft();goright()&lt;/phenotype&gt;<br>
 *  &lt;fitness&gt;0.12&lt;/fitness&gt;<br>
 *  &lt;valid&gt;true&lt;/valid&gt;<br>
 * </code>
 * <br><br>
 * 
 * @author 	Loukas Georgiou
 * @version 1.4, 10/04/06
 * @see 	Core
 * @see 	Population
 * @see 	Genotype
 * @see 	Phenotype
 * @see 	InvalidXMLFragmentException
 * @see		java.lang.Comparable
 * @see		java.lang.Cloneable
 * @since 	JavaGE 0.1
 * @param 	<T> The type of the value of the Genotype
 * @param 	<S> The type of the value of the Phenotype
 */
public class Individual<T, S> implements Comparable<Individual<T, S>>, Cloneable {

	/**
	 * The genotype of the individual.
	 */
	private Genotype<T> genotype;
	
	/**
	 * The phenotype of the individual.
	 */
	private Phenotype<S> phenotype;
	
	/**
	 * The raw fitness value of the individual.
	 * It must be always a non-negative number.
	 * Convention: Higher Raw Fitness means better Individual. 
	 * The default value is zero (0.0).
	 */
	private double rawFitnessValue;
	
	/**
	 * Shows whether the phenotype of the individual is valid or not. 
	 * The default value is false.
	 */
	private boolean valid;
	private int numberOfBins;
	
	
	/**
	 * Default constructor.
	 */
	public Individual() {
		this.genotype = null;
		this.phenotype = null;
		this.rawFitnessValue = 0.0;
		this.valid = false;
		this.numberOfBins = 0;
	}

	/**
	 * Individual constructor.
	 * 
	 * @param genotype The genotype of the individual.
	 */
	public Individual(Genotype<T> genotype) {
		this();
		this.genotype = genotype;

	}

	/**
	 * Individual constructor.
	 * 
	 * @param genotype The genotype of the individual.
	 * @param phenotype The phenotype of the individual.
	 */
	public Individual(Genotype<T> genotype, Phenotype<S> phenotype) {
		this(genotype);
		this.phenotype = phenotype;
	}
	
	/**
	 * Individual Factory Method. Creates, Initialises, and returns an 
	 * <code>Individual&lt;String, String&gt;</code> object based on the values 
	 * of the XML fragment.
	 *  
	 * @param xmlFragment The XML Fragment representation of the individual to be created.
	 * @return Returns an <code>Individual</code> object.
	 * @throws InvalidXMLFragmentException If the passed argument is not a well-formed 
	 * 									   or valid XML Fragment for an <code>Individual</code> object.
	 */
	public static Individual<String, String> createInstance(String xmlFragment) throws InvalidXMLFragmentException {
		
		Individual<String, String> individual = null;
		
		if (xmlFragment == null) 
			throw new InvalidXMLFragmentException("Invalid XML Fragment in the method Individual.createInstance(xmlFragement):\nThe passed argument was null");
		if (xmlFragment.length() <= 0)
			throw new InvalidXMLFragmentException("Invalid XML Fragment in the method Individual.createInstance(xmlFragement):\nThe passed argument was empty");
		
		individual = new Individual<String, String>();
		
		// Add a root element in to the XML Fragment
		xmlFragment = "<root>" + xmlFragment + "</root>";
				
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();		
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xmlFragment.getBytes()));
		
			// Parse the XML Fragment
			String geno = doc.getElementsByTagName("genotype").item(0).getChildNodes().item(0).getNodeValue();
			String pheno = doc.getElementsByTagName("phenotype").item(0).getChildNodes().item(0).getNodeValue();
			String fit = doc.getElementsByTagName("fitness").item(0).getChildNodes().item(0).getNodeValue();
			String val = doc.getElementsByTagName("valid").item(0).getChildNodes().item(0).getNodeValue();
			
			// Intitialise the Individual object
			if (!geno.equals("null"))	individual.setGenotype(new Genotype<String>(geno));			
			if (!pheno.equals("null"))	individual.setPhenotype(new Phenotype<String>(pheno));
			individual.setRawFitnessValue(Double.parseDouble(fit));
			individual.setValid(Boolean.parseBoolean(val));			
			
		}
		catch (Exception e) {
			throw new InvalidXMLFragmentException("Invalid XML Fragment in the method Individual.createInstance(xmlFragement):\n" + e.getMessage());
		}
		
		return individual;

		
	}
	
	/**
	 * Returns the genotype of the individual.
	 * 
	 * @return Returns the genotype.
	 */
	public Genotype<T> getGenotype() {
		return genotype;
	}

	/**
	 * Sets the genotype of the individual.
	 * 
	 * @param genotype The genotype to set.
	 */
	public void setGenotype(Genotype<T> genotype) {
		this.genotype = genotype;
	}	

	/**
	 * Returns the phenotype of the individual.
	 * 
	 * @return Returns the phenotype.
	 */
	public Phenotype<S> getPhenotype() {
		return phenotype;
	}

	/**
	 * Sets the phenotype of the individual.
	 * 
	 * @param phenotype The phenotype to set.
	 */
	public void setPhenotype(Phenotype<S> phenotype) {
		this.phenotype = phenotype;
	}

	/**
	 * Returns the Raw Fitness value of the Individual.<br>
	 * Use the method rawFitness() instead of this one for better performance.<br>
	 * Raw Fitness is always a non-negative number<br>
	 * Convention: Higher Raw Fitness means better Individual. 
	 * 
	 * @return Returns the raw fitness.
	 */
	public double getRawFitnessValue() {
		return rawFitness();
	}

	/**
	 * Sets the number of bins of the Individual.<br>
	 * It must be always a non-negative number.<br>
	 * Convention: lower number of bins means better Individual. 
	 * 
	 * @param numberOfBins The number of Bins value to set.
	 */
	public void setNumberBins(int numberBins) {
		if (numberBins < 0) throw new RuntimeException("Negative value assigned in the Raw Fitness Value");
		this.numberOfBins = numberBins;
	}
	
	/**
	 * Returns the number of Bins of the Individual.<br>
	 * Use the method numBins() instead of this one for better performance.<br>
	 * Number of bins is always a non-negative number<br>
	 * Convention: Lower number of bins means better Individual. 
	 * 
	 * @return Returns the number of Bins.
	 */
	public int getNumberBins() {
		return numBins();
	}

	/**
	 * Sets the Raw Fitness value of the Individual.<br>
	 * It must be always a non-negative number.<br>
	 * Convention: Higher Raw Fitness means better Individual. 
	 * 
	 * @param rawFitnessValue The raw fitness value to set.
	 */
	public void setRawFitnessValue(double rawFitnessValue) {
		if (rawFitnessValue < 0) throw new RuntimeException("Negative value assigned in the Raw Fitness Value");
		this.rawFitnessValue = rawFitnessValue;
	}

	/**
	 * Returns true if the individual is valid, otherwise it returns false.
	 * Use the method isValid() instead of this one for better performance.
	 * 
	 * @return Returns the valid.
	 */
	public boolean getValid() {
		return isValid();
	}

	/**
	 * Sets the individual's validity to either true or false.
	 * If false (invalid) then the Raw Fitness Value becomes 0.
	 * 
	 * @param valid The individual's validity value to set.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
		if (valid == false) this.rawFitnessValue = 0.0;
	}

	/**
	 * Returns the raw fitness value of the individual.
	 * Any object which needs to get the fitness value of the phenotype should
	 * use this method.<br>
	 * Raw Fitness is always a non-negative number<br>
	 * Convention: Higher Raw Fitness means better Individual.
	 * 
	 * @return the fitness value of the phenotype of the individual.
	 */
	public double rawFitness() {
		return rawFitnessValue;
	}

	/**
	 * Returns whether the phenotype of the individual is valid or not. 
	 * Any object which needs to know whether the individual is valid or not
	 * should use this method.
	 * 
	 * @return True if the phenotype of the individual is valid.
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Returns number of bins of the individual. 
	 * Any object which needs to know the number if bins in an individual
	 * should use this method.
	 * 
	 * @return the number of bins of the phenotype of the individual.
	 */
	public int numBins(){
		return numberOfBins;
	}

	/**
	 * Creates an XML representation of the <code>Individual</code> object
	 * and returns an XML fragments which represents the state of the object.
	 * The XML Fragment contains the linear String representations (<code>toString()</code>) of the
	 * <code>Genoype</code> and the <code>Phenotype</code> of the individual object.
	 * If either the Genotype or Phenotype fields are null then the String "null" is the
	 * value of the corresponding XML tags.
	 */
	public String getXMLFragment() {
		
		StringBuilder xml = new StringBuilder();
		
		// Check and Add Genotype
		if (genotype != null) 	xml.append("<genotype>").append(genotype.toString()).append("</genotype>\n");
		else					xml.append("<genotype>").append("null").append("</genotype>\n");
		
		// Check and Add Phenotype
		if (phenotype != null)	xml.append("<phenotype>").append(phenotype.toString()).append("</phenotype>\n");
		else					xml.append("<phenotype>").append("null").append("</phenotype>\n");
		
		// Add Fitness Value
		xml.append("<fitness>").append(rawFitnessValue).append("</fitness>\n");
		
		// Add Validness Value
		if (valid)				xml.append("<valid>").append("true").append("</valid>\n");
		else					xml.append("<valid>").append("false").append("</valid>\n");
		
		return xml.toString();

	}

	
	/**
	 * Returns a printable string representation of the individual.
	 * Also, it prints out the linear String representations (<code>toString()</code>) of the
	 * <code>Genoype</code> and the <code>Phenotype</code> of the individual object.
	 * 
	 * @return The string representation of the <code>Individual</code> object.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		// Add Genotype
		builder.append("Genotype:  ");
		if (genotype != null) 	builder.append(genotype.toString()).append("\n");
		else					builder.append("null\n");
		
		// Add Phenotype
		builder.append("Phenotype: ");
		if (phenotype != null)	builder.append(phenotype.toString()).append("\n");
		else					builder.append("null\n");
		
		// Add Fitness Value
		builder.append("Fitness:   ");
		builder.append(rawFitnessValue).append("\n");
		
		// Add Validness Value
		builder.append("Is Valid:  ");
		if (valid)				builder.append("true\n");
		else					builder.append("false\n");
		
		return builder.toString();
		
	}

	/**
	 * Compares this Individual object with the specified 
	 * Individual <code>individual</code> object for order.
	 * 
	 * @param individual The Individual to be compared.
	 * @return A negative integer, zero, or a positive integer as 
	 * 		   this individual is less than, equal to, or greater than the specified individual.
	 */
	public int compareTo(Individual<T, S> individual) {

		if (this.rawFitness() == individual.rawFitness())
			return 0;
		else if (this.rawFitness() < individual.rawFitness())
			return -1;
		else
			return 1;

	}
	
	/**
	 * Returns a clone of this individual.
	 * 
	 * @return A clone of this individual.
	 */
	public Individual<T, S> clone() {

		Individual<T, S> individual = new Individual<T, S>();
		Genotype<T> geno = this.genotype.clone();
		Phenotype<S> pheno = this.phenotype.clone();
		
		individual.setGenotype(geno);
		individual.setPhenotype(pheno);
		individual.setRawFitnessValue(this.rawFitnessValue);
		individual.setValid(this.valid);
	
		return individual;
	}
	
	/**
	 * Returns an Individual with the same Genotype.
	 * 
	 * @return An Individual with the same Genotype.
	 */
	public Individual<T, S> cloneGenomeOnly() {

		Individual<T, S> individual = new Individual<T, S>();
		Genotype<T> geno = this.genotype.clone();
		individual.setGenotype(geno);
		
		return individual;
	}

}
