/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.io.writer;

import java.io.IOException;
import java.io.OutputStream;

import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.writer.GedcomWriter;

/**
 * A class that writes GEDCOM data in big-endian Unicode format.
 * 
 * @author frizbog
 */
class UnicodeBigEndianWriter extends AbstractEncodingSpecificWriter {

    /**
     * Constructor
     * 
     * @param writer
     *            The {@link GedcomWriter} this object is assisting
     */
    public UnicodeBigEndianWriter(GedcomWriter writer) {
        super(writer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeLine(OutputStream out, String line) throws IOException, WriterCancelledException {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            out.write(c >> 8);
            out.write(c & 0x00FF);
            bytesWritten += 2;
        }
        writeLineTerminator(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeLineTerminator(OutputStream out) throws IOException, WriterCancelledException {
        switch (terminator) {
            case CR_ONLY:
                out.write((byte) 0x00);
                out.write((byte) 0x0D);
                bytesWritten += 2;
                break;
            case LF_ONLY:
                out.write((byte) 0x00);
                out.write((byte) 0x0A);
                bytesWritten += 2;
                break;
            case LFCR:
                out.write((byte) 0x00);
                out.write((byte) 0x0A);
                out.write((byte) 0x00);
                out.write((byte) 0x0D);
                bytesWritten += 4;
                break;
            case CRLF:
                out.write((byte) 0x00);
                out.write((byte) 0x0D);
                out.write((byte) 0x00);
                out.write((byte) 0x0A);
                bytesWritten += 4;
                break;
            default:
                throw new IllegalStateException("Terminator selection of " + terminator + " is an unrecognized value");
        }
        if (writer.isCancelled()) {
            throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
        }
    }

}
