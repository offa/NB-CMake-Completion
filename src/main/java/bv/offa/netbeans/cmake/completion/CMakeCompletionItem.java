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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;

public class CMakeCompletionItem implements CompletionItem
{
    private static final ImageIcon fieldIcon = null;
    private static final Color fieldColor = Color.decode("0x0000B2");
    private final String text;
    private final ItemType type;
    private final int dotOffset;
    private final int caretOffset;

    public CMakeCompletionItem(String text, ItemType type, int dotOffset, int caretOffset)
    {
        this.text = text;
        this.type = type;
        this.dotOffset = dotOffset;
        this.caretOffset = caretOffset;
    }
    
    
    
    @Override
    public void defaultAction(JTextComponent component)
    {
        try
        {
            StyledDocument doc = (StyledDocument) component.getDocument();
            doc.remove(dotOffset, caretOffset - dotOffset);
            
            doc.insertString(dotOffset, formatItem(), null);
            
            if( type == ItemType.FUNCTION )
            {
                component.setCaretPosition(component.getCaretPosition()-1);
            }
            
            Completion.get().hideAll();
        }
        catch( BadLocationException ex )
        {
            Exceptions.printStackTrace(ex);
        }
    }

    
    @Override
    public void processKeyEvent(KeyEvent evt)
    {
        /* Empty */
    }

    
    @Override
    public int getPreferredWidth(Graphics g, Font defaultFont)
    {
        return CompletionUtilities.getPreferredWidth(text, null, g, defaultFont);
    }

    
    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor, 
            Color backgroundColor, int width, int height, boolean selected)
    {
        CompletionUtilities.renderHtml(fieldIcon,
                                        text,
                                        null,
                                        g,
                                        defaultFont, 
                                        ( selected == true ? Color.white : fieldColor ),
                                        width,
                                        height,
                                        selected);
    }

    
    @Override
    public CompletionTask createDocumentationTask()
    {
        /* Empty */
        
        return null;
    }

    
    @Override
    public CompletionTask createToolTipTask()
    {
        /* Empty */
        
        return null;
    }

    
    @Override
    public boolean instantSubstitution(JTextComponent component)
    {
        return false;
    }

    
    @Override
    public int getSortPriority()
    {
        return 0;
    }

    
    @Override
    public CharSequence getSortText()
    {
        return text;
    }

    
    @Override
    public CharSequence getInsertPrefix()
    {
        return text;
    }
    
    
    String formatItem()
    {
        switch(type)
        {
            case FUNCTION:
                return text + "()";
            default:
                return text;
        }
    }
    
    
    
    public static enum ItemType
    {
        FUNCTION,
        VARIABLE,
        OTHER
    }
}
