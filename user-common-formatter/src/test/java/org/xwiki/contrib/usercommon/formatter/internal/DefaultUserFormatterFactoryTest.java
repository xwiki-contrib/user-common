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
package org.xwiki.contrib.usercommon.formatter.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.xwiki.contrib.usercommon.formatter.UserFormatter;
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
        UserFormatter f = userFormatterFactory.create();
        assertEquals("firstnamelastname", f.clean("firstname.@/,:^\t\n\rlastname"));
        f.setForbiddenReplacement("-");
        assertEquals("firstname-lastname", f.clean("firstname.lastname"));
        assertEquals("firstname--lastname", f.clean("firstname. lastname"));
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
    void testFormatForbiddenReplacementIsNotCleaned()
    {
        UserFormatter f = userFormatterFactory.create(dirtyValues);
        f.setForbiddenReplacement("^");
        assertEquals("C^milleDup^nt", f.format("${first.clean}${last.clean}"));
    }

    @Test
    void testCleanFormat()
    {
        UserFormatter f = userFormatterFactory.create(dirtyValues);
        assertEquals("Cmille-Dupnt", f.format("${first.clean}-${last._clean}"));
        f.setForbiddenReplacement("-dash-");
        assertEquals("C-dash-mille-Dup-dash-nt", f.format("${first.clean}-${last._clean}"));
    }

    @Test
    void testFormats()
    {
        UserFormatter f = userFormatterFactory.create(dirtyValues);
        assertEquals("c@mille-dup.nt", f.format("${first.lowerCase}-${last._lowerCase}"));
        assertEquals("C@MILLE-DUP.NT", f.format("${first.upperCase}-${last._upperCase}"));
        assertEquals("cmille-dupnt", f.format("${first.clean.lowerCase}-${last.clean.lowerCase}"));
        assertEquals("CMILLE-DUPNT", f.format("${first.clean.upperCase}-${last._clean._upperCase}"));
    }

    @Test
    void testFormatMissingKeysAreReplaced()
    {
        UserFormatter f = userFormatterFactory.create(cd);
        assertEquals("Camille---Dupont", f.format("${first}-${middle}-${nullkey}-${last}"));
        assertEquals("Camille--Dupont", f.format("${first}-${middle.clean}-${last}"));
    }

    @Test
    void testForbiddenPattern()
    {
        UserFormatter f = userFormatterFactory.create(Pattern.compile("y"), "");
        assertEquals("Camille.Dupont", f.clean("Camyille.Dupont"));
    }

    @Test
    void testForbiddenPatternAndFormat()
    {
        UserFormatter f = userFormatterFactory.create(cd, Pattern.compile("[u.]"), "");
        assertEquals("CamilleDpont", f.format("${first.clean}${last.clean}"));
    }

    @Test
    void testKeyWithDot()
    {
        UserFormatter f = userFormatterFactory.create();
        f.setVariables(Collections.singletonMap("dotted.key", "hello"));
        assertEquals("hello", f.format("${dotted.key}"));
        f.setVariables(Collections.singletonMap("dotted", "hello"));
        assertEquals("", f.format("${dotted.key}"));
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
        UserFormatter f = userFormatterFactory.create();
        Map<String, String> m = Collections.singletonMap("a", "b");
        f.setVariables(m);
        assertEquals(m, f.getVariables());

        Pattern p = Pattern.compile("a");
        f.setForbiddenPattern(p);
        assertEquals(p, f.getForbiddenPattern());

        String c = "^@^";
        f.setForbiddenReplacement(c);
        assertEquals(c, f.getForbiddenReplacement());
    }

    @Test
    void testNullPattern()
    {
        UserFormatter f = userFormatterFactory.create(null, "");
        assertEquals("user^.@name", f.clean("user^.@name"));
    }
}
