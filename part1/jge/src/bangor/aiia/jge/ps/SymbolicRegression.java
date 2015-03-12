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

package bangor.aiia.jge.ps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import bangor.aiia.jge.core.Core;
import bangor.aiia.jge.core.Evaluator;
import bangor.aiia.jge.population.Individual;
import bangor.aiia.jge.population.Population;
import bangor.aiia.jge.util.AbstractCompiler;
import bangor.aiia.jge.util.MathUtil;

/**
 * The class <code>SymbolicRegression</code> represents the specification of
 * a Symbolic Regression problem with one dependent variable (x) 
 * and one independent variable (y).
 * <br>
 * A Symbolic Regression Problem is a problem where a suitable mathematical
 * formula must be found which fits the given observed data. <br>
 * The class <code>SymbolicRegression</code> processes a
 * <code>Population</code> object and evaluates the fitness of the individuals (<code>Individual objects</code>)
 * according to a given mathematical formula. Namely, when an instance of
 * <code>SymbolicRegression</code> is created, the expected formula and the
 * quantity of the sample data points are given. Then, the sample data are
 * calculated and will be used for the evaluation of the fitness of the
 * individuals.
 * 
 * @author 	Loukas Georgiou
 * @version 1.01, 06/04/06
 * @see 	Evaluator
 * @see 	Population
 * @see 	Individual
 * @see 	Core
 * @since 	JavaGE 0.1
 */
public class SymbolicRegression implements Evaluator <String, String> {

	// Flag whether a solution has be found or not
	private boolean solutionFound = false;
	
	/**
	 * The expression to be used for the creation of the sample data.
	 */
	private String expression;

	/**
	 * The quantity of the sample to be used.
	 */
	private int dataPointsAmount;
	
	/**
	 * The Lower Limit of the range of the random data points to be created.
	 */
	private double lowerLimit;
	
	/**
	 * The Upper Limit of the range of the random data points to be created.
	 */
	private double upperLimit;
	
	/**
	 * The compiler which will be used for the compilation and execution of the Java programs.
	 */
	private AbstractCompiler compiler;

	/**
	 * The working directory where temporary files will be saved.
	 */
	private File workingDirectory;

	/**
	 * The sample data used for the evaluation of the individuals.
	 * Pairs of independent and dependent variables: [x][y].
	 */
	private double[][] data;
	
	
	/**
	 * Default Constructor. Should not be used.
	 */
	@SuppressWarnings("unused")
	private SymbolicRegression() {
		super();
	}

	/**
	 * Creates an instance of RegularExpression. The given expression must be a
	 * mathematical formula with one dependent and one independent variable. The
	 * independent variable must always be the character 'x'.<br>
	 * A valid expression is any valid Java expression without variables except the symbol 'x'
	 * which is the dependent variable. <br>
	 * e.g. The expression <code>2*x + 4*x</code> is valid and the dependent variable is the symbol 'x'.
	 * Instead, the expression <code>2*x + 4*y</code> is invalid. The expression can also incorporate
	 * any keyword and operator of the Java language, and any method or field of both Java classes
	 * (class members/methods) and Java objects (instance members/methods).
	 * 
	 * @param expression The expected expression to be found. The dependent variable is
	 *            		 always the symbol x.
	 * @param dataPointsAmount The number of the data points of the data sample to be created (number of x variables).
	 * @param lowerLimit The Lower Limit (inclusive) of the range of the random data points to be created (x).
	 * @param upperLimit The Upper Limit (exclusive) of the range of the random data points to be created (x).
	 * @param realNumbers If true then the randomly created independent variables (x) will be real numbers (Java Type: double).
	 * 					  Otherwise the randomly created independent variables (x) will be Integers (Java Type: int).
	 * @param compiler The compiler which will be used for the compilation and execution of the Java programs.
	 * @param workingDirectory The directory where the temporary files will be saved.
	 */
	public SymbolicRegression(String expression, int dataPointsAmount, double lowerLimit, double upperLimit, boolean realNumbers, AbstractCompiler compiler, File workingDirectory) {
				
		// Initialise Instance Variables
		this.expression = expression;
		this.dataPointsAmount = dataPointsAmount;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.compiler = compiler;
		this.workingDirectory = workingDirectory;		
		data = new double[this.dataPointsAmount][2];
		
		// Local Variables
		FileWriter writer;
		StringBuilder javaSource = new StringBuilder(); // The Java Source Code text
		String tmpOutput = null;						// The Text Output File of the Java Class
		StringTokenizer tokenizer = null;
		File tmpSource = null;							// The Java Source Code File
		File tmpClass = null;							// The Java Class File		
		try {
			tmpSource = new File(this.workingDirectory.getCanonicalPath() + "\\SRSampleData.java");
			tmpClass = new File(this.workingDirectory.getCanonicalPath() + "\\SRSampleData.class");				
		}
		catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
		// Create Random numbers and fill the data sample array with the independent variables (x).
		// Formulas for Random Numbers Generation:
		for (int i = 0; i < data.length; i++) {
			if (realNumbers)
				data[i][0] = MathUtil.randomDouble(this.lowerLimit, this.upperLimit);
			else 
				data[i][0] = MathUtil.randomInteger((int)this.lowerLimit, (int)this.upperLimit);
		}
		        
        // Create the Java Source Code
		javaSource.append("public class SRSampleData {\n");
		javaSource.append("\tpublic static void main(String[] args) {\n");
		for (int i = 0; i< data.length; i++) {
			javaSource.append("\t\tSystem.out.print((");
			javaSource.append(this.expression.replace("x", Double.toString(data[i][0])));  								
			javaSource.append(") + \" \");\n");
		}		
		javaSource.append("\t}\n");
		javaSource.append("}\n");		
        //System.out.println(javaSource);
		
		// Save the file with the Source code
		try {
            writer = new FileWriter(tmpSource, false);                
            writer.append(javaSource.toString()); 
            writer.close();                
        } 
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
				        
        // Complile the .java file
		this.compiler.compile(tmpSource, tmpClass);
		        
        // Execute the Java Class and Capture the ouput
		tmpOutput = this.compiler.execute(tmpClass);		
        //System.out.println(tmpOutput);        
        
        // Parse the output and Fill the Sample Data array with the dependent variables (y)		
		if (tmpOutput != null) {
			tmpOutput = tmpOutput.trim();
			tokenizer = new StringTokenizer(tmpOutput, " ");
			int i = 0;
			while(tokenizer.hasMoreTokens()) {
				data[i][1] = Double.parseDouble(tokenizer.nextToken());
				i++;
			}
		}    
		else {
			data = null;
		}				
        
        // Delete the Temporary Files
        tmpSource.delete();
        tmpClass.delete();

	}

	/**
	 * Evaluates the given population and calculates / assigns the fitness value
	 * of/to each individual of the population.<br>
	 * Note: Only individuals with valid phenotype will be evaluated.
	 * <br><br>
	 * <b>Raw Fitness:</b><br>
	 * The sum, of the absolute values of errors taken over the fitness cases (xi,yi).
	 * <br><br>
	 * <b>Assigned Fitness Value</b><br>
	 * With the above Raw Fitness the best individuals have lower values.
	 * For this reason a kind of Adjusted Fitness will be used and assigned to each individual.
	 * Adjusted Fitness of an individual <code>i</code> is defined as following: 
	 * <code>Fa(i) = 1 / (1 + Fs(i))</code> where <code>Fs</code> the Standardised Fitness of <code>i</code>.<br>
	 * In this case (Symbolic Regression Problem) the Adjusted Fitness of an individual <code>i</code>
	 * is calculated as following: <code>Fa(i) = 1 / (1 + Fr(i))</code> 
	 * where <code>Fr</code> the Raw Fitness of <code>i</code>.<br>
	 * The fitness value varies from 0 to 1 and
	 * Invalid individuals will have Raw Fitness Value 0.
	 * 
	 * @param population The population to be evaluated.
	 */
	public void evaluate(Population<String, String> population) {
	
		// Local Variables		
		solutionFound = false;
		String[] phenotypes = new String[population.size()];
		FileWriter writer;
		StringBuilder javaSource = new StringBuilder(); // The Java Source Code text
		String tmpOutput = null;						// The Text Output File of the Java Class
		StringTokenizer tokenizer = null;
		File tmpSource = null;							// The Java Source Code File
		File tmpClass = null;							// The Java Class File	
		double rawFitness = 0.0;						// The Raw Fitness of the individual
		
		try {
			tmpSource = new File(workingDirectory.getCanonicalPath() + "\\SREvaluation.java");
			tmpClass = new File(workingDirectory.getCanonicalPath() + "\\SREvaluation.class");				
		}
		catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
        
		// Get the phenotypes of the individuals
        for (int i = 0; i < phenotypes.length; i++) {
        	phenotypes[i] = population.getIndividual(i).getPhenotype().value();
        }
        
        // Create the Java Source Code
        javaSource.append("public class SREvaluation {\n\n");		
        javaSource.append("\tpublic static void main(String[] args) {\n");
        javaSource.append("\n\t\tStringBuilder result = new StringBuilder();\n\n");
		for (int i = 0; i < phenotypes.length; i++) {
			if (population.getIndividual(i).isValid()) { // Append only valid phenotypes
				javaSource.append("\t\tresult.append(Double.toString(getDeviationOfIndividual_" + i + "()));\n");			
				javaSource.append("\t\tresult.append(\" \");\n\n");				
			}
		}		
		javaSource.append("\t\tSystem.out.print(result.toString());\n");
		javaSource.append("\t}\n");		
		
		for (int i = 0; i < phenotypes.length; i++) {
			
			if (population.getIndividual(i).isValid()) { // Create methods only for the valid phenotypes
		
				javaSource.append("\n\tpublic static double getDeviationOfIndividual_" + i + "() {\n\n");
				javaSource.append("\t\tdouble result;\n\n");				
				javaSource.append("\t\tresult = ");
				
				for (int j = 0; j < data.length; j++) {
					javaSource.append("\n\t\t\t(Math.abs(");
					javaSource.append(Double.toString(data[j][1]));				
					javaSource.append("-");
					javaSource.append("(" + phenotypes[i].replace("x", Double.toString(data[j][0])) + ")"); 
					javaSource.append("))");
					if (j < data.length - 1)
						javaSource.append("+");		
					else
						javaSource.append(";");
				}				
		
				javaSource.append("\n\n\t\treturn result;\n\n");
				javaSource.append("\t}\n\n");
				
			}
		}
		
		javaSource.append("}\n");
		
		
		// Save the file with the Source code
		try {
            writer = new FileWriter(tmpSource, false);                
            writer.append(javaSource.toString()); 
            writer.close();                
        } 
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
				        
        // Complile the .java file
		this.compiler.compile(tmpSource, tmpClass);
		        
        // Execute the Java Class and Capture the ouput
		tmpOutput = this.compiler.execute(tmpClass);		
        //System.out.println(tmpOutput);        
        
        // Parse the output and Assign the Fitness value to Individuals
		tmpOutput = tmpOutput.trim();
		tokenizer = new StringTokenizer(tmpOutput, " ");
		for (int i = 0; i < population.size(); i++) {
			if (population.getIndividual(i).isValid()) {
				rawFitness = 1.0 / (1.0 + Double.parseDouble(tokenizer.nextToken()));
				// Check if Raw Fitness is NaN
				if (Double.isNaN(rawFitness))
					rawFitness = 0.0;
				population.getIndividual(i).setRawFitnessValue(rawFitness);				
				// Check if a solution is found
				if (rawFitness == 1.0)
					solutionFound = true;
			}		
			else {
				population.getIndividual(i).setRawFitnessValue(0);
			}
			
		}        
        
        // Delete the Temporary Files
        tmpSource.delete();
        tmpClass.delete();
	
	}
	
	/**
	 * Returns true if the solution has been found.<br>
	 * For the Symbolic Regression problem a solution is found
	 * when the current population contains an individual with
	 * phenotype that results to error equal to 0.0 (has raw fitness value = 1.0).
	 * 
	 * @return True, if the solution has been found.
	 */
	public boolean solutionFound() {
		return solutionFound;
	}
	
	/**
	 * Returns a copy of the sample data are used for the evaluation of the individuals.
	 * The array contains pairs of independent and dependent variables: [x][y].
	 * 
	 * @return The sample data array of this problem specification.
	 */
	public double[][] getSampleDataCopy() {
		
		double[][] arrayCopy = null;
		
		if (data != null) {
			arrayCopy = new double[dataPointsAmount][2];
			System.arraycopy(data, 0, arrayCopy, 0, data.length);
		}
		
		return arrayCopy;
		
	}

}
