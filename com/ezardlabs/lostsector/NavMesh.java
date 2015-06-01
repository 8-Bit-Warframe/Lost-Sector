package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.NavMesh.NavPoint.NavPointType;

import java.util.ArrayList;

public class NavMesh {
	private static NavPoint[][] navPoints;

	static class NavPoint {
		NavPointType type = NavPointType.NONE;
		ArrayList<NavPoint> links = new ArrayList<>();

		public enum NavPointType {
			NONE,
			PLATFORM,
			LEFT_EDGE,
			RIGHT_EDGE,
			SOLO
		}
	}

	public static void init(int[][] solidarityMap) {
		solidarityMap = fillInSolidarityMap(solidarityMap, 0, 0);

		navPoints = new NavPoint[solidarityMap.length][solidarityMap[0].length];
		for (int i = 0; i < solidarityMap.length; i++) {
			for (int j = 0; j < solidarityMap[i].length; j++) {
				if (solidarityMap[i][j] == 0) solidarityMap[i][j] = 1;
				if (solidarityMap[i][j] == 2) solidarityMap[i][j] = 0;
				navPoints[i][j] = new NavPoint();
			}
		}

		for (int y = 0; y < solidarityMap[0].length; y++) {
			boolean platformStarted = false;
			for (int x = 0; x < solidarityMap.length; x++) {
				if (!platformStarted) {
					if (!isCollision(solidarityMap, x, y) && isCollision(solidarityMap, x, y + 1)) {
						navPoints[x][y].type = NavPointType.LEFT_EDGE;
						platformStarted = true;
					}
				}
				if (platformStarted) {
					if (isCollision(solidarityMap, x + 1, y + 1) &&
							!isCollision(solidarityMap, x + 1, y) &&
							navPoints[x][y].type != NavPointType.LEFT_EDGE) {
						navPoints[x][y].type = NavPointType.PLATFORM;
					}
					if (!isCollision(solidarityMap, x + 1, y + 1) ||
							isCollision(solidarityMap, x + 1, y)) {
						if (navPoints[x][y].type == NavPointType.LEFT_EDGE) {
							navPoints[x][y].type = NavPointType.SOLO;
						} else {
							navPoints[x][y].type = NavPointType.RIGHT_EDGE;
						}
						platformStarted = false;
					}
				}
			}
		}
		for (int x = 0; x < navPoints.length; x++) {
			for (int y = 0; y < navPoints[x].length; y++) {
				String imagePath = null;
				switch (navPoints[x][y].type) {
					case NONE:
						break;
					case PLATFORM:
						imagePath = "images/green.png";
						break;
					case LEFT_EDGE:
					case RIGHT_EDGE:
						imagePath = "images/pink.png";
						break;
					case SOLO:
						imagePath = "images/orange.png";
						break;
				}
				if (navPoints[x][y].type != NavPointType.NONE) {
					GameObject.instantiate(
							new GameObject(null, true, new Renderer(imagePath, 100, 100)),
							new Vector2(x * 100, y * 100));
				}
			}
		}
	}

	private static int[][] fillInSolidarityMap(int[][] solidarityMap, int x, int y) {
		if (solidarityMap[x][y] >= 1) return solidarityMap;
		if (solidarityMap[x][y] == 0) solidarityMap[x][y] = 2;
		if (x > 0) solidarityMap = fillInSolidarityMap(solidarityMap, x - 1, y);
		if (x < solidarityMap.length - 2)
			solidarityMap = fillInSolidarityMap(solidarityMap, x + 1, y);
		if (y > 0) solidarityMap = fillInSolidarityMap(solidarityMap, x, y - 1);
		if (y < solidarityMap[0].length - 2)
			solidarityMap = fillInSolidarityMap(solidarityMap, x, y + 1);
		return solidarityMap;
	}

	private static boolean isCollision(int[][] solidarityMap, int x, int y) {
		return x >= solidarityMap.length || y >= solidarityMap[0].length ||
				solidarityMap[x][y] == 1;
	}
}