package com.ezardlabs.lostsector.objects.menus;


import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

public class Slider extends Script {
	private final int min;
	private final int max;
	private final String text;
	private final String iconPath;
	private final SliderValueListener listener;
	private GameObject[] bars;
	private GameObject valueBar;
	private GameObject liset;
	private GuiRenderer decrease;
	private GuiRenderer increase;
	private long lastChange = 0;
	private int value = 5;

	Slider(int min, int max, int value, String text, String iconPath,
			SliderValueListener listener) {
		this.min = min;
		this.max = max;
		this.value = value;
		this.text = text;
		this.iconPath = iconPath;
		this.listener = listener;
		this.bars = new GameObject[max - 1];
	}

	@Override
	public void start() {
		GameObject.instantiate(new GameObject("Slider Background",
						new GuiRenderer("images/menus/settings/slider_base.png", 662.5f, 87.5f)),
				new Vector2(transform.position)).transform.setParent(transform);

		GameObject.instantiate(new GameObject("Slider Icon", new GuiRenderer(iconPath, 65, 65)),
				transform.position.offset(12.5f, 12.5f)).transform.setParent(transform);

		TextureAtlas font = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
		GameObject.instantiate(new GameObject("Slider Text", new GuiText(text, font, 31.25f)),
				new Vector2(transform.position.offset(93.75f, -56.25f))).transform
				.setParent(transform);

		GameObject.instantiate(new GameObject("Slider Decrease",
				decrease = new GuiRenderer("images/menus/settings/slider_decrease" + ".png", 68.75f,
						31.25f)), transform.position.offset(493.75f, 31.25f)).transform
				.setParent(transform);

		GameObject.instantiate(new GameObject("Slider Increase",
				increase = new GuiRenderer("images/menus/settings/slider_increase" + ".png", 68.75f,
						31.25f)), transform.position.offset(543.75f, 31.25f)).transform
				.setParent(transform);

		for (int i = 0; i < bars.length; i++) {
			bars[i] = GameObject.instantiate(new GameObject("Slider Bar",
							new GuiRenderer("images/menus/settings/slider_bar_white.png", 37.5f, 25f)),
					new Vector2(10000, 10000));
			bars[i].transform.setParent(transform);
		}
		valueBar = GameObject.instantiate(new GameObject("Slider Bar",
						new GuiRenderer("images/menus/settings/slider_bar_yellow.png", 37.5f, 25f)),
				new Vector2(10000, 10000));
		valueBar.transform.setParent(transform);

		liset = GameObject.instantiate(new GameObject("Slider Liset",
						new GuiRenderer("images/menus/settings/mini_liset.png", 68.75f, 31.25f)),
				new Vector2(10000, 10000));
		liset.transform.setParent(transform);

		moveBars();
	}

	@Override
	public void update() {
		if (Input.getKey(KeyCode.MOUSE_LEFT) && decrease.hitTest(Input.mousePosition) &&
				value > min && System.currentTimeMillis() - lastChange > 100) {
			value--;
			listener.onValueChanged(value);
			lastChange = System.currentTimeMillis();
			moveBars();
		}
		if (Input.getKey(KeyCode.MOUSE_LEFT) && increase.hitTest(Input.mousePosition) &&
				value < max && System.currentTimeMillis() - lastChange > 100) {
			value++;
			listener.onValueChanged(value);
			lastChange = System.currentTimeMillis();
			moveBars();
		}
	}

	private void moveBars() {
		if (value == 0) {
			for (GameObject bar : bars) {
				bar.transform.position.set(10000, 1000);
			}
			valueBar.transform.position.set(10000, 1000);
		} else {
			for (int i = 0; i <= max; i++) {
				if (i == value) {
					valueBar.transform.position
							.set(transform.position.offset(93.75f + (i - 1) * 25, 31.25f));
				}
				if (i < max - 1) {
					if (i + 1 < value) {
						bars[i].transform.position
								.set(transform.position.offset(93.75f + i * 25, 31.25f));
					}
					if (i + 1 >= value) {
						bars[i].transform.position.set(10000, 10000);
					}
				}
			}
		}
		liset.transform.position.set(valueBar.transform.position.offset(-12.5f, -43.75f));
	}

	public interface SliderValueListener {
		void onValueChanged(int newValue);
	}
}
