/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wetteifer.spaceinvaders;

import org.wetteifer.gfw.audio.AudioPlayer;

/**
 *
 * @author wetteifer
 */
public enum Audio {
    
    /*
     * Disparo del cannon.
     */
    SHOOT("org/wetteifer/spaceinvaders/audio/shoot.wav"),
    
    /*
     * Explosión del cannon o de los lasers.
     */
    EXPLOSION("org/wetteifer/spaceinvaders/audio/explosion.wav"),
    
    /*
     * Alien destruido.
     */
    ALIEN_KILLED("org/wetteifer/spaceinvaders/audio/alienkilled.wav");
    
    private static final AudioPlayer AUDIO_PLAYER = AudioPlayer.getAudioPlayer();
    private static final boolean AUDIO_ENABLED = true;
    
    private String path;

    Audio(String path) {
        this.path = path;
    }

    /*
     * Carga los archivos de audio en el caché.
     */
    public static void init() {
        if (AUDIO_ENABLED) {
            for (Audio audio : values()) {
                AUDIO_PLAYER.load(audio.path);
            }
        }
    }

    /*
     * Elimina el caché de audio.
     */
    public static void clear() {
        if (AUDIO_ENABLED) {
            AUDIO_PLAYER.clear();
        }
    }

    /*
     * Reproduce este archivo de audio.
     */
    public void play() {
        if (AUDIO_ENABLED) {
            AUDIO_PLAYER.play(path);
        }
    }

    /*
     * Detiene la reproducción de este archivo de audio.
     */
    public void stop() {
        if (AUDIO_ENABLED) {
            AUDIO_PLAYER.stop(path);
        }
    }

    /*
     * Repite la reproducción de este archivo de audio.
     */
    public void loop() {
        if (AUDIO_ENABLED) {
            AUDIO_PLAYER.loop(path);
        }
    }
    
}
