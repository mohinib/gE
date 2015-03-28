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

package bangor.aiia.jge.util;

/**
 * The class <code>MathUtil</code> is a utility class which provides
 * a collection of useful Mathematical functions.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 08/04/06
 * @since	JavaGE 0.1
 */
public class MathUtil {
	
	/**
	 * Inverts the boolean input (true, false).<br>
	 * Namely, if the input is <code>true</code> returns <code>false</code>
	 * and if the input is <code>false</code> returns <code>true</code>. 
	 * 
	 * @param b The boolean symbol which will be inverted.
	 * @return The inverse of the input.
	 */
	public static boolean invert(boolean b) {
		return !b;		
	}
	
	/**
	 * Inverts the binary input ('0', '1').<br>
	 * Namely, if the input is <code>'0'</code> returns <code>'1'</code> 
	 * and if the input is <code>'1'</code> returns <code>'0'</code>. 
	 * 
	 * @param c The binary symbol which will be inverted.
	 * @return The inverse of the input.
	 * @throws RuntimeException if the input is not binary ('0' or '1').
	 */
	public static char invertBinary(char c) {		
		if (c == '0') return '1';
		else if (c == '1') return '0';
		else throw new RuntimeException("Input was not binary ('0' or '1')");
	}
	
	/**
	 * Inverts the binary input (0, 1).<br>
	 * Namely, if the input is <code>0</code> returns <code>1</code> 
	 * and if the input is <code>1</code> returns <code>0</code>. 
	 * 
	 * @param n The binary symbol which will be inverted.
	 * @return The inverse of the input.
	 * @throws RuntimeException if the input is not binary (0 or 1).
	 */
	public static int invertBinary(int n) {		
		if (n == 0) return 1;
		else if (n == 1) return 0;
		else throw new RuntimeException("Input was not binary (0 or 1)");
	}	
	
	/**
	 * Returns <code>true</code> with probability <code>prob</code>,
	 * otherwise returns <code>false</code>.
	 * 
	 * @param prob The probability to return <code>true</code>.
	 * @return <code>true</code> with probability <code>prob</code>, 
	 * 		   otherwise <code>false</code>.
	 */
	public static boolean flip(double prob) {
		if (prob >= 1.0 || Math.random() < prob)
			return true;
		else 
			return false;	
	}
	
	/**
	 * Returns <code>'1'</code> with probability <code>prob</code>,
	 * otherwise returns <code>'0'</code>.
	 * 
	 * @param prob The probability to return <code>'1'</code>.
	 * @return <code>'1'</code> with probability <code>prob</code>, 
	 * 		   otherwise <code>'0'</code>.
	 */
	public static char flipBinaryChar(double prob) {
		if (flip(prob)) return '1';
		else return '0';
	}
	
	/**
	 * Returns <code>1</code> with probability <code>prob</code>,
	 * otherwise returns <code>0</code>.
	 * 
	 * @param prob The probability to return <code>1</code>.
	 * @return <code>1</code> with probability <code>prob</code>, 
	 * 		   otherwise <code>0</code>.
	 */
	public static int flipBinaryInt(double prob) {
		if (flip(prob)) return 1;
		else return 0;		
	}
	
	/**
	 * Returns a double value with a positive sign, greater than or equal to <code>low</code> 
	 * and less than <code>high</code>.<br>
	 * Note: <code>low</code> limit is inclusive and <code>high</code> limit is exclusive.<br>
	 * The formula used in this method is:<br>
	 * <code>randomDouble = Math.random() * (high - low) + low; (low: Inclusive, high: Exclusive)</code>
	 * 	
	 * @param low The low limit of the result (inclusive).
	 * @param high The high limit of the result (exclusive).
	 * @return The random number in the given range.
	 */
	public static double randomDouble(double low, double high) {
		return Math.random() * (high - low) + low;
	}
		
	/**
	 * Returns an integer value with a positive sign, greater than or equal to <code>low</code> 
	 * and less than or equal to <code>high</code>.<br>
	 * Note: <code>low</code> limit is inclusive and <code>high</code> limit is also inclusive.<br>
	 * The formula used in this method is:<br>
	 * <code>randomInteger = (int) (Math.random() * (high + 1 - low)) + low; (low: Inclusive, high: Inclusive)</code>
	 * 
	 * @param low The low limit of the result (inclusive).
	 * @param high The high limit of the result (inclusive).
	 * @return The random number in the given range.
	 */
	public static int randomInteger(int low, int high) {
		return (int) (Math.random() * (high + 1 - low)) + low;
	}
	

}
