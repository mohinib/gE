/**
 * Project: JavaGE Library
 * Author:  Loukas Georgiou
 * Date:    14 Jan 2006
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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The class <code>CaptureOutputStream</code> extends the class
 * <code>FilterOuputStream</code> and captures (redirects) the output stream
 * of bytes of the underlying <code>OutputStream</code> object in a
 * <code>StringBuilder</code> object.
 * 
 * @author 	Loukas Georgiou
 * @version 1.0, 30/03/06
 * @see 	FilterOutputStream
 * @see 	OutputStream
 * @since 	JavaGE 0.1
 */
public class CapturedOutputStream extends FilterOutputStream {

	/**
	 * The object where the output stream will be stored.
	 */
	StringBuilder target = null;

	/**
	 * CapturedOutputStream Constructor.<br>
	 * Creates an output stream filter built
	 * on top of the specified underlying output stream and stores the bytes in
	 * the passed <code>StringBuilder</code> object.
	 * 
	 * @param out The underlying output stream to be captured.
	 * @param target The object where the output stream will be stored.
	 */
	public CapturedOutputStream(OutputStream out, StringBuilder target) {
		super(out);
		this.target = target;
	}

	/**
	 * Writes b.length bytes to this output stream.
	 * 
	 * @param b The data to be written.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(byte[] b) throws IOException {
		String tmp = new String(b);
		target.append(tmp);
	}

	/**
	 * Writes len bytes from the specified byte array starting at offset off to
	 * this output stream.
	 * 
	 * @param b The data.
	 * @param off The start offset in the data.
	 * @param len The number of bytes to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		String tmp = new String(b, off, len);
		target.append(tmp);
	}

	/**
	 * Writes the specified byte to this output stream.
	 * 
	 * @param b The byte.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(int b) throws IOException {		
		String tmp = new String(new byte[] {(byte)b});
		target.append(tmp);
	}

	/**
	 * Closes this output stream and releases any system resources associated
	 * with the stream.
	 * 
	 * @throws IOException If an I/O error occurs.
	 */
	public void close() throws IOException {
		super.flush();
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be
	 * written out to the stream.
	 * 
	 * @throws IOException If an I/O error occurs.
	 */
	public void flush() throws IOException {
		super.flush();
	}

}
