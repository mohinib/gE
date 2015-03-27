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
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import bangor.aiia.jge.core.Core;
import bangor.aiia.jge.environment.Environment;

/**
 * The class <code>Population&lt;T, S&gt;</code> represents a real world population and
 * holds a collection of <code>Individual</code> objects.<br>
 * It is a Generic class (parameterised type) which means that the classes which
 * instantiate a <code>Population</code> object must specify the actual types of the 
 * <code>Genotype&lt;T&gt;</code> and the <code>Phenotype&lt;S&gt;</code> objects of the <code>Individual</code> objects collection.
 * <br><br>
 * <b>XML Representation of the <code>Population</code> object</b><br>
 * The <code>Population</code> class supports XML representation (XML Fragment)
 * and XML initialisation (XML Fragment) of the state of an instantiated object.
 * In this way the state of the object can be exchanged between different systems,
 * programming platforms, and networks without compatibility issues.<br>
 * WARNING:<br>
 * The XML representation functions support currently only objects
 * of type <code>Genotype&lt;String&gt;</code> and <code>Phenotype&lt;String&gt;</code>.
 * <br><br>
 * The following methods provide the XML support:
 * <ul>
 * <li>getXMLFragment(): String</li>
 * <li>createInstance(xmlFragment : String)</li>
 * </ul>
 * The valid format of an Population's XML Fragment is the following:
 * <br>
 * <code>
 *  &lt;size&gt;[The size of the population]&lt;/size&gt;<br>
 *  &lt;individuals&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;individual&gt;[Individual XML Fragment]&lt;/individual&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;individual&gt;[Individual XML Fragment]&lt;/individual&gt;<br>
 *  &nbsp;&nbsp;&nbsp;.<br>
 *  &nbsp;&nbsp;&nbsp;.<br>
 *  &nbsp;&nbsp;&nbsp;.<br>
 *  &nbsp;&nbsp;&nbsp;&lt;individual&gt;[Individual XML Fragment]&lt;/individual&gt;<br>
 *  &lt;/individuals&gt;<br>
 *  &lt;environment&gt;<br>
 *  &nbsp;&nbsp;&nbsp;[Environment XML Fragment]<br>
 *  &lt;/environment&gt;<br>
 * </code> 
 * <br><br> 
 * For example:
 * <br>
 * <code>
 *  &lt;size&gt;3&lt;/size&gt;<br>
 *  &lt;individuals&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;individual&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;genotype&gt;1010000100100110010101011001&lt;/genotype&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;phenotype&gt;move();goleft();goright()&lt;/phenotype&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;fitness&gt;1.0&lt;/fitness&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;valid&gt;true&lt;/valid&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;/individual&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;individual&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;genotype&gt;0010101011101110110011011000&lt;/genotype&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;phenotype&gt;goleft();goleft();goright()&lt;/phenotype&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;fitness&gt;0.12&lt;/fitness&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;valid&gt;true&lt;/valid&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;/individual&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;individual&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;genotype&gt;00101001101010011100001011001&lt;/genotype&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;phenotype&gt;move();move()&lt;/phenotype&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;fitness&gt;0.32&lt;/fitness&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;valid&gt;true&lt;/valid&gt;<br>
 *  &nbsp;&nbsp;&nbsp;&lt;/individual&gt;<br>
 *  &lt;/individuals&gt;<br>
 *  &lt;environment&gt;<br>
 *  &nbsp;&nbsp;&nbsp;[Not yet supported in the current jGE version]<br>
 *  &lt;/environment&gt;<br>
 * </code>
 * <br><br>
 * 
 * @author 	Loukas Georgiou 
 * @version	1.4, 12/06/06
 * @see 	Core
 * @see 	Individual
 * @see 	Environment
 * @see 	Genotype
 * @see 	Phenotype
 * @see 	InvalidXMLFragmentException
 * @since	JavaGE 0.1
 * @param 	<T> The type of the value of the Genotype of the Individuals collection
 * @param 	<S> The type of the value of the Phenotype of the Individuals collection
 */
public class Population <T, S> {

	/**
	 * The collection of the individuals of the population.
	 */
	private ArrayList<Individual<T, S>> individuals;
	
	/**
	 * The size of the population in terms of the number of individuals which
	 * belong in this population.
	 */
	private int size;
	
	/**
	 * The environment of the population.
	 */
	private Environment environment;
	
	
	/**
	 * Default constructor. Should not be used.
	 */
	private Population() {
		this.individuals = null;
		this.environment = null;
		this.size = 0;
	}

	/**
	 * Population constructor.
	 * 
	 * @param size The size of the new population.
	 */
	public Population(int size) {
		this();
		this.individuals = new ArrayList<Individual<T, S>>(size);
		//Initialise the ArrayList
		for (int i = 0; i < size; i++)
			this.individuals.add(null);
		this.size = size;
	}

	/**
	 * Population constructor.
	 * 
	 * @param individuals The individuals of the new population.
	 */
	public Population(ArrayList<Individual<T, S>> individuals) {
		this();
		this.individuals = individuals;
		this.size = individuals.size();
	}
	
	/**
	 * Population constructor.
	 * 
	 * @param individuals The individuals of the new population.
	 * @param env The environment of the population.
	 */
	public Population(ArrayList<Individual<T, S>> individuals, Environment env) {
		this(individuals);
		this.environment = env;
	}
	
	/**
	 * Population Factory Method. Creates, Initialises, and returns an 
	 * <code>Population&lt;String, String&gt;</code> object based on the values 
	 * of the XML fragment.
	 *  
	 * @param xmlFragment The XML Fragment representation of the population to be created.
	 * 
	 * @return Returns a <code>Population</code> object.
	 * @throws InvalidXMLFragmentException If the passed argument is not a well-formed 
	 * 									   or valid XML Fragment for a <code>Population</code> object.
	 */
	public static Population<String, String> createInstance(String xmlFragment) throws InvalidXMLFragmentException {
		
		Population<String, String> population = null;
		
		if (xmlFragment == null) 
			throw new InvalidXMLFragmentException("Invalid XML Fragment in the method Population.createInstance(xmlFragement):\nThe passed argument was null");
		if (xmlFragment.length() <= 0)
			throw new InvalidXMLFragmentException("Invalid XML Fragment in the method Population.createInstance(xmlFragement):\nThe passed argument was empty");
		
		population = new Population<String, String>();
		
		// Add a root element in to the XML Fragment
		xmlFragment = "<root>" + xmlFragment + "</root>";
				
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();		
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xmlFragment.getBytes()));
		
			// Parse the XML Fragment and Initialise the Population object			
			//String env = doc.getElementsByTagName("environment").item(0).getChildNodes().item(0).getNodeValue();
			int n = Integer.parseInt(doc.getElementsByTagName("size").item(0).getChildNodes().item(0).getNodeValue()); // Get Size of the population
			NodeList nodes = doc.getElementsByTagName("individual"); // Get a list of all individuals
					
			// Intitialise the Population object
			population.setEnvironment(null);
			ArrayList<Individual<String, String>> members = new ArrayList<Individual<String, String>>(n);
			for (int i = 0; i < n; i++) {
				// Get child nodes of each individual
				NodeList list = nodes.item(i).getChildNodes();
				String geno = null;
				String pheno = null;
				String fit = null;
				String val = null;
				Individual<String, String> indi = null;
				
				for (int x = 0; x < list.getLength(); x++) {
					if (list.item(x).getNodeName().equals("genotype")) { // Genotype of an Individual found
						geno = list.item(x).getChildNodes().item(0).getNodeValue();						
					}
					if (list.item(x).getNodeName().equals("phenotype")) { // Phenotype of an Individual found
						pheno = list.item(x).getChildNodes().item(0).getNodeValue();
					}
					if (list.item(x).getNodeName().equals("fitness")) { // Fitness of an Individual found
						fit = list.item(x).getChildNodes().item(0).getNodeValue();
					}
					if (list.item(x).getNodeName().equals("valid")) { // Validity of an Individual found
						val = list.item(x).getChildNodes().item(0).getNodeValue();
					}
					
				}
				
				indi = new Individual<String, String>();
				if (!geno.equals("null"))
					indi.setGenotype(new Genotype<String>(geno));
				if (!pheno.equals("null"))
					indi.setPhenotype(new Phenotype<String>(pheno));				
				indi.setRawFitnessValue(Double.parseDouble(fit));
				indi.setValid(Boolean.parseBoolean(val));
				
				members.add(i, indi);
				
			}
			
			population.setIndividuals(members);
		
		}
		catch (Exception e) {
			throw new InvalidXMLFragmentException("Invalid XML Fragment in the method Population.createInstance(xmlFragement):\n" + e.getMessage());
		}
		
		return population;
		
	}

	/**
	 * Getter of the property <tt>individuals</tt>.
	 * 
	 * @return Returns the individuals collection of the population.
	 */
	public ArrayList<Individual<T, S>> getIndividuals() {
		return individuals;
	}

	/**
	 * Setter of the property <tt>individuals</tt>.
	 * 
	 * @param individuals The individuals collection of the population to set.
	 */
	public void setIndividuals(ArrayList<Individual<T, S>> individuals) {
		this.individuals = individuals;
		this.size = individuals.size();
	}

	/**
	 * Returns the size of the population.<br>
	 * Note: Individuals collection index starts from 0.
	 * 
	 * @return The size of the population.
	 */
	public int size() {
		return size;
	}

	/**
	 * Getter of the property <tt>environment</tt>.
	 * 
	 * @return Returns the <code>Environment</code> object of the population.
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * Setter of the property <tt>environment</tt>.
	 * 
	 * @param environment The environment of the population to set.
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Sets a new individual in the given index.
	 * If the given index is out of bounds of the individuals collection then
	 * the method throws an <code>IndexOutOfBoundsException</code> exception.<br>
	 * Individuals collection index starts from 0.
	 * 
	 * @param index The position in the collection.
	 * @param individual The new individual. 
	 * @return The inserted individual object.
	 * @throws IndexOutOfBoundsException If the index is greater than the population size.
	 */
	public Individual<T, S> setIndividual(int index, Individual<T, S> individual) {

		if (index >= 0 && index < size) {			
			individuals.set(index, individual);
			return individuals.get(index);
		} 
		else
			throw new IndexOutOfBoundsException("Population is too small");
		
	}

	/**
	 * Returns the individual in the given index.
	 * If the given index is out of bounds of the individuals collection then
	 * the method returns null.<br>
	 * Individuals collection index starts from 0.
	 * 
	 * @param index The index of the individual to be returned.
	 * @return The individual in the given index. If the index is invalid then returns null.
	 */
	public Individual<T, S> getIndividual(int index) {
		if (index >= 0 && index < size)
			return individuals.get(index);
		else
			return null;
	}

	/**
	 * Creates an XML representation of the <code>Population</code> object
	 * and returns an XML fragments which represents the state of the object.
	 * The XML Fragment contains the linear String representations (<code>toString()</code>) of the
	 * <code>Genoype</code> and the <code>Phenotype</code> of each one the individual object
	 * in the individuals collection of the population.
	 */
	public String getXMLFragment() {

		StringBuilder xml = new StringBuilder();
		
		// Add Population Size
		xml.append("<size>").append(size).append("</size>\n");
		
		// Add Individuals
		xml.append("<individuals>\n");
		for (int i = 0; i < size; i++) {
			xml.append("<individual>\n");
			xml.append(individuals.get(i).getXMLFragment());
			xml.append("</individual>\n");
		}
		xml.append("</individuals>\n");
		
		// Add Environment
		xml.append("<environment>\n[Not yet supported in the current jGE version]\n</environment>\n");
		
		return xml.toString();  
		
	}
	
	/**
	 * This method sorts the individuals of the population into ascending order 
	 * according to their fitness value.
	 * 
	 * @param ascending If true then sorts into ascending order (e.g. A->Z), 
	 * 					otherwise sorts into descending order (e.g. Z->A).
	 */
	public void sort(boolean ascending) {
		
		if (ascending) 	// Sort into Ascending Order
			Collections.sort(individuals);
		else 			// Sort into Descending Order
			Collections.sort(individuals, Collections.reverseOrder());		
		
	}	
	
	/**
	 * Returns the average of the raw fitness of the individuals
	 * in this population.
	 * 
	 * @return The average of the raw fitness of the individuals.
	 */
	public double averageRawFitness() {
		
		double average = 0.0;
		double sum = 0.0;
		
		for (int i = 0; i < size; i++) {
			sum += individuals.get(i).rawFitness();
		}
		
		average = sum / (double)size;
		
		return average;
		
	}
	
	/**
	 * Returns the Individual with the best raw fitness
	 * in this population.
	 * If there are many individuals with the same best raw fitness
	 * then the first of them in the individuals collection is returned. 
	 * 
	 * @return The Individual with the best raw fitness.
	 */
	public Individual<T, S> bestIndividual() {
		
		Individual<T, S> best = individuals.get(0);
		
		for (int i = 0; i < size; i++) {
			if (individuals.get(i).rawFitness() > best.rawFitness())
				best = individuals.get(i);
		}
	
		return best;
		
	}
	
	/**
	 * Returns the Individual with the worst raw fitness
	 * in this population.
	 * If there are many individuals with the same worst raw fitness
	 * then the first of them in the individuals collection is returned. 
	 * 
	 * @return The Individual with the worst raw fitness.
	 */
	public Individual<T, S> worstIndividual() {
		
		Individual<T, S> worst = individuals.get(0);
		
		for (int i = 0; i < size; i++) {
			if (individuals.get(i).rawFitness() < worst.rawFitness())
				worst = individuals.get(i);
		}
	
		return worst;
		
	}
	
	
	/**
	 * Returns a printable string representation of the population.
	 * Also, it prints out the linear String representations (<code>toString()</code>) of the
	 * <code>Genoype</code> and the <code>Phenotype</code> of the individual object.
	 * 
	 * @return The string representation of the <code>Population</code> object.
	 */
	public String toString() {

		StringBuilder builder = new StringBuilder();
		
		// Add Population Size
		builder.append("Size:   ").append(size).append("\n");
		
		// Add Individuals
		builder.append("Individuals:\n");
		for (int i = 0; i < size; i++) {
			builder.append("Individual #").append(i + 1).append("\n");
			builder.append(individuals.get(i).toString());
			builder.append("\n");
		}
				
		// Add Environment
		builder.append("Environment: [Not yet supported in the current jGE version]\n");
		
		return builder.toString(); 		
		
	}
	

}
