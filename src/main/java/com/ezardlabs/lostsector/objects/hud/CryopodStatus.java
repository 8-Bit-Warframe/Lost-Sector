package com.ezardlabs.lostsector.objects.hud;

import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.TextureAtlas;

public class CryopodStatus extends Component {
	private GuiText health;
	private GuiText shield;
	private GuiRenderer healthBar;
	private GuiRenderer shieldBar;
	private float lastHealth = 0;
	private float lastShield = 0;

	@Override
	public void start() {
		gameObject.renderer.setDepth(0);
		TextureAtlas numberFont = TextureAtlas.load("fonts/lcd-numbers");
		health = new GuiText("000", numberFont, 22.75f, 3.25f);
		shield = new GuiText("000", numberFont, 22.75f, 3.25f);
		GameObject.instantiate(new GameObject("Health Text", health), transform.position.offset(52, 22.75f));
		GameObject.instantiate(new GameObject("Shield Text", shield), transform.position.offset(139.75f, 22.75f));

		GuiRenderer background = new GuiRenderer("images/white.png", 175.5f, 13);
		background.setTint(25f / 255f, 31f / 255f, 31f / 255f);
		background.setDepth(-2);
		GameObject.instantiate(new GameObject("Cryopod Status Background", background),
				transform.position.offset(16.25f, 48.75f));

		healthBar = new GuiRenderer("images/white.png", 81.25f, 6.5f);
		healthBar.setTint(210f / 255f, 25f / 255f, 0);
		healthBar.setDepth(-1);
		GameObject.instantiate(new GameObject("Health Bar", healthBar), transform.position.offset(19.5f, 52));

		shieldBar = new GuiRenderer("images/white.png", 81.25f, 6.5f);
		shieldBar.setTint(0, 186f / 255f, 1);
		shieldBar.setDepth(-1);
		GameObject.instantiate(new GameObject("Shield Bar", shieldBar), transform.position.offset(107.25f, 52));

		GuiText label = new GuiText("Cryopod", TextureAtlas.load("fonts"), 16.25f, 3.25f);
		GameObject.instantiate(new GameObject("Cryopod Status Label", label),
				transform.position.offset(104 - label.getWidth() / 2, 65));
	}

	public void setData(float health, float maxHealth, int shield, int maxShield) {
		if (health != lastHealth) {
			this.health.setText(getString(health < 0 ? 0 : health));
			healthBar.setSize((health / maxHealth) * 81.25f, 6.5f);
			lastHealth = health;
		}
		if (shield != lastShield) {
			this.shield.setText(getString(shield < 0 ? 0 : shield));
			shieldBar.setSize((((float) shield) / ((float) maxShield)) * 81.25f, 6.5f);
			lastShield = shield;
		}
	}

	private static String getString(float number) {
		StringBuilder sb = new StringBuilder(String.valueOf((int) number));
		while (sb.length() < 3) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}
}
