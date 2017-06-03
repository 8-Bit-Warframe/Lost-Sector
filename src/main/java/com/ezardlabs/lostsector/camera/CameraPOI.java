package com.ezardlabs.lostsector.camera;

import com.ezardlabs.dethsquare.Component;

public class CameraPOI extends Component {
	final float outerRadius;
	final float innerRadius;

	public CameraPOI(float outerRadius, float innerRadius) {
		this.outerRadius = outerRadius;
		this.innerRadius = innerRadius;
	}

	@Override
	public void start() {
		SmartCamera.registerPOI(this);
	}

	@Override
	protected void destroy() {
		SmartCamera.unregisterPOI(this);
	}
}
