package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Freeze extends Script {

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas("images/warframes/abilities/frost/freeze/atlas.png", "images/warframes/abilities/frost/freeze/atlas.txt");
		gameObject.renderer.setTextureAtlas(ta, 100, 100);
		gameObject.animator.setAnimations(
				Animations.load("warframes/frost/abilities/freeze", ta, new Validator("move", "shatter")));
		gameObject.animator.play("move");
	}

	@Override
	public void update() {
		transform.translate(15f * transform.scale.x, 0);
	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (other.gameObject.getTag() != null) {
			if (other.gameObject.getTag().equals("enemy")) {
				//noinspection ConstantConditions
				other.gameObject.getComponentOfType(Enemy.class).applyDamage(1, DamageType.COLD, transform.position);
				gameObject.removeComponent(Collider.class);
				Network.destroy(gameObject);
			} else if (other.gameObject.getTag().equals("solid") && other.gameObject.name != null && !other.gameObject.name.equals("Snowglobe")) {
				gameObject.animator.play("shatter");
				gameObject.removeComponent(Freeze.class);
				GameObject.destroy(gameObject, 400);
			}
		}
	}
}
