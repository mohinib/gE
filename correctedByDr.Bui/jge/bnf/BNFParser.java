/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:	11 Feb 2006
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
import java.io.StringReader;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** 
 * The class <code>BNFParser</code> is a utility which provides methods
 * for the processing and parsing of a <code>String</code>
 * representation of a BNF Grammar Definition and its elements.
 * It provides methods for the extraction as <code>String</code> type of 
 * the following elements of a BNF Grammar Definition: 
 * <ul>
 * <li>Rules</li> 
 * <li>Left Part of a Rule (Head Symbol)</li>
 * <li>Productions of a Rule</li>
 * <li>Symbols of a Production</li>
 * </ul>
 * It can be used by any program which needs to parse a BNF Grammar Definition.<br>
 * <br>
 * <b>Current Restrictions in the BNF Format</b><br>
 * Currently it is supported only the classical BNF dialect which conforms to 
 * the following rules:
 * <ul>
 *  <li>The Terminal Symbols are written naturally</li>
 *  <li>The Non-Terminals Symbols are enclosed in &lt;angle brackets&gt;</li>
 *  <li>The definition symbol in rules is "<code> ::= </code>"</li>
 *  <li>There is no rule statement terminator. This means that each rule 
 *  	terminates when the next one begins</li>
 *  </ul>
 *  More specifically the following rules must hold in order to be
 *  correctly parsed a BNF Grammar Definition by the current version of 
 *  the <code>BNFParser</code> class:<br>
 *  <ul>
 *   <li>Rules:<br>Each rule starts with the following regular expression: <code>^&lt;</code><br>Also, if the last character of the rule is the new line character (\n) then it is removed from the Rule's <code>String</code> representation</li>
 *   <li>Rule Header:<br>Each header is identified by the group(1) of the following regular expression: <code>(<.*>) ::= .*</code><br></li>
 *   <li>Productions:<br>Each production is separated from the other one with the pipeline character (<code>|</code>)</li>
 *   <li>Symbols:<br>Each symbol (terminal and non-terminal) is identified with the following regular expression: <code>&lt;.*?&gt;|[^&lt;&gt;]*</code></li>
 *   <li>Invalid Characters:<br>The following characters are invalid for the non-terminal symbols: <code>&lt;</code>,<code>&gt;</code>,<code>|</code><br></li>
 *   						In the case of Java or C programming languages the OR operator should be used only in this form: <code>||<code>. Never as a single pipeline (<code>|</code>).
 *  </ul>
 *  <br>
 *  <b>Examples of Valid BNF Grammar Definitions</b>
 *  <br><br>
 *  <code>
 *  &lt;code&gt; ::= &lt;line&gt; | &lt;code&gt; &lt;line&gt;<br>
 *  &lt;line&gt; ::= &lt;condition&gt; | &lt;op&gt;<br>
 *  &lt;condition&gt; ::= if(food_ahead()) { &lt;line&gt; } else { &lt;line&gt; }<br>
 *  &lt;op&gt; ::=  left(); | right(); | move(); 
 * </code>
 * <br>
 * Source:<br>
 * NICOLAU, M. (2005), libGE: Grammatical Evolution Library for version 0.24, 20 October 2005.
 * Available from: <a href="http://waldo.csisdmz.ul.ie/libGE/libGE.pdf">http://waldo.csisdmz.ul.ie/libGE/libGE.pdf</a> (Accessed 30 November 2005). 
 * <br><br>
 * <code>
 * &lt;Expr&gt; ::= &lt;Int&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| &lt;Expr&gt;&lt;Oper&gt;&lt;Int&gt;<br>
 * &lt;Int&gt; ::= &lt;Int&gt;&lt;Digit&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| &lt;Digit&gt;<br>
 * &lt;Digit&gt; ::= 0|1|2|3|4|5|6|7|8|9<br>
 * &lt;Oper&gt; ::= +<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| -<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| *<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| /
 * </code>
 * <br>
 * Source:<br>
 * NICOLAU, M. (2005), libGE: Grammatical Evolution Library for version 0.24, 20 October 2005.
 * Available from: <a href="http://waldo.csisdmz.ul.ie/libGE/libGE.pdf">http://waldo.csisdmz.ul.ie/libGE/libGE.pdf</a> (Accessed 30 November 2005). 
 * 
 * @author 	Loukas Georgiou 
 * @version	1.01, 04/03/06
 * @see 	BNFGrammar
 * @see 	BNFRule
 * @see 	BNFProduction
 * @see 	BNFSymbol
 * @see 	BNFSymbolType
 * @since	JavaGE 0.1
 */
public class BNFParser {
	
	/**
	 * Constant used by the <code>printVectorElements</code> methdod.
	 * It doesn't add a new line character after each element print out.
	 */	 
	public final static int NOLINE = 0;
	
	/**
	 * Constant used by the <code>printVectorElements</code> methdod.
	 * It adds a new line character after each element print out.
	 */	 
	public final static int NEWLINE = 1;
	
	/**
	 * Constant used by the <code>printVectorElements</code> methdod.
	 * It adds the character <code>#</code> in the boundaries of the element.
	 */	 
	public final static int SHOWBOUNDARIES = 2;
		
	
	/**
	 * Default empty constructor. Should not be used.
	 */
	private BNFParser() {}

	/**
	 * Returns a <code>Vector&lt;String&gt;</code> 
	 * with the rules of the BNF Grammar Definition.
	 * If there are no rules then an <code>InvalidBNFException</code> will be thrown.
	 * 
	 * @param bnf The <code>String</code> representation of the BNF Grammar Definition.
	 * @return Returns the rules of the BNF Grammar Definition.
	 * @throws IOException when there is an exception in the buffering of 
	 * 		   the BNF String representation.
	 * @throws InvalidBNFException when the processed string is not a valid
	 * 		   BNF Grammar Definition.
	 */
	public static Vector<String> getRules(String bnf) throws IOException, InvalidBNFException {

		if (bnf == null) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getRules(bnf):\nThe passed parameter was null");
		
		Vector<String> rules = new Vector<String>();		
		BufferedReader br = new BufferedReader(new StringReader(bnf));
		String line = "";
		StringBuilder cur = new StringBuilder();
				
		try {
			
			while ((line = br.readLine()) != null) {

				if (line.startsWith("<")) { 		// New rule found
					if (cur.toString().trim().length() > 0) {
						// Remove the new line character \n from the end of the rule
						if (cur.charAt(cur.length() - 1) == '\n')
							cur.deleteCharAt(cur.length() - 1);
						rules.add(cur.toString());	// Add to the vector the previous rule
						cur.delete(0, cur.length());
					}
					cur.append(line);
					cur.append("\n");
				}
				else {
					// Is part of the current rule
					cur.append(line);
					cur.append("\n");			
				}						
			}
			if (cur.toString().trim().length() > 0) {
				// Remove the new line character \n from the end of the rule
				if (cur.charAt(cur.length() - 1) == '\n')
					cur.deleteCharAt(cur.length() - 1);
				rules.add(cur.toString());	// Add to the vector the last rule
				cur.delete(0, cur.length());
			}
			
			
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		
		if (rules.size() == 0) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getRules(bnf):\nThere are not rules in the passed text\n" + bnf);
		
		return rules;

	}
	

	/**
	 * Returns the left part of a rule (Header Symbol).
	 * If there is no header symbol then an <code>InvalidBNFException</code> will be thrown.
	 * 
	 * @param rule  The BNF Rule.
	 * @return The left part of the BNF Rule.
	 * @throws InvalidBNFException when the processed string is not a valid
	 * 		   BNF Rule.
	 */
	public static String getRuleHeader(String rule) throws InvalidBNFException {
		
		if (rule == null) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getRuleHeader(rule):\nThe passed parameter was null");
		
		// BNF Rule Header Regullar Expression
		Pattern p = Pattern.compile("(<.*>) ::= .*");
		Matcher m = p.matcher(rule);
		if (m.find())
			return m.group(1);
		else
			throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getRuleHeader(rule):\nNo Rule Header was found\n" + rule);
		
	}

	
	/**
	 * Returns a <code>Vector&lt;String&gt;</code> 
	 * with the right parts of the productions of a BNF Rule.
	 * If there are no productions then an <code>InvalidBNFException</code> will be thrown.
	 * 
	 * @param rule The BNF Rule.
	 * @return Returns the productions of the BNF Rule.
	 * @throws InvalidBNFException when the processed string is not a valid
	 * 		   BNF Rule.
	 */
	public static Vector<String> getProductions(String rule) throws InvalidBNFException {
		
		if (rule == null) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getProductions(rule):\nThe passed parameter was null");
		
		Vector<String> prod = new Vector<String>();
		String token = "";
		
		// Get the right part of the rule
		rule = rule.substring(rule.indexOf("::= ") + 4, rule.length());	
		
		// Replace the Java OR operator (||) in order to not conflict 
		// with the productions separator (|) of the BNF Grammar  
		rule = rule.replace("||", "<@JAVA_OR@>");	
		
		// Get the Productions of the Rule
		StringTokenizer tokenizer = new StringTokenizer(rule, "|");		
		while(tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			// Add again the Java OR operator (||) in the production
			token = token.replace("<@JAVA_OR@>", "||");			
			prod.add(token);
		}
		
		if (prod.size() == 0) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getProductions(rule):\nNo Productions were found\n" +  rule);
		
		return prod;

	}

	
	/**
	 * Returns a <code>Vector&lt;String&gt;</code> 
	 * with the symbols (terminals and non-terminals) of a BNF Production.
	 * If there are no valid symbols then an	 <code>InvalidBNFException</code> will be thrown.
	 * 
	 * @param production The BNF Production.
	 * @return Returns the symbols of the BNF Production.
	 * @throws InvalidBNFException when the processed string is not a valid
	 * 		   BNF Production.
	 */
	public static Vector<String> getSymbols(String production) throws InvalidBNFException {
		
		if (production == null) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getSymbols(production):\nThe passed parameter was null");
		
		Vector<String> symbols = new Vector<String>();
		
		// Terminal and Non-Terminal symbols Reqular Expression
		Pattern p = Pattern.compile("<.*?>|[^<>]*", Pattern.MULTILINE);
		Matcher m = p.matcher(production);
		while (m.find()) {		
			if (m.group().length() > 0)
				symbols.add(m.group());			
		}		
		
		if (symbols.size() == 0) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.getSymbols(production):\nNo Symbols were found\n" + production);
		
		return symbols;
		
	}
	
	
	/**
	 * Returns the BNF Grammar Definition text of the given file.
	 * 
	 * @param path The absolute pathe of the file to be loaded.
	 * @return Returns the BNF text of the file.
	 * @throws InvalidBNFException when the file is empty.
	 */
	public static String loadBNFGrammar(String path) throws InvalidBNFException {

		if (path == null) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.loadBNFGrammar(path):\nThe passed parameter was null");
		
		String bnf = null;
		
		try {
			StringBuilder grammar = new StringBuilder();
			String temp = null;
			FileReader reader = new FileReader(path);
			BufferedReader bufReader = new BufferedReader(reader);
			while((temp = bufReader.readLine()) != null) {
				grammar.append(temp);
				grammar.append("\n");
			}
			bufReader.close();
			reader.close();
	
			bnf = grammar.toString();
			if (bnf.trim().length() == 0) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.loadBNFGrammar(production):\nThe file is empty");
		}
		catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		return bnf;
		
	}
	
	
	/**
	 * Returns the BNF Grammar Definition text of the given file.
	 * 
	 * @param file The <code>File</code> object of the file to be loaded.
	 * @return Returns the text of the file.
	 * @throws InvalidBNFException when the file is empty.
	 */
	public static String loadBNFGrammar(File file) throws InvalidBNFException {
		
		if (file == null) throw new InvalidBNFException("Invalid BNF Grammar Definition in the method BNFParser.loadBNFGrammar(file):\nThe passed parameter was null");
		
		String bnf = null;
		try {
			bnf = loadBNFGrammar(file.getCanonicalPath());
		}
		catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}		
		return bnf;
		
	}
	
	
	/**
	 * Prints in the standard output the elements of the given 
	 * <code>Vector&lt;String&gt;</code> object.
	 * <br>It is useful for the debugging of the static methods of the <code>BNFParser</code> class. 
	 * <br>The valid values for the parameter <code>format</code> are the following:<br>
	 * <ul>
	 *	<li><code>BNFParser.NOLINE</code><br>Prints the list of the elements of the vector without an empty line between them.</li>
	 *	<li><code>BNFParser.NEWLINE</code><br>Prints the list of the elements of the vector WITH an empty line between them.</li>
	 *	<li><code>BNFParser.SHOWBOUNDARIES</code><br>Prints the list of the elements bounded at the start and the end with the character <code>#</code></li>
	 *	<li><code>BNFParser.NOLINE | BNFParser.SHOWBOUNDARIES</code><br>A combination of the effects of both the <code>NOLINE</code> and the <code>SHOWBOUNDARIES</code></li>
	 *	<li><code>BNFParser.NEWLINE | BNFParser.SHOWBOUNDARIES</code><br>A combination of the effects of both the <code>NEWLINE</code> and the <code>SHOWBOUNDARIES</code></li>
	 * </ul>
	 * 
	 * @param vector The <code>Vector&lt;String&gt;</code> to be printed.
	 * @param format The format type of the output.<br>
	 */
	public static void printVectorElements(Vector<String> vector, int format ) {

		if (vector != null) {
			
			for (String item : vector) {

				if ((format & SHOWBOUNDARIES) == SHOWBOUNDARIES) 
					System.out.print("#");
				System.out.print(item);
				if ((format & SHOWBOUNDARIES) == SHOWBOUNDARIES) 
					System.out.print("#");
				if ((format & NEWLINE) == NEWLINE) 
					System.out.println();
			}
		}
		else {
			System.out.print("Vector is Null");
			if (((format & NEWLINE) == NEWLINE) ) System.out.println();
		}
	}


}