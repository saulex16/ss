package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.entity.Entity;
import ar.edu.itba.ss.models.entity.SurfaceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SquareGrid<T extends Entity> {

    private final int size;
    private final int numCells;
    private final List<List<Cell<T>>> cells;

    public SquareGrid(int size, int numCells) {
        this.size = size;
        this.numCells = numCells;
        double cellSize = (double) size / numCells;

        this.cells = new ArrayList<>(numCells);
        for (int i = 0; i < numCells; i++) {
            List<Cell<T>> row = new ArrayList<>(numCells);
            for (int j = 0; j < numCells; j++) {
                row.add(new Cell<>(i, j, cellSize));
            }
            cells.add(row);
        }
    }

    public void place(T entity, double x, double y) {
       locate(x, y).place(entity, x, y);
    }

    public void place(SurfaceEntity<T> entity, double x, double y, double r) {
        locate(x,y,r).forEach(c->c.place(entity));
    }

    public void place(SurfaceEntity<T> surfaceEntity) {
        locate(surfaceEntity.getX(), surfaceEntity.getY()).place(surfaceEntity);
    }

    public Cell<T> locate(double pX, double pY) {
        int cellX = (int) Math.min(pX * numCells / size, numCells - 1);
        int cellY = (int) Math.min(pY * numCells / size, numCells - 1);

        return cells.get(cellY).get(cellX);
    }

    public List<Cell<T>> locate(double pX, double pY, double r) {
        int cellX = (int) Math.min(pX * numCells / size, numCells - 1);
        int cellY = (int) Math.min(pY * numCells / size, numCells - 1);

        Cell<T> mainCell = cells.get(cellY).get(cellX);
        List<Cell<T>> list = getNeighbours(mainCell);
        List<Cell<T>> newList = new ArrayList<>();
        for (Cell<T> cell:list) {
            if(cell.intersectCircle(pX,pY,r))
                newList.add(cell);
        }
        newList.add(mainCell);
        return newList;
    }

    public List<Cell<T>> getNeighbours(Cell<T> cell) {
        return getNeighbours(cell, TraversalOffset.EIGHT_NEIGHBOURS);
    }

    public List<Cell<T>> getNeighbours(Cell<T> cell, boolean periodicBoundaryCondition) {
        return getNeighbours(cell, TraversalOffset.EIGHT_NEIGHBOURS);
    }

    public List<Cell<T>> getNeighbours(Cell<T> cell, int[][] offsets) {
        int i = cell.getI();
        int j = cell.getJ();

        List<Cell<T>> neighbours = new ArrayList<>();

        for (int[] offset : offsets) {
            int neighborI = i + offset[0];
            int neighborJ = j + offset[1];
            if (isValidCell(neighborI, neighborJ)) {
                neighbours.add(cells.get(neighborI).get(neighborJ));
            }
        }

        return neighbours;
    }

    public List<Cell<T>> getPeriodicNeighbours(Cell<T> cell) {
        return getPeriodicNeighbours(cell, TraversalOffset.EIGHT_NEIGHBOURS);
    }

    public List<Cell<T>> getPeriodicNeighbours(Cell<T> cell, int[][] offsets) {
        int i = cell.getI();
        int j = cell.getJ();

        List<Cell<T>> neighbours = new ArrayList<>();

        int numRows = cells.size();
        int numCols = cells.get(0).size();

        for (int[] offset : offsets) {
            int neighborI = (i + offset[0] + numRows) % numRows;
            int neighborJ = (j + offset[1] + numCols) % numCols;
            neighbours.add(cells.get(neighborI).get(neighborJ));
        }

        return neighbours;
    }

    public List<List<Cell<T>>> getCells() {
        return cells;
    }

    public void print() {
        for (List<Cell<T>> row : cells) {
            for (Cell<T> cell : row) {
                System.out.print(cell + "\t\t\t\t\t\t\t");
            }
            System.out.println();
        }
    }

    private boolean isValidCell(int i, int j) {
        return i >= 0 && i < cells.size() && j >= 0 && j < cells.get(0).size();
    }
}
