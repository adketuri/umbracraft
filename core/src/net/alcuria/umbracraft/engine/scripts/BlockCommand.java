package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.editor.modules.EmptyCommand;

/** Allows nesting of {@link ScriptCommand} objects. This enables control flow
 * with things like Conditional Branches and Loops subclassed.
 * @author Andrew Keturi */
public abstract class BlockCommand extends ScriptCommand {

	public ScriptCommand block = new EmptyCommand();

}
