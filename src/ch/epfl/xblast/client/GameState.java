package ch.epfl.xblast.client;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * Class representing the state of the game, seen from the clients side.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class GameState {

    private final List<Player> players;
    private final List<BufferedImage> board, explosion, scores, time;

    /**
     * Gives a new gamestate with the parameters given.
     * 
     * @param players
     *            a list of the players
     * @param board
     *            a list of the images representing the board
     * @param explosion
     *            a list of the images representing the bombs/blasts
     * @param scores
     *            a list of the images representing the scoreboard
     * @param time
     *            a list of the images representing the countdown-bar
     */
    public GameState(List<Player> players, List<BufferedImage> board,
            List<BufferedImage> explosion, List<BufferedImage> scores,
            List<BufferedImage> time) {
        this.players = Collections.unmodifiableList(new ArrayList<Player>(
                Objects.requireNonNull(players)));
        this.board = Collections.unmodifiableList(new ArrayList<BufferedImage>(
                Objects.requireNonNull(board)));
        this.explosion = Collections
                .unmodifiableList(new ArrayList<BufferedImage>(Objects
                        .requireNonNull(explosion)));
        this.scores = Collections
                .unmodifiableList(new ArrayList<BufferedImage>(Objects
                        .requireNonNull(scores)));
        this.time = Collections.unmodifiableList(new ArrayList<BufferedImage>(
                Objects.requireNonNull(time)));
    }

    /**
     * Gives the list of the players.
     * 
     * @return the players
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Gives the list of the images representing the board.
     * 
     * @return the boards images
     */
    public List<BufferedImage> board() {
        return board;
    }

    /**
     * Gives the list of the images representing the bombs/blasts.
     * 
     * @return the bombs/blasts images
     */
    public List<BufferedImage> explosion() {
        return explosion;
    }

    /**
     * Gives the list of the images representing the scoreboard.
     * 
     * @return the scores images
     */
    public List<BufferedImage> scores() {
        return scores;
    }

    /**
     * Gives the list of the images representing the timebar.
     * 
     * @return the timebars images
     */
    public List<BufferedImage> time() {
        return time;
    }

    /**
     * Class representing a player from the clients view.
     * 
     * @author Lorenz Rasch (249937)
     */
    public final static class Player {

        private final PlayerID id;
        private final int lives;
        private final SubCell pos;
        private final BufferedImage img;

        /**
         * Creates a new Player with the parameters given.
         * 
         * @param playerId
         *            the id of the player
         * @param lives
         *            the lives of the player
         * @param position
         *            the position of the player
         * @param image
         *            the image representing the player
         */
        public Player(PlayerID playerId, int lives, SubCell position,
                BufferedImage image) {
            id = Objects.requireNonNull(playerId);
            this.lives = Objects.requireNonNull(lives);
            pos = Objects.requireNonNull(position);
            img = Objects.requireNonNull(image);
        }

        /**
         * Gives the players id.
         * 
         * @return the id
         */
        public PlayerID id() {
            return id;
        }

        /**
         * Gives the players number of lives
         * 
         * @return the number of lives
         */
        public int lives() {
            return lives;
        }

        /**
         * Gives the players position
         * 
         * @return the position
         */
        public SubCell position() {
            return pos;
        }

        /**
         * Gives the players image.
         * 
         * @return the image
         */
        public BufferedImage image() {
            return img;
        }

        /**
         * Tells if the player is still alive: has more than 0 lives
         * 
         * @return true if the player has more than 0 lives, false otherwise
         */
        public boolean isAlive() {
            return lives() > 0;
        }

    }

}
