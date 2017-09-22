package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

public class Menu extends Script {
	private final String[] options;
	private final MenuAction[] actions;
	private final Vector2 offset;

	private TextureAtlas font;

	private GameObject[] pieces;
	private GameObject[] texts;
	private GuiText[] guiTexts;

	private boolean startOpen = false;
	private boolean startOpenProcessed = false;
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

	public Menu(String[] options, MenuAction[] actions, Vector2 offset, boolean startOpen) {
		this.options = options;
		this.actions = actions;
		this.offset = offset;
		this.startOpen = startOpen;

		if (options.length < 2 || options.length > 5) {
			throw new IllegalArgumentException("You must supply between 2 and 5 options to the menu");
		}
		font = TextureAtlas.load("fonts/atlas.png", "fonts/atlas.txt");
	}

	@Override
	public void start() {
		pieces = new GameObject[options.length];
		pieces[0] = GameObject.instantiate(
				new GameObject("Menu Piece 0", new GuiRenderer("images/menus/main/top.png", 816, 156)), new Vector2());
		for (int i = 1; i < pieces.length - 1; i++) {
			pieces[i] = GameObject.instantiate(
					new GameObject("Menu Piece " + i, new GuiRenderer("images/menus/main/middle.png", 816, 156)),
					new Vector2());
		}
		pieces[pieces.length - 1] = GameObject.instantiate(new GameObject("Menu Piece " + (pieces.length - 1),
				new GuiRenderer("images/menus/main/bottom.png", 816, 144)), new Vector2());

		texts = new GameObject[options.length];
		guiTexts = new GuiText[options.length];
		for (int i = 0; i < texts.length; i++) {
			guiTexts[i] = new GuiText(options[i], font, 50, 1);
			texts[i] = GameObject.instantiate(new GameObject("Menu Option", guiTexts[i]), new Vector2());
		}

		float height = (options.length - 1) * 156 + 144;
		for (int i = 0; i < options.length; i++) {
			pieces[i].transform.position.set(Screen.width / 2 - 408, Screen.height / 2 - height / 2 + 156 * i);
			texts[i].transform.position.set(
					pieces[i].transform.position.offset(150, 40 + (i == texts.length - 1 ? 28 : 0)));
		}
	}

	@Override
	public void update() {
		if (!startOpenProcessed) {
			if (startOpen) {
				open();
			} else {
				close();
			}
			startOpenProcessed = true;
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
		for (GameObject piece : pieces) {
			piece.setActive(true);
		}
		for (GameObject text : texts) {
			text.setActive(true);
		}
	}

	public void close() {
		open = false;
		for (GameObject piece : pieces) {
			piece.setActive(false);
		}
		for (GameObject text : texts) {
			text.setActive(false);
		}
	}

	public void toggle() {
		if (isOpen()) {
			close();
		} else {
			open();
		}
	}

	public boolean isOpen() {
		return open;
	}

	public interface MenuAction {
		void onMenuItemSelected(Menu menu, int index, String text);
	}
}
