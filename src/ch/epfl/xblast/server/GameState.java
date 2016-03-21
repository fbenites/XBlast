package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

//TODO comments

public final class GameState {
    
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
    
    public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions, List<Sq<Cell>> blasts) throws IllegalArgumentException, NullPointerException{
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        this.board = Objects.requireNonNull(board, "Given board is null.");
        this.players = Collections.unmodifiableList(Objects.requireNonNull(players, "Given player list is null."));
        this.bombs = Collections.unmodifiableList(Objects.requireNonNull(bombs, "Given bomb list is null."));
        this.explosions = Collections.unmodifiableList(Objects.requireNonNull(explosions, "Given explosions list is null."));
        this.blasts = Collections.unmodifiableList(Objects.requireNonNull(blasts, "Given blasts list is null."));
        if(this.players.size() != 4){
            throw new IllegalArgumentException("Game must have 4 players. (" + this.players.size() + ")");
        }
    }
    
    public GameState(Board board, List<Player> players) throws IllegalArgumentException, NullPointerException{
        this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }
    
    public int ticks(){
        return this.ticks;
    }
    
    public boolean isGameOver(){
        return this.ticks > Ticks.TOTAL_TICKS || this.alivePlayers().size() <= 1;
    }
    
    public double remainingTime(){
        return ((double) Ticks.TOTAL_TICKS - this.ticks()) / Ticks.TICKS_PER_SECOND;
    }
    
    public Optional<PlayerID> winner(){
        if(this.isGameOver() && (this.alivePlayers().size() == 1)){
            return Optional.of(this.alivePlayers().get(0).id());
        } else {
            return Optional.empty();
        }
    }
    
    public Board board(){
        return this.board;
    }
    
    public List<Player> players(){
        return this.players;
    }
    
    public List<Player> alivePlayers(){
        List<Player> list = new ArrayList<Player>();
        for(Player p : this.players()){
            if(p.isAlive()){
                list.add(p);
            }
        }
        //FIXME lambda expression
        //shorter with lambda expression
        return Collections.unmodifiableList(list);
    }
    
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> blasts1 = new ArrayList<Sq<Cell>>();
        for(Sq<Cell> blast0 : blasts0){
            if(!blast0.tail().isEmpty() && board0.blockAt(blast0.head()).isFree()){
                blasts1.add(blast0.tail());
            }
        }
        for(Sq<Sq<Cell>> explosion0 : explosions0){
            blasts1.add(explosion0.head());
        }
        return Collections.unmodifiableList(blasts1);
    }

}
