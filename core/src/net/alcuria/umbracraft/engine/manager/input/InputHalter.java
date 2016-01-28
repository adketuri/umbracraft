package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;

/** A small helper class to handle multiple scripts halting input. It is
 * responsible for marking when input ought to be relinquished if a script is
 * halting input and restoring that input once all scripts halting have
 * completed.
 * @author Andrew Keturi */
public class InputHalter {

	private int haltCounter;
	private boolean haltInput;

	/** Checks an event, updating any halt counters as necessary
	 * @param event an incoming {@link Event} */
	public void check(Event event) {
		if (event instanceof ScriptStartedEvent) {
			if (((ScriptStartedEvent) event).page.haltInput) {
				haltCounter++;
				Game.log("Halting input. counter: " + haltCounter);
				haltInput = true;
			}
		} else if (event instanceof ScriptEndedEvent) {
			if (((ScriptEndedEvent) event).page.haltInput) {
				haltCounter--;
				if (haltCounter <= 0) {
					haltInput = false;
					Game.log("Resuming input");
					haltCounter = 0;
				}
			}
		}
	}

	/** @return whether or not this input is halted */
	public boolean isHalted() {
		return haltInput;
	}
}
