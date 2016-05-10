package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * Class allowing to serialize a GameState, grouping bytes for Board, Players,
 * Bombs/Blasts and remaining time.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class GameStateSerializer {

    private GameStateSerializer() {
    }

    /**
     * Gives the list, representing the serialized GameState. Board(spiral
     * order) and Bombs/Blasts(row major order) encoded by run length encoding.
     * Then Players and remaining time.
     * 
     * @param lvl
     *            the current Level
     * @return list of the serialized gamestate
     */
    public static List<Byte> serialize(Level lvl) {
        List<Byte> serial = new ArrayList<Byte>();
        // create list of bytes for the board
        List<Byte> board = new ArrayList<Byte>();
        for (Cell c : Cell.SPIRAL_ORDER) {
            board.add(lvl.boardPainter()
                    .byteForCell(lvl.gameState().board(), c));
        }
        // compress board-list and add to return list
        serial.addAll(RunLengthEncoder.encode(board));
        // create list of bytes for bombs and blasts
        List<Byte> exp = new ArrayList<Byte>();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            if (lvl.gameState().bombedCells().containsKey(c)) {
                // cell with bomb, add byte for this bomb
                exp.add(ExplosionPainter.byteForBomb(lvl.gameState()
                        .bombedCells().get(c)));
            } else if (lvl.gameState().blastedCells().contains(c)) {
                // cell with blast
                if (!lvl.gameState().board().blockAt(c).isFree()) {
                    // not empty cell with blast, take invalid byte
                    exp.add(ExplosionPainter.BYTE_FOR_EMPTY);
                } else {
                    // empty cell with blast, check four directions
                    boolean[] nesw = new boolean[] {};
                    for (Direction d : Direction.values()) {
                        nesw[d.ordinal()] = lvl.gameState().blastedCells()
                                .contains(c.neighbor(d));
                    }
                    // add byte for blast
                    exp.add(ExplosionPainter.byteForBlast(nesw));
                }
            } else {
                exp.add(ExplosionPainter.BYTE_FOR_EMPTY);
            }
        }
        // compress explosion list and add to return list
        serial.addAll(RunLengthEncoder.encode(exp));
        // add bytes for each player
        for (Player p : lvl.gameState().players()) {
            serial.add((byte) p.lives());
            serial.add((byte) p.position().x());
            serial.add((byte) p.position().y());
            serial.add(PlayerPainter.byteForPlayer(lvl.gameState().ticks(), p));
        }
        // add byte for game time
        serial.add((byte) Math.ceil(lvl.gameState().remainingTime() / 2));
        return serial;
    }

}
