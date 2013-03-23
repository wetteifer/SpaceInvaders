/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wetteifer.spaceinvaders;

import java.awt.Color;
import org.wetteifer.gfw.shape.Matrix;
import static org.wetteifer.spaceinvaders.SpaceInvaders.*;

/**
 *
 * @author wetteifer
 */
public class Laser extends Matrix {
    
    protected static final float LASER_REMOVE_LIMIT = 10.0f * MATRIX_SIZE;
    
    protected SpaceInvaders space;
    
    public Laser(SpaceInvaders space,
                 float x, float y, float speed,
                 int width, int height, int[][] matrix,
                 Color color, String cache) {
        super(x, y, matrix, width, height, MATRIX_SIZE, color, cache);        
        this.space = space;        
        setYSpeed(speed);
    }
    
    public static class Cannon extends Laser {
        
        public static final int WIDTH  = 1;
        public static final int HEIGHT = 4;
        
        private static final int[][] LASER_CANNON = {{1,1,1,1}};        
        
        private static final float LASER_CANNON_SPEED = 250.0f * MATRIX_SIZE;
        
        private static final String CACHE_NAME = "laser_cannon";
        
        public Cannon(SpaceInvaders space, org.wetteifer.spaceinvaders.Cannon cannon) {
            super(space, cannon.getX() + cannon.getWidth() / 2.0f - 1.0f,
                  cannon.getY(), LASER_CANNON_SPEED, WIDTH, HEIGHT, LASER_CANNON,
                  Color.WHITE, CACHE_NAME);
        }
        
        @Override
        public void update(long delta) {
            super.update(-delta);            
            if (y <= -LASER_REMOVE_LIMIT) {
                space.remove(this);
            }
        }
        
    }
    
    public static class Alien extends Laser {
        
        public static final int WIDTH  = 1;
        public static final int HEIGHT = 8;
        
        private static final int[][] LASER_ALIEN = {{1,1,1,1,1,1,1,1}};
        
        private static final float LASER_ALIEN_SPEED = 200.0f * MATRIX_SIZE;
        
        private static final String CACHE_NAME = "laser_alien";
        
        public Alien(SpaceInvaders space, org.wetteifer.spaceinvaders.Alien alien) {
            super(space, alien.getX() + alien.getWidth() / 2.0f - 1.0f,
                  alien.getY() + alien.getHeight(), LASER_ALIEN_SPEED, WIDTH, HEIGHT,
                  LASER_ALIEN, Color.WHITE, CACHE_NAME);
        }
        
        @Override
        public void update(long delta) {
            super.update(delta);
            if (y >= SCREEN_HEIGHT + LASER_REMOVE_LIMIT) {
                space.remove(this);
            }
        }
        
    }
    
}