package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

/**
 * Main class of the server of the XBlast game. Communicates with the players
 * and calculates the GameState.
 * 
 * @author Lorenz Rasch (249937)
 */
public class Main {

    private static final int PORT = 2016;

    /**
     * Main method of the XBlast server. Takes number of players as only
     * argument. 4 Players (number of PlayerIDs) as default. Calculates
     * gamestate and sends it to all clients.
     * 
     * @param args
     *            number of players, the only argument
     * @throws IOException
     *             I/O exceptions of the DatagramChannel (send/receive)
     * @throws InterruptedException
     *             exception with Thread.sleep
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {
        // determine number of clients connecting
        int numberOfClients = PlayerID.values().length;
        try {
            numberOfClients = Integer.parseInt(args[0]);
            // too many players specified, reset number of clients to avoid
            // exceptions
            if (numberOfClients > PlayerID.values().length) {
                numberOfClients = PlayerID.values().length;
            }
        } catch (Exception e) {
            // can't parse to int(no int or no entry in args), ignore exception
        }
        // initialize UDP
        DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(PORT));
        ByteBuffer buffer;
        // 1 players joining
        Map<SocketAddress, PlayerID> clients = new HashMap<SocketAddress, PlayerID>();
        while (clients.size() < numberOfClients) {
            // receive JOIN_GAME requests until enough clients connected
            buffer = ByteBuffer.allocate(1);
            SocketAddress client = channel.receive(buffer);
            // check for correct request and uniqueness of client
            if (buffer.get(0) == PlayerAction.JOIN_GAME.ordinal()
                    && !clients.containsKey(client)) {
                clients.put(client, PlayerID.values()[clients.size()]);
            }
        }
        // 2 game
        channel.configureBlocking(false);
        // first Level
        Level lvl = Level.DEFAULT_LEVEL;
        // first gamestate
        GameState gs = lvl.gameState();
        List<Byte> serial;
        // time when game starts
        long startTime = System.nanoTime();

        while (!gs.isGameOver()) {
            // 1 send gamestate
            // set new gamestate
            lvl = lvl.withGameState(gs);
            // serialize gamestate
            serial = GameStateSerializer.serialize(lvl);
            // send gamestate to every client
            for (Map.Entry<SocketAddress, PlayerID> e : clients.entrySet()) {
                buffer = ByteBuffer.allocate(serial.size() + 1);
                buffer.put((byte) e.getValue().ordinal());
                for (Byte b : serial) {
                    // add every byte from serialized gamestate
                    buffer.put(b.byteValue());
                }
                buffer.flip();
                channel.send(buffer, e.getKey());
            }
            // 2 wait for next tick
            long ticks = gs.ticks();
            // wait until next tick, only if elapsed time is shorter than it
            // should be
            if (startTime + (ticks * Ticks.TICK_NANOSECOND_DURATION) > System
                    .nanoTime()) {
                // calculate total wait time
                long totalWaitTime = startTime
                        + (ticks * Ticks.TICK_NANOSECOND_DURATION)
                        - System.nanoTime();
                // calculate milisecond part of wait time
                long waitTimeMilis = totalWaitTime
                        / (Time.NS_PER_S / Time.MS_PER_S);
                // caluculate nanosecond part of wait time
                int waitTimeNanos = (int) (totalWaitTime % (Time.NS_PER_S / Time.MS_PER_S));
                Thread.sleep(waitTimeMilis, waitTimeNanos);
            }
            // listen for events
            Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<PlayerID, Optional<Direction>>();
            Set<PlayerID> bombDropEvents = new HashSet<PlayerID>();
            buffer = ByteBuffer.allocate(1);
            SocketAddress sender = channel.receive(buffer);
            // get all events, until receive returns null
            while (sender != null) {
                // create events according to received byte
                switch (PlayerAction.values()[buffer.get(0)]) {
                case MOVE_N:
                    speedChangeEvents.put(clients.get(sender),
                            Optional.of(Direction.N));
                    break;
                case MOVE_E:
                    speedChangeEvents.put(clients.get(sender),
                            Optional.of(Direction.E));
                    break;
                case MOVE_S:
                    speedChangeEvents.put(clients.get(sender),
                            Optional.of(Direction.S));
                    break;
                case MOVE_W:
                    speedChangeEvents.put(clients.get(sender),
                            Optional.of(Direction.W));
                    break;
                case STOP:
                    speedChangeEvents
                            .put(clients.get(sender), Optional.empty());
                    break;
                case DROP_BOMB:
                    bombDropEvents.add(clients.get(sender));
                    break;
                default:
                    break;
                }
                buffer.clear();
                sender = channel.receive(buffer);
            }
            // 3 claculate next gamestate
            gs = gs.next(speedChangeEvents, bombDropEvents);
        }
        // print winner and exit
        if (gs.winner().isPresent()) {
            System.out.println(gs.winner().get().toString());
        }
        System.exit(1);
    }

}
