package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

//TODO comments

public enum Block {
    
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL,
    BONUS_BOMB(Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);
    
    private Bonus bonus;
    
    private Block(Bonus b){
        this.bonus = b;
    }
    
    private Block(){
        this.bonus = null;
    }
    
    public boolean isFree(){
        return this == FREE;
    }
    
    public boolean canHostPlayer(){
        return this.isFree() || this.isBonus();
    }
    
    public boolean castsShadow(){
        return this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL || this == CRUMBLING_WALL;
    }
    
    public boolean isBonus(){
        return this == BONUS_BOMB || this == BONUS_RANGE;
    }
    
    public Bonus associatedBonus() throws NoSuchElementException{
        if(!this.isBonus()){
            throw new NoSuchElementException("There is no Bonus associated with Block: " + this.toString());
        } else {
            return this.bonus;
        }
    }
}