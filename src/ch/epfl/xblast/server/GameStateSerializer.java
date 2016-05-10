package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * TODO
 * 
 * @author Lorenz Rasch (249937)
 *
 */
public final class GameStateSerializer {

    private GameStateSerializer() {
    }

    /**
     * TODO
     * 
     * @param lvl
     * @return
     */
    public static List<Byte> serialize(Level lvl) {
        List<Byte> serial = new ArrayList<Byte>();
        // create list of bytes for the board
        List<Byte> board = new ArrayList<Byte>();
        for (Cell c : Cell.SPIRAL_ORDER) {
            board.add(lvl.getBoardPainter().byteForCell(
                    lvl.getGameState().board(), c));
        }
        // compress board-list and add to return list
        serial.addAll(RunLengthEncoder.encode(board));
        // create list of bytes for bombs and blasts
        List<Byte> exp = new ArrayList<Byte>();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            if (lvl.getGameState().bombedCells().containsKey(c)) {
                // cell with bomb, add byte for this bomb
                exp.add(ExplosionPainter.byteForBomb(lvl.getGameState()
                        .bombedCells().get(c)));
            } else if (lvl.getGameState().blastedCells().contains(c)) {
                // cell with blast
                if (!lvl.getGameState().board().blockAt(c).isFree()) {
                    // not empty cell with blast, take invalid byte
                    exp.add(ExplosionPainter.BYTE_FOR_EMPTY);
                } else {
                    // empty cell with blast, check four directions
                    boolean[] nesw = new boolean[] {};
                    for (Direction d : Direction.values()) {
                        nesw[d.ordinal()] = lvl.getGameState().blastedCells()
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
        for (Player p : lvl.getGameState().players()) {
            serial.add((byte) p.lives());
            serial.add((byte) p.position().x());
            serial.add((byte) p.position().y());
            serial.add(PlayerPainter.byteForPlayer(lvl.getGameState().ticks(),
                    p));
        }
        // add byte for game time
        serial.add((byte) Math.ceil(lvl.getGameState().remainingTime() / 2));
        return serial;
    }

}
