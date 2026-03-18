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
package org.contrib.xwiki.usercommon.formatter;

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
     * @return the pattern to match characters to replace
     */
    Pattern charactersToClean();

    /**
     * @param charactersToClean the pattern to match characters to replace
     * @return a copy of this, with the given charactersToClean
     */
    UserFormatter charactersToClean(Pattern charactersToClean);

    /**
     * @return the available values with which to replace the placeholders
     */
    Map<String, String> variables();

    /**
     * @param variables the available values with which to replace the placeholders
     * @return a copy of this, with the given variables
     */
    UserFormatter variables(Map<String, String> variables);

    /**
     * @return what forbidden characters are replaced with
     */
    String cleanReplacement();

    /**
     * @param cleanReplacement what forbidden characters are replaced with
     * @return a copy of this, with the given cleanReplacement
     */
    UserFormatter cleanReplacement(String cleanReplacement);

}
