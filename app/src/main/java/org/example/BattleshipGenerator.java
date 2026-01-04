package org.example;

import java.util.Random;
import java.util.ArrayList;

class BasicBattleshipGenerator implements BattleshipGenerator {
    private final int size = 10;
    // true means that cell is taken
    private final boolean[] map = new boolean[size * size];
    private int genLimit;

    private boolean getMap(int row, int col) {
        return map[row * size + col];
    }

    private void setMap(int row, int col, boolean c) {
        map[row * size + col] = c;
    }

    private boolean isValid(int row, int col) {
        return 0 <= row && row < size &&
               0 <= col && col < size;
    }

    private boolean isFree(int row, int col) {
        if (!isValid(row, col)) {
            return false;
        }

        for (int drow = -1; drow <= 1; drow++) {
            for (int dcol = -1; dcol <= 1; dcol++) {
                if (isValid(row + drow, col + dcol) && getMap(row + drow, col + dcol)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isWithin(int row, int col, ArrayList<int[]> ship) {
        for (int[] cell: ship) {
            if (cell[0] == row && cell[1] == col) {
                return true;
            }
        }
        return false;
    }

    private int[] genNextCell(Random r, int[] cell) {
        int choice = r.nextInt(4);

        int[] res = {cell[0], cell[1]};

        res[0] = switch (choice) {
        case 0 -> res[0] + 1;
        case 1 -> res[0] - 1;
            default -> res[0];
        };

        res[1] = switch (choice) {
        case 2 -> res[1] + 1;
        case 3 -> res[1] - 1;
            default -> res[1];
        };

        return res;
    }

    private void decLimit() throws Exception {
        if (genLimit-- < 0) {
            throw new Exception("Abort generating");
        }
    }

    private void attemptGenerate() throws Exception {
        genLimit = 5000;

        for (int i = 0; i < map.length; i++) map[i] = false;
        final Random r = new Random();

        final int shipTypes = 4;
        final int[] shipSize = {4, 3, 2, 1};
        final int[] shipCount = {1, 2, 3, 4};

        for (int type = 0; type < shipTypes; type++) {
            for (int cnt = 0; cnt < shipCount[type]; cnt++) {
                int row = -1, col = -1;

                while (!isFree(row, col)) {
                    row = r.nextInt(size);
                    col = r.nextInt(size);
                    decLimit();
                }

                ArrayList<int[]> shipCells = new ArrayList<>();
                shipCells.add(new int[] {row, col});

                for (int i = 0; i < shipSize[type] - 1; i++) {
                    row = -1;
                    col = -1;

                    while (!isFree(row, col) || isWithin(row, col, shipCells)) {
                        int[] cell = genNextCell(r, shipCells.get(shipCells.size() - 1));
                        row = cell[0];
                        col = cell[1];
                        decLimit();
                    }

                    shipCells.add(new int[] {row, col});
                }

                // yay, successfully made a ship!
                for (int[] cell: shipCells) {
                    setMap(cell[0], cell[1], true);
                }
            }
        }
    }

    public String generateMap() {
        String res = "";

        while (true) {
            try {
                attemptGenerate();
                break;
            }
            catch (Exception e) {
                continue;
            }
        }

        for (int i = 0; i < map.length; i++) {
            res = res + (map[i] ? "#" : ".");
        }

        return res;
    }
}

public interface BattleshipGenerator {

    String generateMap();

    static BattleshipGenerator defaultInstance() {
        return new BasicBattleshipGenerator();
    }

}
