package com.ezardlabs.lostsector.objects.weapons;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animation;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public abstract class MeleeWeapon extends Weapon {
	private final Game.DamageType damageType;
	protected final GameObject wielder;
	protected String currentAnimation = null;

	public MeleeWeapon(String name, Game.DamageType damageType, GameObject wielder) {
		super(name);
		this.damageType = damageType;
		this.wielder = wielder;
	}

	public abstract Animation[] getAnimations(TextureAtlas ta);

	public abstract String getNextAnimation(int direction);

	public abstract boolean isWaiting();

	public abstract boolean shouldStow();

	public abstract boolean isStowed();

	public final void reset() {
		currentAnimation = null;
	}

	protected void damageEnemies(float damage, float left, float top, float right, float bottom) {
		Collider c;
		for (GameObject go : GameObject.findAllWithTag("enemy")) {
			if ((c = go.getComponent(Collider.class)) != null && c.getBounds().intersects(left, top, right, bottom)) {
				//noinspection ConstantConditions
				go.getComponentOfType(Enemy.class).applyDamage(damage, damageType, wielder.transform.position);
			}
		}
	}
}
