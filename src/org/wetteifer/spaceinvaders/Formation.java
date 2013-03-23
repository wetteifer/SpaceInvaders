/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wetteifer.spaceinvaders;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.wetteifer.gfw.Drawable;
import static org.wetteifer.spaceinvaders.SpaceInvaders.*;

/**
 *
 * @author wetteifer
 */
public class Formation implements Drawable {

    private static final int ALIEN_ROWS       = 5;
    private static final int ALIEN_COLUMNS    = 11;
    private static final int ALIEN_MAX_COUNT  = ALIEN_ROWS * ALIEN_COLUMNS;
    private static final int ALIEN_MAX_WIDTH  = Alien.C.WIDTH  * MATRIX_SIZE;
    private static final int ALIEN_MAX_HEIGHT = Alien.C.HEIGHT * MATRIX_SIZE;

    private static final float ALIEN_INITIAL_X        =  5.0f * MATRIX_SIZE;
    private static final float ALIEN_INITIAL_Y        = 30.0f * MATRIX_SIZE;
    private static final float ALIEN_HORIZONTAL_GAP   =  4.0f * MATRIX_SIZE;
    private static final float ALIEN_VERTICAL_GAP     =  4.0f * MATRIX_SIZE;
    private static final float ALIEN_HORIZONTAL_LIMIT =  8.0f * MATRIX_SIZE;
    private static final float ALIEN_VERTICAL_LIMIT   = 40.0f * MATRIX_SIZE;

    private SpaceInvaders space;
    private List<Alien> aliens;

    private int alienMoveInterval;
    private long lastAlienMove;
    private float alienXSpeed, alienYSpeed;
    private boolean onLeftLimit, onRightLimit;
    private boolean moveDown, moveFast;

    public Formation(SpaceInvaders space) {
        this.space  = space;
        this.aliens = new ArrayList<Alien>(ALIEN_MAX_COUNT);
        reset();
    }
    
    private void createAliens() {
        float x, y, w, h, aw, bw;

        w = ALIEN_MAX_WIDTH  + ALIEN_HORIZONTAL_GAP;
        h = ALIEN_MAX_HEIGHT + ALIEN_VERTICAL_GAP;

        aw = ALIEN_MAX_WIDTH - Alien.A.WIDTH * MATRIX_SIZE;
        bw = ALIEN_MAX_WIDTH - Alien.B.WIDTH * MATRIX_SIZE;
        
        aliens.clear();

        for (int i = 0; i < ALIEN_COLUMNS; i++) {
            for (int j = 0; j < ALIEN_ROWS; j++) {
                x = ALIEN_INITIAL_X + w * i;
                y = ALIEN_INITIAL_Y + h * j;

                // FIXME: Y si hay mas filas?
                switch (j) {
                    case 0:
                        aliens.add(new Alien.A(x + aw, y));
                        break;
                    case 1: case 2:
                        aliens.add(new Alien.B(x + bw, y));
                        break;
                    case 3: case 4:
                        aliens.add(new Alien.C(x, y));
                        break;
                }
            }
        }
    }

    public final void reset() {
        Alien.reset();

        alienMoveInterval = 500;
        lastAlienMove = 0;
        alienXSpeed = 200.0f * MATRIX_SIZE;
        alienYSpeed = 800.0f * MATRIX_SIZE;
        onLeftLimit = true;
        onRightLimit = false;
        moveDown = false;
        moveFast = false;

        createAliens();
    }

    public void speedUp() {
        if (!moveFast) {
            if (aliens.size() > 1) {
                alienXSpeed *= 1.01f;
                alienMoveInterval -= 7;
            } else {
                alienXSpeed = 125.0f * MATRIX_SIZE;
                moveFast = true;
            }
        }
    }

    public void clear() {
        aliens.clear();
    }

    public Alien get(int index) {
        return aliens.get(index);
    }

    public Alien remove(int index) {
        return aliens.remove(index);
    }

    public Alien random() {
        return aliens.get((int) (Math.random() * aliens.size()));
    }

    public int size() {
        return aliens.size();
    }

    public boolean isEmpty() {
        return aliens.isEmpty();
    }

    @Override
    public void update(long delta) {
        if (!moveFast) {
            if (System.currentTimeMillis() - lastAlienMove < alienMoveInterval) {
                return;
            }
        }

        if (moveDown) {
            moveDown = false;

            for (Alien alien : aliens) {
                alien.setXSpeed(0.0f);
                alien.setYSpeed(alienYSpeed);

                alien.update(delta);

                if (alien.getY() + alien.getHeight() > SCREEN_HEIGHT - ALIEN_VERTICAL_LIMIT) {
                    space.notifyInvasionSuccessful();
                }
            }

            lastAlienMove = System.currentTimeMillis();

            return;
        }

        if (!onRightLimit) {
            for (Alien alien : aliens) {
                alien.setXSpeed(alienXSpeed);
                alien.setYSpeed(0.0f);

                alien.update(delta);

                if (alien.getX() + alien.getWidth() > SCREEN_WIDTH - ALIEN_HORIZONTAL_LIMIT) {
                    onRightLimit = true;
                    onLeftLimit = false;
                    moveDown = true;
                }
            }
        }

        if (!onLeftLimit) {
            for (Alien alien : aliens) {
                alien.setXSpeed(-alienXSpeed);
                alien.setYSpeed(0.0f);

                alien.update(delta);

                if (alien.getX() < ALIEN_HORIZONTAL_LIMIT) {
                    onRightLimit = false;
                    onLeftLimit = true;
                    moveDown = true;
                }
            }
        }

        lastAlienMove = System.currentTimeMillis();
    }

    @Override
    public void draw(Graphics2D g) {
        for (Alien alien : aliens) {
            alien.draw(g);
        }
    }

}
