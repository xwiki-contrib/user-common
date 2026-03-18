/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.contrib.xwiki.usercommon.formatter.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.contrib.xwiki.usercommon.formatter.UserFormatter;
import org.junit.jupiter.api.Test;
import org.xwiki.test.junit5.mockito.ComponentTest;
import org.xwiki.test.junit5.mockito.InjectMockComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for DefaultUserFormatterFactory
 * @version $Id$
 */
@ComponentTest
class DefaultUserFormatterFactoryTest
{
    @InjectMockComponents
    private DefaultUserFormatterFactory userFormatterFactory;

    private static final Map<String, String> cd = new HashMap<>(2);
    static {
        cd.put("first", "Camille");
        cd.put("last", "Dupont");
        cd.put("nullkey", null);
    }

    private static final Map<String, String> dirtyValues = new HashMap<>(2);
    static {
        dirtyValues.put("first", "C@mille");
        dirtyValues.put("last", "Dup.nt");
    }

    @Test
    void testClean()
    {
        UserFormatter bareFormatter = userFormatterFactory.create();
        UserFormatter dashFormatter = bareFormatter.cleanReplacement("-");
        assertEquals("firstname-lastname", dashFormatter.clean("firstname.lastname"));
        assertEquals("firstname--lastname", dashFormatter.clean("firstname. lastname"));
        assertEquals("firstnamelastname", bareFormatter.clean("firstname.@/,:^\t\n\rlastname"));
    }

    @Test
    void testFormatSimple()
    {
        assertEquals("Camille-Dupont", userFormatterFactory.create(cd).format("${first}-${last}"));
    }

    @Test
    void testFormatTemplatesAndValuesAreNotCleaned()
    {
        assertEquals("C@mille-@-Dup.nt", userFormatterFactory.create(dirtyValues).format("${first}-@-${last}"));
    }

    @Test
    void testFormatCleanReplacementIsNotCleaned()
    {
        assertEquals("C^milleDup^nt",
            userFormatterFactory.create(dirtyValues).cleanReplacement("^").format("${first.clean}${last.clean}"));
    }

    @Test
    void testCleanFormat()
    {
        UserFormatter cdFormatter = userFormatterFactory.create(dirtyValues);
        UserFormatter fancyDashFormatter = cdFormatter.cleanReplacement("-dash-");
        assertEquals("Cmille-Dupnt", cdFormatter.format("${first.clean}-${last._clean}"));
        assertEquals("C-dash-mille-Dup-dash-nt", fancyDashFormatter.format("${first.clean}-${last._clean}"));
    }

    @Test
    void testFormats()
    {
        UserFormatter dvFormatter = userFormatterFactory.create(dirtyValues);
        assertEquals("c@mille-dup.nt", dvFormatter.format("${first.lowerCase}-${last._lowerCase}"));
        assertEquals("C@MILLE-DUP.NT", dvFormatter.format("${first.upperCase}-${last._upperCase}"));
        assertEquals("cmille-dupnt", dvFormatter.format("${first.clean.lowerCase}-${last.clean.lowerCase}"));
        assertEquals("CMILLE-DUPNT", dvFormatter.format("${first.clean.upperCase}-${last._clean._upperCase}"));
    }

    @Test
    void testFormatMissingKeysAreReplaced()
    {
        UserFormatter userFormatter = userFormatterFactory.create(cd);
        assertEquals("Camille---Dupont", userFormatter.format("${first}-${middle}-${nullkey}-${last}"));
        assertEquals("Camille--Dupont", userFormatter.format("${first}-${middle.clean}-${last}"));
    }

    @Test
    void testCharactersToClean()
    {
        UserFormatter f = userFormatterFactory.create(Pattern.compile("y"));
        assertEquals("Camille.Dupont", f.clean("Camyille.Dupont"));
    }

    @Test
    void testCharactersToCleanAndFormat()
    {
        UserFormatter f = userFormatterFactory.create(cd, Pattern.compile("[u.]"));
        assertEquals("CamilleDpont", f.format("${first.clean}${last.clean}"));
    }

    @Test
    void testKeyWithDot()
    {
        UserFormatter bareFormatter = userFormatterFactory.create();
        assertEquals(
            "hello",
            bareFormatter.variables(Collections.singletonMap("dotted.key", "hello")).format("${dotted.key}"));
        assertEquals(
            "",
            bareFormatter.variables(Collections.singletonMap("dotted", "hello")).format("${dotted.key}"));
    }

    @Test
    void testEmptyValues()
    {
        UserFormatter f = userFormatterFactory.create(Collections.singletonMap("k", ""));
        assertEquals("", f.format("${}"));
        assertEquals("", f.format("${k}"));
        assertEquals("", f.format("${k.clean.lowerCase}"));
    }

    @Test
    void testSetGet()
    {
        UserFormatter bareFormatter = userFormatterFactory.create();
        Map<String, String> m = Collections.singletonMap("a", "b");
        UserFormatter f1 = bareFormatter.variables(m);
        assertNotEquals(bareFormatter, f1);
        assertEquals(m, f1.variables());

        Pattern p = Pattern.compile("a");
        UserFormatter f2 = bareFormatter.charactersToClean(p);
        assertNotEquals(bareFormatter, f2);
        assertEquals(p, f2.charactersToClean());

        String c = "^@^";
        UserFormatter f3 = bareFormatter.cleanReplacement(c);
        assertNotEquals(bareFormatter, f3);
        assertEquals(c, f3.cleanReplacement());
    }
}