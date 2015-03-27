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
 * The <code>BNFRule</code> represents a rule of the BNF Grammar. 
 * A <code>BNFGrammar</code> object is a collection of all the specified BNF rules. 
 * Each one <code>BNFRule</code> object is a set of all productions (<code>BNFProduction</code>)
 * associated with a non-terminal symbol (<code>head:BNFSymbol</code>) 
 * which is the left part (condition) of the rule.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.01, 04/03/06
 * @see 	BNFGrammar
 * @see 	BNFProduction
 * @see 	BNFSymbol
 * @see 	BNFSymbolType
 * @since	JavaGE 0.1
 */
public class BNFRule {

	/**
	 * The set of the possible transformations (productions) of the rule.
	 * @uml.property   name="production"
	 * @uml.associationEnd   multiplicity="(0 -1)" dimension="1" ordering="true" aggregation="composite" inverse="bNFRule:bangor.aiia.jge.bnf.BNFProduction"
	 */
	private final BNFProduction[] productions;
	
	/**
	 * It stores the amount of all the possible productions of the rule.
	 * 
	 * @uml.property  name="numOfProductions" readOnly="true"
	 */
	private final int numOfProductions;
	
	/**
	 * The left (condition) part of the rule.
	 */
	private final BNFSymbol head;
	
	
	/**
	 * Default empty constructor. Should not be used.
	 */
	@SuppressWarnings("unused")
	private BNFRule() {
		this.head = null;
		this.productions = null;
		this.numOfProductions = 0;
	}
	

	/**
	 * Creates a new rule.
	 * 
	 * @param head  The condition of the rule.
	 * @param productions  The set of productions of the rule.  
	 */
	public BNFRule(BNFSymbol head, BNFProduction[] productions) {
		this.head = head;
		this.productions = productions;
		this.numOfProductions = productions.length;
	}

	
	/**
	 * Returns the left part symbol (condition) of the rule
	 * (Getter of the property <tt>head</tt>).
	 * 
	 * @return  Returns the condition of the rule.
	 */
	public BNFSymbol getHead() {
		return head;
	}

	
	/**
	 * Returns the set of productions associated with this rule
	 * (Getter of the property <tt>productions</tt>)
	 * 
	 * @return  Returns the set of productions of the rule.
	 */
	public BNFProduction[] getProductions() {
		return productions;
	}
	
	
	/**
	 * Counts and returns the number of the productions of this rule.
	 * 
	 * @return The number of the productions of this rule.
	 */
	public int count(){
		return numOfProductions;
	}
	
	
	/**
	 * Returns the string representation of the value of the rule. 
	 * 
	 * @return Returns the string representation of the object.
	 */
	public String toString(){
		
		StringBuilder out = new StringBuilder();
		out.append(head.toString());
		out.append("\n");
		
		//Create the String representation of the BNFRule.
		for (int i = 0; i < numOfProductions; i++) {
			out.append("\t");
			out.append(productions[i].toString());
			out.append("\n");
		}	
		
		return out.toString();
		
	}
	

}
