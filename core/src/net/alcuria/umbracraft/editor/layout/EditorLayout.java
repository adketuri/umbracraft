package net.alcuria.umbracraft.editor.layout;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.events.HideTooltip;
import net.alcuria.umbracraft.editor.events.ShowTooltip;
import net.alcuria.umbracraft.editor.modules.AnimationCollectionListModule;
import net.alcuria.umbracraft.editor.modules.AnimationGroupListModule;
import net.alcuria.umbracraft.editor.modules.AnimationsModule;
import net.alcuria.umbracraft.editor.modules.AreaListModule;
import net.alcuria.umbracraft.editor.modules.BattleAnimationGroupListModule;
import net.alcuria.umbracraft.editor.modules.ConfigModule;
import net.alcuria.umbracraft.editor.modules.EnemyListModule;
import net.alcuria.umbracraft.editor.modules.EntityListModule;
import net.alcuria.umbracraft.editor.modules.FlagListModule;
import net.alcuria.umbracraft.editor.modules.HeroListModule;
import net.alcuria.umbracraft.editor.modules.ItemListModule;
import net.alcuria.umbracraft.editor.modules.MapListModule;
import net.alcuria.umbracraft.editor.modules.Module;
import net.alcuria.umbracraft.editor.modules.ScriptListModule;
import net.alcuria.umbracraft.editor.modules.SkillListModule;
import net.alcuria.umbracraft.editor.modules.TilesetListModule;
import net.alcuria.umbracraft.editor.modules.VariableListModule;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** The top-level screen for the editor. Here, all of the {@link Module} classes
 * are added to a left nav.
 * @author Andrew Keturi */
public class EditorLayout extends Layout implements EventListener {

	private boolean debug = false;
	private final Array<Module<?>> modules;
	private final Label tooltipLabel;
	private final Table tooltipWindow;

	public EditorLayout() {
		Editor.publisher().subscribe(this);
		modules = new Array<Module<?>>();
		addModules();
		Gdx.input.setInputProcessor(stage);
		Table root = new Table();
		root.setFillParent(true);
		final Table menu = new Table();
		content = new Table();
		tooltip = new Table();
		for (final Module<?> m : modules) {
			menu.defaults().uniformX().expandX().fillX();
			menu.add(m.getButton()).row();
			m.getButton().addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					content.clear();
					m.populate(content);
				}
			});
		}
		Stack stack = new Stack();
		stack.add(content);
		stack.add(tooltip);
		final ScrollPane scroll = new ScrollPane(stack) {
			@Override
			public void act(float delta) {
				if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
					super.act(delta);
				}
			}

		};
		root.add(topnav()).expandX().fill().height(40).padBottom(10).row();
		root.add(new Table() {
			{
				add(menu).expandY().top();
				add(scroll).expand().fill();
			}
		}).expand().fill().padLeft(5);
		stage.addActor(root);
		tooltipWindow = new Table();
		tooltipWindow.setTransform(true);
		tooltipWindow.getColor().a = 0;
		tooltipWindow.add(tooltipLabel = new VisLabel(""));
		tooltipWindow.setBackground(Drawables.get("blue"));
		tooltip.addActor(tooltipWindow);
	}

	private void addModules() {
		modules.add(new HeroListModule());
		modules.add(new ItemListModule());
		modules.add(new SkillListModule());
		modules.add(new AnimationsModule());
		modules.add(new AnimationGroupListModule());
		modules.add(new BattleAnimationGroupListModule());
		modules.add(new AnimationCollectionListModule());
		modules.add(new EnemyListModule());
		modules.add(new EnemyGroupListModule());
		modules.add(new TilesetListModule());
		modules.add(new MapListModule());
		modules.add(new AreaListModule());
		modules.add(new EntityListModule());
		modules.add(new ScriptListModule());
		modules.add(new FlagListModule());
		modules.add(new VariableListModule());
		modules.add(new ConfigModule());
	}

	@Override
	public void dispose() {
		Editor.publisher().unsubscribe(this);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof ShowTooltip) {
			ShowTooltip tooltipEvent = ((ShowTooltip) event);
			tooltipLabel.setText(tooltipEvent.text);
			tooltipWindow.pack();
			tooltipWindow.setPosition(tooltipEvent.x, tooltipEvent.y);
			tooltipWindow.clearActions();
			tooltipWindow.addAction(Actions.alpha(1, 0.2f, Interpolation.fade));
		} else if (event instanceof HideTooltip) {
			tooltipWindow.clearActions();
			tooltipWindow.addAction(Actions.sequence(Actions.alpha(0, 0.2f, Interpolation.fade), Actions.moveBy(4000, 0))); // this just moves it way offscreen so it doesn't get in the way
		}
	}

	private Table topnav() {
		return new Table() {
			{
				setBackground(Drawables.get("black"));
				add(new VisTextButton("Save") {
					{
						addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								for (Module<?> m : modules) {
									m.save();
								}
								Editor.reloadDb();
							};
						});
					}
				});
			}
		};
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (Gdx.input.isKeyJustPressed(Keys.F1)) {
			debug = !debug;
			stage.setDebugAll(debug);
		}
	}

}
