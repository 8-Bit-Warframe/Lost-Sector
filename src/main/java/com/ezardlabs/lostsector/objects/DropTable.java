package com.ezardlabs.lostsector.objects;

import java.util.List;

public class DropTable {
	private final List<String> prefabNames;
	private final List<Float> chances;

	public DropTable(List<String> prefabNames, List<Float> chances) {
		this.prefabNames = prefabNames;
		this.chances = chances;
	}

	public String getDrop() {
		double rand = Math.random();
		float total = 0;
		for (int i = 0; i < chances.size(); i++) {
			if (rand < chances.get(i) + total) {
				return prefabNames.get(i);
			} else {
				total += chances.get(i);
			}
		}
		return null;
	}
}
