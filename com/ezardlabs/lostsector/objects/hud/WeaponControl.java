package com.ezardlabs.lostsector.objects.hud;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Touch;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;
import com.ezardlabs.lostsector.objects.weapons.Weapon;

public class WeaponControl {
	private TextureAtlas ta;
	private Weapon primary;
	private Weapon secondary;
	private boolean isCurrentPrimary = true;
	private GuiRenderer attackButton;
	private boolean attackButtonPressed;

	void init(TextureAtlas ta) {
		this.ta = ta;
		//187.5
		GameObject.instantiate(new GameObject("Weapon Control", new GuiRenderer(ta, ta.getSprite("weapons"), 600, 300)), new Vector2((Screen.width - 600 * Screen.scale) / Screen.scale, Screen.height - 300 - 12));
		GameObject.instantiate(new GameObject("Attack Button", attackButton = new GuiRenderer(ta, ta.getSprite("melee"), 187.5f, 187.5f)), new Vector2((Screen.width - 212.5f * Screen.scale) / Screen.scale, Screen.height - 206.25f - 12));
	}

	void update() {
		attackButtonPressed = false;
		for (Touch t : Input.touches) {
			if (attackButton.hitTest(t.startPosition) && attackButton.hitTest(t.position)) {
				attackButtonPressed = true;
				break;
			}
		}
		if ((isCurrentPrimary ? primary : secondary) instanceof MeleeWeapon) {
			attackButton.setSprite(ta.getSprite("melee" + (attackButtonPressed ? "_focus" : "")));
		} else {
			attackButton.setSprite(ta.getSprite("ranged" + (attackButtonPressed ? "_focus" : "")));
		}
	}

	public void setWeapons(Weapon primary, Weapon secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

	public Weapon switchWeapons() {
		isCurrentPrimary = !isCurrentPrimary;
		if ((isCurrentPrimary ? primary : secondary) instanceof MeleeWeapon) {
			attackButton.setSprite(ta.getSprite("melee"));
		} else {
			attackButton.setSprite(ta.getSprite("ranged"));
		}
		return isCurrentPrimary ? primary : secondary;
	}

	public Weapon getCurrentWeapon() {
		return isCurrentPrimary ? primary : secondary;
	}

	public boolean isAttackButtonPressed() {
		return attackButtonPressed;
	}
}
