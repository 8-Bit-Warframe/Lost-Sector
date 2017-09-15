package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.NavMesh.NavPoint.NavPointType;
import com.ezardlabs.lostsector.map.MapManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class NavMesh {
	public static NavPoint[][] navPoints;
	private static int maxIndex = 0;
	private static NavPoint closest;
	private static boolean found = false;

	enum LinkType {
		WALK,
		FALL,
		JUMP
	}

	private static ArrayList<NavPoint> pointsWithAlteredIndices = new ArrayList<>();

	public static class NavPoint {
		public NavPointType type = NavPointType.NONE;
		public final HashSet<Link> links = new HashSet<>();
		public final int id;
		public final Vector2 position;
		private int index = 0;

		static class Link {
			LinkType linkType;
			NavPoint target;

			public Link(LinkType linkType, NavPoint target) {
				this.linkType = linkType;
				this.target = target;
			}
		}

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
			if (found || (this.index > 0 && index >= this.index)) return;
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
			for (Link l : links) {
				if (!(l.linkType == LinkType.FALL && l.target.position.y < position.y) && !(l.linkType == LinkType.JUMP && l.target.position.y > position.y)) {
					if ((l.target.index <= 0 || index + 1 < l.target.index)) {
						toIndex.addLast(l.target);
						indices.addLast(index + 1);
//						indices.addLast(index + (int) (Math.abs(position.x - l.target.position.x) + Math.abs(position.y - l.target.position.y)));
					}
				}
			}
		}

		@Override
		public String toString() {
			String ret = "";
			for (Link l : links) {
				ret += l.target.id + ", ";
			}
			return "NavPoint: id = " + id + ", position = " + position + ", index = " + index + ", links = " + ret;
		}
	}

	private static ArrayDeque<NavPoint> toIndex = new ArrayDeque<>();
	private static ArrayDeque<Integer> indices = new ArrayDeque<>();

	public static NavPoint[] getPath(Transform a, Transform b) {
		return getPath(a.position, b.position);
	}

	public static NavPoint[] getPath(Vector2 a, Vector2 b) {
		return getPath(getNavPoint(a), getNavPoint(b));
	}

	public static NavPoint[] getPath(NavPoint start, NavPoint end) {
		if (start == null || end == null) return new NavPoint[0];

		for (int i = 0; i < pointsWithAlteredIndices.size(); i++) {
			pointsWithAlteredIndices.get(i).index = 0;
		}
		pointsWithAlteredIndices.clear();

		end.index = -1;
		pointsWithAlteredIndices.add(end);

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
				for (NavPoint.Link l : path[i + 1].links) {
					if (l.target.index == i + 1 && !(l.target.position.y > path[i + 1].position.y && l.linkType == LinkType.FALL) && !(l.target.position.y < path[i + 1].position.y && l.linkType == LinkType.JUMP)) {
						path[i] = l.target;
						break;
					}
				}
			}
			return path;
		}
	}

	public static NavPoint getNavPoint(Vector2 position) {
		return getNavPoint(position.x, position.y);
	}

	public static NavPoint getNavPoint(float x, float y) {
		return navPoints[Math.round(x / 100f)][Math.round(y / 100f) + 1];
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

		// Walk links
		for (int y = 0; y < solidityMap[0].length; y++) {
			boolean platformStarted = false;
			for (int x = 0; x < solidityMap.length; x++) {
				if (!platformStarted) {
					if (!isCollision(solidityMap, x, y) && isCollision(solidityMap, x, y + 1) && !isCollision(solidityMap, x, y - 1)) {
						navPoints[x][y].type = NavPointType.LEFT_EDGE;
						platformStarted = true;
					}
				}
				if (platformStarted) {
					if (isCollision(solidityMap, x + 1, y + 1) &&
							!isCollision(solidityMap, x + 1, y) &&
							navPoints[x][y].type != NavPointType.LEFT_EDGE) {
						navPoints[x][y].type = NavPointType.PLATFORM;
						addLink(x, y, x - 1, y, LinkType.WALK);
					}
					if (!isCollision(solidityMap, x + 1, y + 1) || isCollision(solidityMap, x + 1, y)) {
						if (navPoints[x][y].type == NavPointType.LEFT_EDGE) {
							navPoints[x][y].type = NavPointType.SOLO;
						} else {
							navPoints[x][y].type = NavPointType.RIGHT_EDGE;
							addLink(x, y, x - 1, y, LinkType.WALK);
						}
						platformStarted = false;
					}
				}
			}
		}

		// Fall links
		for (int x = 0; x < navPoints.length; x++) {
			for (int y = 0; y < navPoints[x].length; y++) {
				if (navPoints[x][y].type == NavPointType.RIGHT_EDGE || navPoints[x][y].type == NavPointType.SOLO) {
					if (x + 2 < navPoints.length && !isCollision(solidityMap, x + 1, y) && !isCollision(solidityMap, x + 2, y) && !isCollision(solidityMap, x + 1, y - 1) &&
							!isCollision(solidityMap, x + 2, y - 1)) {
						int yTemp = y + 1;
						while (yTemp < navPoints[x].length - 1 && !isCollision(solidityMap, x + 1, yTemp) && !isCollision(solidityMap, x + 2, yTemp)) {
							yTemp++;
						}
						yTemp--;
						if (isCollision(solidityMap, x + 1, yTemp + 1)) {
							addLink(x, y, x + 1, yTemp, LinkType.FALL);
							if (yTemp - (y + 1) < 3) {
								addLink(x, y, x + 1, yTemp, LinkType.JUMP);
							}
						} else {
							addLink(x, y, x + 1, yTemp, LinkType.FALL);
							if (yTemp - (y + 1) < 3) {
								addLink(x, y, x + 1, yTemp, LinkType.JUMP);
							}
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
							addLink(x, y, x - 2, yTemp, LinkType.FALL);
							if (yTemp - (y + 1) < 3) {
								addLink(x, y, x - 2, yTemp, LinkType.JUMP);
							}
						} else {
							addLink(x, y, x - 1, yTemp, LinkType.FALL);
							if (yTemp - (y + 1) < 3) {
								addLink(x, y, x - 1, yTemp, LinkType.JUMP);
							}
						}
					}
				}
			}
		}

		// Jump links
		/*for (int x = 0; x < navPoints.length; x++) {
			for (int y = 0; y < navPoints[x].length; y++) {
				if (navPoints[x][y].type == NavPointType.RIGHT_EDGE || navPoints[x][y].type == NavPointType.SOLO) {
					if (isCollision(solidityMap, x + 1, y)) {
						for (int i = 1; i < 4; i++) {
							if (!isCollision(solidityMap, x + 1, y - i) && !isCollision(solidityMap, x + 1, y - i - 1)) {
//								addLink(x - 1, y, x + 1, y - i, LinkType.JUMP);
								break;
							}
						}
					}
				}
				if (navPoints[x][y].type == NavPointType.LEFT_EDGE || navPoints[x][y].type == NavPointType.SOLO) {
					if (isCollision(solidityMap, x - 1, y)) {
						for (int i = 1; i < 4; i++) {
							if (!isCollision(solidityMap, x - 1, y - i) && !isCollision(solidityMap, x - 1, y - i - 1)) {
//								addLink(x, y, x - 1, y - i, LinkType.JUMP);
								break;
							}
						}
					}
				}
			}
		}*/
	}

	private static void addLink(int x1, int y1, int x2, int y2, LinkType type) {
		navPoints[x1][y1].links.add(new NavPoint.Link(type, navPoints[x2][y2]));
		navPoints[x2][y2].links.add(new NavPoint.Link(type, navPoints[x1][y1]));
	}

	private static int[][] fillInSolidityMap(int[][] solidityMap) {
		if (solidityMap == null) return null;

		Stack<int[]> stack = new Stack<>();
		stack.push(new int[]{(int) MapManager.playerSpawn.x / 100,
				(int) MapManager.playerSpawn.y / 100});

		while (!stack.empty()) {
			int[] pop = stack.pop();
			int x = pop[0];
			int y = pop[1];
			if (solidityMap[x][y] >= 1) continue;
			if (solidityMap[x][y] == 0) solidityMap[x][y] = 2;
			if (x > 0) stack.push(new int[]{x - 1,
					y});
			if (x < solidityMap.length - 2) stack.push(new int[]{x + 1,
					y});
			if (y > 0) stack.push(new int[]{x,
					y - 1});
			if (y < solidityMap[0].length - 2) stack.push(new int[]{x,
					y + 1});
		}
		return solidityMap;
	}

	private static boolean isCollision(int[][] solidity, int x, int y) {
		return x >= solidity.length || x < 0 || y >= solidity[0].length || y < 0 ||
				solidity[x][y] == 1;
	}
}