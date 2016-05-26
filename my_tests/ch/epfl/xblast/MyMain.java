package ch.epfl.xblast;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;

import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.BlockImage;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.BoardPainter;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

/**
 * Simple test for graphical representation of XBlast. Etape 10
 * 
 * @author Lorenz Rasch (249937)
 */
public class MyMain {

    private static Block __ = Block.FREE;
    private static Block XX = Block.INDESTRUCTIBLE_WALL;
    private static Block xx = Block.DESTRUCTIBLE_WALL;

    private static Board BOARD = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

    private static List<Player> PLAYERS = new ArrayList<Player>(Arrays.asList(
            new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3), new Player(
                    PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3), new Player(
                    PlayerID.PLAYER_3, 3, new Cell(13, 11), 2, 3), new Player(
                    PlayerID.PLAYER_4, 3, new Cell(1, 11), 2, 3)));

    public static void main(String[] args) throws InterruptedException {
        // initialize xblast component
        XBlastComponent xbc = new XBlastComponent();
        // initialize server side gamestate
        ch.epfl.xblast.server.GameState server = new GameState(BOARD, PLAYERS);
        // set keyboard bindings, consumer
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        Consumer<PlayerAction> c = System.out::println;
        // set board images
        Map<Block, BlockImage> m = new HashMap<Block, BlockImage>();
        m.put(Block.FREE, BlockImage.IRON_FLOOR);
        m.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        m.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        m.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        m.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        m.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
        BoardPainter bp = new BoardPainter(m, BlockImage.IRON_FLOOR_S);
        // initialize random events
        RandomEventGenerator r = new RandomEventGenerator(2016, 30, 100);
        // initialize client side gamestate, add to component
        ch.epfl.xblast.client.GameState client = GameStateDeserializer
                .deserialize(GameStateSerializer.serialize(Level.DEFAULT_LEVEL));
        xbc.setGameState(client, PlayerID.PLAYER_1);
        // initialize window, add xblast component
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(xbc);
        f.pack();
        f.setVisible(true);
        xbc.addKeyListener(new KeyboardEventHandler(kb, c));
        // after set visible or it wont work
        xbc.requestFocusInWindow();
        // run game until game is over
        while (!server.isGameOver()) {
            server = server.next(r.randomSpeedChangeEvents(),
                    r.randomBombDropEvents());
            client = GameStateDeserializer.deserialize(GameStateSerializer
                    .serialize(new Level(bp, server)));
            xbc.setGameState(client, PlayerID.PLAYER_1);

            Thread.sleep(50);
        }
        // print winner and exit
        System.out.println(server.winner().get().toString());
        System.exit(1);
    }

}
