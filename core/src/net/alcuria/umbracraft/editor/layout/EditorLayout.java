package net.alcuria.umbracraft.editor.layout;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.modules.AnimationCollectionListModule;
import net.alcuria.umbracraft.editor.modules.AnimationGroupListModule;
import net.alcuria.umbracraft.editor.modules.AnimationsModule;
import net.alcuria.umbracraft.editor.modules.AreaListModule;
import net.alcuria.umbracraft.editor.modules.BattleAnimationGroupListModule;
import net.alcuria.umbracraft.editor.modules.ConfigModule;
import net.alcuria.umbracraft.editor.modules.EntityListModule;
import net.alcuria.umbracraft.editor.modules.FlagListModule;
import net.alcuria.umbracraft.editor.modules.HeroModule;
import net.alcuria.umbracraft.editor.modules.MapListModule;
import net.alcuria.umbracraft.editor.modules.Module;
import net.alcuria.umbracraft.editor.modules.ScriptListModule;
import net.alcuria.umbracraft.editor.modules.TilesetsModule;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** The top-level screen for the editor. Here, all of the {@link Module} classes
 * are added to a left nav.
 * @author Andrew Keturi */
public class EditorLayout extends Layout {

	private boolean debug = false;
	private final Array<Module<?>> modules;

	public EditorLayout() {
		modules = new Array<Module<?>>();
		addModules();
		Gdx.input.setInputProcessor(stage);
		Table root = new Table();
		root.setFillParent(true);
		final Table menu = new Table();
		content = new Table();
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
		final ScrollPane scroll = new ScrollPane(content) {
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
	}

	private void addModules() {
		modules.add(new HeroModule());
		modules.add(new ConfigModule());
		modules.add(new TilesetsModule());
		modules.add(new AnimationsModule());
		modules.add(new AnimationGroupListModule());
		modules.add(new AnimationCollectionListModule());
		modules.add(new AreaListModule());
		modules.add(new MapListModule());
		modules.add(new EntityListModule());
		modules.add(new ScriptListModule());
		modules.add(new FlagListModule());
		modules.add(new BattleAnimationGroupListModule());
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
