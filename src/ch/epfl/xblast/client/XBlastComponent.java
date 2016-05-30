package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * Component class allowing graphical representation of a XBlast game.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class XBlastComponent extends JComponent {

    private final static int WIDTH = 960;
    private final static int HEIGHT = 688;
    private final static int SCORES_Y = 659;
    private final static int[] SCORES_X = { 96, 240, 768, 912 };
    private final static int PLAYER_X_MODIFIER = -24;
    private final static int PLAYER_Y_MODIFIER = -52;

    private GameState gs;
    private Comparator<GameState.Player> c;

    /**
     * Gives a new instance of a XBlastComponent. Initial GameState is set as
     * null, initial comparator by player id.
     */
    public XBlastComponent() {
        gs = null;
        // to avoid exceptions where comparator hasn't been initialized,
        // initialize comparator by playerID
        c = (p1, p2) -> Integer.compare(p1.id().ordinal(), p2.id().ordinal());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        // don't paint anything if there is no gamestate
        if (gs == null) {
            return;
        }
        Graphics2D g = (Graphics2D) g0;

        // paint board and explosions row for row
        int h = gs.board().get(0).getHeight();
        int w = gs.board().get(0).getWidth();
        for (int y = 0; y < Cell.ROWS; y++) {
            paintRow(g,
                    gs.board()
                            .subList(y * Cell.COLUMNS, (y + 1) * Cell.COLUMNS),
                    h * y, w);
            paintRow(
                    g,
                    gs.explosion().subList(y * Cell.COLUMNS,
                            (y + 1) * Cell.COLUMNS), h * y, w);
        }

        // paint score-board row
        h = h * Cell.ROWS;
        paintRow(g, gs.scores(), h, gs.scores().get(0).getWidth());

        // paint time bar
        h += gs.scores().get(0).getHeight();
        paintRow(g, gs.time(), h, gs.time().get(0).getWidth());

        // paint number of lives and players
        // set font
        Font font = new Font("Arial", Font.BOLD, 25);
        g.setColor(Color.WHITE);
        g.setFont(font);
        // sort players according to comparator; y coordinate then ID
        List<GameState.Player> players = new ArrayList<GameState.Player>(
                gs.players());
        players.sort(c);
        for (GameState.Player p : players) {
            // draw number of lifes according to players id
            g.drawString(Integer.toString(p.lives()),
                    SCORES_X[p.id().ordinal()], SCORES_Y);
            // draw player image
            g.drawImage(p.image(), 4 * p.position().x() + PLAYER_X_MODIFIER, 3
                    * p.position().y() + PLAYER_Y_MODIFIER, null);
        }
    }

    /**
     * This method paints a row of images of same width on the same height.
     * 
     * @param g
     *            the graphics object that paints the images
     * @param img
     *            list of images to paint
     * @param h
     *            height at which to paint the images
     * @param w
     *            width of images
     */
    private static void paintRow(Graphics2D g, List<BufferedImage> img, int h,
            int w) {
        for (int i = 0; i < img.size(); i++) {
            g.drawImage(img.get(i), w * i, h, null);
        }
    }

    /**
     * Allows to change/set the GameState of this XBlastComponent and will
     * repaint it. Specified Player (id) will be painted over others to allow
     * better visibility.
     * 
     * @param state
     *            the new game state
     * @param id
     *            the id of the player for which to paint the gamestate
     */
    public void setGameState(GameState state, PlayerID id) {
        // set new gamestate
        gs = state;
        // set new comparator; first by y coordinate
        c = (p1, p2) -> Integer.compare(p1.position().y(), p2.position().y());
        // then compare by id, offset to make a rotation to put specified ID as
        // last
        c = c.thenComparing((p1, p2) -> Integer.compare(
                Math.floorMod(p1.id().ordinal() - id.ordinal() - 1,
                        PlayerID.values().length),
                Math.floorMod(p2.id().ordinal() - id.ordinal() - 1,
                        PlayerID.values().length)));
        repaint();
    }
}
