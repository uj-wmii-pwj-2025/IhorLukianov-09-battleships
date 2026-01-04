package org.example;

import java.util.LinkedList;

public class MyEngine {
	public static final int DIM = 10;
	private static final int[][] DIR = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

	public static final char SHIP = '#';
	public static final char EMPTY = '.';
	public static final char MISS = '~';
	public static final char HIT = '@';

	public enum HitResult {
		Miss,
		Hit,
		Destroy,
		End
	}

	private final char[][] map;
	private final int[][] shipId;
	private int ships;
	private final LinkedList<Integer> shipHp;

	public MyEngine(String strmap) {
		map = new char[DIM][DIM];
		shipId = new int[DIM][DIM];
		shipHp = new LinkedList<>();

		for (int i = 0; i < DIM; i++) {
			for (int ii = 0; ii < DIM; ii++) {
				shipId[i][ii] = -1;

				if (strmap.charAt(i * DIM + ii) == '.') {
					map[i][ii] = EMPTY;
				}
				else {
					map[i][ii] = SHIP;
				}
			}
		}

		for (int i = 0; i < DIM; i++) {
			for (int ii = 0; ii < DIM; ii++) {
				if (shipId[i][ii] == -1 && map[i][ii] == '#') {
					shipHp.add(0);
					parseShip(i, ii, shipHp.size() - 1);
				}
			}
		}

		ships = shipHp.size();
	}

	private void parseShip(int row, int col, int id) {
		shipId[row][col] = id;
		shipHp.set(id, shipHp.get(id) + 1);

		for (int[] dir: DIR) {
			int r = row + dir[0];
			int c = col + dir[1];

			if (0 <= r && r < DIM && 0 <= c && c < DIM && map[r][c] == '#' && shipId[r][c] == -1) {
				parseShip(r, c, id);
			}
		}
	}

	public char get(int row, int col) {
		return map[row][col];
	}

	public HitResult hit(int row, int col) {
		int id = shipId[row][col];
		if (id == -1) {
			map[row][col] = MISS;
			return HitResult.Miss;
		}

		int hp = shipHp.get(id);
		map[row][col] = HIT;
		if (hp == 0) {
			return HitResult.Destroy;
		}
		else {
			hp--;
			shipHp.set(id, hp);
			if (hp == 0) {
				if (--ships == 0) {
					return HitResult.End;
				}
				else {
					return HitResult.Destroy;
				}
			}
			return HitResult.Hit;
		}
	}
}
