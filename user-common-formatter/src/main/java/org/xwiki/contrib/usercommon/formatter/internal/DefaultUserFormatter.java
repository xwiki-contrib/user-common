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

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.text.StringSubstitutor;
import org.xwiki.contrib.usercommon.formatter.UserFormatter;

/**
 * User formatter.
 * @since 1.0.0
 * @version $Id$
 */
public class DefaultUserFormatter implements UserFormatter
{
    private Map<String, String> variables;
    private String forbiddenReplacement;
    private Pattern forbiddenPattern;

    private StringSubstitutor stringSubstitutor;

    /**
     * @param variables the variables to use
     * @param forbiddenPattern the characters to clean
     * @param forbiddenReplacement the replacement for the characters to clean
     */
    public DefaultUserFormatter(Map<String, String> variables, Pattern forbiddenPattern, String forbiddenReplacement)
    {
        this.variables = variables;
        this.forbiddenPattern = forbiddenPattern;
        this.forbiddenReplacement = forbiddenReplacement;
    }

    @Override
    public void setVariables(Map<String, String> variables)
    {
        this.stringSubstitutor = null;
        this.variables = variables;
    }

    @Override
    public Map<String, String> getVariables()
    {
        return variables;
    }

    @Override
    public void setForbiddenReplacement(String forbiddenReplacement)
    {
        this.stringSubstitutor = null;
        this.forbiddenReplacement = forbiddenReplacement;
    }

    @Override
    public String getForbiddenReplacement()
    {
        return forbiddenReplacement;
    }

    @Override
    public void setForbiddenPattern(Pattern forbiddenPattern)
    {
        this.stringSubstitutor = null;
        this.forbiddenPattern = forbiddenPattern;
    }

    @Override
    public Pattern getForbiddenPattern()
    {
        return forbiddenPattern;
    }

    @Override
    public String clean(String name)
    {
        return forbiddenPattern == null ? name : forbiddenPattern.matcher(name).replaceAll(forbiddenReplacement);
    }

    @Override
    public String format(String template)
    {
        if (stringSubstitutor == null) {
            stringSubstitutor = new StringSubstitutor(s -> getValue(variables, s));
        }
        return stringSubstitutor.replace(template);
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
