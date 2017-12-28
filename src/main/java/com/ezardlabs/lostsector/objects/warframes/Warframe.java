package com.ezardlabs.lostsector.objects.warframes;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animation;
import com.ezardlabs.dethsquare.animation.AnimationType;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.animation.Animator;
import com.ezardlabs.dethsquare.graphics.Renderer;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.objects.ShieldedEntity;
import com.ezardlabs.lostsector.objects.weapons.Arm;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;
import com.ezardlabs.lostsector.objects.weapons.PrimaryWeapon;
import com.ezardlabs.lostsector.objects.weapons.melee.Fists;
import com.ezardlabs.lostsector.objects.weapons.melee.Nikana;
import com.ezardlabs.lostsector.objects.weapons.primary.Gorgon;
import com.ezardlabs.lostsector.objects.weapons.primary.Lanka;
import com.ezardlabs.lostsector.objects.weapons.primary.Supra;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Warframe extends ShieldedEntity {
	private static final String PRIMARY_WEAPON_MESSAGE = "primary_weapon";
	private static final String MELEE_WEAPON_MESSAGE = "melee_weapon";

	protected final TextureAtlas ta;
	protected final int maxEnergy;
	protected int energy;
	public MeleeWeapon meleeWeapon;
	private PrimaryWeapon primaryWeapon;
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

		gameObject.animator.getAnimation("grip_primary_body").setAnimationType(new AnimationType() {
			@Override
			public int update(int currentFrame, int numFrames) {
				return currentFrame;
			}
		});

		GameObject armObject = Network.instantiate("arm", transform.position);
		armObject.addComponent(arm);
	}

	@Override
	public void update() {
		super.update();
		Animation animation = gameObject.animator.getCurrentAnimation();
		if (animation != null && "grip_primary_body".equals(animation.name)) {
			int frame = primaryWeapon.getAnimationFrame();
			gameObject.animator.setCurrentAnimationFrame(frame);
			arm.gameObject.animator.setCurrentAnimationFrame(frame);
		}
	}

	public abstract String getName();

	public abstract void ability1();

	public abstract void ability2();

	public abstract void ability3();

	public abstract void ability4();

	protected abstract String getDataPath();

	public TextureAtlas getTextureAtlas() {
		return ta;
	}

	public final void shoot() {
		gameObject.animator.play("grip_primary_body");
		arm.gameObject.animator.play("grip_primary_arm", true);
		primary.animator.play("shoot", true);
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
		this.primaryWeapon = primaryWeapon;
		this.primaryClass = primaryWeapon.getClass();
	}

	public PrimaryWeapon getPrimaryWeapon() {
		return primaryWeapon;
	}

	public final void setMeleeWeapon(MeleeWeapon meleeWeapon) {
		this.meleeWeapon = meleeWeapon;
		gameObject.animator.addAnimations(meleeWeapon.getAnimations(ta));
	}

	public MeleeWeapon getMeleeWeapon() {
		return meleeWeapon;
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
		spawnGravestone();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				transform.position.set(MapManager.playerSpawn);
				health = maxHealth;
				shield = maxShield;
				energy = maxEnergy;
			}
		}, 2000);
	}

	private void spawnGravestone() {
		GameObject.instantiate(
				new GameObject("Tombstone", new Renderer(ta, ta.getSprite("gravestone"), 200, 200)),
				transform.position.offset(0, 25));
	}

	private enum PrimaryWeaponType {
		GORGON("gorgon", Gorgon.class, Gorgon::new),
		LANKA("lanka", Lanka.class, Lanka::new),
		SUPRA("supra", Supra.class, Supra::new);

		private final String name;
		private final Class<? extends PrimaryWeapon> clazz;
		private final PrimaryWeaponCreator creator;

		PrimaryWeaponType(String name, Class<? extends PrimaryWeapon> clazz, PrimaryWeaponCreator creator) {
			this.name = name;
			this.clazz = clazz;
			this.creator = creator;
		}

		private interface PrimaryWeaponCreator {
			PrimaryWeapon create();
		}

		static PrimaryWeapon create(String s) {
			for (PrimaryWeaponType primaryWeaponType : values()) {
				if (primaryWeaponType.name.equals(s)) {
					return primaryWeaponType.creator.create();
				}
			}
			return null;
		}
	}

	private enum MeleeWeaponType {
		FISTS("fists", Fists.class, Fists::new),
		NIKANA("nikana", Nikana.class, Nikana::new);

		private final String name;
		private final Class<? extends MeleeWeapon> clazz;
		private final MeleeWeaponCreator creator;

		MeleeWeaponType(String name, Class<? extends MeleeWeapon> clazz, MeleeWeaponCreator creator) {
			this.name = name;
			this.clazz = clazz;
			this.creator = creator;
		}

		private interface MeleeWeaponCreator {
			MeleeWeapon create(GameObject wielder);
		}

		static MeleeWeapon create(String s, GameObject wielder) {
			for (MeleeWeaponType meleeWeaponType : values()) {
				if (meleeWeaponType.name.equals(s)) {
					return meleeWeaponType.creator.create(wielder);
				}
			}
			return null;
		}
	}

	/*@Override
	protected void receiveMessage(String message) {
		String[] split = message.split("\\|");
		switch (split[0]) {
			case PRIMARY_WEAPON_MESSAGE:
				setPrimaryWeapon(PrimaryWeaponType.create(split[1]));
				break;
			case MELEE_WEAPON_MESSAGE:
				setMeleeWeapon(MeleeWeaponType.create(split[1], gameObject));
				break;
			default:
				break;
		}
	}*/
}
