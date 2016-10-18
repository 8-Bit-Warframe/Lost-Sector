package com.ezardlabs.lostsector.multiplayer;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.dethsquare.multiplayer.NetworkBehaviour;
import com.ezardlabs.lostsector.objects.Player;

import java.nio.ByteBuffer;

public class PlayerNetworkBehaviour extends NetworkBehaviour {
	protected final ByteBuffer data = ByteBuffer.allocate(getSize());
	private Animator animator;

	@Override
	public void start() {
		super.start();
		animator = gameObject.getComponent(Animator.class);

		if (playerId != Network.getPlayerID()) {
			gameObject.removeComponent(Player.class);
		}
	}

	@Override
	protected ByteBuffer onSend() {
		data.putFloat(0, transform.position.x); // 0 - 3
		data.putFloat(4, transform.position.y); // 4 - 7
		data.putInt(8, animator.getCurrentAnimationId()); // 8 - 11
		data.putInt(12, animator.getCurrentAnimationFrame()); // 12 - 15
		return data;
	}

	@Override
	protected void onReceive(ByteBuffer data, int index) {
		transform.position.x = data.getFloat(index);
		transform.position.y = data.getFloat(index + 4);
		animator.setCurrentAnimationId(data.getInt(index + 8));
		animator.setCurrentAnimationFrame(data.getInt(index + 12));
	}

	@Override
	public int getSize() {
		return 16;
	}
}
