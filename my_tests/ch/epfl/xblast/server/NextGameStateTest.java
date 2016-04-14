package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
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
            Arrays.asList(__, __, __, __, xx, XX, xx),
            Arrays.asList(__, __, __, xx, __, xx, __)));

    private static List<Player> PLAYERS = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, 2, new Cell(1, 1), 1, 1), new Player(
                    PlayerID.PLAYER_2, 3, new Cell(2, 2), 2, 2), new Player(
                    PlayerID.PLAYER_3, 4, new Cell(3, 3), 3, 3), new Player(
                    PlayerID.PLAYER_4, 5, new Cell(4, 4), 4, 4)));

    private static List<Player> PLAYERS2 = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(1, Player.LifeState.State.VULNERABLE)),
                    Sq.constant(new Player.DirectedPosition(SubCell.centralSubCellOf(new Cell(6,5)).neighbor(Direction.E), Direction.S)), 1, 1),
                    new Player(PlayerID.PLAYER_2, Sq.constant(new Player.LifeState(3, Player.LifeState.State.VULNERABLE)),
                            Sq.constant(new Player.DirectedPosition(SubCell.centralSubCellOf(new Cell(5,6)), Direction.S)), 1, 1), new Player(
                                    PlayerID.PLAYER_3, 5, new Cell(1, 1), 3, 3), new Player(
                                            PlayerID.PLAYER_4, 5, new Cell(1, 2), 4, 4)));

    private static List<Bomb> BOMBS = new ArrayList<Bomb>(Arrays.asList(
            new Bomb(PlayerID.PLAYER_1, new Cell(5, 5), 3, 5), new Bomb(
                    PlayerID.PLAYER_2, new Cell(6, 6), 1, 3)));

    private static List<Bomb> BOMBS2 = new ArrayList<Bomb>(Arrays.asList(
            new Bomb(PlayerID.PLAYER_1, new Cell(6, 8), 1, 4), new Bomb(
                    PlayerID.PLAYER_1, new Cell(6, 6), 9, 3)));

    private static List<Bomb> BOMBS3 = new ArrayList<Bomb>(Arrays.asList(
            new Bomb(PlayerID.PLAYER_1, new Cell(7, 6), 1, 5), new Bomb(
                    PlayerID.PLAYER_1, new Cell(9, 6), 3, 3)));

    @Test
    public void nextTest() {
        GameState gs = new GameState(BOARD, PLAYERS);
        for (int i = 0; i < 5; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        assertFalse(gs.isGameOver());
        assertEquals(4, gs.alivePlayers().size());
        assertEquals(5, gs.ticks());
    }

    // 2 normal, independet explosions
    @Test
    public void bombExplosionAndBlastTest() {
        GameState gs = new GameState(0, BOARD, PLAYERS, BOMBS,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        assertEquals(0, gs.bombedCells().size());
        assertEquals(24, gs.blastedCells().size());
//        System.out.println("----------------------------");
        for (int i = 10; i < 30; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("--- Game State " + gs.ticks() + " ---");
//        GameStatePrinter.printGameState(gs);
        assertEquals(12, gs.blastedCells().size());
    }

    // 2 bombs, one exploding the other
    @Test
    public void explosionBombTest() {
        GameState gs = new GameState(0, BOARD, PLAYERS, BOMBS2,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
        assertEquals(0, gs.bombedCells().size());
        assertEquals(18, gs.blastedCells().size());
//        System.out.println("----------------------------");
        for (int i = 10; i < 30; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("--- Game State " + gs.ticks() + " ---");
//        GameStatePrinter.printGameState(gs);
        assertEquals(8, gs.blastedCells().size());
    }

    // indestructible walls blocking blasts!
    @Test
    public void indestructibleWallTest() {
        GameState gs = new GameState(0, BOARD2, PLAYERS, BOMBS2,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("----------------------------");
        for (int i = 10; i < 30; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("--- Game State " + gs.ticks() + " ---");
        assertTrue(true);
//        GameStatePrinter.printGameState(gs);
    }

    // wall get destroyed, bonuses vanish
    @Test
    public void destructibleWallTest() {
        GameState gs = new GameState(0, BOARD3, PLAYERS, BOMBS3,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 10; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("----------------------------");
        assertEquals(Block.CRUMBLING_WALL, gs.board().blockAt(new Cell(6, 6)));
        for (int i = 10; i < 30; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("----------------------------");
        for (int i = 30; i < 35; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("--- Game State " + gs.ticks() + " ---");
//        GameStatePrinter.printGameState(gs);
//        System.out.println("----------------------------");
        for (int i = 35; i < 75; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("----------------------------");
//        System.out.println("--- Game State " + gs.ticks() + " ---");
//        GameStatePrinter.printGameState(gs);

    }

    //2 players losing a life, one dying
    @Test
    public void playerLosingLife() {
        GameState gs = new GameState(0, BOARD, PLAYERS2, BOMBS,
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        for (int i = 0; i < 2; i++) {
//            System.out.println("--- Game State " + gs.ticks() + " ---");
//            GameStatePrinter.printGameState(gs);
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
        }
//        System.out.println("--- Game State " + gs.ticks() + " ---");
//        GameStatePrinter.printGameState(gs);
        assertEquals(2, gs.ticks());
        for(Player p : gs.alivePlayers()){
            if(p.id() == PlayerID.PLAYER_1 || p.id() == PlayerID.PLAYER_2){
                assertEquals(Player.LifeState.State.VULNERABLE, p.lifeState().state());
            }
        }
        gs = gs.next(Collections.emptyMap(), Collections.emptySet());
//        System.out.println("--- Game State " + gs.ticks() + " ---");
//        GameStatePrinter.printGameState(gs);
        assertEquals(3, gs.ticks());
        for(Player p : gs.alivePlayers()){
            if(p.id() == PlayerID.PLAYER_1 || p.id() == PlayerID.PLAYER_2){
                assertEquals(Player.LifeState.State.DYING, p.lifeState().state());
            }
        }
        while(gs.ticks() < 10){
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
//            System.out.println("--- Game State " + gs.ticks() + " ---");
        }
        assertEquals(10, gs.ticks());
        assertEquals(4, gs.alivePlayers().size());
        for(Player p : gs.alivePlayers()){
            if(p.id() == PlayerID.PLAYER_1 || p.id() == PlayerID.PLAYER_2){
                assertEquals(Player.LifeState.State.DYING, p.lifeState().state());
            }
        }
        gs = gs.next(Collections.emptyMap(), Collections.emptySet());
//        System.out.println("--- Game State " + gs.ticks() + " ---");
//        GameStatePrinter.printGameState(gs);
        assertEquals(11, gs.ticks());
        assertEquals(3, gs.alivePlayers().size());
        for(Player p : gs.players()){
            if(p.id() == PlayerID.PLAYER_1){
                assertEquals(Player.LifeState.State.DEAD, p.lifeState().state());
            } else if (p.id() == PlayerID.PLAYER_2){
                assertEquals(Player.LifeState.State.INVULNERABLE, p.lifeState().state());
                assertEquals(2, p.lives());
            }
        }
        while(gs.ticks() < 74){
            gs = gs.next(Collections.emptyMap(), Collections.emptySet());
//            System.out.println("--- Game State " + gs.ticks() + " ---");
        }
        assertEquals(74, gs.ticks());
        for(Player p : gs.players()){
            if (p.id() == PlayerID.PLAYER_2){
                assertEquals(Player.LifeState.State.INVULNERABLE, p.lifeState().state());
            }
        }
        gs = gs.next(Collections.emptyMap(), Collections.emptySet());
//        System.out.println("--- Game State " + gs.ticks() + " ---");
        assertEquals(75, gs.ticks());
        for(Player p : gs.players()){
            if (p.id() == PlayerID.PLAYER_2){
                assertEquals(Player.LifeState.State.VULNERABLE, p.lifeState().state());
                assertEquals(2, p.lives());
            }
        }
    }

}
