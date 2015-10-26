package com.ezardlabs.lostsector.objects.warframes;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.objects.Avatar;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.hud.StatusIndicator;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;
import com.ezardlabs.lostsector.objects.weapons.RangedWeapon;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Warframe extends Avatar {
	protected final TextureAtlas ta;
	protected final int maxShields;
	protected int shields;
	protected final int maxEnergy;
	protected int energy;
	public RangedWeapon rangedWeapon;
	public MeleeWeapon meleeWeapon;
	private StatusIndicator statusIndicator;

	public Warframe(String name, int maxHealth, int maxShields, int maxEnergy) {
		super(maxHealth);
		this.maxShields = maxShields;
		shields = maxShields;
		this.maxEnergy = maxEnergy;
		energy = maxEnergy;
		ta = new TextureAtlas("images/warframes/" + name + "/atlas.png", "images/warframes/" + name + "/atlas.txt");
	}

	@Override
	public void start() {
		gameObject.renderer.setTextureAtlas(ta, 200, 200);
		gameObject.animator.setAnimations(getIdleAnimation(), getRunAnimation(), getJumpAnimation(), getDoubleJumpAnimation(), getFallAnimation(), getLandAnimation(), getDieAnimation());
		gameObject.animator.play("idle");
	}

	protected Animation getIdleAnimation() {
		return new Animation("idle", new Sprite[]{ta.getSprite("idle0")}, AnimationType.ONE_SHOT, 0);
	}

	protected Animation getRunAnimation() {
		return new Animation("run", new Sprite[]{ta.getSprite("run0"), ta.getSprite("run1"), ta.getSprite("run2"), ta.getSprite("run3"), ta.getSprite("run4"), ta.getSprite("run5")},
				AnimationType.LOOP, 100);
	}

	protected Animation getJumpAnimation() {
		return new Animation("jump", new Sprite[]{ta.getSprite("jump0")}, AnimationType.ONE_SHOT, 0);
	}

	protected Animation getDoubleJumpAnimation() {
		return new Animation("doublejump",
				new Sprite[]{ta.getSprite("jump1"), ta.getSprite("jump2"), ta.getSprite("jump3"), ta.getSprite("jump4"), ta.getSprite("jump5"), ta.getSprite("jump6"), ta.getSprite("jump7")},
				AnimationType.ONE_SHOT, 50, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				animator.play("fall");
			}
		});
	}

	protected Animation getFallAnimation() {
		return new Animation("fall", new Sprite[]{ta.getSprite("fall0"), ta.getSprite("fall1"), ta.getSprite("fall2")}, AnimationType.LOOP, 90);
	}

	protected Animation getLandAnimation() {
		return new Animation("land", new Sprite[]{ta.getSprite("land0"), ta.getSprite("land1"), ta.getSprite("land2")}, AnimationType.ONE_SHOT, 100);
	}

	protected Animation getDieAnimation() {
		return new Animation("die", new Sprite[]{ta.getSprite("die0"), ta.getSprite("die1"), ta.getSprite("die2"), ta.getSprite("die3"), ta.getSprite("die4")}, AnimationType.ONE_SHOT, 100, new
				AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
				gameObject.renderer.setOffsets(-200, -100);
				gameObject.renderer.setSize(400, 300);
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {
				statusIndicator.spawnGravestone(transform.position);
				GameObject.destroy(gameObject, 2000);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						Game.createPlayer();
					}
				}, 2000);
			}
		});
	}

	public abstract void ability1();

	public abstract void ability2();

	public abstract void ability3();

	public abstract void ability4();

	public final void setPrimaryWeapon(RangedWeapon rangedWeapon) {
		this.rangedWeapon = rangedWeapon;
		gameObject.animator.addAnimations(rangedWeapon.getAnimation(ta));
	}

	public final void setMeleeWeapon(MeleeWeapon meleeWeapon) {
		this.meleeWeapon = meleeWeapon;
		gameObject.animator.addAnimations(meleeWeapon.getAnimations(ta));
	}

	public void setStatusIndicator(StatusIndicator statusIndicator) {
		this.statusIndicator = statusIndicator;
	}

	public void addHealth(int health) {
		this.health += health;
		if (this.health > maxHealth) this.health = maxHealth;
	}

	public void removeHealth(int health) {
		this.health -= health;
		if (this.health < 0) {
			this.health = 0;
			gameObject.animator.play("die");
			if (gameObject.getComponent(Player.class) != null) {
				gameObject.getComponent(Player.class).dead = true;
			}
			gameObject.setTag(null);
			Game.players = new GameObject[0];
		}
	}

	public void addEnergy(int energy) {
		this.energy += energy;
		if (this.energy > maxEnergy) this.energy = maxEnergy;
	}

	public void removeEnergy(int energy) {
		this.energy -= energy;
		if (this.energy < 0) throw new Error("Energy cannot be reduced to below 0");
	}

	public boolean hasEnergy(int energy) {
		return this.energy >= energy;
	}

	public int getEnergy() {
		return energy;
	}
}
