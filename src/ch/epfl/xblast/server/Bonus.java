package ch.epfl.xblast.server;

//TODO comments

public enum Bonus {
    
    INC_BOMB {
        @Override
        public Player applyTo(Player player){
            if(player.maxBombs() < MAX_BOMBS){
                return player.withMaxBombs(player.maxBombs() + 1);
            } else {
                return player;
            }
        }
    },
    INC_RANGE {
        @Override
        public Player applyTo(Player player){
            if(player.bombRange() < MAX_RANGE){
                return player.withBombRange(player.bombRange() + 1);
            } else {
                return player;
            }
        }
    };
    
    private static int MAX_BOMBS = 9;
    private static int MAX_RANGE = 9;
    
    abstract public Player applyTo(Player player);

}
