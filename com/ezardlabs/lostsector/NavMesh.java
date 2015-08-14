package com.ezardlabs.lostsector;

import android.util.Log;

import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.NavMesh.NavPoint.NavPointType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class NavMesh {
	public static NavPoint[][] navPoints;
	private static int maxIndex = 0;
	private static NavPoint closest;
	private static boolean found = false;

	private static ArrayList<NavPoint> pointsWithAlteredIndices = new ArrayList<>();

	public static class NavPoint {
		public NavPointType type = NavPointType.NONE;
		public final ArrayList<NavPoint> links = new ArrayList<>();
		public final int id;
		public final Vector2 position;
		private int index = 0;

		public NavPoint(int id, Vector2 position) {
			this.id = id;
			this.position = position;
		}

		public enum NavPointType {
			NONE,
			PLATFORM,
			LEFT_EDGE,
			RIGHT_EDGE,
			SOLO
		}

		void index(int index) {
			if (found) return;
			if (this.index == -1) {
				this.index = index;
				maxIndex = index;
				closest = this;
				found = true;
				pointsWithAlteredIndices.add(this);
				toIndex.clear();
				indices.clear();
				return;
			}
			this.index = index;
			pointsWithAlteredIndices.add(this);
			if (index > maxIndex) {
				maxIndex = index;
				closest = this;
			}
			for (NavPoint np : links) {
				if (np.index <= 0 || np.index > index + 1) {
					toIndex.addFirst(np);
					indices.addFirst(index + 1);
				}
			}
		}

		@Override
		public String toString() {
			String l = "";
			for (NavPoint np : links) {
				l += np.id + ", ";
			}
			return "NavPoint: id = " + id + ", index = " + index + ", links = " + l;
		}
	}

	private static int count = 0;

	public static void init(int[][] solidityMap) {
		solidityMap = fillInSolidityMap(solidityMap);

		navPoints = new NavPoint[solidityMap.length][solidityMap[0].length];
		for (int i = 0; i < solidityMap.length; i++) {
			for (int j = 0; j < solidityMap[i].length; j++) {
				if (solidityMap[i][j] == 0) solidityMap[i][j] = 1;
				if (solidityMap[i][j] == 2) solidityMap[i][j] = 0;
				navPoints[i][j] = new NavPoint(count++, new Vector2(i * 100, j * 100));
			}
		}

		for (int y = 0; y < solidityMap[0].length; y++) {
			boolean platformStarted = false;
			for (int x = 0; x < solidityMap.length; x++) {
				if (!platformStarted) {
					if (!isCollision(solidityMap, x, y) && isCollision(solidityMap, x, y + 1)) {
						navPoints[x][y].type = NavPointType.LEFT_EDGE;
						platformStarted = true;
					}
				}
				if (platformStarted) {
					if (isCollision(solidityMap, x + 1, y + 1) &&
							!isCollision(solidityMap, x + 1, y) &&
							navPoints[x][y].type != NavPointType.LEFT_EDGE) {
						navPoints[x][y].type = NavPointType.PLATFORM;
						navPoints[x][y].links.add(navPoints[x - 1][y]);
						navPoints[x - 1][y].links.add(navPoints[x][y]);
					}
					if (!isCollision(solidityMap, x + 1, y + 1) || isCollision(solidityMap, x + 1, y)) {
						if (navPoints[x][y].type == NavPointType.LEFT_EDGE) {
							navPoints[x][y].type = NavPointType.SOLO;
						} else {
							navPoints[x][y].type = NavPointType.RIGHT_EDGE;
							navPoints[x][y].links.add(navPoints[x - 1][y]);
							navPoints[x - 1][y].links.add(navPoints[x][y]);
						}
						platformStarted = false;
					}
				}
			}
		}

		for (int x = 0; x < navPoints.length; x++) {
			for (int y = 0; y < navPoints[x].length; y++) {
				if (navPoints[x][y].type == NavPointType.RIGHT_EDGE || navPoints[x][y].type == NavPointType.SOLO) {
					if (x + 2 < navPoints.length && !isCollision(solidityMap, x + 1, y) && !isCollision(solidityMap, x + 2, y) && !isCollision(solidityMap, x + 1, y - 1) && !isCollision(solidityMap, x + 2, y - 1)) {
						int yTemp = y + 1;
						while (yTemp < navPoints[x].length - 1 && !isCollision(solidityMap, x + 1, yTemp) && !isCollision(solidityMap, x + 2, yTemp)) {
							yTemp++;
						}
						yTemp--;
						if (isCollision(solidityMap, x + 2, yTemp + 1)) {
							navPoints[x][y].links.add(navPoints[x + 2][yTemp]);
							navPoints[x + 2][yTemp].links.add(navPoints[x][y]);
						} else {
							navPoints[x][y].links.add(navPoints[x + 1][yTemp]);
							navPoints[x + 1][yTemp].links.add(navPoints[x][y]);
						}
					}
				}
				if (navPoints[x][y].type == NavPointType.LEFT_EDGE || navPoints[x][y].type == NavPointType.SOLO) {
					if (x - 2 > 0 && !isCollision(solidityMap, x - 1, y) && !isCollision(solidityMap, x - 2, y) && !isCollision(solidityMap, x - 1, y - 1) && !isCollision(solidityMap, x - 2, y - 1)) {
						int yTemp = y + 1;
						while (yTemp < navPoints[x].length - 1 && !isCollision(solidityMap, x - 1, yTemp) && !isCollision(solidityMap, x - 2, yTemp)) {
							yTemp++;
						}
						yTemp--;
						if (isCollision(solidityMap, x - 2, yTemp + 1)) {
							navPoints[x][y].links.add(navPoints[x - 2][yTemp]);
							navPoints[x - 2][yTemp].links.add(navPoints[x][y]);
						} else {
							navPoints[x][y].links.add(navPoints[x - 1][yTemp]);
							navPoints[x - 1][yTemp].links.add(navPoints[x][y]);
						}
					}
				}
			}
		}
//
//		for (int y = 0; y < navPoints[0].length; y++) {
//			for (int x = 0; x < navPoints.length; x++) {
//				if (navPoints[x][y].type == NavPointType.LEFT_EDGE || navPoints[x][y].type == NavPointType.SOLO) {
//					if (x >= 2) {
//						boolean groundHit = false;
//						int yDepth = y;
//						while(!groundHit) {
//							yDepth++;
//						}
//					}
//				}
//				if (navPoints[x][y].type == NavPointType.RIGHT_EDGE || navPoints[x][y].type == NavPointType.SOLO) {
//
//				}
//			}
//		}
//		for (int x = 0; x < navPoints.length; x++) {
//			for (int y = 0; y < navPoints[x].length; y++) {
//				String imagePath = null;
//				switch (navPoints[x][y].type) {
//					case NONE:
//						break;
//					case PLATFORM:
//						imagePath = "images/green.png";
//						break;
//					case LEFT_EDGE:
//					case RIGHT_EDGE:
//						imagePath = "images/pink.png";
//						break;
//					case SOLO:
//						imagePath = "images/orange.png";
//						break;
//				}
//				if (navPoints[x][y].type != NavPointType.NONE) {
////					GameObject.instantiate(new GameObject("NavMesh", true, new Renderer(imagePath, 100, 100)), new Vector2(x * 100, y * 100));
//				}
//			}
//		}
	}

	private static int[][] fillInSolidityMap(int[][] solidityMap) {
		if (solidityMap == null) return null;

		Stack<int[]> stack = new Stack<>();
		stack.push(new int[]{0, 0});

		while (!stack.empty()) {
			int[] pop = stack.pop();
			int x = pop[0];
			int y = pop[1];
			if (solidityMap[x][y] >= 1) continue;
			if (solidityMap[x][y] == 0) solidityMap[x][y] = 2;
			if (x > 0) stack.push(new int[]{x - 1, y});
			if (x < solidityMap.length - 2) stack.push(new int[]{x + 1, y});
			if (y > 0) stack.push(new int[]{x, y - 1});
			if (y < solidityMap[0].length - 2) stack.push(new int[]{x, y + 1});
		}
		return solidityMap;
	}

	private static boolean isCollision(int[][] solidity, int x, int y) {
		return x >= solidity.length || y >= solidity[0].length ||
				solidity[x][y] == 1;
	}

	private static ArrayDeque<NavPoint> toIndex = new ArrayDeque<>();
	private static ArrayDeque<Integer> indices = new ArrayDeque<>();

	public static NavPoint[] getPath(Transform self, Transform target) {
		NavPoint start = navPoints[(int) (self.position.x / 100f)][(int) (self.position.y / 100f) + 1];
		NavPoint end = navPoints[(int) (target.position.x / 100f)][(int) (target.position.y / 100f) + 1];

		if (start == null || end == null) return null;

		for (int i = 0; i < pointsWithAlteredIndices.size(); i++) {
			pointsWithAlteredIndices.get(i).index = 0;
		}
		pointsWithAlteredIndices.clear();

		end.index = -1;

		maxIndex = 0;

		found = false;

		toIndex.clear();
		indices.clear();

		toIndex.add(start);
		indices.add(1);

		while (!toIndex.isEmpty()) {
			toIndex.pop().index(indices.pop());
		}

		if (closest.id != end.id) {
			return new NavPoint[0];
		}

		NavPoint[] path = new NavPoint[maxIndex];

		if (maxIndex == 0) {
			return path;
		} else if (maxIndex == 1) {
			path[0] = end;
			return path;
		} else {
			path[maxIndex - 1] = end;
			for (int i = maxIndex - 2; i >= 0; i--) {
				for (NavPoint np : path[i + 1].links) {
					if (np.index == i + 1) {
						path[i] = np;
						break;
					}
				}
			}
			return path;
		}
	}
}