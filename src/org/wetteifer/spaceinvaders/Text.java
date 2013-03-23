/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wetteifer.spaceinvaders;

import java.awt.Color;
import org.wetteifer.gfw.text.Font;
import org.wetteifer.gfw.text.Notalot35;
import static org.wetteifer.spaceinvaders.SpaceInvaders.*;

/**
 *
 * @author wetteifer
 */
public class Text extends org.wetteifer.gfw.text.Text {
    
    private static final Font TEXT_FONT = new Notalot35();
    
    public Text(String text, int size) {
        super(text, TEXT_FONT, Color.WHITE, size);
    }
    
    public Text(String text) {
        this(text, MATRIX_SIZE);
    }
    
    public void centerOnScreen() {
        setLocation((SCREEN_WIDTH  - getWidth())  / 2.0f,
                    (SCREEN_HEIGHT - getHeight()) / 2.0f);
    }
    
}
