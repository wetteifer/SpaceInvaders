/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wetteifer.spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import org.wetteifer.gfw.shape.Matrix;
import static org.wetteifer.spaceinvaders.SpaceInvaders.*;

/**
 *
 * @author wetteifer
 */
public class Bunker extends Matrix {
    
    public static final int WIDTH  = 20;    
    public static final int HEIGHT = 15;
    
    private static final int[][] BUNKER =
        {{0,0,0,0,1,1,1,1,1,1,1,1,1,1,1},
         {0,0,0,1,1,1,1,1,1,1,1,1,1,1,1},
         {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1},
         {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
         {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
         {1,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,0,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,1,0,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
         {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
         {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
         {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1},
         {0,0,0,1,1,1,1,1,1,1,1,1,1,1,1},
         {0,0,0,0,1,1,1,1,1,1,1,1,1,1,1}};
    
    /*
     * Número de ataques que puede recibir el bunker.
     */
    private static final int DAMAGE_COUNT = 246;
    
    /*
     * Número de ataques al hacer colisión con un laser.
     */
    private static final int DAMAGE_HITS = 30;
    
    private int[][] sprite;
    private int count;
    private boolean destroyed;
    
    public Bunker() {
        sprite = new int[WIDTH][HEIGHT];
        
        setColor(Color.YELLOW);        
        setMatrix(sprite, WIDTH, HEIGHT);
        setSize(MATRIX_SIZE);
        
        reset();
    }
    
    @Override
    public void update(long delta) {
        if (!destroyed) {
            super.update(delta);
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        if (!destroyed) {
            super.draw(g);
        }
    }
    
    public final void reset() {
        count = DAMAGE_COUNT;
        destroyed = false;
        
        for (int i = 0; i < WIDTH; i++) {
            System.arraycopy(BUNKER[i], 0, sprite[i], 0, HEIGHT);
        }
        
        createImage();
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public void damage() {
        if (!destroyed) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int xb, yb;

                    for (int i = 0; i < DAMAGE_HITS; i++) {
                        if (count == 0) {
                            destroyed = true;
                            break;
                        }

                        do {
                            xb = (int) (Math.random() * WIDTH);
                            yb = (int) (Math.random() * HEIGHT);
                        } while (sprite[xb][yb] == 0);

                        count--;
                        sprite[xb][yb] = 0;
                    }

                    createImage();
                }
            }).start();
        }
    }
    
}
