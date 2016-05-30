package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

/**
 * Main class of the client of the XBlast game. Communicates with the server and
 * displays the gamestate.
 * 
 * @author Lorenz Rasch (249937)
 */
public class Main {

    private static final int PORT = 2016;
    private static final int MAX_BUFFER = 410;

    private static XBlastComponent xbc;
    private static DatagramChannel channel;
    private static ByteBuffer bufferSend, bufferReceive;
    private static SocketAddress serverAddress;

    /**
     * Method that creates the UI in the Event Dispatching Thread.
     */
    private static void createUI() {
        // create window and component
        JFrame w = new JFrame();
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        xbc = new XBlastComponent();

        // set keyboard bindings and consumer
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        Consumer<PlayerAction> c = (pa) -> {
            bufferSend = ByteBuffer.allocate(1);
            bufferSend.put((byte) pa.ordinal()).flip();
            try {
                channel.configureBlocking(false);
                channel.send(bufferSend, serverAddress);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
        xbc.addKeyListener(new KeyboardEventHandler(kb, c));
        // add component to window, display
        w.getContentPane().add(xbc);
        w.pack();
        w.setVisible(true);

        xbc.requestFocusInWindow();
    }

    /**
     * Main method of the XBlast client. Takes host address as single argument.
     * Attempts to connect to server. Then receives gamestates and sends player
     * actions.
     * 
     * @param args
     *            host address, the only argument
     * @throws IOException
     *             exception of DatagramChannel (send/receive)
     * @throws InvocationTargetException
     *             exception of Swing Thread
     * @throws InterruptedException
     *             exception of Thread.sleep
     */
    public static void main(String[] args) throws IOException,
            InvocationTargetException, InterruptedException {
        // get host address or set localhost as default
        String server = "localhost";
        if (args.length != 0) {
            server = args[0];
        }
        // 1 send join until first gamestate is received
        // initialize UDP
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        serverAddress = new InetSocketAddress(server, PORT);
        channel.configureBlocking(false);
        // prepare buffer to receive first gamestate
        bufferReceive = ByteBuffer.allocate(MAX_BUFFER);
        while (channel.receive(bufferReceive) == null) {
            // prepare buffer to send join action
            bufferSend = ByteBuffer.allocate(1);
            bufferSend.put((byte) PlayerAction.JOIN_GAME.ordinal()).flip();
            // send join action, wait a little
            channel.send(bufferSend, serverAddress);
            Thread.sleep(Time.MS_PER_S);
        }
        // 2 await further gamestates
        List<Byte> code;
        GameState gs;
        // create UI and EventDispatcherThread
        SwingUtilities.invokeAndWait(() -> createUI());
        while (true) {
            code = new ArrayList<Byte>();
            // prepare buffer for reading
            bufferReceive.flip();
            // get PlayerID for this client
            PlayerID p = PlayerID.values()[bufferReceive.get()];
            // get gamestate and deserialize
            while (bufferReceive.hasRemaining()) {
                code.add(bufferReceive.get());
            }
            gs = GameStateDeserializer.deserialize(code);
            // display new gamestate
            xbc.setGameState(gs, p);
            bufferReceive.clear();
            // wait for next gamestate
            channel.configureBlocking(true);
            channel.receive(bufferReceive);
        }
    }

}
