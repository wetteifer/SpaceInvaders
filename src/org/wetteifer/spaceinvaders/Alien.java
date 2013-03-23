/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wetteifer.spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;
import org.wetteifer.gfw.shape.Entity;
import org.wetteifer.gfw.shape.Matrix;

/**
 *
 * @author wetteifer
 */
public class Alien extends Entity {

    private static final int MAX_SPRITES = 2;

    private Matrix[] sprites;
    private int width, height;

    private static int currentIndex;
    private static int alienChangeInterval;
    private static long lastAlienChange;

    public Alien(float x, float y,
                 int width, int height,
                 int[][][] aliens,
                 String[] caches) {
        super(x, y);

        this.width = width;
        this.height = height;

        sprites = new Matrix[MAX_SPRITES];

        for (int i = 0; i < MAX_SPRITES; i++) {
            sprites[i] = new Matrix(x, y, aliens[i], width, height,
                                    SpaceInvaders.MATRIX_SIZE,
                                    Color.WHITE, caches[i]);
        }
    }
    
    public static void reset() {
        currentIndex = 0;
        alienChangeInterval = 500;
        lastAlienChange = 0;
    }
    
    /* Este método deberia ser estático */
    public void speedUp() {
        alienChangeInterval -= 7;
    }
    
    @Override
    public void setXSpeed(float dx) {
        super.setXSpeed(dx);
        for (int i = 0; i < MAX_SPRITES; i++) {
            sprites[i].setXSpeed(dx);
        }
    }
    
    @Override
    public void setYSpeed(float dy) {
        super.setYSpeed(dy);        
        for (int i = 0; i < MAX_SPRITES; i++) {
            sprites[i].setYSpeed(dy);
        }
    }

    @Override
    public int getWidth() {
        return SpaceInvaders.MATRIX_SIZE * width;
    }

    @Override
    public int getHeight() {
        return SpaceInvaders.MATRIX_SIZE * height;
    }

    @Override
    public void update(long delta) {
        super.update(delta);

        for (int i = 0; i < MAX_SPRITES; i++) {
            sprites[i].update(delta);
        }

        if (System.currentTimeMillis() - lastAlienChange >= alienChangeInterval) {
            lastAlienChange = System.currentTimeMillis();

            // FIXME: Y si hay mas de dos sprites? Alguna cosita mejor plis.
            currentIndex = (currentIndex == 1) ? 0 : 1;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        sprites[currentIndex].draw(g);
    }

    public static class A extends Alien {
        
        public static final int WIDTH  = 8;
        public static final int HEIGHT = 8;

        private static final int[][][] ALIEN_A =
           {{{0,0,0,1,1,0,1,0},
             {0,0,1,1,1,1,0,1},
             {0,1,1,0,1,0,0,0},
             {1,1,1,1,1,1,0,0},
             {1,1,1,1,1,1,0,0},
             {0,1,1,0,1,0,0,0},
             {0,0,1,1,1,1,0,1},
             {0,0,0,1,1,0,1,0}},

            {{0,0,0,1,1,0,0,1},
             {0,0,1,1,1,0,1,0},
             {0,1,1,0,1,1,0,1},
             {1,1,1,1,1,0,1,0},
             {1,1,1,1,1,0,1,0},
             {0,1,1,0,1,1,0,1},
             {0,0,1,1,1,0,1,0},
             {0,0,0,1,1,0,0,1}}};

        private static final String[] CACHE_NAMES = {
            "alien_a1", "alien_a2"
        };

        public A(float x, float y) {
            super(x, y, WIDTH, HEIGHT, ALIEN_A, CACHE_NAMES);
        }

    }

    public static class B extends Alien {
        
        public static final int WIDTH  = 11;
        public static final int HEIGHT = 8;

        private static final int[][][] ALIEN_B =
           {{{0,0,0,0,1,1,1,0},
             {0,0,0,1,1,0,0,0},
             {1,0,1,1,1,1,1,0},
             {0,1,1,0,1,1,0,1},
             {0,0,1,1,1,1,0,1},
             {0,0,1,1,1,1,0,0},
             {0,0,1,1,1,1,0,1},
             {0,1,1,0,1,1,0,1},
             {1,0,1,1,1,1,1,0},
             {0,0,0,1,1,0,0,0},
             {0,0,0,0,1,1,1,0}},

            {{0,1,1,1,1,0,0,0},
             {0,0,0,1,1,1,0,1},
             {1,0,1,1,1,1,1,0},
             {0,1,1,0,1,1,0,0},
             {0,0,1,1,1,1,0,0},
             {0,0,1,1,1,1,0,0},
             {0,0,1,1,1,1,0,0},
             {0,1,1,0,1,1,0,0},
             {1,0,1,1,1,1,1,0},
             {0,0,0,1,1,1,0,1},
             {0,1,1,1,1,0,0,0}}};

        private static final String[] CACHE_NAMES = {
            "alien_b1", "alien_b2"
        };

        public B(float x, float y) {
            super(x, y, WIDTH, HEIGHT, ALIEN_B, CACHE_NAMES);
        }

    }

    public static class C extends Alien {
        
        public static final int WIDTH  = 12;
        public static final int HEIGHT = 8;

        private static final int[][][] ALIEN_C =
           {{{0,0,1,1,1,0,0,0},
             {0,1,1,1,1,0,1,0},
             {0,1,1,1,1,1,1,1},
             {0,1,1,0,1,1,0,1},
             {1,1,1,0,1,1,0,0},
             {1,1,1,1,1,0,1,0},
             {1,1,1,1,1,0,1,0},
             {1,1,1,0,1,1,0,0},
             {0,1,1,0,1,1,0,1},
             {0,1,1,1,1,1,1,1},
             {0,1,1,1,1,0,1,0},
             {0,0,1,1,1,0,0,0}},

            {{0,0,1,1,1,0,0,1},
             {0,1,1,1,1,0,0,1},
             {0,1,1,1,1,0,1,0},
             {0,1,1,0,1,1,1,0},
             {1,1,1,0,1,1,0,0},
             {1,1,1,1,1,0,1,0},
             {1,1,1,1,1,0,1,0},
             {1,1,1,0,1,1,0,0},
             {0,1,1,0,1,1,1,0},
             {0,1,1,1,1,0,1,0},
             {0,1,1,1,1,0,0,1},
             {0,0,1,1,1,0,0,1}}};

        private static final String[] CACHE_NAMES = {
            "alien_c1", "alien_c2"
        };

        public C(float x, float y) {
            super(x, y, WIDTH, HEIGHT, ALIEN_C, CACHE_NAMES);
        }

    }

}