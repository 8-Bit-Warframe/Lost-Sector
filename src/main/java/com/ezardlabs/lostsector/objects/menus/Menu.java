package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

public class Menu extends Script {
	private final String[] options;
	private final MenuAction[] actions;

	private TextureAtlas font;

	private GameObject gameObject;
	private GameObject[] texts;
	private GuiText[] guiTexts;

	public Menu(String[] options, MenuAction[] actions) {
		this.options = options;
		this.actions = actions;

		if (options.length < 1 || options.length > 4) {
			throw new Error("You must supply between 1 and 4 options to the menu");
		}
		font = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
	}

	@Override
	public void update() {
		if (guiTexts != null && Input.getKeyDown(KeyCode.MOUSE_LEFT)) {
			System.out.println(guiTexts.length);
			for (int i = 0; i < guiTexts.length; i++) {
				if (guiTexts[i].hitTest(Input.mousePosition)) {
					actions[i].onMenuItemSelected(this, i, options[i]);
					return;
				}
			}
		}
	}

	public void open() {
		float height = (2 * options.length + 1) * 50;
		gameObject = new GameObject("Menu",
				new Renderer("images/menus/main/menu" + options.length + ".png", 400, height));
		GameObject.instantiate(gameObject, new Vector2(Screen.width / 2 - 200,
				Screen.height / 2 - ((2 * options.length + 1) * 50) / 2));
		texts = new GameObject[options.length];
		guiTexts = new GuiText[options.length];
		for (int i = 0; i < texts.length; i++) {
			guiTexts[i] = new GuiText(options[i], font, 50);
			texts[i] = GameObject.instantiate(new GameObject("Menu Option", guiTexts[i]),
					new Vector2(Screen.width / 2f - guiTexts[i].getWidth() / 2f,
							Screen.height / 2 - height / 2 + (1 + i * 2) * 50));
		}
	}

	public void close() {
		for (int i = 0; i < texts.length; i++) {
			GameObject.destroy(texts[i]);
		}
		GameObject.destroy(gameObject);

		gameObject = null;
		texts = null;
		guiTexts = null;
	}

	public static abstract class MenuAction {
		public abstract void onMenuItemSelected(Menu menu, int index, String text);
	}
}
