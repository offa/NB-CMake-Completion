/*
 * NB CMake Completion - CMake completion for NetBeans.
 * Copyright (C) 2015-2019  offa
 *
 * This file is part of NB CMake Completion.
 *
 * NB CMake Completion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NB CMake Completion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NB CMake Completion.  If not, see <http://www.gnu.org/licenses/>.
 */

package bv.offa.netbeans.cmake.completion;

import bv.offa.netbeans.cmake.completion.CMakeCompletionItem.ItemType;
import static com.google.common.truth.Truth.assertThat;
import org.junit.jupiter.api.Test;

public class CMakeCompletionItemTest
{
    @Test
    public void formatItem()
    {
        final CMakeCompletionItem funcItem = createItem(ItemType.FUNCTION);
        assertThat(funcItem.formatItemText()).isEqualTo("testName()");

        final CMakeCompletionItem varItem = createItem(ItemType.VARIABLE);
        assertThat(varItem.formatItemText()).isEqualTo("testName");

        final CMakeCompletionItem varExpItem = createItem(ItemType.VARIABLE_EXPANSION);
        assertThat(varExpItem.formatItemText()).isEqualTo("testName{}");

        final CMakeCompletionItem otherItem = createItem(ItemType.OTHER);
        assertThat(otherItem.formatItemText()).isEqualTo("testName");
    }


    private CMakeCompletionItem createItem(ItemType type)
    {
        return new CMakeCompletionItem("testName", type, 0, 1);
    }

}
