package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.debug.GameStatePrinter;

/**
 * @author Lorenz Rasch (249937)
 *
 */
public class NextGameStateTest {

    private static Block __ = Block.FREE;
    private static Block XX = Block.INDESTRUCTIBLE_WALL;
    private static Block xx = Block.DESTRUCTIBLE_WALL;

    private static Board BOARD = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __)));

    private static Board BOARD2 = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, XX, __),
            Arrays.asList(__, __, __, __, __, __, __)));

    private static Board BOARD3 = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, __, __),
            Arrays.asList(__, __, __, __, __, XX, xx),
            Arrays.asList(__, __, __, __, __, xx, __)));

    private static List<Player> PLAYERS = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, 2, new Cell(1, 1), 1, 1), new Player(
                    PlayerID.PLAYER_2, 3, new Cell(2, 2), 2, 2), new Player(
                    PlayerID.PLAYER_3, 4, new Cell(3, 3), 3, 3), new Player(
                    PlayerID.PLAYER_4, 5, new Cell(4, 4), 4, 4)));

    private static List<Bomb> BOMBS = new ArrayList<Bomb>(Arrays.asList(
            new Bomb(PlayerID.PLAYER_1, new Cell(5, 5), 3, 5), new Bomb(
                    PlayerID.PLAYER_1, new Cell(6, 6), 1, 3)));

    private static List<Bomb> BOMBS2 = new ArrayList<Bomb>(Arrays.asList(
            new Bomb(PlayerID.PLAYER_1, new Cell(6, 8), 1, 4), new Bomb(
                    PlayerID.PLAYER_1, new Cell(6, 6), 50, 3)));

    private static List<Bomb> BOMBS3 = new ArrayList<Bomb>(Arrays.asList(
            new Bomb(PlayerID.PLAYER_1, new Cell(7, 6), 1, 5), new Bomb(
                    PlayerID.PLAYER_1, new Cell(9, 6), 3, 3)));

    @Test
    public void nextTest() {
        GameState gs = new GameState(BOARD, PLAYERS);
        for (int i = 0; i < 5; i++) {
            System.out.println("--- Game State " + i + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        assertFalse(gs.isGameOver());
        assertEquals(4, gs.alivePlayers().size());
    }

    @Test
    public void bombExplosionAndBlastTest() {
        GameState gs = new GameState(0, BOARD, PLAYERS, BOMBS,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        assertEquals(24, gs.blastedCells().size());
        System.out.println("----------------------------");
        for (int i = 10; i < 30; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("--- Game State " + gs.ticks() + " ---");
        GameStatePrinter.printGameState(gs);
        assertEquals(0, gs.bombedCells().size());
        assertEquals(12, gs.blastedCells().size());
    }

    @Test
    public void explosionBombTest() {
        GameState gs = new GameState(0, BOARD, PLAYERS, BOMBS2,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        assertEquals(18, gs.blastedCells().size());
        System.out.println("----------------------------");
        for (int i = 10; i < 30; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("--- Game State " + gs.ticks() + " ---");
        GameStatePrinter.printGameState(gs);
        assertEquals(0, gs.bombedCells().size());
        assertEquals(8, gs.blastedCells().size());
    }

    @Test
    public void indestructibleWallTest() {
        GameState gs = new GameState(0, BOARD2, PLAYERS, BOMBS2,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("----------------------------");
        for (int i = 10; i < 30; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("--- Game State " + gs.ticks() + " ---");
        GameStatePrinter.printGameState(gs);
    }

    @Test
    public void destructibleWallTest() {
        GameState gs = new GameState(0, BOARD3, PLAYERS, BOMBS3,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("----------------------------");
        assertEquals(Block.CRUMBLING_WALL, gs.board().blockAt(new Cell(6, 6)));
        for (int i = 10; i < 30; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("--- Game State " + gs.ticks() + " ---");
        GameStatePrinter.printGameState(gs);
        System.out.println("----------------------------");
        for (int i = 35; i < 75; i++) {
            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        System.out.println("----------------------------");
        System.out.println("--- Game State " + gs.ticks() + " ---");
        GameStatePrinter.printGameState(gs);

    }

}
