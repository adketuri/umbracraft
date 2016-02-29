package net.alcuria.umbracraft.engine.components;

import com.badlogic.gdx.utils.Array;

/** The base class for a {@link Component} that all others should extend from
 * @author Andrew */
public abstract class BaseComponent implements Component {

	private Array<String> args;

	/** Called to fetch runtime arguments. May be <code>null</code> */
	public Array<String> getArgs() {
		return args;
	}

	/** Called to set any runtime arguments needed by the component */
	void setArgs(Array<String> args) {
		this.args = args;
	}
}
