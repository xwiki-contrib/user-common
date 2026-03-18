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
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import org.contrib.xwiki.usercommon.formatter.UserFormatter;
import org.contrib.xwiki.usercommon.formatter.UserFormatterFactory;
import org.xwiki.component.annotation.Component;

/**
 * Default User Formatter Factory.
 * @version $Id$
 */
@Component
@Singleton
public class DefaultUserFormatterFactory implements UserFormatterFactory
{
    private static final Pattern DEFAULT_CHARACTERS_TO_CLEAN = Pattern.compile("[.:\\s,@^/]");

    @Override
    public UserFormatter create()
    {
        return create(Collections.emptyMap());
    }

    @Override
    public UserFormatter create(Map<String, String> variables)
    {
        return new DefaultUserFormatter(variables, DEFAULT_CHARACTERS_TO_CLEAN, "");
    }

    @Override
    public UserFormatter create(Pattern charactersToClean)
    {
        return new DefaultUserFormatter(Collections.emptyMap(), charactersToClean, "");
    }

    @Override
    public UserFormatter create(Map<String, String> variables, Pattern charactersToClean)
    {
        return new DefaultUserFormatter(variables, charactersToClean, "");
    }
}
