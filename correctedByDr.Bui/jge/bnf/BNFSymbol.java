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
 * The <code>BNFSymbol</code> represents a terminal or non-terminal symbol 
 * of the BNF Grammar. A terminal symbol is a symbol which value can appear 
 * in legal sentences of the grammar. A non-terminal symbol is a symbol which value 
 * is used in the generation of other terminal or non-terminal symbols 
 * according to the rules of the BNF grammar.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.01, 04/03/06
 * @see 	BNFGrammar
 * @see 	BNFRule
 * @see 	BNFProduction
 * @see 	BNFSymbolType
 * @since	JavaGE 0.1
 */
public class BNFSymbol {

	/**
	 * The type of this symbol: Terminal or Non-Terminal
	 */
	private final BNFSymbolType type;
	
	/**
	 * The <code>String</code> value of this symbol.
	 */
	private final String value;
	

	/**
	 * Default empty constructor. Should not be used.
	 */
	@SuppressWarnings("unused")
	private BNFSymbol(){
		type = null;
		value = null;
	}
	
	
	/**
	 * Creates a new symbol.
	 * 
	 * @param type  The type of the symbol (terminal, non-terminal).
	 * @param value  The string representation of the symbol (value).
	 */
	public BNFSymbol(BNFSymbolType type, String value){
		this.type = type;
		this.value = value;
	}
	
	
	/**
	 * If this symbol is a terminal symbol returns true otherwise returns false.
	 * 
	 * @return True if the symbol is a Terminal Symbol. Otherwise returns false.
	 */
	public boolean isTerminal(){
		if (type == BNFSymbolType.Terminal) return true;
		else return false;
	}
	
	
	/**
	 * Returns the type of the symbol which can be terminal or non-terminal
	 * (Getter of the property <tt>type</tt>).
	 * 
	 * @return  Returns the type of the symbol.
	 */
	public BNFSymbolType getType() {
		return type;
	}


	/**
	 * Returns the string representation (value) of the symbol
	 * (Getter of the property <tt>value</tt>).
	 * 
	 * @return  Returns the value.
	 */
	public String getValue() {
		return value;
	}

	
	/**
	 * Prints the value of the symbol. 
	 * It replaces the following special characters \r, \n, \t with #r, #n, #t.
	 * 
	 * @return Returns the string representation of the object.
	 */
	public String toString(){
		
		String symbol = getValue();
		symbol = symbol.replace("\r", "#r"); 	// Carriage Return Character
		symbol = symbol.replace("\n", "#n");	// New Line Character
		symbol = symbol.replace("\t", "#t");	// Tab Character	
		
		return symbol;
	}
	

}
