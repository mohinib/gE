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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import bangor.aiia.jge.bnf.BNFGrammar;
import bangor.aiia.jge.bnf.BNFProduction;
import bangor.aiia.jge.bnf.BNFRule;
import bangor.aiia.jge.bnf.BNFSymbol;
import bangor.aiia.jge.population.Genotype;
import bangor.aiia.jge.population.InvalidPhenotypeException;
import bangor.aiia.jge.population.Phenotype;

/**
 * The class <code>GEMapper</code> implements the interface
 * <code>Mapper&lt;String, String&gt;</code> and is an implementation of the Grammatical Evolution mapping
 * process from genotype to phenotype.<br>
 * Grammatical Evolution is an evolutionary algorithm invented by Michael O'Neill and Conor Ryan:<br>
 * O'NEILL, M. and RYAN, C. (2003) <u>Grammatical Evolution: Evolutionary Automatic Programming in an Arbitrary Language</u>. 
 * USA: Kluwer Academic Publishers.<br><br>
 * 
 * <b>The Grammatical Evolution Mapping Process</b><br>
 * The main characteristics of the Grammatical Evolution mapping process which have been implemented
 * by the <code>GEMapper</code> class are the following:<br>
 * <ul>
 * <li>Phenotype-Genotype Separation<br>It separates the search space (genotype) from the solution space (phenotype)<br>&nbsp;</li>
 * <li>Many-to-one Mapping<br>It uses a many-to-one mapping process from genotype to
 * phenotype based on production rules in a Backus-Naur form grammar definition.<br>&nbsp;</li>
 * <li>Context-Free Grammars (BNF)<br>It was initially designed to work with
 * Context-Free Grammars (CFG) which means that a specific codon value will
 * always result in a specific production for a non-terminal symbol, regardless
 * of the context in which appears.<br>&nbsp;</li>
 * <li>Genotype Encoding<br>It uses a binary encoded genotype.<br>&nbsp;</li>
 * <li>Mapping Process<br>The mapping process is as follows: 
 * <br> 1. The genotype is used to
 * map the start symbol of the BNF onto terminals. 
 * <br> 2. It reads "codons" of 8
 * bits and generates the corresponding integer (RNA) each time a non-terminal
 * to be translated has more than one production rules. 
 * <br> 3. The selected production
 * rule is calculated with the formula: rule = [codon integer value] MOD [number
 * of rules for the current non-terminal].<br>&nbsp;</li>
 * <li>Wrapping Operation<br>It uses a wrapping technique (with an upper limit of
 * possible wraps of the genome of an individual) when an individual runs out of
 * codons. A typical wrapping threshold is 10.<br>&nbsp;</li>
 * <li>Size of the Codon<br>The 8-bit codons is the recommended size. But this size is
 * not expected to be optimal across all problems. So, an investigation into
 * more appropriate codon sizes for each problem may be beneficial.<br>&nbsp;</li> 
 * <li>Special Commands<br>The libGE introduces a set of commands that can be
 * incorporated into grammars in order to be improved the control over the
 * mapping process. Such an example is the use of &lt;GECodonValue&gt; symbol which
 * allows the user to insert codon values directly from the genotype into the
 * resulting phenotype (the codon then is considered as "used") .<br>
 * Another special command introduced by O'Neill and Ryan is Intron (&lt;GEIntron&gt;).
 * When a codon evaluates to an Intron symbol to being selected we simply skip this codon
 * and the non-terminal symbol remains unchanged.
 * <br><br>
 * Note: In the current version of the JavaGE library
 * Special Directives are not yet supported.<br>&nbsp;</li>
 * </ul>
 * 
 * @author	Loukas Georgiou
 * @version 1.01, 11/03/06
 * @see 	Core
 * @see 	Mapper
 * @see 	Genotype
 * @see 	Phenotype
 * @see 	BNFGrammar
 * @see 	BNFSymbol
 * @since 	JavaGE 0.1
 */
public class GEMapper implements Mapper<String, String> {

	/**
	 * The set of the special directives (non-terminal symbols string values).
	 * @uml.property name="directives" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] directives;
	
	/**
	 * The size of each codon in bits.
	 * @uml.property name="codonSize"
	 */
	private int codonSize;
	
	/**
	 * The maximum allowable wraps of the genotype.
	 * @uml.property name="maxWraps"
	 */
	private int maxWraps;
	
	/**
	 * The BNF Grammar Definition which will be used in the mapping process.
	 * @uml.property name="grammar"
	 */
	private BNFGrammar grammar;
	
	/**
	 * The current wrap (cycle) of reads of the RNA
	 */
	private int currentWrap;
	
	/**
	 * The codons array of the genotype (RNA)
	 */
	private int[] rna;
	
	/**
	 * The next codon from RNA to be used if needed
	 */
	private int currentCodonIndex;
	
	/**
	 * The phenotyte's string value
	 */
	private String phenotypeValue;
	
	/**
	 * The currently processed BNF Symbol
	 */
	BNFSymbol symbol;
	
	
	/**
	 * Default Constructor.
	 */
	public GEMapper() {
		codonSize = 8;	// The default codon size of Grammatical Evolution.
		maxWraps = 10;	// The default maximum wraps of Grammatical Evolution.	
		grammar = null;
		directives = new String[] {"<GECodonValue>", "<GEIntron>"}; 

		currentWrap = 0;	
		rna = null;
		currentCodonIndex = -1;
		phenotypeValue = null;
		symbol = null;
	
	}
	
	/**
	 * GEMapper constructor.
	 * 
	 * @param grammar The BNF Grammar Definition which will be used in the mapping process.
	 * @param codonSize The size of the codon in bits.
	 * @param maxWraps The maximum allowable wraps.
	 */
	public GEMapper(BNFGrammar grammar, int codonSize, int maxWraps) {
		this();
		this.codonSize = codonSize;
		this.maxWraps = maxWraps;
		this.grammar = grammar;
	}

	/**
	 * Implements the Grammatical Evolution mapping algorithm 
	 * from genotype to phenotype. 
	 * 
	 * @param genotype The genotype to be processed.
	 * @return The corresponding phenotype.
	 * @throws InvalidPhenotypeException when an invalid <code>Phenotype</code> is resulted
	 * 									 by the mapping process. <br>
	 * 									 This exception will be thrown in any of the following conditions:
	 * 									 <ul>
	 * 									 <li>The maximum number of allowable wraps of the genotype is reached</li>
	 * 									 <li>There is no production rule for a processed non-terminal symbol</li>
	 * 									 <li>The processed non-terminal symbol has only one production rule and it exists in this production rule (endless recursion)</li>
	 * 									 </ul>
	 */
	public Phenotype<String> getPhenotype(Genotype<String> genotype) throws InvalidPhenotypeException {
		
		// Initialise instance variables
		currentWrap = 0;
		rna = null;
		currentCodonIndex = -1;
		phenotypeValue = null;
		symbol = null;
		
		// Declare and Initialise the local variables
		String dna = null;						// The binary string of the genotype (DNA)		
		int codonsNum = 0;						// Number of codons in the genotype
		Phenotype<String> phenotype = null; 	// The phenotype to be created and returned
		LinkedList<BNFSymbol> working = null;	// The working collection of BNFSymbols for the step by step creation of the final phenotype
		BNFRule rule = null;					// The current BNF Rule
		BNFProduction[] production = null;		// The current BNF Productions collection
		BNFSymbol[]symbols = null;				// The BNF Symbol collection of the current production
		boolean hasNonTerminal = true;			// True if the working set has Non-Terminal BNF Symbols
		int currentIndex = 0;					// The index of the current symbol in the working set
		int choosenProduction = 0;				// The production to be used if there are more than one productions for the current symbol
		
		// Get the String value of the genotype
		dna = genotype.value();
		//System.out.println("dna= " +dna);
		
		// Calculate the number of codons in the genotype string
		// Note: It is possible the last codon to have less than codonSize bits
		codonsNum = (int) Math.ceil( (double) dna.length() / (double) codonSize);
		
		// Convert the Binary String (DNA) into an Integers Array (RNA)
		rna = new int[codonsNum];
		for (int i = 0; i < codonsNum; i++) {
			// Calculate the position of the next codon
			int start = i * codonSize;
			//System.out.println(start);
			int end = (start + codonSize < dna.length())? start + codonSize : dna.length();
			// Convert the binary string value into a decimal integer value and store it
			rna[i] = Integer.parseInt(dna.substring(start, end), 2);			
		}		
		
		// Initialise the working set
		working = new LinkedList<BNFSymbol>();
		
		// Get Start Symbol
		symbol = grammar.getStartSymbol();
		
		// Add symbol to the Working Set
		working.add(symbol);
		
		// Process the symbols of the working set
		while (hasNonTerminal) {
			
			
			// Pickup the current symbol of the working set			
			symbol = working.get(currentIndex);
			
			//System.out.println("Current Symbol: " + symbol.toString());
			//System.out.println("Current Codon: " + currentCodon);
			//System.out.println("Current Wrap: " + currentWrap);
			//System.out.println("RNA Length: " + rna.length);
			
			
			if (symbol.isTerminal()) {					// TERMINAL SYMBOL
				
				// Increase current index
				currentIndex++;
				
				// Check if it is the last symbol	
				if (currentIndex == working.size())		
					hasNonTerminal = false;
				
			}
			
			else if (isDirective(symbol)) {				// SPECIAL DIRECTIVE
				
				// Increase current index
				currentIndex++;
				
				// Execute here the Special Directive	
				// [Check the kind of directive and
				// execute the corresponding steps
				
				// Check if it is the last symbol
				if (currentIndex == working.size())		
					hasNonTerminal = false;
				
			}
			
			else {										// NON-TERMINAL SYMBOL
								
				// Get the rule of this symbol
				rule = grammar.getRule(symbol);
				
				// Check if this non-terminal symbol has an associated rule
				if (rule == null) {
					createPhenotypeValue(working);
					throw new InvalidPhenotypeException("Invalid Phenotype: Non-Terminal " + symbol.getValue() + " with no Production");
				}
											
				// Check if this non-terminal symbol has production(s)
				if (rule.count() == 0) {
					createPhenotypeValue(working);
					throw new InvalidPhenotypeException("Invalid Phenotype: Non-Terminal " + symbol.getValue() + " with no Production");
				}
				
				production = rule.getProductions();			
				
				// Replace the current non-terminal symbol with its production symbols
				if (rule.count() == 1) {
					
					symbols = production[0].getSymbols();
					
					// Remove the current symbol and add the new symbols in the working set
					working.remove(currentIndex);
					for (int i = symbols.length - 1; i >= 0; i--) {
						// Check for BNF Grammar Recursion (current symbol exists in the production)
						if (symbols[i].getValue().equals(symbol.getValue())) {
							createPhenotypeValue(working);
							throw new InvalidPhenotypeException("Invalid Phenotype: Recursive BNF Exception in rule " + symbol.getValue());
						}
						else { // Otherwise add the symbol in the working set
							working.add(currentIndex, symbols[i]);
						}
					}
					
				}
				else {
					
					// Increase the index of the codon which will be used (currentCodonIndex)
					increaseCodonIndex(working);
					
					// Select the production rule
					choosenProduction = rna[currentCodonIndex] % rule.count();
					
					// Get the new symbols to be added in the working set
					symbols = production[choosenProduction].getSymbols();
					
					// Remove the current symbol and add the new symbols in the working set
					working.remove(currentIndex);
					for (int i = symbols.length - 1; i >= 0; i--) {
							working.add(currentIndex, symbols[i]);
					}					
	
				
				}
				
			}
						
		}
		
		// Create the corresponding phenotype
		phenotype = new Phenotype<String>(createPhenotypeValue(working));
		
		return phenotype;
		
	}
	
	/**
	 * Creates the String representation of the phenotype as it is in the working linked list.
	 * 
	 * @param list The working linked list with the processed phenotype symbols
	 * @return The String representation of the current status of the processed phenotype
	 */
	private String createPhenotypeValue(LinkedList<BNFSymbol> list) {
		StringBuilder temp = new StringBuilder();
		Iterator<BNFSymbol> iterator = list.iterator();
		while (iterator.hasNext())
			temp.append(iterator.next().getValue());
		phenotypeValue = temp.toString();
		return phenotypeValue;
	}
	
	/**
	 * Calculates and sets the next codon index of the RNA which will be used.
	 * Also, it updates the <code>currentWrap</code> field if necessary.
	 * 
	 * @param list The working linked list with the processed phenotype symbols
	 */
	private void increaseCodonIndex(LinkedList<BNFSymbol> list) throws InvalidPhenotypeException {
		
		int previousIndex = currentCodonIndex;
		
		// Set the index of the codon to be used
		currentCodonIndex++;
		
		// Check if a wrap is necessary
		if (currentCodonIndex == rna.length) {
			currentCodonIndex = 0;
			currentWrap++;
		}
		
		// Check if maximum allowed wraps is reached
		if (currentWrap > maxWraps) {
			createPhenotypeValue(list);
			currentCodonIndex = previousIndex;
			throw new InvalidPhenotypeException("Invalid Phenotype: Max Wraps exceeded (" + (currentWrap - 1) + ")");
		}
		
	}
	

	/**
	 * Implements the reverse process of the Grammatical Evolution
	 * mapping algorithm. Namely, it maps a phenotype to many candidate
	 * genotypes.<br><br>
	 * WARNING: This method is not yet implemented and will throw
	 * an <code>UnsupportedOperationException</code> exception if it will be invoked.
	 * 
	 * @param phenotype The phenotype to be processed.
	 * @return The candidate genotypes.
	 */
	public Vector<Genotype<String>> getGenotype(Phenotype<String> phenotype) {
		throw new UnsupportedOperationException("The method gemapper.getGenotype(phenotype) is not yet supported");
	}

	/**
	 * Getter of the property <tt>codonSize</tt>.
	 * It is the size of each codon in terms of bits.
	 * 
	 * @return Returns the codon size.
	 */
	public int getCodonSize() {
		return codonSize;
	}

	/**
	 * Setter of the property <tt>codonSize</tt>.
	 * It is the size of each codon in terms of bits.
	 * 
	 * @param codonSize The codon size to set.
	 */
	public void setCodonSize(int codonSize) {
		this.codonSize = codonSize;
	}

	/**
	 * Getter of the property <tt>maxWraps</tt>.
	 * It is the number of the maximum allowable wraps of the genotype.
	 * 
	 * @return Returns the maximum allowable wraps of the genotype.
	 */
	public int getMaxWraps() {
		return maxWraps;
	}

	/**
	 * Setter of the property <tt>maxWraps</tt>.
	 * It is the number of the maximum allowable wraps of the genotype.
	 * 
	 * @param maxWraps The maximum allowable wraps of the genotype to set.
	 */
	public void setMaxWraps(int maxWraps) {
		this.maxWraps = maxWraps;
	}

	/**
	 * Getter of the property <tt>grammar</tt>.
	 * It is the BNF Grammar Definition which will be used in the mapping process.
	 * 
	 * @return Returns the BNF Grammar Definition.
	 */
	public BNFGrammar getGrammar() {
		return grammar;
	}

	/**
	 * Setter of the property <tt>grammar</tt>.
	 * It is the BNF Grammar Definition which will be used in the mapping process.
	 * 
	 * @param grammar The BNF Grammar Definition to set.
	 */
	public void setGrammar(BNFGrammar grammar) {
		this.grammar = grammar;
	}

		
	/** 
	 * Checks if a Non-Terminal BNFSymbol is a special directive or not.
	 * If the symbol is a special directive returns true, otherwise returns false.
	 * 
	 * @param symbol The BNFSymbol be checked.
	 * @return True if the symbol is a special directive, otherwise false.
	 */
	public boolean isDirective(BNFSymbol symbol){
		
		if (symbol.isTerminal()) return false;
		
		for (String d : directives)
			if (d.equals(symbol.getValue()))return true;
		
		return false;
		
	}
	
	/**
	 * Returns the Wraps number of the last's run of the mapping process.
	 * If no wrap occured then it returns 0.
	 * 
	 * @return The number of wraps of the last mapping process.
	 */
	public int lastRunWraps() {
		return currentWrap;
	}
	
	/**
	 * Returns the RNA (integers array) of the last's run of the mapping process.
	 * 
	 * @return The RNA (integers array) of the last mapping process.
	 */
	public int[] lastRunRNA() {
		return rna;
	}
	
	/**
	 * Returns the last used codon index of the last's run of the mapping process.<br>
	 * Codons index starts from 0.
	 * 
	 * @return The last used codon index of the last mapping process.
	 */
	public int lastRunCodonIndex() {
		return currentCodonIndex;
	}
	
	/**
	 * Returns the String value of the phenotype of the last's run of the mapping process.
	 * 
	 * @return The value of the phenotype of the last mapping process.
	 */
	public String lastRunPhenotypeValue() {
		return phenotypeValue;
	}
	
	/**
	 * Returns the last processed BNF Symbol of the last's run of the mapping process.
	 * 
	 * @return The last processed BNF Symbol of the phenotype of the last mapping process.
	 */
	public BNFSymbol lastRunSymbol() {
		return symbol;
	}

	
	
}
