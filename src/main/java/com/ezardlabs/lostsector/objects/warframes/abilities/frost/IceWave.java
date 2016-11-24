package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

import java.util.ArrayList;

public class IceWave extends Script {
	private int direction = 0;
	private ArrayList<GameObject> damagedEnemies = new ArrayList<>();

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas("images/warframes/abilities/frost/icewave/atlas.png",
				"images/warframes/abilities/frost/icewave/atlas.txt");
		gameObject.renderer.setTextureAtlas(ta, 800, 400);
		gameObject.animator.setAnimations(new Animation("move", new Sprite[]{ta.getSprite("iw0"),
				ta.getSprite("iw1"),
				ta.getSprite("iw2"),
				ta.getSprite("iw3"),
				ta.getSprite("iw4"),
				ta.getSprite("iw5"),
				ta.getSprite("iw6"),
				ta.getSprite("iw7"),
				ta.getSprite("iw8"),
				ta.getSprite("iw9"),
				ta.getSprite("iw10"),
				ta.getSprite("iw11"),
				ta.getSprite("iw12"),
				ta.getSprite("iw13"),
				ta.getSprite("iw14"),
				ta.getSprite("iw15"),
				ta.getSprite("iw16"),
				ta.getSprite("iw17"),
				ta.getSprite("iw18"),
				ta.getSprite("iw19"),
				ta.getSprite("iw20"),
				ta.getSprite("iw21"),
				ta.getSprite("iw22"),
				ta.getSprite("iw23"),
				ta.getSprite("iw24"),
				ta.getSprite("iw25"),
				ta.getSprite("iw26"),
				ta.getSprite("iw27"),
				ta.getSprite("iw28"),
				ta.getSprite("iw29"),
				ta.getSprite("iw30"),
				ta.getSprite("iw31")}, AnimationType.ONE_SHOT, 50));
		gameObject.animator.play("move");
		Network.destroy(gameObject, 1600);
		if (Network.isHost()) {
			GameObject.destroy(GameObject.instantiate(
					new GameObject("Ice Wave Collider", new Collider(10, 200, true),
							new IceWaveCollision()),
					direction == -1 ? new Vector2(transform.position.x + 800,
							transform.position.y + 200) : new Vector2(transform.position.x,
							transform.position.y + 200)), 1000);
		}
	}

	public void setDirection(int direction) {
		this.direction = direction;
		gameObject.renderer.setFlipped(direction == -1, false);
	}

	private class IceWaveCollision extends Script {

		@Override
		public void update() {
			transform.translate(10 * direction, 0);
		}

		@Override
		public void onTriggerEnter(Collider other) {
			if (other.gameObject.getTag() != null && other.gameObject.getTag().equals("enemy") &&
					!damagedEnemies.contains(other.gameObject)) {
				//noinspection ConstantConditions
				other.gameObject.getComponentOfType(Enemy.class)
								.applyDamage(1, DamageType.COLD, transform.position);
				damagedEnemies.add(other.gameObject);
			}
		}
	}
}
