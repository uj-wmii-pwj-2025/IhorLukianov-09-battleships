package org.example;

import java.util.LinkedList;

public class EnemyEngine {
	public static final int DIM = 10;
	private static final int[][] DIR = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

	public static final char SHIP = '#';
	public static final char EMPTY = '.';
	public static final char UNKNOWN = '?';

	public enum HitResult {
		Miss,
		Hit,
		Destroy
	}

	private final char[][] map;
	private final boolean[][] destroyed;

	public EnemyEngine() {
		map = new char[DIM][DIM];
		destroyed = new boolean[DIM][DIM];

		for (int i = 0; i < DIM; i++) {
			for (int ii = 0; ii < DIM; ii++) {
				map[i][ii] = UNKNOWN;
				destroyed[i][ii] = false;
			}
		}
	}

	public char get(int row, int col) {
		return map[row][col];
	}

	private void propagateDestroyed(int row, int col) {
		destroyed[row][col] = true;

		for (int[] dir: DIR) {
			int r = row + dir[0];
			int c = col + dir[1];

			if (0 <= r && r < DIM && 0 <= c && c < DIM && map[r][c] == '#' && !destroyed[r][c]) {
				propagateDestroyed(r, c);
			}
		}
	}

	private void uncover() {
		for (int i = 0; i < DIM; i++) {
			for (int ii = 0; ii < DIM; ii++) {
				if (map[i][ii] != UNKNOWN) {
					continue;
				}

				for (int[] dir: DIR) {
					int r = i + dir[0];
					int c = ii + dir[1];

					if (0 <= r && r < DIM && 0 <= c && c < DIM && destroyed[r][c]) {
						map[i][ii] = EMPTY;
						break;
					}
				}
			}
		}
	}

	public void update(int row, int col, HitResult res) {
		if (res == HitResult.Hit) {
			map[row][col] = SHIP;
		}
		else if (res == HitResult.Destroy) {
			map[row][col] = SHIP;
			propagateDestroyed(row, col);
			uncover();
		}
	}
}
