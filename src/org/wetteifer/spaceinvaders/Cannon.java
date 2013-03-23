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
public class Cannon extends Matrix {
    
    public static final int WIDTH  = 13;
    public static final int HEIGHT = 8;

    private static final int[][] CANNON =
        {{0,0,0,0,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,1,1,1,1,1,1,1},
         {1,1,1,1,1,1,1,1},
         {0,1,1,1,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,0,0,1,1,1,1,1},
         {0,0,0,0,1,1,1,1}};

    private static final String CACHE_NAME = "cannon";

    private static final int CANNON_RESET_INTERVAL = 800;

    private static final float CANNON_SPEED = 100.0f * MATRIX_SIZE;

    private SpaceInvaders space;
    private boolean freeze;
    private long lastCannonReset;

    public Cannon(float x, float y) {
        super(x, y, CANNON, WIDTH, HEIGHT, MATRIX_SIZE, Color.YELLOW, CACHE_NAME);
    }

    public Cannon(SpaceInvaders space) {
        this(0.0f, 0.0f);
        this.space = space;
        reset();
    }

    public final void reset() {
        freeze = false;
        lastCannonReset = 0;
        setLocation((SCREEN_WIDTH  - getWidth()) / 2.0f,
                     SCREEN_HEIGHT - getHeight());
    }

    public void freeze(boolean value) {
        if (value) {
            lastCannonReset = System.currentTimeMillis();
        }

        freeze = value;
    }

    @Override
    public void update(long delta) {
        if (freeze) {
            if (System.currentTimeMillis() - lastCannonReset >= CANNON_RESET_INTERVAL) {
                space.notifyCannonCanShoot();
                if (space.getLives() > 0) {
                    reset();
                }
            }
        } else {
            dx = 0.0f;

            if (space.isLeftPressed() && !space.isRightPressed()) {
                dx = -CANNON_SPEED;
            } else if (!space.isLeftPressed() && space.isRightPressed()) {
                dx =  CANNON_SPEED;
            }

            if (x <= 0.0f && dx < 0.0f) {
                return;
            }

            if (x >= SCREEN_WIDTH - getWidth() && dx > 0.0f) {
                return;
            }

            super.update(delta);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (!freeze) {
            super.draw(g);
        }
    }

}
