package com.ezardlabs.lostsector.objects.hud;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.graphics.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

public class StatusIndicator {
	private TextureAtlas ta;
	private String warframeName = "";
	private GuiText warframeNameGuiText;

	private GuiRenderer[] health = new GuiRenderer[15];
	private GuiRenderer[] energy = new GuiRenderer[15];
	private GuiRenderer[] shield = new GuiRenderer[3];

	void init(TextureAtlas ta) {
		this.ta = ta;

		float left = (Screen.width - 416 * Screen.scale) / Screen.scale;
		float top = 12;

		GameObject.instantiate(new GameObject("Status Indicator",
						new GuiRenderer(ta, ta.getSprite("base"), 416, 98/*97.5*/)),
				new Vector2(left, top));

		GameObject.instantiate(new GameObject("Warframe Name",
						warframeNameGuiText = new GuiText(null,
								TextureAtlas.load("fonts/atlas" + ".png", "fonts/atlas.txt"), 20)),
				new Vector2(left + 302.25f, top + 16.25f));

		for (int i = 0; i < health.length; i++) {
			GameObject.instantiate(new GameObject("Health Indicator " + i,
							health[i] = new GuiRenderer(ta, ta.getSprite("health"), 20, 13)),
					new Vector2(left + 52 + (i * 13), top + 13));
		}

		for (int i = 0; i < energy.length; i++) {
			GameObject.instantiate(new GameObject("Energy Indicator " + i,
							energy[i] = new GuiRenderer(ta, ta.getSprite("energy"), 20, 13)),
					new Vector2(left + 52 + (i * 13), top + 58.5f));
		}

		for (int i = 0; i < shield.length; i++) {
			GameObject.instantiate(new GameObject("Shield Indicator " + i,
							shield[i] = new GuiRenderer(ta, ta.getSprite("shield"), 20, 13)),
					new Vector2(left + 52 + (i * 65), top + 35.75f));
		}
	}

	public void update(int health, int shield, int energy) {
		setHealth(health);
		setShield(shield);
		setEnergy(energy);
	}

	private void setHealth(int health) {
		for (int i = 0; i < this.health.length; i++) {
			if (health > 0 && health >= i + 1) {
				this.health[i].setSize(20, 13);
			} else {
				this.health[i].setSize(0, 0);
			}
		}
	}

	private void setShield(int shield) {
		for (int i = 0; i < this.shield.length; i++) {
			if (shield > 0 && shield >= i + 1) {
				this.shield[i].setSize(72, 13);
			} else {
				this.shield[i].setSize(0, 0);
			}
		}
	}

	private void setEnergy(int energy) {
		for (int i = 0; i < this.energy.length; i++) {
			if (energy > 0 && energy / 10 >= i + 1) {
				this.energy[i].setSize(13, 7);
			} else {
				this.energy[i].setSize(0, 0);
			}
		}
	}

	void setWarframeName(String warframeName) {
		if (this.warframeName != null && !this.warframeName.equals(warframeName)) {
			this.warframeName = warframeName;
			warframeNameGuiText.setText(warframeName);
		}
	}

	public void spawnGravestone(Vector2 position) {
		GameObject.instantiate(new GameObject("Tombstone",
						new Renderer(ta, ta.getSprite("gravestone"), 200, 200)),
				position.offset(0, 25));
	}
}
