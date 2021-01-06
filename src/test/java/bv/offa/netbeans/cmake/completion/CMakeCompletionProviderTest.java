/*
 * NB CMake Completion - CMake completion for NetBeans.
 * Copyright (C) 2015-2021  offa
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
import org.junit.jupiter.api.Test;
import org.netbeans.spi.editor.completion.CompletionProvider;

public class CMakeCompletionProviderTest
{
    @Test
    public void createTaskSettings()
    {
        final CMakeCompletionProvider cp = new CMakeCompletionProvider();
        assertThat(cp.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, null)).isNotNull();
        assertThat(cp.createTask(CompletionProvider.DOCUMENTATION_QUERY_TYPE, null)).isNull();
        assertThat(cp.createTask(CompletionProvider.TOOLTIP_QUERY_TYPE, null)).isNull();
        assertThat(cp.createTask(CompletionProvider.COMPLETION_ALL_QUERY_TYPE, null)).isNull();
    }


    @Test
    public void indexOfWhitespace()
    {
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine("a3Ce "))).isEqualTo(4);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine("aBc.,-_ d"))).isEqualTo(7);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine(" A.Qc"))).isEqualTo(0);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine("7-bc"))).isEqualTo(-1);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine("a Br_0E f"))).isEqualTo(7);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine("a B-\t7a"))).isEqualTo(4);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine("h G-\nu&"))).isEqualTo(4);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine("\t"))).isEqualTo(0);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine(" "))).isEqualTo(0);
        assertThat(CMakeCompletionProvider.indexOfWhitespace(asLine(""))).isEqualTo(-1);
    }


    @Test
    public void getRowFirstNonWhitespace() throws BadLocationException
    {
        assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(makeDocument(""), 0)).isEqualTo(0);
        assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(makeDocument("abc"), 0)).isEqualTo(0);
        assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(makeDocument(" a"), 0)).isEqualTo(1);
        assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(makeDocument("\tb"), 0)).isEqualTo(1);
        assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(makeDocument("\n b"), 1)).isEqualTo(2);
        assertThat(CMakeCompletionProvider.getRowFirstNonWhitespace(makeDocument("         h 4"), 0)).isEqualTo(9);
    }


    private StyledDocument makeDocument(String str)
    {
        final JTextComponent comp = new JTextPane();
        comp.setText(str);

        return (StyledDocument) comp.getDocument();
    }

    private char[] asLine(String s)
    {
        return s.toCharArray();
    }


}
