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

/**
 * The class {@code CMakeCompletionItem} represents a
 * {@link CompletionItem Completion item}.
 * 
 * @author  offa
 */
public class CMakeCompletionItem implements CompletionItem
{
    private static final ImageIcon FIELD_ICON = null;
    private static final Color FIELD_COLOR = Color.decode("0x0000B2");
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
    
    
    /**
     * Action for this item.
     * 
     * @param component         Component for which the completion was invoced
     * @see                     CompletionItem#defaultAction(JTextComponent) 
     */
    @Override
    public void defaultAction(JTextComponent component)
    {
        try
        {
            StyledDocument doc = (StyledDocument) component.getDocument();
            doc.remove(dotOffset, caretOffset - dotOffset);
            doc.insertString(dotOffset, formatItemText(), null);
            
            switch(type)
            {
                case FUNCTION:
                case VARIABLE_EXPANSION:
                    component.setCaretPosition(component.getCaretPosition()-1);
                    break;
                default:
                    break;
            }
            
            Completion.get().hideAll();
        }
        catch( BadLocationException ex )
        {
            Exceptions.printStackTrace(ex);
        }
    }

    
    /**
     * Process the key event.
     * 
     * @param evt       Key vent
     * @see             CompletionItem#processKeyEvent(KeyEvent) 
     */
    @Override
    public void processKeyEvent(KeyEvent evt)
    {
        /* Empty */
    }

    
    /**
     * Get preferred visual width.
     * 
     * @param g             Graphics
     * @param defaultFont   Default font
     * @return              Rendering width ({@code >= 0})
     * @see                 CompletionItem#getPreferredWidth(Graphics, Font) 
     */
    @Override
    public int getPreferredWidth(Graphics g, Font defaultFont)
    {
        return CompletionUtilities.getPreferredWidth(formatItemText(), null, g, defaultFont);
    }

    
    /**
     * Renders the item to the graphics.
     * 
     * @param g                 Graphics
     * @param defaultFont       Default font
     * @param defaultColor      Default color
     * @param backgroundColor   Background color
     * @param width             Width
     * @param height            Height
     * @param selected          Selected
     * @see CompletionItem#render(Graphics, Font, Color, Color, int, int, boolean) 
     */
    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor, 
            Color backgroundColor, int width, int height, boolean selected)
    {
        CompletionUtilities.renderHtml(FIELD_ICON,
                                        formatItemText(),
                                        null,
                                        g,
                                        defaultFont, 
                                        ( selected == true ? Color.white : FIELD_COLOR ),
                                        width,
                                        height,
                                        selected);
    }

    
    /**
     * Creates a documentation task.
     * 
     * @return      Documentation task
     * @see         CompletionItem#createDocumentationTask() 
     */
    @Override
    public CompletionTask createDocumentationTask()
    {
        return null;
    }

    
    /**
     * Creates a tooltip task.
     * 
     * @return      Completion task
     * @see         CompletionItem#createToolTipTask() 
     */
    @Override
    public CompletionTask createToolTipTask()
    {
        return null;
    }

    
    /**
     * Returns whether there should be an instanstant subsititution for the
     * component.
     * 
     * @param component     Component for which the completion was invoced
     * @return              Returns if there should be an instant substitution
     * @see                 CompletionItem#instantSubstitution(JTextComponent) 
     */
    @Override
    public boolean instantSubstitution(JTextComponent component)
    {
        return false;
    }

    
    /**
     * Returns the item's priority.
     * 
     * @return      Sortpriority
     * @see         CompletionItem#getSortPriority() 
     */
    @Override
    public int getSortPriority()
    {
        return 0;
    }

    
    /**
     * Returns a text to sort the item.
     * 
     * @return      Returns the text
     * @see         CompletionItem#getSortText() 
     */
    @Override
    public CharSequence getSortText()
    {
        return text;
    }

    
    /**
     * Returns a text used for finding of a longest common prefix.
     * 
     * @return      Returns the text
     * @see         CompletionItem#getInsertPrefix() 
     */
    @Override
    public CharSequence getInsertPrefix()
    {
        return text;
    }
    
    
    /**
     * Formats the {@code text} of the item, depending on it's type.
     * 
     * @return      Formated {@code text}
     */
    String formatItemText()
    {
        switch(type)
        {
            case FUNCTION:
                return text + "()";
            case VARIABLE_EXPANSION:
                return text + "{}";
            default:
                return text;
        }
    }
    
    
    
    /**
     * Enumeration of item types.
     */
    public static enum ItemType
    {
        /** Function or macro. */
        FUNCTION,
        /** Variable. */
        VARIABLE,
        /** Variable expansion. */
        VARIABLE_EXPANSION,
        /** Other. */
        OTHER
    }
}
