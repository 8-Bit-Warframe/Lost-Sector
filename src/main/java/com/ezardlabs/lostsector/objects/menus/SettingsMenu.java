package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.AudioManager;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Time;
import com.ezardlabs.dethsquare.Vector2;

public class SettingsMenu extends Script {
	private static final String AUDIO_MASTER_VOLUME = "audio_master_volume";
	private static final String AUDIO_MUSIC_VOLUME = "audio_music_volume";
	private static final String AUDIO_SFX_VOLUME = "audio_sfx_volume";
	private final TextureAtlas FONT = TextureAtlas.load("fonts");
	private final BackButtonAction backButtonAction;
	private final GuiText menuText = new GuiText("MENU", FONT, 31.25f);

	SettingsMenu(BackButtonAction backButtonAction) {
		this.backButtonAction = backButtonAction;
	}

	@Override
	public void start() {
		runWhenPaused = true;
		GameObject.instantiate(
				new GameObject("Settings Background", new GuiRenderer("images/menus/settings/base.png", 1200, 1000)),
				transform.position).transform.setParent(transform);

		GameObject.instantiate(
				new GameObject("Menu Button", new GuiRenderer("images/menus/settings/menu.png", 575, 118.75f)),
				new Vector2(transform.position.offset(550, 881.25f))).transform.setParent(transform);

		GameObject.instantiate(new GameObject("Menu Button Text", menuText), new Vector2(
				transform.position.offset(837.5f - menuText.getWidth() / 2,
						943.75f - menuText.getFontSize() / 2))).transform.setParent(transform);

		GameObject.instantiate(new GameObject("Settings Tab Manager",
						new SettingsTabManager(new AudioSettingsTab(), new GraphicsSettingsTab(), new ControlsSettingsTab())),
				new Vector2(transform.position)).transform.setParent(transform);
		gameObject.setActive(false);
	}

	@Override
	public void update() {
		if (Input.getKeyDown(KeyCode.MOUSE_LEFT) && menuText.hitTest(Input.mousePosition)) {
			backButtonAction.onBackButtonClicked(this);
		}
		Time.pause();
	}

	private class SettingsTabManager extends Script {
		private final SettingsTab[] tabs;
		private final GuiText[] titles = new GuiText[3];
		private final GameObject[] roots = new GameObject[3];

		private SettingsTabManager(SettingsTab tab1, SettingsTab tab2, SettingsTab tab3) {
			this.tabs = new SettingsTab[]{
					tab1,
					tab2,
					tab3
			};
		}

		@Override
		public void start() {
			runWhenPaused = true;
			for (int i = 0; i < tabs.length; i++) {
				titles[i] = new GuiText(tabs[i].getTitle(), FONT, 31.25f);
				GameObject.instantiate(new GameObject(null, titles[i]),
						transform.position.offset(212.5f + (i * 343.75f) - (titles[i].getWidth() / 2f),
								77.5f - (titles[i].getFontSize() / 2))).transform.setParent(transform);
				roots[i] = tabs[i].getRootObject();
				roots[i].transform.setParent(transform);
				if (i > 0) {
					roots[i].setActive(false);
				}
				GameObject.instantiate(new GameObject("Circle " + i,
								new GuiRenderer("images/menus/settings/circle" + i + ".png", 81.25f, 81.25f)),
						transform.position.offset(1106.25f, 125)).transform.setParent(roots[i].transform);
			}
		}

		@Override
		public void update() {
			if (Input.getKeyDown(KeyCode.MOUSE_LEFT)) {
				boolean hit = false;
				int index = -1;
				for (int i = 0; i < titles.length; i++) {
					if (titles[i].hitTest(Input.mousePosition)) {
						hit = true;
						index = i;
						break;
					}
				}
				if (hit) {
					for (int i = 0; i < titles.length; i++) {
						roots[i].setActive(i == index);
					}
				}
			}
		}
	}

	private interface SettingsTab {
		String getTitle();

		GameObject getRootObject();
	}

	private class AudioSettingsTab implements SettingsTab {

		@Override
		public String getTitle() {
			return "AUDIO";
		}

		@Override
		public GameObject getRootObject() {
			GameObject root = GameObject.instantiate(new GameObject(), new Vector2());
			GameObject.instantiate(new GameObject("Master Audio Slider",
							new Slider(0, 15, 5, "MASTER", "images/menus/settings/slider_icon_audio_master" + ".png",
									AUDIO_MASTER_VOLUME, newValue -> AudioManager.setMasterVolume(newValue / 15f))),
					new Vector2(transform.position.x + 206.25f, transform.position.y + 256.25f)).transform.setParent(
					root.transform);
			GameObject.instantiate(new GameObject("Music Audio Slider",
							new Slider(0, 15, 5, "MUSIC", "images/menus/settings/slider_icon_audio_music" + ".png",
									AUDIO_MUSIC_VOLUME, newValue -> AudioManager.setMusicVolume(newValue / 15f))),
					new Vector2(transform.position.x + 206.25f, transform.position.y + 456.25f)).transform.setParent(
					root.transform);
			GameObject.instantiate(new GameObject("SFX Audio Slider",
							new Slider(0, 15, 5, "SOUND EFFECTS", "images/menus/settings/slider_icon_audio_sfx.png",
									AUDIO_SFX_VOLUME, newValue -> AudioManager.setSfxVolume(newValue / 15f))),
					new Vector2(transform.position.x + 206.25f, transform.position.y + 656.25f)).transform.setParent(
					root.transform);
			return root;
		}
	}

	private class GraphicsSettingsTab implements SettingsTab {

		@Override
		public String getTitle() {
			return "GRAPHICS";
		}

		@Override
		public GameObject getRootObject() {
			return GameObject.instantiate(new GameObject(), new Vector2());
		}
	}

	private class ControlsSettingsTab implements SettingsTab {

		@Override
		public String getTitle() {
			return "CONTROLS";
		}

		@Override
		public GameObject getRootObject() {
			return GameObject.instantiate(new GameObject(), new Vector2());
		}
	}

	interface BackButtonAction {
		void onBackButtonClicked(SettingsMenu settingsMenu);
	}
}
