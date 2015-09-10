package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.animationtypes.LoopAnimation;
import com.ezardlabs.dethsquare.animationtypes.OneShotAnimation;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Freeze extends Script {
	private final boolean hFlipped;

	public Freeze(boolean hFlipped) {
		this.hFlipped = hFlipped;
	}

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas("images/warframes/abilities/frost/freeze/atlas.png", "images/warframes/abilities/frost/freeze/atlas.txt");
		gameObject.renderer.setTextureAtlas(ta, 100, 100);
		gameObject.renderer.setFlipped(hFlipped, false);
		gameObject.animator.setAnimations(new Animation("move", new Sprite[]{ta.getSprite("cube0"),
						ta.getSprite("cube1"),
						ta.getSprite("cube2"),
						ta.getSprite("cube3"),
						ta.getSprite("cube4"),
						ta.getSprite("cube5")}, new LoopAnimation(), 100), new Animation("shatter", new Sprite[]{ta.getSprite("cube_shatter0"),
						ta.getSprite("cube_shatter1"),
						ta.getSprite("cube_shatter2"),
						ta.getSprite("cube_shatter3")}, new OneShotAnimation(), 100, new AnimationListener() {
					@Override
					public void onAnimatedStarted(Animator animator) {
						gameObject.removeComponent(Freeze.class);
					}

					@Override
					public void onFrame(Animator animator, int frameNum) {

					}

					@Override
					public void onAnimationFinished(Animator animator) {
						GameObject.destroy(gameObject);
					}
				}));
		gameObject.animator.play("move");
	}

	@Override
	public void update() {
		transform.translate(15f * (hFlipped ? -1 : 1), 0);
	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (other.gameObject.getTag() != null) {
			if (other.gameObject.getTag().equals("enemy")) {
				//noinspection ConstantConditions
				((Enemy) other.gameObject.getComponentOfType(Enemy.class)).applyDamage(1, DamageType.COLD);
				gameObject.removeComponent(Collider.class);
				GameObject.destroy(gameObject);
			} else if (other.gameObject.getTag().equals("solid")) {
				gameObject.animator.play("shatter");
			}
		}
	}
}
