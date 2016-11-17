package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

public class Menu extends Script {
	private final String[] options;
	private final MenuAction[] actions;
	private final Vector2 offset;

	private TextureAtlas font;

	private GameObject background;
	private GameObject[] texts;
	private GuiText[] guiTexts;

	private boolean startOpen = false;
	private boolean open = false;

	public Menu(String[] options, MenuAction[] actions) {
		this(options, actions, new Vector2(), false);
	}

	public Menu(String[] options, MenuAction[] actions, boolean open) {
		this(options, actions, new Vector2(), open);
	}

	public Menu(String[] options, MenuAction[] actions, Vector2 offset) {
		this(options, actions, offset, false);
	}

	public Menu(String[] options, MenuAction[] actions, Vector2 offset, boolean open) {
		this.options = options;
		this.actions = actions;
		this.offset = offset;
		this.startOpen = open;

		if (options.length < 1 || options.length > 4) {
			throw new Error("You must supply between 1 and 4 options to the menu");
		}
		font = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
	}

	@Override
	public void start() {
		float height = (2 * options.length + 1) * 50;
		background = new GameObject("Menu",
				new GuiRenderer("images/menus/main/menu" + options.length + ".png", 400, height));
		background = GameObject.instantiate(background, new Vector2(-10000, -10000));
		texts = new GameObject[options.length];
		guiTexts = new GuiText[options.length];
		for (int i = 0; i < texts.length; i++) {
			guiTexts[i] = new GuiText(options[i], font, 50, 1);
			texts[i] = GameObject.instantiate(new GameObject("Menu Option", guiTexts[i]),
					new Vector2(-10000, -10000));
		}
	}

	@Override
	public void update() {
		if (startOpen) {
			open();
			startOpen = false;
		}
		if (open && guiTexts != null && Input.getKeyDown(KeyCode.MOUSE_LEFT)) {
			for (int i = 0; i < guiTexts.length; i++) {
				if (guiTexts[i].hitTest(Input.mousePosition)) {
					actions[i].onMenuItemSelected(this, i, options[i]);
					return;
				}
			}
		}
	}

	public void open() {
		open = true;
		float height = (2 * options.length + 1) * 50;
		background.transform.position.set(1920 / 2 - 200 + offset.x,
				1080 / 2 - ((2 * options.length + 1) * 50) / 2 + offset.y);
		for (int i = 0; i < texts.length; i++) {
			texts[i].transform.position.set(1920 / 2f - guiTexts[i].getWidth() / 2f + offset.x,
					1080 / 2 - height / 2 + (1 + i * 2) * 50 + offset.y);
		}
	}

	public void close() {
		open = false;
		// TODO implement GameObject/Component enabling/disabling in Dethsquare so this is neater
		background.transform.position.set(-10000, -10000);
		for (GameObject text : texts) {
			text.transform.position.set(-10000, -10000);
		}
	}

	public boolean isOpen() {
		return open;
	}

	public interface MenuAction {
		void onMenuItemSelected(Menu menu, int index, String text);
	}
}
