package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

//TODO comments

public final class Board {
    private List<Sq<Block>> blocks;
    
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException{
        if(blocks.size() != Cell.COUNT){
            throw new IllegalArgumentException("Specified List of blocks does not contain " + Cell.COUNT + " entries. (" + blocks.size() +")");
        } else {
            this.blocks = Collections.unmodifiableList(new ArrayList<Sq<Block>>(blocks));
        }
    }
    
    public Sq<Block> blocksAt(Cell c){
        return this.blocks.get(c.rowMajorIndex());
    }
    
    public Block blockAt(Cell c){
        return this.blocksAt(c).head();
    }
    
    public static Board ofRows(List<List<Block>> rows) throws IllegalArgumentException{
        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);
        List<Sq<Block>> construct = new ArrayList<Sq<Block>>(Cell.COUNT);
        for(List<Block> row : rows){
            for(Block cell : row){
                construct.add(Sq.constant(cell));
            }
        }
        return new Board(construct);
    }

    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) throws IllegalArgumentException{
        checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);
        List<Block> outerWall = Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL);
        
        List<List<Block>> rows = new ArrayList<List<Block>>(Cell.ROWS);
        rows.add(outerWall);
        List<Block> innerRow;
        for(List<Block> row : innerBlocks){
            innerRow = new ArrayList<Block>(Cell.COLUMNS);
            innerRow.add(Block.INDESTRUCTIBLE_WALL);
            for(Block b : row){
                innerRow.add(b);
            }
            innerRow.add(Block.INDESTRUCTIBLE_WALL);
            rows.add(innerRow);
        }
        rows.add(outerWall);
        return ofRows(rows);
    }
        
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) throws IllegalArgumentException{
        checkBlockMatrix(quadrantNWBlocks, (Cell.ROWS / 2), (Cell.COLUMNS / 2));
        List<List<Block>> innerBlocks = new ArrayList<List<Block>>(Cell.ROWS - 2);
        for(List<Block> row : quadrantNWBlocks){
            innerBlocks.add(Lists.mirrored(row));
        }
        return ofInnerBlocksWalled(Lists.mirrored(innerBlocks));
    }
    
    //TODO check matrix null?
    private static void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns) throws IllegalArgumentException{
        if(matrix.size() != rows){
            throw new IllegalArgumentException("Specified list of rows does not contain " + rows + " entries. (" + matrix.size() + ")");
        } else {
            int count = 0;
            for(List<Block> row : matrix){
                if(row.size() != columns){
                    throw new IllegalArgumentException("Row " + count + " does not contain " + columns + " entries. (" + row.size() + ")");
                }
                count++;
            }
        }
    }

}
