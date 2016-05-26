package ch.epfl.xblast.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public class GameStateDeserializerTest {

    List<Integer> x = new ArrayList<Integer>(Arrays.asList(121, -50, 2, 1, -2,
            0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3, 1, 3, 1, 1, -2, 0, 1, 3, 1, 3,
            -2, 0, -1, 1, 3, 1, 3, 1, 3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
            3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
            3, 2, 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3, 1,
            3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2, 3, 1, -2, 0,
            3, -2, 0, 1, 3, 2, 1, 2,

            4, -128, 16, -63, 16,

            1, 24, 24, 6,
            2, -40, 24, 26,
            3, -40, -72, 46,
            0, 24, -72, 66,

            54));

    private List<Byte> getCode(List<Integer> list) {
        List<Byte> code = new ArrayList<Byte>();
        for (Integer i : list) {
            code.add(i.byteValue());
        }
        return code;
    }

    @Test
    public void test01() {
        GameState gs = GameStateDeserializer.deserialize(getCode(x));
        // time tests
        assertEquals(60, gs.time().size());
        assertEquals(53, gs.time().lastIndexOf(gs.time().get(0)));

        // explosion test
        assertEquals(Cell.COUNT, gs.explosion().size());

        // player
        assertEquals(PlayerID.values().length, gs.players().size());
        assertEquals(1, gs.players().get(0).lives());
        assertEquals(SubCell.centralSubCellOf(new Cell(13, 1)), gs.players()
                .get(1).position());
        assertEquals(PlayerID.PLAYER_3, gs.players().get(2).id());
        assertFalse(gs.players().get(3).isAlive());

        // board
        assertEquals(Cell.COUNT, gs.board().size());
        assertEquals(15,
                gs.board().subList(0, 25).lastIndexOf(gs.board().get(0)));
    }

}
