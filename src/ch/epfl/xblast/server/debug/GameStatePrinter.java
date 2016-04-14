package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public final class GameStatePrinter {
    
    private final static String BLACK = "\u001b[30m";
    private final static String RED = "\u001b[31m";
    private final static String GREEN = "\u001b[32m";
    private final static String YELLOW = "\u001b[33m";
    private final static String BLUE = "\u001b[34m";
    private final static String MAGENTA = "\u001b[35m";
    private final static String CYAN = "\u001b[36m";
    private final static String WHITE = "\u001b[37m";
    private final static String STD = "\u001b[m";
    
    private GameStatePrinter() {
    }

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Board board = s.board();

        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                for (Player p : ps) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(stringForPlayer(p));
                        continue xLoop;
                    }
                }
                if (s.bombedCells().containsKey(c)) {
//                    System.out.print(BLUE + "óó" + STD);
                    System.out.print("óó");
                    continue xLoop;
                } else if (board.blockAt(c).castsShadow()) {
                    Block b = board.blockAt(c);
                    System.out.print(stringForBlock(b));
                } else if (s.blastedCells().contains(c)) {
                    System.out.print("OO");
                    continue xLoop;
                } else {
                    Block b = board.blockAt(c);
                    System.out.print(stringForBlock(b));
                }
            }
        System.out.print("  ");
        stringForLine(y, s);
        System.out.println();
        }
    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
//        b.append(CYAN);
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N:
            b.append('^');
            break;
        case E:
            b.append('>');
            break;
        case S:
            b.append('v');
            break;
        case W:
            b.append('<');
            break;
        }
//        b.append(STD);
        return b.toString();
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE:
            return "  ";
        case INDESTRUCTIBLE_WALL:
//            return BLACK + "##" + STD;
            return "##";
        case DESTRUCTIBLE_WALL:
//            return BLACK + "??" + STD;
            return "??";
        case CRUMBLING_WALL:
//            return BLACK + "¿¿" + STD;
            return "¿¿";
        case BONUS_BOMB:
//            return RED + "+b" + STD;
            return "+b";
        case BONUS_RANGE:
//            return RED + "+r" + STD;
            return "+r";
        default:
            throw new Error();
        }
    }

    private static String stringForLine(int y, GameState s) {
        List<Player> players = new ArrayList<Player>(s.players());
        Collections.sort(players, (p1, p2) -> Integer.compare(p1.id().ordinal(), p2.id().ordinal()));
        switch(y){
        case 0:
            System.out.print("P1 : ");
            System.out.print(players.get(0).lives() + " lives (");
            System.out.print(players.get(0).lifeState().state() + ")");
            break;
        case 1:
            System.out.print("     ");
            System.out.print("max bombs : " + players.get(0).maxBombs());
            System.out.print(", range : " + players.get(0).bombRange());
            break;
        case 2:
            System.out.print("     ");
            System.out.print("position : " + players.get(0).position().containingCell());
            break;
        case 3:
            System.out.print("P2 : ");
            System.out.print(players.get(1).lives() + " lives (");
            System.out.print(players.get(1).lifeState().state() + ")");
            break;
        case 4:
            System.out.print("     ");
            System.out.print("max bombs : " + players.get(1).maxBombs());
            System.out.print(", range : " + players.get(1).bombRange());
            break;
        case 5:
            System.out.print("     ");
            System.out.print("position : " + players.get(1).position().containingCell());
            break;
        case 6:
            System.out.print("P3 : ");
            System.out.print(players.get(2).lives() + " lives (");
            System.out.print(players.get(2).lifeState().state() + ")");
            break;
        case 7:
            System.out.print("     ");
            System.out.print("max bombs : " + players.get(2).maxBombs());
            System.out.print(", range : " + players.get(2).bombRange());
            break;
        case 8:
            System.out.print("     ");
            System.out.print("position : " + players.get(2).position().containingCell());
            break;
        case 9:
            System.out.print("P4 : ");
            System.out.print(players.get(3).lives() + " lives (");
            System.out.print(players.get(3).lifeState().state() + ")");
            break;
        case 10:
            System.out.print("     ");
            System.out.print("max bombs : " + players.get(3).maxBombs());
            System.out.print(", range : " + players.get(3).bombRange());
            break;
        case 11:
            System.out.print("     ");
            System.out.print("position : " + players.get(3).position().containingCell());
            break;
        case 12:
            System.out.print("remaining time : " + s.remainingTime());
            
        }
        return "";
    }
}
