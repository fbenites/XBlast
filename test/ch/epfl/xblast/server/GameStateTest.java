package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * @author Lorenz Rasch (249937)
 */
public class GameStateTest {
    
    private static Block __ = Block.FREE;
    private static Block XX = Block.INDESTRUCTIBLE_WALL;
    private static Block xx = Block.DESTRUCTIBLE_WALL;
    
    private static Board BOARD = Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(
                    Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
    
    private static List<Player> PLAYERS = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, 2, new Cell(1,1), 5, 5),
            new Player(PlayerID.PLAYER_2, 3, new Cell(1,11), 6, 6),
            new Player(PlayerID.PLAYER_3, 4, new Cell(13,1), 3, 3),
            new Player(PlayerID.PLAYER_4, 5, new Cell(13,11), 2, 2)
            ));
    private static List<Player> DEAD_PLAYERS = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, 0, new Cell(1,1), 5, 5),
            new Player(PlayerID.PLAYER_2, 0, new Cell(1,11), 6, 6),
            new Player(PlayerID.PLAYER_3, 0, new Cell(13,1), 3, 3),
            new Player(PlayerID.PLAYER_4, 0, new Cell(13,11), 2, 2)
            ));
    
    private static List<Player> WINNER = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, 1, new Cell(1,1), 5, 5),
            new Player(PlayerID.PLAYER_2, 0, new Cell(1,11), 6, 6),
            new Player(PlayerID.PLAYER_3, 0, new Cell(13,1), 3, 3),
            new Player(PlayerID.PLAYER_4, 0, new Cell(13,11), 2, 2)
            ));
    
    private static List<Player> PLAYER = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, 2, new Cell(1,1), 5, 5)));
    
    private static List<Bomb> BOMBS = new ArrayList<Bomb>(Arrays.asList(
            new Bomb(PlayerID.PLAYER_1, new Cell(1,2), 3, 3),
            new Bomb(PlayerID.PLAYER_2, new Cell(2,11), 3, 3)
            ));
    
    private static List<Sq<Cell>> BLASTS = new ArrayList<Sq<Cell>>(Arrays.asList(
            Sq.iterate(new Cell(1,1), u -> u.neighbor(Direction.S)).limit(3),
            Sq.iterate(new Cell(1,1), u -> u.neighbor(Direction.E)).limit(4)
            ));
    
    
    //primary constructor
    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest01() {
        new GameState(-4, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest02() {
        new GameState(1, BOARD, PLAYER, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest03() {
        new GameState(1, null, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest04() {
        new GameState(1, BOARD, null, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest05() {
        new GameState(1, BOARD, PLAYERS, null, new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest06() {
        new GameState(1, BOARD, PLAYERS, new ArrayList<Bomb>(), null, new ArrayList<Sq<Cell>>());
    }

    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest07() {
        new GameState(1, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void constructorExceptionTest08(){
        new GameState(BOARD, PLAYER);
    }
    
    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest09(){
        new GameState(null, PLAYERS);
    }
    
    @Test(expected = NullPointerException.class)
    public void constructorExceptionTest10(){
        new GameState(BOARD, null);
    }
    
    @Test
    public void ticksTest(){
        GameState gs = new GameState(BOARD, PLAYERS);
        assertEquals(0, gs.ticks());
        gs = new GameState(5, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertEquals(5, gs.ticks());
    }
    
    @Test
    public void gameOverTest(){
        GameState gs = new GameState(BOARD, PLAYERS);
        assertFalse(gs.isGameOver());
        gs = new GameState(Ticks.TOTAL_TICKS, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertFalse(gs.isGameOver());
        gs = new GameState(Ticks.TOTAL_TICKS + 1, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertTrue(gs.isGameOver());
        gs = new GameState(100, BOARD, DEAD_PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertTrue(gs.isGameOver());
        gs = new GameState(100, BOARD, WINNER, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertTrue(gs.isGameOver());
    }
    
    @Test
    public void gameTimeTest(){
        GameState gs = new GameState(1200, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertEquals(60.0, gs.remainingTime(), 0.0);
        gs = new GameState(BOARD, PLAYERS);
        assertEquals(120.0, gs.remainingTime(), 0.0);
        gs = new GameState(Ticks.TOTAL_TICKS, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertEquals(0.0, gs.remainingTime(), 0.0);
    }
    
    @Test
    public void winnerTest(){
        GameState gs = new GameState(600, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertEquals(Optional.empty(), gs.winner());
        gs = new GameState(600, BOARD, WINNER, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertEquals(PlayerID.PLAYER_1, gs.winner().get());
        gs = new GameState(2401, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertEquals(Optional.empty(), gs.winner());
    }
    
    @Test
    public void boardTest(){
        GameState gs = new GameState(BOARD, PLAYERS);
        assertEquals(BOARD, gs.board());
        assertEquals(Block.FREE, gs.board().blockAt(new Cell(1,1)));
    }
    
    @Test
    public void playersTest(){
        List<Player> p = new ArrayList<Player>(PLAYERS);
        GameState gs = new GameState(BOARD, p);
        assertEquals(p, gs.players());
        p.remove(2);
        assertFalse(p.equals(gs.players()));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void playersImmutabilityTest(){
        GameState gs = new GameState(BOARD, PLAYERS);
        gs.players().remove(2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void alivePlayersImmutabilityTest(){
        GameState gs = new GameState(1200, BOARD, PLAYERS, BOMBS, new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        gs.alivePlayers().remove(0);
    }
    
    @Test
    public void alivePlayersTest(){
        GameState gs = new GameState(BOARD, PLAYERS);
        assertEquals(PLAYERS, gs.alivePlayers());
        gs = new GameState(BOARD, WINNER);
        assertEquals(1, gs.alivePlayers().size());
        gs = new GameState(BOARD, DEAD_PLAYERS);
        assertEquals(new ArrayList<Player>(), gs.alivePlayers());
    }
    
    @Test
    public void bombedCellsTest(){
        GameState gs = new GameState(1200, BOARD, PLAYERS, BOMBS, new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        assertEquals(2, gs.bombedCells().size());
        assertTrue(gs.bombedCells().keySet().contains(new Cell(1, 2)));
        assertTrue(gs.bombedCells().keySet().contains(new Cell(2, 11)));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void bombedCellsImmutabilityTest(){
        GameState gs = new GameState(1200, BOARD, PLAYERS, BOMBS, new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        gs.bombedCells().put(new Cell(3,3), new Bomb(PlayerID.PLAYER_1, new Cell(3,3), 4, 2));
    }
    
    @Test
    public void blastedCellsTest(){
        GameState gs = new GameState(1200, BOARD, PLAYERS, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), BLASTS);
        assertEquals(1, gs.blastedCells().size());
        assertTrue(gs.blastedCells().contains(new Cell(1,1)));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void blastedCellsImmutabilityTest(){
        GameState gs = new GameState(1200, BOARD, PLAYERS, BOMBS, new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        gs.blastedCells().remove(0);
    }

}
