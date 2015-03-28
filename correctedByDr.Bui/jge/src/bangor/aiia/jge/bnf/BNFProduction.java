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

/** 
 * The class <code>BNFProduction</code> represents a production of the BNF Grammar, 
 * namely a specific transformation associated with a non-terminal symbol 
 * (<code>BNFSymbol</code>). 
 * Each one <code>BNFProduction</code> object is a set of terminal and/or non-terminal 
 * symbols (<code>BNFSymbol</code>).
 *
 * @author 	Loukas Georgiou 
 * @version	1.01, 04/03/06
 * @see 	BNFGrammar
 * @see 	BNFRule
 * @see 	BNFSymbol
 * @see 	BNFSymbolType
 * @since	JavaGE 0.1
 */
public class BNFProduction {

	/**
	 * The set of symbols of the production.
	 * @uml.property   name="symbol"
	 * @uml.associationEnd   multiplicity="(0 -1)" dimension="1" ordering="true" aggregation="composite" inverse="bNFProduction:bangor.aiia.jge.bnf.BNFSymbol"
	 */
	private final BNFSymbol[] symbols;
	
	/**
	 * It stores the amount of the symbols of this production.
	 * 
	 * @uml.property  name="numOfSymbols" readOnly="true"
	 */
	private final int numOfSymbols;
	
	
	/**
	 * Default empty constructor. Should not be used.
	 */
	@SuppressWarnings("unused")
	private BNFProduction() {
		this.symbols = null;
		this.numOfSymbols = 0;
	}
	
	
	/**
	 * Creates a new Production. The production is a specific transformation
	 * associated with the condition of a rule. It is a collection of terminal
	 * and non-terminal symbols.
	 * 
	 * @param symbols  The symbols set of the production.
	 */
	public BNFProduction(BNFSymbol[] symbols) {
		this.symbols = symbols;
		this.numOfSymbols = symbols.length;
	}

	
	/**
	 * Returns the set of symbols which composes this production.
	 * (Getter of the property <tt>productions</tt>)
	 * 
	 * @return  Returns the set of symbols of the production.
	 */
	public BNFSymbol[] getSymbols() {
		return symbols;
	}
	
	
	/**
	 * Counts and returns the number of the symbols of this production.
	 * 
	 * @return The number of the symbols of this production.
	 */
	public int count(){
		return numOfSymbols;
	}
	
		
	/**
	 * Prints the value of the production. 
	 * It replaces the following special characters \r, \n, \t with #r, #n, #t.
	 * 
	 * @return Returns the string representation of the object.
	 */
	public String toString(){
		
		StringBuilder out = new StringBuilder();
		
		// Create the String representation of the BNFProduction.
		for (int i = 0; i < numOfSymbols; i++) {
			out.append(symbols[i].toString());
		}
		
		String production = out.toString();
		production = production.replace("\r", "#r"); 	// Carriage Return Character
		production = production.replace("\n", "#n");	// New Line Character
		production = production.replace("\t", "#t");	// Tab Character
		
		return production;
		
	}
	

}
