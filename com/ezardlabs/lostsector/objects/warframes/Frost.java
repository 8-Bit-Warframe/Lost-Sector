package com.ezardlabs.lostsector.objects.warframes;

import android.util.Log;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Avalanche;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Freeze;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.IceWave;

public class Frost extends Warframe {

	public Frost() {
		super("frost");
	}

	@Override
	public void start() {
		super.start();
		gameObject.renderer.setzIndex(4);
		gameObject.animator.addAnimation(new Animation("cast_avalanche", new Sprite[]{ta.getSprite("avalanche0"),
				ta.getSprite("avalanche1"),
				ta.getSprite("avalanche2"),
				ta.getSprite("avalanche3"),
				ta.getSprite("avalanche4"),
				ta.getSprite("avalanche5"),
				ta.getSprite("avalanche6"),
				ta.getSprite("avalanche7"),
				ta.getSprite("avalanche8"),
				ta.getSprite("avalanche8"),
				ta.getSprite("avalanche10"),
				ta.getSprite("avalanche11"),
				ta.getSprite("avalanche12"),
				ta.getSprite("avalanche13")}, AnimationType.ONE_SHOT, 100));
	}

	@Override
	public void ability1() {
		GameObject.instantiate(new GameObject("Freeze", new Renderer(), new Animator(), new Freeze(gameObject.renderer.hFlipped), new Collider(100, 100, true)),
				new Vector2(gameObject.renderer.hFlipped ? transform.position.x : transform.position.x + 100, transform.position.y + 50));
	}

	@Override
	public void ability2() {
		//noinspection ConstantConditions
		if (gameObject.getComponent(Player.class).jumpCount == 0) {
			GameObject.instantiate(new GameObject("Ice Wave", new Renderer(), new Animator(), new IceWave(gameObject.renderer.hFlipped), new Collider(300, 200, true)),
					new Vector2(gameObject.renderer.hFlipped ? transform.position.x - 800 : transform.position.x + 200, transform.position.y - 200));
		}
	}

	@Override
	public void ability3() {
		Log.i("", "[ability3] ");
	}

	@Override
	public void ability4() {
		Log.i("", "[ability4] ");
		gameObject.animator.play("cast_avalanche");
		GameObject.instantiate(new GameObject("Avalanche", new Avalanche()), transform.position.offset(-300, -100));
//		TextureAtlas ta = new TextureAtlas("images/warframes/abilities/frost/avalanche/atlas.png", "images/warframes/abilities/frost/avalanche/atlas.txt");
//		transform.position.x -= 300;
//		GameObject
//				.instantiate(new GameObject(null, new Renderer(ta, ta.getSprite("avalanche_background0"), 800, 300).setzIndex(2)), transform.position.offset(-300, -100));
//		GameObject.instantiate(new GameObject(null, new Renderer(ta, ta.getSprite("avalanche_foreground0"), 800, 300).setzIndex(5)),
//				transform.position.offset(-300, -100));
	}
}
