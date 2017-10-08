package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Time;
import com.ezardlabs.dethsquare.Vector2;

public class Menu extends Script {
	private final String[] options;
	private final MenuAction[] actions;
	private final Vector2 offset;

	private GameObject[] pieces;
	private GameObject[] texts;
	private GuiText[] guiTexts;
	private GameObject[] highlights;

	private boolean startOpen = false;
	private boolean open = false;

	protected Menu() {
		options = getOptions();
		actions = getActions();
		offset = getOffset();
		startOpen = shouldStartOpen();
	}

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
	}

	@Override
	public void start() {
		runWhenPaused = true;
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
			guiTexts[i] = new GuiText(options[i], TextureAtlas.load("fonts"), 50, 1);
			texts[i] = GameObject.instantiate(new GameObject("Menu Option", guiTexts[i]), new Vector2());
		}

		highlights = new GameObject[options.length];
		for (int i = 0; i < highlights.length - 1; i++) {
			highlights[i] = GameObject.instantiate(
					new GameObject("Menu Highlight", new GuiRenderer("images/menus/main/highlight.png", 156, 66)),
					new Vector2());
		}
		highlights[highlights.length - 1] = GameObject.instantiate(
				new GameObject("Menu Highlight", new GuiRenderer("images/menus/main/highlight_bottom.png", 414, 66)),
				new Vector2());

		float height = (options.length - 1) * 156 + 144;
		for (int i = 0; i < options.length; i++) {
			pieces[i].transform.position.set(960 - 408, 540 - height / 2 + 156 * i);
			highlights[i].transform.position.set(pieces[i].transform.position.offset(i == texts.length - 1 ? 66 : 636,
					i == texts.length - 1 ? 54 : 25));
			texts[i].transform.position.set(
					pieces[i].transform.position.offset(150, 40 + (i == texts.length - 1 ? 28 : 0)));
		}
		if (startOpen) {
			open();
		} else {
			close();
		}
	}

	@Override
	public void update() {
		if (open && guiTexts != null) {
			for (int i = 0; i < guiTexts.length; i++) {
				if (guiTexts[i].hitTest(Input.mousePosition)) {
					highlights[i].setActive(true);
					if (Input.getKeyDown(KeyCode.MOUSE_LEFT)) {
						actions[i].onMenuItemSelected(this, i, options[i]);
					}
				} else {
					highlights[i].setActive(false);
				}
			}
		}
	}

	public void open() {
		open = true;
		for (GameObject piece : pieces) {
			piece.setActive(true);
		}
		for (GameObject highlight : highlights) {
			highlight.setActive(false);
		}
		for (GameObject text : texts) {
			text.setActive(true);
		}
		Time.pause();
		Screen.setCursorVisible(true);
	}

	public void close() {
		open = false;
		for (GameObject piece : pieces) {
			piece.setActive(false);
		}
		for (GameObject highlight : highlights) {
			highlight.setActive(false);
		}
		for (GameObject text : texts) {
			text.setActive(false);
		}
		Time.resume();
		Screen.setCursorVisible(false);
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

	protected String[] getOptions() {
		return new String[0];
	}

	protected MenuAction[] getActions() {
		return new MenuAction[0];
	}

	protected Vector2 getOffset() {
		return new Vector2();
	}

	protected boolean shouldStartOpen() {
		return false;
	}

	public interface MenuAction {
		void onMenuItemSelected(Menu menu, int index, String text);
	}
}
