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
package org.xwiki.contrib.usercommon.formatter;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Format and Clean user and group names.
 * @version $Id$
 * @since 1.0.0
 */
public interface UserFormatter
{
    /**
     * @param name the user or group name to clean
     * @return the user or group name where all forbidden characters are replaced with cleanReplacement
     */
    String clean(String name);

    /**
     * Replace placeholders in a template with the provided values.
     * Placeholders are of the shape:
     * - ${key} - replaced with the value of the key
     * - ${key.lowerCase} - replaced with the value of the key, lowercased
     * - ${key._lowerCase} - replaced with the value of the key, lowercased
     * - ${key.upperCase} - replaced with the value of the key, uppercased
     * - ${key._upperCase} - replaced with the value of the key, uppercased
     * - ${key._clean}  - replaced with the value of the key, cleaned
     * - ${key.clean.lowerCase} - replaced with the value of the key, cleaned and then lowercased
     * - ${key._clean._lowerCase - replaced with the value of the key, cleaned and then lowercased
     * - ${key.clean.upperCase} - replaced with the value of the key, cleaned and then uppercased
     * - ${key._clean._upperCase} - replaced with the value of the key, cleaned and then uppercased
     * where "cleaned" means that spaces and the following characters are replaced with cleanReplacement: .:,@,^/
     * @param template the string containing placeholders to replace
     * @return the template string in which placeholders are replaced with values from the variables map
     */
    String format(String template);

    /**
     * @param variables the available values with which to replace the placeholders
     */
    void setVariables(Map<String, String> variables);

    /**
     * @return the available values with which to replace the placeholders
     */
    Map<String, String> getVariables();

    /**
     * @param forbiddenPattern the pattern to match characters to replace
     */
    void setForbiddenPattern(Pattern forbiddenPattern);

    /**
     * @return the pattern to match characters to replace
     */
    Pattern getForbiddenPattern();

    /**
     * @param cleanReplacement what forbidden characters are replaced with
     */
    void setForbiddenReplacement(String cleanReplacement);

    /**
     * @return what forbidden characters are replaced with
     */
    String getForbiddenReplacement();
}
