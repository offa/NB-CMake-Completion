/*
 * NB CMake Completion - CMake completion for NetBeans.
 * Copyright (C) 2015-2016  offa
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
import static org.junit.Assert.*;
import org.junit.Test;

public class CMakeCompletionItemTest
{
    @Test
    public void formatItem()
    {
        CMakeCompletionItem funcItem = createItem(ItemType.FUNCTION);
        assertEquals("testName()", funcItem.formatItemText());
        
        CMakeCompletionItem varItem = createItem(ItemType.VARIABLE);
        assertEquals("testName", varItem.formatItemText());
        
        CMakeCompletionItem varExpItem = createItem(ItemType.VARIABLE_EXPANSION);
        assertEquals("testName{}", varExpItem.formatItemText());
        
        CMakeCompletionItem otherItem = createItem(ItemType.OTHER);
        assertEquals("testName", otherItem.formatItemText());
    }
    
    
    private CMakeCompletionItem createItem(ItemType type)
    {
        return new CMakeCompletionItem("testName", type, 0, 1);
    }
    
}
