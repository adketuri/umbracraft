package net.alcuria.umbracraft.engine.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Pauses script execution for some amount of seconds
 * @author Andrew Keturi */
public class PauseScriptCommand extends ScriptCommand {
	private float curTime;
	public float time;

	public PauseScriptCommand() {
	}

	public PauseScriptCommand(float time) {
		this.time = time;
	}

	@Override
	public String getName() {
		return "Pause: " + time;
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return null;
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
