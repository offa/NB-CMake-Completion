/*
 * NB CMake Completion - CMake completion for NetBeans.
 * Copyright (C) 2015  offa
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

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.spi.editor.completion.CompletionProvider;

public class CMakeCompletionProviderTest
{
    @Test
    public void testCreateTask()
    {
        CMakeCompletionProvider cp = new CMakeCompletionProvider();
        final JTextComponent tc = null;
        
        assertNotNull(cp.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, tc));
        assertNull(cp.createTask(CompletionProvider.DOCUMENTATION_QUERY_TYPE, tc));
        assertNull(cp.createTask(CompletionProvider.TOOLTIP_QUERY_TYPE, tc));
        assertNull(cp.createTask(CompletionProvider.COMPLETION_ALL_QUERY_TYPE, tc));
    }
    

    @Test
    public void testIndexOfWhitespace()
    {
        char lines[] = "a3Ce ".toCharArray();
        assertEquals(4, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = "aBc.,-_ d".toCharArray();
        assertEquals(7, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = " A.Qc".toCharArray();
        assertEquals(0, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = "7-bc".toCharArray();
        assertEquals(-1, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = "a Br_0E f".toCharArray();
        assertEquals(7, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = "a B-\t7a".toCharArray();
        assertEquals(4, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = "h G-\nu&".toCharArray();
        assertEquals(4, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = "\t".toCharArray();
        assertEquals(0, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = " ".toCharArray();
        assertEquals(0, CMakeCompletionProvider.indexOfWhitespace(lines));
        lines = "".toCharArray();
        assertEquals(-1, CMakeCompletionProvider.indexOfWhitespace(lines));
    }
    
    
    @Test
    public void testGetRowFirstNonWhitespace()
    {
        try
        {
            StyledDocument doc = makeDocument("");
            assertEquals(0, CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0));
            doc = makeDocument("abc");
            assertEquals(0, CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0));
            doc = makeDocument(" a");
            assertEquals(1, CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0));
            doc = makeDocument("\tb");
            assertEquals(1, CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0));
            doc = makeDocument("\n b");
            assertEquals(2, CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 1));
            doc = makeDocument("         h 4");
            assertEquals(9, CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0));
        }
        catch( BadLocationException ex )
        {
            fail(ex.getMessage());
        }
    }
    
    
    private StyledDocument makeDocument(String str)
    {
        final JTextComponent comp = new JTextPane();
        comp.setText(str);
        
        return (StyledDocument) comp.getDocument();
    }
}
