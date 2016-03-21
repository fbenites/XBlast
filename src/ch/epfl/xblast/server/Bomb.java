package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

//TODO comments

public final class Bomb {
    
    private final PlayerID owner;
    private final Cell pos;
    private final Sq<Integer> fuse;
    private final int range;

    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) throws NullPointerException, IllegalArgumentException{
        this.owner = Objects.requireNonNull(ownerId, "Given PlayerID is null.");
        this.pos = Objects.requireNonNull(position, "Given position is null.");
        if(Objects.requireNonNull(fuseLengths, "Given fuse length is null.").isEmpty()){
            throw new IllegalArgumentException("The given fuse sequence is empty.");
        } else {
            this.fuse = fuseLengths;
        }
        this.range = ArgumentChecker.requireNonNegative(range);
    }
    
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) throws NullPointerException, IllegalArgumentException{
        this(ownerId, position, Sq.iterate(ArgumentChecker.requireNonNegative(fuseLength), u -> u - 1).limit(fuseLength), range);
    }
    
    public PlayerID ownerId(){
        return this.owner;
    }
    
    public Cell position(){
        return this.pos;
    }
    
    public Sq<Integer> fuseLengths(){
        return this.fuse;
    }
    
    public int fuseLength(){
        return this.fuse.head();
    }
    
    public int range(){
        return this.range;
    }
    
    public List<Sq<Sq<Cell>>> explosion(){
        List<Sq<Sq<Cell>>> l = new ArrayList<Sq<Sq<Cell>>>();
        for(Direction d : Direction.values()){
            l.add(this.explosionArmTowards(d));
        }
        return l;
    }
    
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir){
        return Sq.repeat(Ticks.EXPLOSION_TICKS, Sq.iterate(this.position(), u -> u.neighbor(dir)).limit(this.range()));
    }
}
