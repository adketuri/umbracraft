package net.alcuria.umbracraft.engine.scripts;

import com.badlogic.gdx.Gdx;

/** Pauses script execution for some amount of seconds
 * @author Andrew Keturi */
public class PauseScriptCommand extends ScriptCommand {
	float curTime;
	private final float time;

	public PauseScriptCommand(float time) {
		this.time = time;
	}

	@Override
	public String getName() {
		return "pause";
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted() {
		curTime = 0;
	}

	@Override
	public void update() {
		curTime += Gdx.graphics.getDeltaTime();
		if (curTime > time) {
			complete();
		}

	}
}
