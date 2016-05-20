package ch.epfl.xblast.client;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

/**
 * Class capable of deserializing the encoded game state made by
 * GameStateSerializer.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class GameStateDeserializer {

    // constants for the scoreboard images
    private final static int TEXT_MIDDLE = 10;
    private final static int TEXT_RIGHT = 11;
    private final static List<BufferedImage> VOID_TILES = Collections.nCopies(
            8, ImageCollection.SCORE.image(12));

    // constants for the timebar images
    private final static int TIMEBAR_LENGTH = 60;
    private final static int LED_OFF = 20;
    private final static int LED_ON = 21;

    // constants for deserialisation
    private final static int PLAYERS_CODE_LENGTH = 16;

    private GameStateDeserializer() {
    }

    /**
     * Gives a GameState client view of the game from the given code.
     * 
     * @param code
     *            the serialized game state
     * @return a GameState client view
     */
    public static GameState deserialize(List<Byte> code) {
        // reading the code lengths
        int boardEnd = code.get(0) + 1;
        int explosionEnd = code.get(boardEnd) + boardEnd + 1;
        // splitting up the code
        List<Byte> boardCode = RunLengthEncoder.decode(code
                .subList(0, boardEnd));
        List<Byte> explosionCode = RunLengthEncoder.decode(code.subList(
                boardEnd, explosionEnd));
        List<Byte> playersCode = code.subList(explosionEnd, explosionEnd
                + PLAYERS_CODE_LENGTH);
        Byte timeCode = code.get(code.size() - 1);
        // converting the code to Images/Players
        List<Player> players = getPlayers(playersCode);
        List<BufferedImage> board = getBoard(boardCode);
        List<BufferedImage> explosion = getExplosion(explosionCode);
        List<BufferedImage> scores = getScores(players);
        List<BufferedImage> time = getTime(timeCode);
        // creating the gamestate
        return new GameState(players, board, explosion, scores, time);
    }

    /**
     * Gives a list of the players from the code given.
     * 
     * @param pc
     *            code, list of bytes representing the players
     * @return list of players
     */
    private static List<Player> getPlayers(List<Byte> pc) {
        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < PlayerID.values().length; i++) {
            // read number of lives
            int lives = Byte
                    .toUnsignedInt(pc.get(0 + (i * PlayerID.values().length)));
            // read coordinates
            int x = Byte
                    .toUnsignedInt(pc.get(1 + (i * PlayerID.values().length)));
            int y = Byte
                    .toUnsignedInt(pc.get(2 + (i * PlayerID.values().length)));
            // read image number
            int img = Byte
                    .toUnsignedInt(pc.get(3 + (i * PlayerID.values().length)));
            // create player with values given, image null for dead players
            players.add(new Player(PlayerID.values()[i], lives, new SubCell(x,
                    y), ImageCollection.PLAYERS.imageOrNull(img)));
        }
        return players;
    }

    /**
     * Gives a list of the images representing the board, row major order, from
     * the given code bytes, spiral order.
     * 
     * @param bc
     *            code of the board, spiral order
     * @return list of images, row major order
     */
    private static List<BufferedImage> getBoard(List<Byte> bc) {
        BufferedImage[] img = new BufferedImage[Cell.COUNT];
        for(int i = 0; i < Cell.COUNT; i++){
            img[Cell.SPIRAL_ORDER.get(i).rowMajorIndex()] = ImageCollection.BLOCKS.image(bc.get(i));
        }
        return new ArrayList<BufferedImage>(Arrays.asList(img));
    }

    /**
     * Gives the list of images representing the bombs/blasts given by the list
     * of bytes.
     * 
     * @param ec
     *            the list of bytes for the bombs/blasts
     * @return the list of images for the bombs/blasts
     */
    private static List<BufferedImage> getExplosion(List<Byte> ec) {
        List<BufferedImage> exp = new ArrayList<BufferedImage>();
        // add picture for every byte to list, null for empty spaces
        for (Byte b : ec) {
            exp.add(ImageCollection.EXPLOSIONS.imageOrNull(b));
        }
        return exp;
    }

    /**
     * Gives a list of images representing the score board from the players
     * given. Each player is represented by his head(dead or alive) and two
     * tiles making a text-field. In the middle, 8 empty tiles are added.
     * 
     * @param players
     *            the list of players
     * @return list of images for the scoreboard
     */
    private static List<BufferedImage> getScores(List<Player> players) {
        List<BufferedImage> scores = new ArrayList<BufferedImage>();
        for (Player p : players) {
            // add players head
            if (p.isAlive()) {
                scores.add(ImageCollection.SCORE.image(p.id().ordinal() * 2));
            } else {
                scores.add(ImageCollection.SCORE
                        .image(p.id().ordinal() * 2 + 1));
            }
            // add tiles to make up text-field
            scores.add(ImageCollection.SCORE.image(TEXT_MIDDLE));
            scores.add(ImageCollection.SCORE.image(TEXT_RIGHT));
        }
        // add empty tiles in the middle
        scores.addAll(scores.size() / 2, VOID_TILES);
        return scores;
    }

    /**
     * Gives the list of images representing the timebar.
     * 
     * @param tc
     *            the time left divided by 2
     * @return list of images for the time bar
     */
    private static List<BufferedImage> getTime(Byte tc) {
        List<BufferedImage> time = new ArrayList<BufferedImage>();
        // add images representing the time left
        time.addAll(Collections.nCopies(Byte.toUnsignedInt(tc),
                ImageCollection.SCORE.image(LED_ON)));
        // add images representing the passed time
        time.addAll(Collections.nCopies(TIMEBAR_LENGTH - tc,
                ImageCollection.SCORE.image(LED_OFF)));
        return time;
    }

}
