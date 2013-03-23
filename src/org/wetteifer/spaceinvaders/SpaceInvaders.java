/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wetteifer.spaceinvaders;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.wetteifer.gfw.Game;
import org.wetteifer.gfw.shape.Entity;
import org.wetteifer.gfw.shape.Matrix;
import org.wetteifer.gfw.shape.Shape;

/**
 *
 * @author wetteifer
 */
public class SpaceInvaders extends Game {
    
    /*
     * Inicializar el audio antes que todo.
     */
    static {
        Audio.init();
    }

    /*
     * Incremento en el tamaño de cada cuadrado de los elementos.
     * No deberia ser mayor a 3.
     */
    public static final int MATRIX_SIZE = 2;

    /*
     * Dimensiones de la pantalla.
     */
    public static final int SCREEN_WIDTH  = 220 * MATRIX_SIZE;
    public static final int SCREEN_HEIGHT = 230 * MATRIX_SIZE;

    /*
     * Número máximo de vidas.
     */
    private static final int MAX_LIVES = 3;

    /*
     * Número máximo de bunkers.
     */
    private static final int MAX_BUNKERS = 5;

    /*
     * Puntajes de cada alien al ser destruido.
     */
    private static final int ALIEN_A_SCORE = 300;
    private static final int ALIEN_B_SCORE = 200;
    private static final int ALIEN_C_SCORE = 100;

    /*
     * Duración en milisegundos de cierto evento.
     */
    private static final int CANNON_SHOOT_INTERVAL = 500;
    private static final int ALIEN_SHOOT_INTERVAL  = 800;
    private static final int GAME_WIN_INTERVAL     = 2500;
    private static final int GAME_LOSE_INTERVAL    = 1000;

    /*
     * Elementos del juego.
     */
    private Cannon cannon;
    private Formation aliens;
    private List<Bunker> bunkers;
    private List<Laser> cannonShoots, aliensShoots;
    private List<Explosion> explosions;

    /*
     * Dibujan el número de vidas.
     */
    private Cannon[] cannons;

    /*
     * Texto desplegado en la pantalla.
     */
    private Text title1, title2, info, author1, author2;
    private Text cannonLivesText;
    private Text scoreText, scoreCountText;
    private Text pausingText, winningText, loosingText;

    /*
     * Utilizados para calcular las colisiones.
     */
    private Rectangle2D r1 = new Rectangle2D.Float();
    private Rectangle2D r2 = new Rectangle2D.Float();

    /*
     * Número de vidas.
     */
    private int lives = MAX_LIVES;

    /*
     * Puntaje actual.
     */
    private int score = 0;

    /*
     * Tiempo en que ocurrió cierto evento.
     */
    private long lastCannonShoot;
    private long lastAlienShoot;
    private long lastGameWin;
    private long lastGameLose;

    /*
     * Indica si el cannon esta disparando.
     */
    private boolean shooting;

    /*
     * Indica si el cannon puede disparar.
     */
    private boolean canShoot = true;

    /*
     * Indicadores de la dirección del cannon.
     */
    private boolean leftPressed, rightPressed;

    /*
     * Indica si los aliens han llegado al fondo de la pantalla.
     */
    private boolean invasionSuccessful;
    
    /*
     * Indica si se debe esperar a que se presione la tecla
     * ENTER para cambiar de estado.
     */
    private boolean waitForKeyPress = true;

    /*
     * Interfaz con los eventos de cada estado del juego.
     */
    private interface State {

        /*
         * Actualiza la posición de los elementos en la pantalla.
         */
        void update(long delta);

        /*
         * Dibuja los elementos de este estado.
         */
        void draw(Graphics2D g);

    }

    /*
     * Inicio del juego. Mostrando la pantalla de título.
     */
    private final State STATE_START = new State() {

        @Override
        public void update(long delta) {
            // No hay lógica.
        }

        @Override
        public void draw(Graphics2D g) {
            title1.draw(g);
            title2.draw(g);            
            info.draw(g);            
            author1.draw(g);
            author2.draw(g);
        }

    };

    /*
     * Juego en proceso.
     */
    private final State STATE_PLAY = new State() {

        @Override
        public void update(long delta) {
            /*
             * Mover las entidades.
             */
            cannon.update(delta);
            aliens.update(delta);

            for (Laser laser : cannonShoots) {
                laser.update(delta);
            }

            for (Laser laser : aliensShoots) {
                laser.update(delta);
            }

            for (Explosion explosion : explosions) {
                explosion.update(delta);
            }

            /*
             * Si ya no hay aliens, terminó el juego.
             */
            if (aliens.isEmpty()) {
                if (lastGameWin != 0) {
                    if (System.currentTimeMillis() - lastGameWin >= GAME_WIN_INTERVAL) {
                        waitForKeyPress = true;
                        setStateCallback(STATE_WIN);
                        return;
                    }
                } else {
                    lastGameWin = System.currentTimeMillis();
                }
            }

            /*
             * Los aliens llegaron al final o ya no hay vidas.
             */
            if (invasionSuccessful || lives <= 0) {
                if (lastGameLose != 0) {
                    if (System.currentTimeMillis() - lastGameLose >= GAME_LOSE_INTERVAL) {
                        waitForKeyPress = true;
                        setStateCallback(STATE_LOSE);
                        return;
                    }
                } else {
                    lastGameLose = System.currentTimeMillis();
                }
            }

            /*
             * Disparo del cannon.
             */
            if (shooting && System.currentTimeMillis() - lastCannonShoot >= CANNON_SHOOT_INTERVAL) {                
                lastCannonShoot = System.currentTimeMillis();     
                
                cannonShoots.add(new Laser.Cannon(SpaceInvaders.this, cannon));
                Audio.SHOOT.play();
                
                shooting = false;
            }

            /*
             * Disparo de los aliens.
             */
            if (!aliens.isEmpty() && System.currentTimeMillis() - lastAlienShoot >= ALIEN_SHOOT_INTERVAL) {
                lastAlienShoot = System.currentTimeMillis();
                aliensShoots.add(new Laser.Alien(SpaceInvaders.this, aliens.random()));
            }
            
            int i, j;

            /*
             * Checar colisiones con el cannon.
             */
            for (i = 0; i < aliensShoots.size(); i++) {
                Laser laser = aliensShoots.get(i);
                if (checkCollision(cannon, laser)) {                    
                    lives--;
                    canShoot = false;
                    
                    cannon.freeze(true);
                    
                    explosions.add(new Explosion.Cannon(SpaceInvaders.this, cannon));
                    Audio.EXPLOSION.play();
                    
                    aliensShoots.remove(i);
                    break;
                }
            }

            /*
             * Checar colisiones entre los lasers.
             */
            for (i = 0; i < cannonShoots.size(); i++) {
                Laser laserCannon = cannonShoots.get(i);
                for (j = 0; j < aliensShoots.size(); j++) {
                    Laser laserAlien = aliensShoots.get(j);
                    if (checkCollision(laserCannon, laserAlien)) {                        
                        explosions.add(new Explosion.Laser(SpaceInvaders.this, laserAlien));
                        Audio.EXPLOSION.play();
                        
                        cannonShoots.remove(i);
                        aliensShoots.remove(j);                        
                    }
                }
            }

            /*
             * Checar colisiones de los aliens.
             */
            for (i = 0; i < aliens.size(); i++) {
                Alien alien = aliens.get(i);
                for (j = 0; j < cannonShoots.size(); j++) {
                    Laser laser = cannonShoots.get(j);
                    if (checkCollision(alien, laser)) {
                        explosions.add(new Explosion.Alien(SpaceInvaders.this, alien));
                        Audio.ALIEN_KILLED.play();
                        
                        aliens.remove(i);
                        cannonShoots.remove(j);
                        
                        alien.speedUp();
                        aliens.speedUp();
                        
                        updateScore(alien);
                    }
                }
            }
            
            /*
             * Checar colisiones con los bunkers.
             */
            for (Bunker bunker : bunkers) {
                if (bunker.isDestroyed()) {
                    continue;
                }
                
                for (j = 0; j < cannonShoots.size(); j++) {
                    Laser laser = cannonShoots.get(j);
                    if (checkCollision(bunker, laser)) {
                        cannonShoots.remove(j);
                        bunker.damage();
                    }
                }
                
                for (j = 0; j < aliensShoots.size(); j++) {
                    Laser laser = aliensShoots.get(j);
                    if (checkCollision(bunker, laser)) {
                        aliensShoots.remove(j);
                        bunker.damage();
                    }
                }
            }
        }

        @Override
        public void draw(Graphics2D g) {
            cannon.draw(g);

            for (Laser laser : cannonShoots) {
                laser.draw(g);
            }

            aliens.draw(g);

            for (Explosion explosion : explosions) {
                explosion.draw(g);
            }

            for (Laser laser : aliensShoots) {
                laser.draw(g);
            }

            for (Bunker bunker : bunkers) {
                bunker.draw(g);
            }

            scoreText.draw(g);
            scoreCountText.draw(g);
            cannonLivesText.draw(g);

            for (int i = lives - 1; i >= 0; i--) {
                cannons[i].draw(g);
            }

            if (isPaused()) {
                pausingText.draw(g);
            }
        }

    };

    /*
     * El jugador ha destruido todos los aliens.
     */
    private final State STATE_WIN = new State() {

        @Override
        public void update(long delta) {
            // No hay lógica.
        }

        @Override
        public void draw(Graphics2D g) {
            winningText.draw(g);
        }

    };

    /*
     * El jugador se ha quedado sin vidas.
     */
    private final State STATE_LOSE = new State() {

        @Override
        public void update(long delta) {
            // No hay lógica.
        }

        @Override
        public void draw(Graphics2D g) {
            loosingText.draw(g);
        }

    };

    /*
     * Clase que controla los estados del juego.
     */
    private class StateHandler {

        private State callback;

        public StateHandler(State callback) {
            this.callback = callback;
        }
        
        public State getStateCallback() {
            return callback;
        }

        public void setStateCallback(State callback) {
            this.callback = callback;
        }

        public void update(long delta) {
            callback.update(delta);
        }

        public void draw(Graphics2D g) {
            callback.draw(g);
        }

    }

    /*
     * Estado actual del juego.
     */
    private StateHandler state = new StateHandler(STATE_START);

    public SpaceInvaders() {
        super("Space Invaders", SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK);
    }
    
    private void reset() {
        lives = MAX_LIVES;
        score = 0;
        
        updateScore(null);
        
        lastCannonShoot = 0;
        lastAlienShoot  = 0;
        lastGameWin     = 0;
        lastGameLose    = 0;
        
        shooting = false;
        canShoot = true;
        leftPressed = false;
        rightPressed = false;
        invasionSuccessful = false;
        waitForKeyPress = true;
        
        cannon.reset();
        aliens.reset();
        
        for (Bunker bunker : bunkers) {
            bunker.reset();
        }
        
        cannonShoots.clear();
        aliensShoots.clear();
        explosions.clear();
    }

    private void createEntities() {
        cannon = new Cannon(this);
        aliens = new Formation(this);
        bunkers = new CopyOnWriteArrayList<Bunker>();
        explosions = new CopyOnWriteArrayList<Explosion>();
        cannonShoots = new CopyOnWriteArrayList<Laser>();
        aliensShoots = new CopyOnWriteArrayList<Laser>();

        cannons = new Cannon[MAX_LIVES];

        title1          = new Text("SPACE", MATRIX_SIZE + 3);
        title2          = new Text("INVADERS", MATRIX_SIZE + 3);
        info            = new Text("PRESS ENTER");
        author1         = new Text("Dario Lumbreras");
        author2         = new Text("December 2012");
        
        scoreText       = new Text("SCORE");
        scoreCountText  = new Text("0000000");
        cannonLivesText = new Text("LIVES");
        winningText     = new Text("YOU WIN !");
        loosingText     = new Text("GAME OVER");
        pausingText     = new Text("PAUSE");
    }

    private void locateEntities() {
        float horizontalGap = 10.0f * MATRIX_SIZE;
        float verticalGap   =  5.0f * MATRIX_SIZE;
        float innerGap      =  2.5f * MATRIX_SIZE;
        
        /*
         * Pantalla de inicio.
         */
        title1.setLocation((SCREEN_WIDTH - title1.getWidth()) / 2.0f, 25.0f * MATRIX_SIZE);        
        title2.setLocation((SCREEN_WIDTH - title2.getWidth()) / 2.0f, title1.getY() + title1.getHeight() + 5.0f * MATRIX_SIZE);

        author2.setLocation((SCREEN_WIDTH - author2.getWidth()) / 2.0f, SCREEN_HEIGHT - 25.0f * MATRIX_SIZE);
        author1.setLocation((SCREEN_WIDTH - author1.getWidth()) / 2.0f, author2.getY() - 5.0f * MATRIX_SIZE - author1.getHeight());
        
        float infoY = title2.getY() + title2.getHeight() + (author1.getY() - (title2.getY() + title2.getHeight())) / 2.0f - info.getHeight() / 2.0f;
        info.setLocation((SCREEN_WIDTH - info.getWidth()) / 2.0f, infoY);

        /*
         * Puntaje.
         */
        scoreText.setLocation(horizontalGap + (scoreCountText.getWidth() - scoreText.getWidth()) / 2.0f, verticalGap);
        scoreCountText.setLocation(horizontalGap, verticalGap + scoreText.getHeight() + innerGap);

        /*
         * Vidas.
         */
        int   cannonWidth = Cannon.WIDTH * MATRIX_SIZE;
        float cannonGap   = 2.5f * MATRIX_SIZE;
        float cannonY     = verticalGap + cannonLivesText.getHeight() + innerGap;

        for (int i = 0; i < MAX_LIVES; i++) {
            cannons[i] = new Cannon(this);

            // En la primera iteración, agregamos la separación horizontal.
            if (i == 0) {
                cannons[i].setLocation(SCREEN_WIDTH - horizontalGap - cannonWidth, cannonY);
            }

            // En el resto, agregamos la separación entre los cannons.
            else {
                cannons[i].setLocation(cannons[i - 1].getX() - cannonGap - cannonWidth, cannonY);
            }
        }

        float cannonsLivesWidth = cannons[0].getX() + cannonWidth - cannons[MAX_LIVES - 1].getX();
        cannonLivesText.setLocation(cannons[MAX_LIVES - 1].getX() + (cannonsLivesWidth - cannonLivesText.getWidth()) / 2.0f, verticalGap);

        /*
         * Textos en el centro de la pantalla.
         */
        winningText.centerOnScreen();
        loosingText.centerOnScreen();
        pausingText.centerOnScreen();
        
        /*
         * Bunkers.
         */
        int   bunkerWidth = Bunker.WIDTH  * MATRIX_SIZE;
        float bunkerY     = SCREEN_HEIGHT - 40.0f * MATRIX_SIZE;

        Bunker first = new Bunker();
        first.setLocation(horizontalGap, bunkerY);

        Bunker last = new Bunker();
        last.setLocation(SCREEN_WIDTH - bunkerWidth - horizontalGap, bunkerY);

        float bunkerGap = ((last.getX() - (first.getX() + bunkerWidth)) - (MAX_BUNKERS - 2) * bunkerWidth) / (MAX_BUNKERS - 1);

        bunkers.add(first);
        
        for (int i = 1; i < MAX_BUNKERS - 1; i++) {
            Bunker bunker = new Bunker();
            bunker.setLocation(bunkers.get(i - 1).getX() + bunkerWidth + bunkerGap, bunkerY);
            bunkers.add(bunker);
        }
        
        bunkers.add(last);
    }
    
    private State getStateCallback() {
        return state.getStateCallback();
    }

    private void setStateCallback(State callback) {
        state.setStateCallback(callback);
    }

    private boolean checkCollision(Shape s1, Shape s2) {
        r1.setRect(s1.getX(), s1.getY(), s1.getWidth(), s1.getHeight());
        r2.setRect(s2.getX(), s2.getY(), s2.getWidth(), s2.getHeight());
        return r1.intersects(r2);
    }

    @Override
    public void load() {
        createEntities();
        locateEntities();
    }

    @Override
    public void update(long delta) {
        state.update(delta);
    }

    @Override
    public void draw(Graphics2D g) {
        state.draw(g);
    }

    @Override
    public void unload() {
        // Eliminamos el caché de audio.
        Audio.clear();

        // Eliminamos el caché de imágenes.
        Matrix.clear();

        // Eliminamos los elementos de las listas.
        aliens.clear();
        cannonShoots.clear();
        aliensShoots.clear();
        explosions.clear();
        bunkers.clear();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (waitForKeyPress) {
                    State callback = getStateCallback();                    
                    if (callback == STATE_START) {
                        waitForKeyPress = false;
                        setStateCallback(STATE_PLAY);
                    } else if (callback == STATE_WIN || callback == STATE_LOSE) {
                        reset();
                        setStateCallback(STATE_START);
                    }
                } else {
                    pause();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                exit();
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_SPACE:
                if (canShoot && !isPaused()) {
                    shooting = true;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
        }
    }

    public void remove(Entity entity) {
        if (entity instanceof Laser.Cannon) {
            cannonShoots.remove((Laser) entity);
        } else if (entity instanceof Laser.Alien) {
            aliensShoots.remove((Laser) entity);
        } else if (entity instanceof Explosion) {
            explosions.remove((Explosion) entity);
        } else if (entity instanceof Bunker) {
            bunkers.remove((Bunker) entity);
        }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
    
    public int getLives() {
        return lives;
    }

    public void notifyInvasionSuccessful() {
        invasionSuccessful = true;
    }

    public void notifyCannonCanShoot() {
        canShoot = true;
    }

    public void updateScore(Alien alien) {
        int n = 0;

        if (alien instanceof Alien.A) {
            n = ALIEN_A_SCORE;
        } else if (alien instanceof Alien.B) {
            n = ALIEN_B_SCORE;
        } else if (alien instanceof Alien.C) {
            n = ALIEN_C_SCORE;
        }

        score += n;
        scoreCountText.setText(String.format("%07d", score));
        scoreCountText.createImage();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SpaceInvaders().start();
            }
        });
    }

}





