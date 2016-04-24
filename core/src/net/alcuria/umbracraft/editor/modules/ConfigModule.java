package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.config.ConfigDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;

/** The module for the configuration options.
 * @author Andrew Keturi */
public class ConfigModule extends Module<ConfigDefinition> {

	public ConfigModule() {
		super();
		load(ConfigDefinition.class);
	}

	@Override
	public String getTitle() {
		return "Configuration";
	}

	@Override
	public void populate(Table content) {
		content.defaults().pad(20);
		content.add(new Table() {
			{
				populate(this, ConfigDefinition.class, rootDefinition, new PopulateConfig() {
					{
						cols = 1;
						textFieldWidth = 300;
						labelWidth = 200;
					}
				});
			}
		});
		content.add(new Table() {
			{
				add(new Table() {
					{
						add(WidgetUtils.tooltip("A list of entities present on ALL maps."));
						add(new VisLabel("Global Entities:"));
					}
				}).row();
				WidgetUtils.divider(this, "blue");
				WidgetUtils.modifiableList(this, rootDefinition.globalEntities, new Array<String>(Editor.db().entities().keys()));
			}
		});
		content.add(new Table() {
			{
				add(new Table() {
					{
						add(WidgetUtils.tooltip("The default party."));
						add(new VisLabel("Starting Party:"));
					}
				}).row();
				WidgetUtils.divider(this, "blue");
				WidgetUtils.modifiableList(this, rootDefinition.startingParty, new Array<String>(Editor.db().heroes().keys()));
			}
		});
	}

}
