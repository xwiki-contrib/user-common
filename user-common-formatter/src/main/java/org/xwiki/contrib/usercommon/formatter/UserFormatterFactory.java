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

import org.xwiki.component.annotation.Role;

/**
 * User Formatter Factory.
 * @version $Id$
 */
@Role
public interface UserFormatterFactory
{
    /**
     * @return an empty user formatter purely aimed at cleaning, with the default characters to clean.
     */
    UserFormatter create();

    /**
     * @return a user formatter with the given variables and the default characters to clean.
     * @param variables the variables to use
     */
    UserFormatter create(Map<String, String> variables);

    /**
     * @return an empty user formatter purely aimed at cleaning, with the given characters to clean.
     * @param charactersToClean the pattern to use to determine characters to clean
     */
    UserFormatter create(Pattern charactersToClean);

    /**
     * @return a user formatter with the given variables and the given characters to clean.
     * @param variables the variables to use
     * @param charactersToClean the pattern to use to determine characters to clean
     */
    UserFormatter create(Map<String, String> variables, Pattern charactersToClean);
}
