package com.ezardlabs.lostsector.objects.warframes;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.ShieldedEntity;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;
import com.ezardlabs.lostsector.objects.weapons.RangedWeapon;

public abstract class Warframe extends ShieldedEntity {
	protected final TextureAtlas ta;
	protected final int maxEnergy;
	protected int energy;
	public RangedWeapon rangedWeapon;
	public MeleeWeapon meleeWeapon;
	private long nextShieldRegen = 0;

	public Warframe(String name, int maxHealth, int maxShield, int maxEnergy) {
		super(maxHealth, maxShield, 2000);
		this.maxEnergy = maxEnergy;
		energy = maxEnergy;
		ta = new TextureAtlas("images/warframes/" + name + "/atlas.png", "images/warframes/" + name + "/atlas.txt");
	}

	@Override
	public void start() {
		gameObject.renderer.setTextureAtlas(ta, 200, 200);
		gameObject.animator.setAnimations(Animations.load(getAnimationPath(), ta,
				new Validator("idle", "run", "jump", "double-jump", "fall", "land", "die")));
		gameObject.animator.play("idle");
	}

	public abstract String getName();

	public abstract void ability1();

	public abstract void ability2();

	public abstract void ability3();

	public abstract void ability4();

	protected abstract String getAnimationPath();

	public final void setPrimaryWeapon(RangedWeapon rangedWeapon) {
		this.rangedWeapon = rangedWeapon;
		gameObject.animator.addAnimations(rangedWeapon.getAnimation(ta));
	}

	public final void setMeleeWeapon(MeleeWeapon meleeWeapon) {
		this.meleeWeapon = meleeWeapon;
		gameObject.animator.addAnimations(meleeWeapon.getAnimations(ta));
	}

	public void addEnergy(int energy) {
		this.energy += energy;
		if (this.energy > maxEnergy) this.energy = maxEnergy;
	}

	public void removeEnergy(int energy) {
		this.energy -= energy;
		if (this.energy < 0) throw new IllegalStateException(
				"Energy cannot be reduced to below 0");
	}

	public boolean hasEnergy(int energy) {
		return this.energy >= energy;
	}

	public int getEnergy() {
		return energy;
	}

	@Override
	protected void die(DamageType damageType, Vector2 attackOrigin) {
		gameObject.animator.play("die");
		gameObject.setTag(null);
		spawnGravestone();
	}

	private void spawnGravestone() {
		GameObject.instantiate(
				new GameObject("Tombstone", new Renderer(ta, ta.getSprite("gravestone"), 200, 200)),
				transform.position.offset(0, 25));
	}
}
