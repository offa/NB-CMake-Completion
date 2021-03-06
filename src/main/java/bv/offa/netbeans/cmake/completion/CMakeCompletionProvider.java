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

import bv.offa.netbeans.cmake.completion.CMakeCompletionItem.ItemType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;

/**
 * The class {@code CMakeCompletionProvider} implements a
 * {@link CompletionProvider completion provider} for CMake files.
 *
 * @author  offa
 */
@MimeRegistrations({
    @MimeRegistration(mimeType = "text/x-cmake", service = CompletionProvider.class),
    @MimeRegistration(mimeType = "text/x-cmake-include", service = CompletionProvider.class)
    })
public class CMakeCompletionProvider implements CompletionProvider
{
    private final Set<String> keyWords;

    public CMakeCompletionProvider()
    {
        this.keyWords = getCommands();
    }


    /**
     * Creates a completion task for query.
     *
     * @param queryType     Query type
     * @param component     Text component
     * @return              Completion task or {@code null} if the type is not
     *                      supported (unsupported is everything except
     *                      {@link CompletionProvider#COMPLETION_QUERY_TYPE
     *                      COMPLETION_QUERY_TYPE})
     * @see                 CompletionProvider#createTask(int, JTextComponent)
     */
    @Override
    public CompletionTask createTask(int queryType, JTextComponent component)
    {
        if( queryType != CompletionProvider.COMPLETION_QUERY_TYPE )
        {
            return null;
        }

        return new AsyncCompletionTask(new AsyncCompletionQuery()
        {
            @Override
            protected void query(CompletionResultSet resultSet, Document doc, int caretOffset)
            {
                String filter = null;
                int startOffset = caretOffset - 1;

                try
                {
                    final StyledDocument styledDoc = (StyledDocument) doc;
                    final int lineStartOffset = getRowFirstNonWhitespace(styledDoc, caretOffset);
                    final char line[] = styledDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    final int whiteSpaceOffset = indexOfWhitespace(line);

                    filter = String.valueOf(line, whiteSpaceOffset + 1, line.length - whiteSpaceOffset - 1);

                    if( whiteSpaceOffset > 0 )
                    {
                        startOffset = lineStartOffset + whiteSpaceOffset + 1;
                    }
                    else
                    {
                        startOffset = lineStartOffset;
                    }
                }
                catch( BadLocationException ex )
                {
                    Exceptions.printStackTrace(ex);
                }

                final Iterator<String> itr = keyWords.iterator();
                int num = 0;

                while( itr.hasNext() == true )
                {
                    final String keyWord = itr.next();

                    if( keyWord.startsWith(filter) == true )
                    {
                        resultSet.addItem(new CMakeCompletionItem(keyWord, ItemType.FUNCTION, startOffset, caretOffset));
                        num++;
                    }
                }

                if( filter != null && (num == keyWords.size() || num == 0) )
                {
                    int n = getVariableExpansionOffset(filter);
                    resultSet.addItem(new CMakeCompletionItem("$", ItemType.VARIABLE_EXPANSION, caretOffset - n, caretOffset));
                }

                resultSet.finish();
            }
        }, component);
    }


    /**
     * Returns whether the completion window should popup automatically if text
     * is typed in the component.
     *
     * @param component     Component
     * @param typedText     Typed text
     * @return              Any combination of completion query types or
     *                      {@code 0} if no no automatically query should be
     *                      executed
     * @see                 CompletionProvider#getAutoQueryTypes(JTextComponent, String)
     */
    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText)
    {
        return 0;
    }


    /**
     * Returns the position of non-whitespace character within the paragraph of
     * the given offset.
     *
     * @param doc       Document
     * @param offset    Offset
     * @return          Position
     * @throws          BadLocationException If an illegal location occurs
     */
    static int getRowFirstNonWhitespace(StyledDocument doc, int offset) throws BadLocationException
    {
        Element element = doc.getParagraphElement(offset);
        int start = element.getStartOffset();

        while( start + 1 < element.getEndOffset() )
        {
            try
            {
                if( Character.isWhitespace(doc.getText(start, 1).charAt(0)) == false )
                {
                    break;
                }
            }
            catch( BadLocationException ex )
            {
                throw (BadLocationException) new BadLocationException("Calling getText("
                        + start + ", " + (start + 1) + ") on document of length "
                        + doc.getLength(), start)
                        .initCause(ex);
            }

            start++;
        }

        return start;
    }


    /**
     * Returns the offset - the number of characters matching.
     *
     * @param str       Input string
     * @return          Number of matching characters
     */
    private int getVariableExpansionOffset(String str)
    {
        if( str.endsWith("${") )
        {
            return 2;
        }
        else if( str.endsWith("$") )
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }


    /**
     * Returns the index of of last trailing whitespace character.
     *
     * @param line      Line as character array
     * @return          Index or {@code -1} if no whitespace character is found
     */
    static int indexOfWhitespace(char line[])
    {
        int i = line.length;

        while( --i > -1 )
        {
            final char c = line[i];

            if( Character.isWhitespace(c) == true )
            {
                return i;
            }
        }

        return -1;
    }


    /**
     * Returns a set of all CMake commands.
     *
     * @return      Set of commands
     */
    static Set<String> getCommands()
    {
        final String functions[] = new String[]
        {
            // Scripting Commands
            "break",
            "cmake_host_system_information",
            "cmake_minimum_required",
            "cmake_parse_arguments",
            "cmake_policy",
            "configure_file",
            "continue",
            "elseif",
            "else",
            "endforeach",
            "endfunction",
            "endif",
            "endmacro",
            "endwhile",
            "execute_process",
            "file",
            "find_file",
            "find_library",
            "find_package",
            "find_path",
            "find_program",
            "foreach",
            "function",
            "get_cmake_property",
            "get_directory_property",
            "get_filename_component",
            "get_property",
            "if",
            "include",
            "include_guard",
            "list",
            "macro",
            "mark_as_advanced",
            "math",
            "message",
            "option",
            "return",
            "separate_arguments",
            "set_directory_properties",
            "set_property",
            "set",
            "site_name",
            "string",
            "unset",
            "variable_watch",
            "while",
            // Project Commands
            "add_compile_options",
            "add_custom_command",
            "add_custom_target",
            "add_definitions",
            "add_dependencies",
            "add_executable",
            "add_library",
            "add_subdirectory",
            "add_test",
            "aux_source_directory",
            "build_command",
            "create_test_sourcelist",
            "define_property",
            "enable_language",
            "enable_testing",
            "export",
            "fltk_wrap_ui",
            "get_source_file_property",
            "get_target_property",
            "get_test_property",
            "include_directories",
            "include_external_msproject",
            "include_regular_expression",
            "install",
            "link_directories",
            "link_libraries",
            "load_cache",
            "project",
            "qt_wrap_cpp",
            "qt_wrap_ui",
            "remove_definitions",
            "set_source_files_properties",
            "set_target_properties",
            "set_tests_properties",
            "source_group",
            "target_compile_definitions",
            "target_compile_features",
            "target_compile_options",
            "target_include_directories",
            "target_link_libraries",
            "target_sources",
            "try_compile",
            "try_run",
            // CTest Commands
            "ctest_build",
            "ctest_configure",
            "ctest_coverage",
            "ctest_empty_binary_directory",
            "ctest_memcheck",
            "ctest_read_custom_files",
            "ctest_run_script",
            "ctest_sleep",
            "ctest_start",
            "ctest_submit",
            "ctest_test",
            "ctest_update",
            "ctest_upload",
            // Deprecated Commands
            "build_name",
            "exec_program",
            "export_library_dependencies",
            "install_files",
            "install_programs",
            "install_targets",
            "load_command",
            "make_directory",
            "output_required_files",
            "remove",
            "subdir_depends",
            "subdirs",
            "use_mangled_mesa",
            "utility_source",
            "variable_requires",
            "write_file"
        };

        Set<String> cmds = new HashSet<>(functions.length);
        Collections.addAll(cmds, functions);

        return cmds;
    }


}
