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
package org.gedcom4j.validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gedcom4j.model.*;
import org.junit.Test;

/**
 * A test for Issue 111, where we wanted to introduce duplicate elimination in the validators when autorepair is on.
 * 
 * @author frizbog
 */
public class Issue111Test {

    /**
     * Test removal of duplicate names
     */
    @Test
    public void testRemoveDupNames() {
        Gedcom g = new Gedcom();
        Submitter subm = new Submitter();
        subm.setName(new StringWithCustomTags("Sean /Connery/"));
        subm.setXref("@SUBM@");
        g.getSubmitters().put("@SUBM@", subm);
        Individual i = new Individual();
        i.setXref("@I001@");
        PersonalName pn1 = new PersonalName();
        pn1.setBasic("Duncan /Highlander/");
        i.getNames(true).add(pn1);
        PersonalName pn2 = new PersonalName();
        pn2.setBasic("Duncan /Highlander/");
        i.getNames().add(pn2);
        g.getIndividuals().put(i.getXref(), i);
        GedcomValidator gv = new GedcomValidator(g);
        gv.validate();
        assertFalse(gv.hasErrors());
        assertFalse(gv.hasWarnings());
        assertTrue(gv.hasInfo());
        assertEquals(1, gv.getFindings().size());
        assertEquals("INFO: 1 duplicate names found and removed (Duncan /Highlander/)", gv.getFindings().get(0).toString());
        assertEquals("There can be only one", 1, i.getNames().size());
    }

}
