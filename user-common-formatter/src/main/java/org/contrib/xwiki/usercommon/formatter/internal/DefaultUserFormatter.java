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

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.text.StringSubstitutor;
import org.contrib.xwiki.usercommon.formatter.UserFormatter;

/**
 * User formatter.
 * @since 1.0.0
 * @version $Id$
 */
public class DefaultUserFormatter implements UserFormatter
{
    private final Map<String, String> variables;
    private final String cleanReplacement;
    private final Pattern charactersToClean;

    private StringSubstitutor stringSubstitutor;

    /**
     * @param variables the variables to use
     * @param charactersToClean the characters to clean
     * @param cleanReplacement the replacement for the characters to clean
     */
    public DefaultUserFormatter(Map<String, String> variables, Pattern charactersToClean, String cleanReplacement)
    {
        this.variables = variables;
        this.charactersToClean = charactersToClean;
        this.cleanReplacement = cleanReplacement;
    }

    @Override
    public UserFormatter variables(Map<String, String> variables)
    {
        return new DefaultUserFormatter(variables, charactersToClean, cleanReplacement);
    }

    @Override
    public String cleanReplacement()
    {
        return cleanReplacement;
    }

    @Override
    public UserFormatter cleanReplacement(String cleanReplacement)
    {
        return new DefaultUserFormatter(variables, charactersToClean, cleanReplacement);
    }

    @Override
    public UserFormatter charactersToClean(Pattern charactersToClean)
    {
        return new DefaultUserFormatter(variables, charactersToClean, cleanReplacement);
    }

    @Override
    public Map<String, String> variables()
    {
        return variables;
    }

    @Override
    public String clean(String name)
    {
        return charactersToClean.matcher(name).replaceAll(cleanReplacement);
    }

    @Override
    public String format(String template)
    {
        if (stringSubstitutor == null) {
            stringSubstitutor = new StringSubstitutor(s -> getValue(variables, s));
        }
        return stringSubstitutor.replace(template);
    }

    @Override
    public Pattern charactersToClean()
    {
        return charactersToClean;
    }

    private String getValue(Map<String, String> variables, String key)
    {
        if (key == null) {
            return "";
        }

        String r = variables.get(key);
        if (r != null) {
            return r;
        }

        int dotIndex = key.lastIndexOf('.');
        return dotIndex == -1 ? "" : applyOp(variables, key, key.substring(dotIndex), dotIndex);
    }

    private String applyOp(Map<String, String> variables, String key, String op, int dotIndex)
    {
        switch (op) {
            case ".lowerCase":
            case "._lowerCase":
                return getValue(variables, key.substring(0, dotIndex)).toLowerCase();
            case ".upperCase":
            case "._upperCase":
                return getValue(variables, key.substring(0, dotIndex)).toUpperCase();
            case ".clean":
            case "._clean":
                return clean(getValue(variables, key.substring(0, dotIndex)));
            default:
                return "";
        }
    }
}
