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

import bv.offa.netbeans.cmake.completion.CMakeCompletionItem.ItemType;
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

@MimeRegistrations(
{
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
                
                while( itr.hasNext() == true )
                {
                    final String keyWord = itr.next();
                    
                    if( keyWord.startsWith(filter) == true )
                    {
                        resultSet.addItem(new CMakeCompletionItem(keyWord, ItemType.FUNCTION, startOffset, caretOffset));
                    }
                }
                
                resultSet.finish();
            }
        }, component);
    }

    
    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText)
    {
        return 0;
    }

    
    
    static int getRowFirstNonWhitespace(StyledDocument doc, int offset) throws BadLocationException
    {
        Element element = doc.getParagraphElement(offset);
        int start = element.getStartOffset();
        
        while( start + 1 < element.getEndOffset() )
        {
            try
            {
                if( doc.getText(start, 1).charAt(0) != ' ' )
                {
                    break;
                }
            }
            catch( BadLocationException ex )
            {
                throw (BadLocationException) new BadLocationException("Calling getText(" 
                        + start + ", " + (start + 1 ) + ") on document of length " 
                        + doc.getLength(), start)
                            .initCause(ex);
            }
            
            start++;
        }
        
        return start;
    }
    
    
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
    
    
    // v3.1
    static Set<String> getCommands()
    {
        Set<String> cmds = new HashSet<String>(115);
        
        // Normal commands
        cmds.add("add_compile_options");
        cmds.add("add_custom_command");
        cmds.add("add_custom_target");
        cmds.add("add_definitions");
        cmds.add("add_dependencies");
        cmds.add("add_executable");
        cmds.add("add_library");
        cmds.add("add_subdirectory");
        cmds.add("add_test");
        cmds.add("aux_source_directory");
        cmds.add("break");
        cmds.add("build_command");
        cmds.add("cmake_host_system_information");
        cmds.add("cmake_minimum_required");
        cmds.add("cmake_policy");
        cmds.add("configure_file");
        cmds.add("create_test_sourcelist");
        cmds.add("define_property");
        cmds.add("elseif");
        cmds.add("else");
        cmds.add("enable_language");
        cmds.add("enable_testing");
        cmds.add("endforeach");
        cmds.add("endfunction");
        cmds.add("endif");
        cmds.add("endmacro");
        cmds.add("endwhile");
        cmds.add("execute_process");
        cmds.add("export");
        cmds.add("file");
        cmds.add("find_file");
        cmds.add("find_library");
        cmds.add("find_package");
        cmds.add("find_path");
        cmds.add("find_program");
        cmds.add("fltk_wrap_ui");
        cmds.add("foreach");
        cmds.add("function");
        cmds.add("get_cmake_property");
        cmds.add("get_directory_property");
        cmds.add("get_filename_component");
        cmds.add("get_property");
        cmds.add("get_source_file_property");
        cmds.add("get_target_property");
        cmds.add("get_test_property");
        cmds.add("if");
        cmds.add("include_directories");
        cmds.add("include_external_msproject");
        cmds.add("include_regular_expression");
        cmds.add("include");
        cmds.add("install");
        cmds.add("link_directories");
        cmds.add("list");
        cmds.add("load_cache");
        cmds.add("load_command");
        cmds.add("macro");
        cmds.add("mark_as_advanced");
        cmds.add("math");
        cmds.add("message");
        cmds.add("option");
        cmds.add("project");
        cmds.add("qt_wrap_cpp");
        cmds.add("qt_wrap_ui");
        cmds.add("remove_definitions");
        cmds.add("return");
        cmds.add("separate_arguments");
        cmds.add("set_directory_properties");
        cmds.add("set_property");
        cmds.add("set");
        cmds.add("set_source_files_properties");
        cmds.add("set_target_properties");
        cmds.add("set_tests_properties");
        cmds.add("site_name");
        cmds.add("source_group");
        cmds.add("string");
        cmds.add("target_compile_definitions");
        cmds.add("target_compile_features");
        cmds.add("target_compile_options");
        cmds.add("target_include_directories");
        cmds.add("target_link_libraries");
        cmds.add("target_sources");
        cmds.add("try_compile");
        cmds.add("try_run");
        cmds.add("unset");
        cmds.add("variable_watch");
        cmds.add("while");
        
        // Deprecated
        cmds.add("build_name");
        cmds.add("exec_program");
        cmds.add("export_library_dependencies");
        cmds.add("install_files");
        cmds.add("install_programs");
        cmds.add("install_targets");
        cmds.add("link_libraries");
        cmds.add("make_directory");
        cmds.add("output_required_files");
        cmds.add("remove");
        cmds.add("subdir_depends");
        cmds.add("subdirs");
        cmds.add("use_mangled_mesa");
        cmds.add("utility_source");
        cmds.add("variable_requires");
        cmds.add("write_file");
        
        // CTest commands
        cmds.add("ctest_build");
        cmds.add("ctest_configure");
        cmds.add("ctest_coverage");
        cmds.add("ctest_empty_binary_directory");
        cmds.add("ctest_memcheck");
        cmds.add("ctest_read_custom_files");
        cmds.add("ctest_run_script");
        cmds.add("ctest_sleep");
        cmds.add("ctest_start");
        cmds.add("ctest_submit");
        cmds.add("ctest_test");
        cmds.add("ctest_update");
        cmds.add("ctest_upload");
        
        return cmds;
    }

}
