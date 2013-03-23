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
public class Explosion extends Matrix {
    
    private SpaceInvaders space;
    private long interval, initTime;
    
    public Explosion(SpaceInvaders space, int[][] matrix,
                     int width, int height, long interval, Color color, String cache) {
        super(matrix, width, height, MATRIX_SIZE, color, cache);
        this.space = space;
        this.interval = interval;
        this.initTime = System.currentTimeMillis();
    }
    
    @Override
    public void update(long delta) {
        if (System.currentTimeMillis() - initTime >= interval) {
            space.remove(this);
        }
    }
    
    public static class Cannon extends Explosion {
        
        public static final int WIDTH  = 13;
        public static final int HEIGHT = 7;
        
        private static final int[][] EXPLOSION_CANNON =
            {{0,0,0,0,0,1,1},
             {0,0,0,0,1,0,0},
             {1,0,0,1,0,0,1},
             {0,1,0,1,1,1,1},
             {0,0,0,0,0,0,0},
             {0,1,0,1,1,0,0},
             {0,0,0,1,1,1,1},
             {0,1,1,0,0,1,1},
             {1,0,0,0,1,1,1},
             {0,0,0,0,0,1,1},
             {0,0,1,0,0,1,1},
             {0,0,0,0,0,0,1},
             {0,0,0,0,1,0,0}};
        
        private static final String CACHE_NAME = "explosion_cannon";
        
        private static final int CANNON_EXPLOSION_INTERVAL = 800;
        
        public Cannon(SpaceInvaders space, org.wetteifer.spaceinvaders.Cannon cannon) {
            super(space, EXPLOSION_CANNON, WIDTH, HEIGHT, CANNON_EXPLOSION_INTERVAL, Color.YELLOW, CACHE_NAME);
            setLocation(cannon.getX(), cannon.getY() + MATRIX_SIZE);
        }
        
    }
    
    public static class Alien extends Explosion {
        
        public static final int WIDTH  = 13;
        public static final int HEIGHT = 7;
        
        private static final int[][] EXPLOSION_ALIEN =
            {{0,0,0,1,0,0,0},
             {1,0,0,1,0,0,1},
             {0,1,0,0,0,1,0},
             {0,0,1,0,1,0,0},
             {1,0,0,0,0,0,1},
             {0,1,0,0,0,1,0},
             {0,0,0,0,0,0,0},
             {0,1,0,0,0,1,0},
             {1,0,0,0,0,0,1},
             {0,0,1,0,1,0,0},
             {0,1,0,0,0,1,0},
             {1,0,0,1,0,0,1},
             {0,0,0,1,0,0,0}};
        
        private static final String CACHE_NAME = "explosion_alien";
        
        private static final int ALIEN_EXPLOSION_INTERVAL = 400;
        
        public Alien(SpaceInvaders space, org.wetteifer.spaceinvaders.Alien alien) {
            super(space, EXPLOSION_ALIEN, WIDTH, HEIGHT, ALIEN_EXPLOSION_INTERVAL, Color.WHITE, CACHE_NAME);
            setLocation(alien.getX() - (getWidth() - alien.getWidth()) / 2.0f, alien.getY());
        }
        
    }
    
    public static class Laser extends Explosion {
        
        public static final int WIDTH  = 8;
        public static final int HEIGHT = 8;
        
        private static final int[][] EXPLOSION_LASER =
            {{1,0,0,1,1,0,0,1},
             {0,0,1,1,1,1,0,0},
             {0,1,1,1,1,1,1,0},
             {0,0,1,1,1,1,0,1},
             {1,0,1,1,1,1,0,0},
             {0,0,1,1,1,1,1,0},
             {0,1,1,1,1,1,0,0},
             {1,0,0,1,1,0,0,1}};
        
        private static final String CACHE_NAME = "explosion_laser";
        
        private static final int LASER_EXPLOSION_INTERVAL = 400;
        
        public Laser(SpaceInvaders space, org.wetteifer.spaceinvaders.Laser laser) {
            super(space, EXPLOSION_LASER, WIDTH, HEIGHT, LASER_EXPLOSION_INTERVAL, Color.WHITE, CACHE_NAME);
            setLocation(laser.getX() - getWidth() / 2.0f,
                        laser.getY() + laser.getHeight() - getHeight() / 2.0f);
        }
        
    }
    
}
