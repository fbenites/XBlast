package ch.epfl.xblast.client;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class GameStateTest {

    @Test(expected = NullPointerException.class)
    public void exceptionTest01() {
        new GameState(null, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void exceptionTest02() {
        new GameState(Collections.emptyList(), null, Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void exceptionTest03() {
        new GameState(Collections.emptyList(), Collections.emptyList(), null,
                Collections.emptyList(), Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void exceptionTest04() {
        new GameState(Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), null, Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void exceptionTest05() {
        new GameState(Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), null);
    }

    @Test
    public void playerTest() {
        GameState gs = GameStateDeserializer.deserialize(GameStateSerializer
                .serialize(Level.DEFAULT_LEVEL));
        assertEquals(PlayerID.values().length, gs.players().size());

        for (int i = 0; i < PlayerID.values().length; i++) {
            assertEquals(PlayerID.values()[i], gs.players().get(i).id());
            assertEquals(3, gs.players().get(i).lives());
        }
        assertEquals(SubCell.centralSubCellOf(new Cell(1, 1)), gs.players()
                .get(0).position());
        assertEquals(SubCell.centralSubCellOf(new Cell(13, 1)), gs.players()
                .get(1).position());
        assertEquals(SubCell.centralSubCellOf(new Cell(13, 11)), gs.players()
                .get(2).position());
        assertEquals(SubCell.centralSubCellOf(new Cell(1, 11)), gs.players()
                .get(3).position());
    }
    
    @Test
    public void general(){
        GameState gs = GameStateDeserializer.deserialize(GameStateSerializer.serialize(Level.DEFAULT_LEVEL));
        assertEquals(PlayerID.values().length, gs.players().size());
        assertEquals(Cell.COUNT, gs.board().size());
        assertEquals(Cell.COUNT, gs.explosion().size());
        assertEquals(60, gs.time().size());
        assertEquals(20, gs.scores().size());
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void listExceptionTest01(){
        GameState gs = GameStateDeserializer.deserialize(GameStateSerializer
                .serialize(Level.DEFAULT_LEVEL));
        gs.players().addAll(Collections.emptyList());
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void listExceptionTest02(){
        GameState gs = GameStateDeserializer.deserialize(GameStateSerializer
                .serialize(Level.DEFAULT_LEVEL));
        gs.board().addAll(Collections.emptyList());
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void listExceptionTest03(){
        GameState gs = GameStateDeserializer.deserialize(GameStateSerializer
                .serialize(Level.DEFAULT_LEVEL));
        gs.explosion().addAll(Collections.emptyList());
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void listExceptionTest04(){
        GameState gs = GameStateDeserializer.deserialize(GameStateSerializer
                .serialize(Level.DEFAULT_LEVEL));
        gs.scores().addAll(Collections.emptyList());
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void listExceptionTest05(){
        GameState gs = GameStateDeserializer.deserialize(GameStateSerializer
                .serialize(Level.DEFAULT_LEVEL));
        gs.time().addAll(Collections.emptyList());
    }

}
