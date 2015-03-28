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
 * The <code>BNFSymbolType</code> is an enumeration of all the possible types of a symbol. 
 * The BNF Grammar definition uses two types of symbols:<br /> Terminal (<code>BNFSymbolType.Terminal</code>) 
 * and Non-Terminal (<code>BNFSymbolType.NonTerminal</code>).
 * 
 * @author 	Loukas Georgiou 
 * @version	1.01, 04/03/06
 * @see 	BNFGrammar
 * @see 	BNFRule
 * @see 	BNFProduction
 * @see 	BNFSymbol
 * @since	JavaGE 0.1
 */
public enum BNFSymbolType {
	
	/**
	 * Indicates that the symbol is a Terminal symbol.
	 */
	Terminal, 
	/**
	 * Indicates that the symbol is a Non-Terminal symbol.
	 */
	NonTerminal

}
