package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.skill.SkillDefinition;
import net.alcuria.umbracraft.definitions.skill.SkillPositionDefinition;
import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;

/** Manages targeting of a {@link SkillDefinition}
 * @author Andrew Keturi */
public class SkillTargetingWidget {

	private static final int CENTER_OFFSET = 3;
	private Table content;
	private final SkillDefinition definition;

	public SkillTargetingWidget(SkillDefinition definition) {
		this.definition = definition;
	}

	public Actor getActor() {
		if (content == null) {
			content = new Table();
			update();
		}
		return content;
	}

	private boolean isChecked(int x, int y) {
		if (definition == null || definition.targets == null) {
			return false;
		}
		for (SkillPositionDefinition position : definition.targets) {
			if (position.x == x && position.y == y) {
				return true;
			}
		}
		return false;
	}

	private void setTarget(int x, int y, boolean checked) {
		if (definition.targets == null) {
			definition.targets = new Array<SkillPositionDefinition>();
		}
		SkillPositionDefinition target = null;
		for (int i = 0; i < definition.targets.size; i++) {
			final SkillPositionDefinition pos = definition.targets.get(i);
			if (pos.x == x && pos.y == y && !checked) {
				definition.targets.removeIndex(i);
				return;
			}
		}
		if (target == null && checked) {
			definition.targets.add(new SkillPositionDefinition(x, y));
		}
	}

	private void update() {
		content.clear();
		content.add(new Table() {
			{
				add(WidgetUtils.tooltip("The skill's range, relative to a centered target."));
				add(new VisLabel("Targets"));
			}
		}).row();
		content.add(new Table() {
			{
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 7; j++) {
						final int x = i - CENTER_OFFSET;
						final int y = j - CENTER_OFFSET;
						add(new VisCheckBox("", isChecked(x, y)) {
							{
								addListener(new EventListener() {
									@Override
									public boolean handle(Event event) {
										if (event instanceof ChangeEvent) {
											setTarget(x, y, isChecked());
											return true;
										}
										return false;
									}
								});
								if (x == 0 && y == 0) {
									setDebug(true);
									setColor(Color.YELLOW);
									setBackground(Drawables.get("blue"));
								}
							}
						});
					}
					row();
				}
			}
		});
	}
}
