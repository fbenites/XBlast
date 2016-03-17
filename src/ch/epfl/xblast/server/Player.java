package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class Player {
    
    private final PlayerID id;
    private final Sq<LifeState> ls;
    private final Sq<DirectedPosition> dPos;
    private final int maxB, bRange;
    // do i declare throw or only on ArgumentChecker?
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange){
        this.id = Objects.requireNonNull(id);
        this.ls = Objects.requireNonNull(lifeStates);
        this.dPos = Objects.requireNonNull(directedPos);
        this.maxB = ArgumentChecker.requireNonNegative(maxBombs);
        this.bRange = ArgumentChecker.requireNonNegative(bombRange);
    }
    // do i check Arguments or only in primary constructor?
    // 0 lives? see LifeState
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange){
        this(id, Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, LifeState.State.INVULNERABLE)).concat(Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE))), Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)), maxBombs, bombRange);
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
        
        // creating lifestate with 0 lives always gives state dead?
        public LifeState(int lives, State state){
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.state = Objects.requireNonNull(state);
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
        
        public DirectedPosition(SubCell position, Direction direction){
            this.pos = Objects.requireNonNull(position);
            this.d = Objects.requireNonNull(direction);
        }
        
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);
        }
        // infinite sequence? through 0?
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
