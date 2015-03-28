/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	21 Jan 2006
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

package bangor.aiia.jge.bnf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


import static bangor.aiia.jge.bnf.BNFSymbolType.*;

/** 
 * The class <code>BNFGrammar</code> is the mapping of a BNF Grammar to a Java Object.
 * This class loads a BNF Grammar from a text file or a string and creates 
 * the appropriate representation of the grammar in the form of Java objects. 
 * In this way the grammar can be used in a convenient way by the java programs 
 * which need to access and process a BNF grammar.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.01, 04/03/06
 * @see 	BNFRule
 * @see 	BNFProduction
 * @see 	BNFSymbol
 * @see 	BNFSymbolType
 * @since	JavaGE 0.1
 */
public class BNFGrammar {

	/**
	 * The set of the rules of the BNF Grammar.
	 * @uml.property   name="rule"
	 * @uml.associationEnd   multiplicity="(0 -1)" dimension="1" ordering="true" aggregation="composite" inverse="bNFGrammar:bangor.aiia.jge.bnf.BNFRule"
	 */
	private BNFRule[] rules;
	
	/**
	 * The start symbol of the BNF Grammar.
	 * 
	 * @uml.property  name="startSymbol" readOnly="true"
	 */
	private BNFSymbol startSymbol;
	
	
	
	/**
	 * Default empty constructor. Should not be used.
	 */
	@SuppressWarnings("unused")
	private BNFGrammar() {}
	
	
	/**
	 * Creates a new <code>BNFGrammar</code> object from a file with the BNF Grammar.
	 * 
	 * @param file  The file with the BNF Grammar.
	 * @throws IOException when the file does not exist or there is a problem
	 * 		   with the reading of the file.
	 */
	public BNFGrammar(File file) throws IOException {
		
		StringBuilder grammar = new StringBuilder();
		String temp = null;
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		
		//Read the text from the file
		while((temp = bufReader.readLine()) != null) {
			grammar.append(temp);
			grammar.append("\n");
		}		
		bufReader.close();
		reader.close();
		
		//Parse the Grammar and create the Java Objects Representation
		//of the BNF Grammar.
		parse(grammar.toString());
		
	}

			
	/**
	 * Initialises the <code>BNFGrammar</code> object from a string with the BNF Grammar
	 * and creates all the necessary objects which represent the rules, the productions,
	 * and the symbols of the BNF Grammar.
	 * 
	 * @param grammar  The BNF Grammar.
	 */
	public BNFGrammar(String grammar){		
		//Parse the Grammar and create the Java Objects Representation
		//of the BNF Grammar.
		parse(grammar);			
	}
		
		
	/** 
	 * Parses the loaded BNF Grammar and creates the set of rules.
	 * This method uses the <code>BNFParser</code> utility and the methods
	 * it provides for the processing and parsing of a BNF Grammar Definition,
	 * and creates the appropriate Java Objects representation of the given
	 * BNF Grammar Definition. Namely, it creates the <code>BNFRule</code>,
	 * <code>BNFProduction</code>, and <code>BNFSymbol</code> objects
	 * which represent an Java Object-Oriented Representation of the given
	 * BNF Grammar Definition. This representation is accessable through
	 * this object (<code>BNFGrammar</code>) and its methods and fields.
	 * 
	 * @param grammar  The BNF Grammar.	
	 */
	private void parse(String grammar){

		String header = null;
		Vector<String> rules = null;
		Vector<String> productions = null;
		Vector<String> symbols = null;		
		
		BNFRule[] bnfRules = null;		
		BNFProduction[] bnfProductions = null;
		BNFSymbol[] bnfSymbols = null;
		
		// Parse the BNF Grammar Definition
		try {
			
			// Get the rules
			rules = BNFParser.getRules(grammar);
			System.out.println("Rules are: " +rules);
			// Initialise the BNFRule[] array
			bnfRules = new BNFRule[rules.size()];
			int r = 0; // Rules Index;

			// Iteration through all rules of the BNF Grammar Definition
			for(String rule : rules) {
				
				// Get the header and the productions of the rule
				header = BNFParser.getRuleHeader(rule);
				productions = BNFParser.getProductions(rule);
				
				// Initialise the BNFProduction[] array
				bnfProductions = new BNFProduction[productions.size()];
				int p = 0; // Productions Index;
				
				// Set the Start Symbol of the Grammar: 
				// The First Non-Terminal Symbol (Header) of the BNF Grammar Definition
				if (this.startSymbol == null)
					this.startSymbol = new BNFSymbol(NonTerminal, header);				
								
				// Iteration through all the BNF Productions of a BNF Rule
				for (String production : productions) {
					
					// Get the symbols of the production
					symbols = BNFParser.getSymbols(production);
					// Initialise the BNFSymbol[] array
					bnfSymbols = new BNFSymbol[symbols.size()];
					int s = 0; // Symbols Index
					
					// Iteration through all the BNF Rules of a BNF Production
					for (String symbol : symbols) {					
						
						// Add the new BNFSymbol objects in the BNFSymbol[] array
						if (symbol.startsWith("<")) // Non-Terminal Symbol
							bnfSymbols[s++] = new BNFSymbol(NonTerminal, symbol);
						else						// Terminal Symbol
							bnfSymbols[s++] = new BNFSymbol(Terminal, symbol);
						
					} // End of Symbols Iteration
					
					// Add the new BNFProduction object in the BNFProduction[] array
					bnfProductions[p++] = new BNFProduction(bnfSymbols);
					
				} // End of Productions Iteration

				// Add the new BNFRule object in the BNFRule[] array
				bnfRules[r++] = new BNFRule(new BNFSymbol(NonTerminal, header), bnfProductions);	
				
			} // End of Rules Iteration
			
			// Set the BNFRule[] object of this BNFGrammar
			this.rules = bnfRules;
			
		
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			ioe.printStackTrace();
		} catch (InvalidBNFException ibe) {
			System.out.println(ibe.getMessage());
			ibe.printStackTrace();
		}	

		
	}

	
	/**
	 * Returns the start symbol of the BNF Grammar
	 * (Getter of the property <tt>startSymbol</tt>).
	 * 
	 * @return  Returns the startSymbol.
	 */
	public BNFSymbol getStartSymbol() {
		return startSymbol;
	}

		
	/**
	 * Returns the rule for the specific non-terminal symbol.
	 * If either there is no rule for the non-terminal symbol or
	 * the symbol was a terminal symbol then null is returned.
	 * 
	 * @param symbol  The symbol for which the rule is returned.
	 * @return Returns the rule for the symbol.
	 */
	public BNFRule getRule(BNFSymbol symbol){	
		
		if (symbol.isTerminal())
			return null;
		
		//Iterate all the Rules and return the appropriate BNFRule object.
		for (int i = 0; i < rules.length; i++) {
			if (rules[i].getHead().getValue().equals(symbol.getValue()))
				return rules[i];
		}
		
		return null;		
		
	}

	
	/**
	 * The <code>String</code> representation of the BNF Grammar Definition.
	 * 
	 * @return Returns the <code>String</code> representation of the BNFGrammar object.
	 */
	public String toString(){
		
		StringBuilder out = new StringBuilder();
		
		//Create the String representation of the BNF Grammar Definition.
		for (int i = 0; i < rules.length; i++) {
			out.append(rules[i].toString());
			out.append("\n");
		}
		
		return out.toString();
		
	}		
	
	/**
	 * Prints in the standard output the <code>String</code> 
	 * representation of the BNF Grammar Definition.
	 */
	public void printGrammar() {
		System.out.println(this.toString());		
	}


}