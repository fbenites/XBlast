package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

/**
 * Class helping in realizing keyboard inputs.
 * 
 * @author Lorenz Rasch (249937)
 */
public final class KeyboardEventHandler extends KeyAdapter implements
        KeyListener {

    private final Map<Integer, PlayerAction> actions;
    private final Consumer<PlayerAction> consumer;

    /**
     * Gives a new KeyboardEventHandler with the parameters given.
     * 
     * @param actions
     *            mapping of PlayerActions to Integers
     * @param consumer
     *            consumer of PlayerActions
     */
    public KeyboardEventHandler(Map<Integer, PlayerAction> actions,
            Consumer<PlayerAction> consumer) {
        this.actions = Collections
                .unmodifiableMap(new HashMap<Integer, PlayerAction>(actions));
        this.consumer = consumer;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (actions.containsKey(e.getKeyCode())) {
            consumer.accept(actions.get(e.getKeyCode()));
        }
    }

}
