package com.ezardlabs.lostsector.objects.warframes;

import com.ezardlabs.dethsquare.animation.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.graphics.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.objects.ShieldedEntity;
import com.ezardlabs.lostsector.objects.weapons.Arm;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;
import com.ezardlabs.lostsector.objects.weapons.PrimaryWeapon;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Warframe extends ShieldedEntity {
	protected final TextureAtlas ta;
	protected final int maxEnergy;
	protected int energy;
	public MeleeWeapon meleeWeapon;
	private long nextShieldRegen = 0;
	private Class<? extends PrimaryWeapon> primaryClass;

	private Arm arm = new Arm(this);
	private GameObject primary;

	public Warframe(String name, int maxHealth, int maxShield, int maxEnergy) {
		super(maxHealth, maxShield, 2000);
		this.maxEnergy = maxEnergy;
		energy = maxEnergy;
		ta = TextureAtlas.load(getDataPath());
	}

	@Override
	public void start() {
		gameObject.renderer.setTextureAtlas(ta, 200, 200);
		gameObject.animator.setAnimations(Animations.load(getDataPath(), ta,
				new Validator("idle", "run", "jump", "double-jump", "fall", "land", "die", "grip_primary_body",
						"grip_primary_arm")));
		gameObject.animator.play("idle");

		GameObject.instantiate(new GameObject("Arm", new Renderer(ta),
				new Animator(gameObject.animator.getAnimation("grip_primary_arm")), arm), transform.position);
	}

	public abstract String getName();

	public abstract void ability1();

	public abstract void ability2();

	public abstract void ability3();

	public abstract void ability4();

	protected abstract String getDataPath();

	public final void shoot() {
		gameObject.animator.play("grip_primary_body");
		arm.gameObject.animator.play("grip_primary_arm");
		primary.animator.play("shoot");
	}

	public final void stopShooting() {
	}

	public final void setPrimaryWeapon(PrimaryWeapon primaryWeapon) {
		if (primary == null) {
			primary = GameObject.instantiate(new GameObject("Primary Weapon", new Renderer(), new Animator()),
					transform.position);
			primary.transform.setParent(transform);
		} else {
			primary.removeComponent(primaryClass);
		}
		primary.addComponent(primaryWeapon);
		this.primaryClass = primaryWeapon.getClass();

		gameObject.animator.getAnimation("grip_primary_body").setAnimationType(primaryWeapon.getAnimationType().clone());
		arm.setAnimationType("grip_primary_arm", primaryWeapon.getAnimationType().clone());
	}

	public final void setMeleeWeapon(MeleeWeapon meleeWeapon) {
		this.meleeWeapon = meleeWeapon;
		gameObject.animator.addAnimations(meleeWeapon.getAnimations(ta));
	}

	public boolean addEnergy(int energy) {
		if (this.energy == maxEnergy) return false;
		this.energy += energy;
		if (this.energy > maxEnergy) {
			this.energy = maxEnergy;
		}
		return true;
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
		gameObject.rigidbody.velocity.set(0, 0);
		gameObject.setTag(null);
		spawnGravestone();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				transform.position = MapManager.playerSpawn;
				health = maxHealth;
				shield = maxShield;
				energy = maxEnergy;
				gameObject.setTag("player");
			}
		}, 2000);
	}

	private void spawnGravestone() {
		GameObject.instantiate(
				new GameObject("Tombstone", new Renderer(ta, ta.getSprite("gravestone"), 200, 200)),
				transform.position.offset(0, 25));
	}
}
