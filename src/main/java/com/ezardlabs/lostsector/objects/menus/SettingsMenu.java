package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

import java.util.HashMap;

public class SettingsMenu extends Script {
	private static final TextureAtlas FONT = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
	private final SettingsTab audio = new SettingsTab();
	private final SettingsTab graphics = new SettingsTab();
	private final SettingsTab controls = new SettingsTab();
	private final GuiText audioText = new GuiText("AUDIO", FONT, 31.25f);
	private final GuiText graphicsText = new GuiText("GRAPHICS", FONT, 31.25f);
	private final GuiText controlsText = new GuiText("CONTROLS", FONT, 31.25f);

	@Override
	public void start() {
		GameObject.instantiate(new GameObject("Settings Background",
				new GuiRenderer("images/menus/settings/base.png", 1200, 1000)), transform.position);

		setupAudioTab();
		setupGraphicsTab();
		setupControlsTab();
	}

	@Override
	public void update() {
		if (Input.getKeyDown(KeyCode.MOUSE_LEFT) && audioText.hitTest(Input.mousePosition)) {
			System.out.println("Showing audio");
			audio.show();
			graphics.hide();
			controls.hide();
		}
		if (Input.getKeyDown(KeyCode.MOUSE_LEFT) && graphicsText.hitTest(Input.mousePosition)) {
			System.out.println("Showing graphics");
			audio.hide();
			graphics.show();
			controls.hide();
		}
		if (Input.getKeyDown(KeyCode.MOUSE_LEFT) && controlsText.hitTest(Input.mousePosition)) {
			System.out.println("Showing controls");
			audio.hide();
			graphics.hide();
			controls.show();
		}
	}

	private void setupAudioTab() {
		GameObject.instantiate(new GameObject("Audio Tab", audioText),
				new Vector2(transform.position.x + 212.5f - audioText.getWidth() / 2f,
						transform.position.y + 77.5f - audioText.getFontSize() / 2));

		audio.addObject(new GameObject("Master Audio Slider",
						new Slider(0, 15, 5, "images/menus/settings/slider_icon_audio_master.png",
								newValue -> {
								})),
				new Vector2(transform.position.x + 206.25f, transform.position.y + 256.25f));
		audio.addObject(new GameObject("Music Audio Slider",
						new Slider(0, 15, 5, "images/menus/settings/slider_icon_audio_music.png",
								newValue -> {
								})),
				new Vector2(transform.position.x + 206.25f, transform.position.y + 456.25f));
		audio.addObject(new GameObject("SFX Audio Slider",
						new Slider(0, 15, 5, "images/menus/settings/slider_icon_audio_sfx.png",
								newValue -> {
								})),
				new Vector2(transform.position.x + 206.25f, transform.position.y + 656.25f));
	}

	private void setupGraphicsTab() {
		GameObject.instantiate(new GameObject("Graphics Tab", graphicsText),
				new Vector2(transform.position.x + 556.25f - graphicsText.getWidth() / 2f,
						transform.position.y + 77.5f - graphicsText.getFontSize() / 2));
	}

	private void setupControlsTab() {
		GameObject.instantiate(new GameObject("Controls Tab", controlsText),
				new Vector2(transform.position.x + 900 - controlsText.getWidth() / 2f,
						transform.position.y + 77.5f - controlsText.getFontSize() / 2));
	}

	private class SettingsTab {
		private HashMap<GameObject, Vector2> objects = new HashMap<>();

		private void addObject(GameObject object, Vector2 position) {
			objects.put(GameObject.instantiate(object, position), position);
		}

		private void show() {
			for (GameObject go : objects.keySet()) {
				go.transform.position.set(objects.get(go));
			}
		}

		private void hide() {
			System.out.println(objects.keySet());
			for (GameObject go : objects.keySet()) {
				go.transform.position.set(10000, 10000);
			}
		}
	}
}
