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
import org.contrib.xwiki.usercommon.formatter.UserFormatterFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DefaultUserFormatterTest
{
    private static final UserFormatter bareFormatter = UserFormatterFactory.create();

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

    private static final UserFormatter cdFormatter = UserFormatterFactory.create(dirtyValues);
    private static final UserFormatter dirtyValuesFormatter = bareFormatter.variables(dirtyValues);

    @Test
    public void testClean()
    {
        UserFormatter dashFormatter = bareFormatter.cleanReplacement("-");
        assertEquals("firstname-lastname", dashFormatter.clean("firstname.lastname"));
        assertEquals("firstname--lastname", dashFormatter.clean("firstname. lastname"));
        assertEquals("firstnamelastname", bareFormatter.clean("firstname.@/,:^\t\n\rlastname"));
    }

    @Test
    public void testFormatSimple()
    {
        assertEquals("Camille-Dupont", UserFormatterFactory.create(cd).format("${first}-${last}"));
    }

    @Test
    public void testFormatTemplatesAndValuesAreNotCleaned()
    {
        assertEquals("C@mille-@-Dup.nt", UserFormatterFactory.create(dirtyValues).format("${first}-@-${last}"));
    }

    @Test
    public void testFormatCleanReplacementIsNotCleaned()
    {
        assertEquals("C^milleDup^nt",
            UserFormatterFactory.create(dirtyValues).cleanReplacement("^").format("${first.clean}${last.clean}"));
    }

    @Test
    public void testCleanFormat()
    {

        UserFormatter fancyDashFormatter = cdFormatter.cleanReplacement("-dash-");
        assertEquals("Cmille-Dupnt", cdFormatter.format("${first.clean}-${last._clean}"));
        assertEquals("C-dash-mille-Dup-dash-nt", fancyDashFormatter.format("${first.clean}-${last._clean}"));
    }

    @Test
    public void testFormats()
    {
        assertEquals("c@mille-dup.nt", dirtyValuesFormatter.format("${first.lowerCase}-${last._lowerCase}"));
        assertEquals("C@MILLE-DUP.NT", dirtyValuesFormatter.format("${first.upperCase}-${last._upperCase}"));
        assertEquals("cmille-dupnt", dirtyValuesFormatter.format("${first.clean.lowerCase}-${last.clean.lowerCase}"));
        assertEquals("CMILLE-DUPNT", dirtyValuesFormatter.format("${first.clean.upperCase}-${last._clean._upperCase}"));
    }

    @Test
    public void testFormatMissingKeysAreReplaced()
    {
        UserFormatter userFormatter = bareFormatter.variables(cd);
        assertEquals("Camille---Dupont", userFormatter.format("${first}-${middle}-${nullkey}-${last}"));
        assertEquals("Camille--Dupont", userFormatter.format("${first}-${middle.clean}-${last}"));
    }

    @Test
    public void testCharactersToClean()
    {
        UserFormatter f = UserFormatterFactory.create(Pattern.compile("y"));
        assertEquals("Camille.Dupont", f.clean("Camyille.Dupont"));
    }

    @Test
    public void testCharactersToCleanAndFormat()
    {
        UserFormatter f = UserFormatterFactory.create(cd, Pattern.compile("[u.]"));
        assertEquals("CamilleDpont", f.format("${first.clean}${last.clean}"));
    }

    @Test
    public void testKeyWithDot()
    {
        assertEquals(
            "hello",
            bareFormatter.variables(Collections.singletonMap("dotted.key", "hello")).format("${dotted.key}"));
        assertEquals(
            "",
            bareFormatter.variables(Collections.singletonMap("dotted", "hello")).format("${dotted.key}"));
    }

    @Test
    public void testEmptyValues()
    {
        UserFormatter f = bareFormatter.variables(Collections.singletonMap("k", ""));
        assertEquals("", f.format("${}"));
        assertEquals("", f.format("${k}"));
        assertEquals("", f.format("${k.clean.lowerCase}"));
    }

    @Test
    public void testSetGet()
    {
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