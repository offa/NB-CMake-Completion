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

import static com.google.common.truth.Truth.assertThat;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.netbeans.spi.editor.completion.CompletionProvider;

public class CMakeCompletionProviderTest
{
    @Test
    public void createTaskSettings()
    {
        CMakeCompletionProvider cp = new CMakeCompletionProvider();
        final JTextComponent tc = null;

        assertThat(cp.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, tc)).isNotNull();
        assertThat(cp.createTask(CompletionProvider.DOCUMENTATION_QUERY_TYPE, tc)).isNull();
        assertThat(cp.createTask(CompletionProvider.TOOLTIP_QUERY_TYPE, tc)).isNull();
        assertThat(cp.createTask(CompletionProvider.COMPLETION_ALL_QUERY_TYPE, tc)).isNull();
    }


    @Test
    public void indexOfWhitespace()
    {
        char lines[] = "a3Ce ".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(4);
        lines = "aBc.,-_ d".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(7);
        lines = " A.Qc".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(0);
        lines = "7-bc".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(-1);
        lines = "a Br_0E f".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(7);
        lines = "a B-\t7a".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(4);
        lines = "h G-\nu&".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(4);
        lines = "\t".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(0);
        lines = " ".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(0);
        lines = "".toCharArray();
        assertThat(CMakeCompletionProvider.indexOfWhitespace(lines)).isEqualTo(-1);
    }


    @Test
    public void getRowFirstNonWhitespace()
    {
        try
        {
            StyledDocument doc = makeDocument("");
            assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0)).isEqualTo(0);
            doc = makeDocument("abc");
            assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0)).isEqualTo(0);
            doc = makeDocument(" a");
            assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0)).isEqualTo(1);
            doc = makeDocument("\tb");
            assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0)).isEqualTo(1);
            doc = makeDocument("\n b");
            assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 1)).isEqualTo(2);
            doc = makeDocument("         h 4");
            assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(doc, 0)).isEqualTo(9);
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
