package ch.epfl.xblast.server;

/**
 * Enumeration representing the various Bonuses of XBlast.
 * 
 * @author Lorenz Rasch (249937)
 */
public enum Bonus {

    // the Bonus allowing a player to carry an additional Bomb if possible
    INC_BOMB {
        // maximum amount of bombs
        private static final int MAX_BOMBS = 9;

        @Override
        public Player applyTo(Player player) {
            if (player.maxBombs() < MAX_BOMBS) {
                return player.withMaxBombs(player.maxBombs() + 1);
            } else {
                return player;
            }
        }
    },

    // the Bonus giving a players bombs additional range if possible
    INC_RANGE {
        // maximum length of range
        private static final int MAX_RANGE = 9;

        @Override
        public Player applyTo(Player player) {
            if (player.bombRange() < MAX_RANGE) {
                return player.withBombRange(player.bombRange() + 1);
            } else {
                return player;
            }
        }
    };

    /**
     * Creates a player with this Bonus applied.
     * 
     * @param player
     *            the player to apply the bonus to
     * @return player with the applied bonus
     */
    abstract public Player applyTo(Player player);

}
