package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

//TODO comments

public final class Player {
    
    private final PlayerID id;
    private final Sq<LifeState> ls;
    private final Sq<DirectedPosition> dPos;
    private final int maxB, bRange;

    //if maxBombs/bombRange over 9 leave it be
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) throws IllegalArgumentException, NullPointerException{
        this.id = Objects.requireNonNull(id, "Given PlayerID is null.");
        this.ls = Objects.requireNonNull(lifeStates, "Given LifeState is null.");
        this.dPos = Objects.requireNonNull(directedPos, "Given DirectedPosition is null.");
        this.maxB = ArgumentChecker.requireNonNegative(maxBombs);
        this.bRange = ArgumentChecker.requireNonNegative(bombRange);
    }
    
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) throws IllegalArgumentException, NullPointerException{
        this(id, Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, LifeState.State.INVULNERABLE)).concat(Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE))), Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)), maxBombs, bombRange);
        //TODO create method to create LiveStates, used here and in statesForNextLife
    }
    
    public PlayerID id(){
        return id;
    }
    
    public Sq<LifeState> lifeStates(){
        return ls;
    }
    
    public LifeState lifeState(){
        return this.lifeStates().head();
    }
    
    public Sq<LifeState> statesForNextLife(){
        if(this.lives() == 1){
            return Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(1, LifeState.State.DYING)).concat(Sq.constant(new LifeState(0, LifeState.State.DEAD)));
        } else {
            return Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(this.lives(), LifeState.State.DYING)).concat(Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(this.lives() - 1, LifeState.State.INVULNERABLE))).concat(Sq.constant(new LifeState(this.lives() - 1, LifeState.State.VULNERABLE)));
        }
    }
    
    public int lives(){
        return this.lifeState().lives();
    }
    
    public boolean isAlive(){
        return (this.lives() > 0);
    }
    
    public Sq<DirectedPosition> directedPositions(){
        return this.dPos;
    }
    
    public SubCell position(){
        return this.directedPositions().head().position();
    }
    
    public Direction direction(){
        return this.directedPositions().head().direction();
    }
    
    public int maxBombs(){
        return this.maxB;
    }
    
    public Player withMaxBombs(int newMaxBombs){
        return new Player(this.id, this.ls, this.dPos, newMaxBombs, this.bRange);
    }
    
    public int bombRange(){
        return this.bRange;
    }
    
    public Player withBombRange(int newBombRange){
        return new Player(this.id, this.ls, this.dPos, this.maxB, newBombRange);
    }
    
    public Bomb newBomb(){
        return new Bomb(this.id, this.position().containingCell(), Ticks.BOMB_FUSE_TICKS, this.bRange);
    }
    
    public final static class LifeState{
        
        private final int lives;
        private final State state;
        
        public LifeState(int lives, State state) throws IllegalArgumentException, NullPointerException{
            this.lives = ArgumentChecker.requireNonNegative(lives);
            if(lives == 0){
                this.state = State.DEAD;
            } else {
                this.state = Objects.requireNonNull(state, "Given State is null.");
            }
        }
        
        public int lives(){
            return lives;
        }
        
        public State state(){
            return state;
        }
        
        public boolean canMove(){
            return (state == State.VULNERABLE) || (state == State.INVULNERABLE);
        }
        
        public enum State{
            INVULNERABLE, VULNERABLE, DYING, DEAD; 
        }
        
    }
    
    public final static class DirectedPosition{
        
        private final SubCell pos;
        private final Direction d;
        
        public DirectedPosition(SubCell position, Direction direction) throws NullPointerException{
            this.pos = Objects.requireNonNull(position, "Given position is null.");
            this.d = Objects.requireNonNull(direction, "Given direction is null.");
        }
        
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);
        }
        
        public static Sq<DirectedPosition> moving(DirectedPosition p){
            return Sq.iterate(p, u -> u.withPosition(u.position().neighbor(u.direction())));
        }
        
        public SubCell position(){
            return pos;
        }
        
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition(newPosition, this.direction());
        }
        
        public Direction direction(){
            return d;
        }
        
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition(this.position(), newDirection);
        }
    }

}
