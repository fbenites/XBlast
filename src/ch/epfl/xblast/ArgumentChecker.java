package ch.epfl.xblast;

//TODO comments

public final class ArgumentChecker {
    
    private ArgumentChecker(){}
    
    public static int requireNonNegative(int value) throws IllegalArgumentException{
        if(value >= 0){
            return value;
        } else {
            throw new IllegalArgumentException("The given value is smaller than zero. (" + value + ")");
        }
    }

}
