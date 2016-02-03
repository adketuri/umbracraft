package net.alcuria.umbracraft.editor.modules;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.events.HideTooltip;
import net.alcuria.umbracraft.editor.events.ShowTooltip;
import net.alcuria.umbracraft.editor.widget.SuggestionWidget;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.TypeListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

/** The base class of a module. Modules contain UI to modify {@link Definition}
 * classes.
 * @author Andrew Keturi
 * @param <T> */
public abstract class Module<T extends Definition> {

	public static enum FieldType {
		FLOAT, INT, STRING;

		public static FieldType from(Field field) {
			if (field.getType().toString().equals("float")) {
				return FLOAT;
			} else if (field.getType().toString().equals("int")) {
				return INT;
			}
			return STRING;
		}
	}

	/** Same as a {@link Field}, but ordered based on an optional order value.
	 * @author Andrew Keturi */
	public class OrderedField implements Comparable<OrderedField> {
		public Field field;
		public int order;

		@Override
		public int compareTo(Module<T>.OrderedField o) {
			if (o.order == order) {
				return field.getName().compareTo(o.field.getName());
			} else {
				return order - o.order;
			}
		}
	}

	/** A helper class to define layout configurations when populating
	 * definition.
	 * @author Andrew Keturi */
	public static class PopulateConfig {
		/** Columns for each field */
		public int cols = 3;
		/** If non-null, only displays field names included in this set */
		public Set<String> filter;
		/** Width of the label */
		public int labelWidth = 130;
		/** A custom listener to invoke when a field changes value */
		public TypeListener<String> listener;
		/** Maps a field to an array of suggestions */
		public ObjectMap<String, Array<String>> suggestions;
		/** Width of the text fields */
		public int textFieldWidth = 100;
	}

	public VisTextButton button;
	protected T rootDefinition;

	private final Vector2 tmp = new Vector2();

	public Module() {
		button = new VisTextButton(getTitle());
	}

	public Button getButton() {
		return button;
	}

	public abstract String getTitle();

	public void load(final Class<T> clazz) {
		Json json = new Json();
		json.setIgnoreUnknownFields(true);
		final FileHandle handle = Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json");
		if (handle.exists()) {
			rootDefinition = json.fromJson(clazz, handle);
		} else {
			try {
				rootDefinition = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void populate(Table content);

	/** A generic populate method. Populates a table with a class's fields which
	 * may be modified. For ints and string fields, a textfield is created where
	 * the value of the field is updated when the textfield changes. For boolean
	 * fields a checkbox is used. For enums, a dropdown menu will be generated.
	 * Other objects and private fields are not displayed.
	 * @param content the table to update
	 * @param clazz the class definitions
	 * @param definition the definition we want to update (when fields change
	 *        and so on)
	 * @param config configuration settings for how the populate is handled */
	public void populate(final Table content, final Class<?> clazz, final Definition definition, final PopulateConfig config) {
		assert (config.cols > 0);
		try {
			content.add(new Table() {
				{
					int idx = 0;
					// get all fields
					Array<Field> fieldList = new Array<Field>(clazz.getDeclaredFields());
					// sort by @Order annotation
					Array<OrderedField> orderedFields = new Array<OrderedField>();
					for (int i = 0; i < fieldList.size; i++) {
						OrderedField ordered = new OrderedField();
						final Order orderAnnotation = fieldList.get(i).getAnnotation(Order.class);
						ordered.order = orderAnnotation != null ? orderAnnotation.value() : Integer.MAX_VALUE;
						ordered.field = fieldList.get(i);
						orderedFields.add(ordered);
					}
					orderedFields.sort();
					for (int i = 0; i < orderedFields.size; i++) {
						final Field field = orderedFields.get(i).field;
						if (visible(field, config) && field.getModifiers() != Modifier.PRIVATE && (field.getType().isEnum() || field.getType().toString().equals("int") || field.getType().toString().equals("float") || field.getType() == String.class || field.getType().toString().equals("boolean"))) {
							if (idx % config.cols == config.cols - 1) {
								add(keyInput(definition, field)).row();
							} else {
								add(keyInput(definition, field));
							}
							idx++;
						}
					}
					int size = fieldList.size;
					while (size % config.cols != 0) {
						add();
						size++;
					}
				}

				private Table keyInput(final Definition definition, final Field field) {
					return new Table() {
						{
							// check if we should show a tooltip for this field
							final Tooltip annotation = field.getAnnotation(Tooltip.class);
							if (annotation != null) {
								final VisLabel helpLabel = new VisLabel("[?]", Color.YELLOW);
								add(helpLabel).pad(5);
								helpLabel.addListener(new ClickListener() {
									@Override
									public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
										tmp.x = -160;
										tmp.y = -30;
										Editor.publisher().publish(new ShowTooltip(helpLabel.localToStageCoordinates(tmp), annotation.value()));
									};

									@Override
									public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
										Editor.publisher().publish(new HideTooltip());
									};
								});
							}
							if (field.getType().isEnum()) {
								add(new VisLabel(field.getName())).minWidth(config.labelWidth);
								final VisSelectBox selectBox = new VisSelectBox();
								selectBox.setItems(field.getType().getEnumConstants());
								add(selectBox).width(config.textFieldWidth).expandX().fill().left();
								try {
									selectBox.setSelected(field.get(definition));
								} catch (Exception e) {
								}
								selectBox.addListener(selected(selectBox, definition, field));
							} else if (field.getType().toString().equals("boolean")) {
								add(new VisLabel(field.getName())).minWidth(config.labelWidth);
								boolean value = false;
								try {
									value = Boolean.valueOf(field.getBoolean(definition));
								} catch (Exception e) {
								}
								final VisCheckBox checkBox = new VisCheckBox("", value);
								checkBox.addListener(new ClickListener() {
									@Override
									public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
										try {
											field.setBoolean(definition, checkBox.isChecked());
											if (config.listener != null) {
												config.listener.invoke(field.getName());
											}
										} catch (IllegalArgumentException | IllegalAccessException e) {
											e.printStackTrace();
										}
									};
								});
								checkBox.align(Align.left);
								add(checkBox).width(config.textFieldWidth).expandX().fill().left();
							} else if (field.getType().toString().equals("int") || field.getType().toString().equals("float") || field.getType() == String.class) {
								add(new VisLabel(field.getName())).minWidth(config.labelWidth);
								String value;
								try {
									value = String.valueOf(field.get(definition));
								} catch (Exception e) {
									value = "";
								}
								if ("null".equals(value)) {
									value = "";
								}
								VisTextField textField = null;
								SuggestionWidget widget = null;
								if (config.suggestions != null && config.suggestions.containsKey(field.getName())) {
									widget = new SuggestionWidget(config.suggestions.get(field.getName()), config.textFieldWidth);
									add(widget.getActor()).width(config.textFieldWidth);
									textField = widget.getTextField();
									textField.setText(value);
									final VisTextField tf = textField;
									final FieldType type = FieldType.from(field);
									widget.addSelectListener(new Listener() {

										@Override
										public void invoke() {
											saveField(type, field, definition, tf, config);
										}

									});

								} else {
									textField = new VisTextField(value);
									add(textField).width(config.textFieldWidth);
								}
								final SuggestionWidget w = widget;
								final FieldType type = FieldType.from(field);
								textField.setTextFieldListener(new TextFieldListener() {

									@Override
									public void keyTyped(VisTextField textField, char c) {
										if (c == '\t') {
											return;
										}
										saveField(type, field, definition, textField, config);
										if (w != null) {
											w.populateSuggestions();
										}
									}
								});

							}

						}
					};
				}

				private EventListener selected(final VisSelectBox selectBox, final Definition definition, final Field field) {
					return new EventListener() {

						@Override
						public boolean handle(Event event) {
							if (event instanceof ChangeEvent) {
								try {
									field.set(definition, selectBox.getSelected());
								} catch (IllegalArgumentException | IllegalAccessException e) {
									e.printStackTrace();
								}
								if (config.listener != null) {
									config.listener.invoke(field.getName());
								}
								//page.trigger = ((SelectBox<ScriptTrigger>) event.getTarget()).getSelected();
								return true;
							}
							return false;
						}
					};
				}

				/** Determines whether or not a field should be visible based on
				 * filteration.
				 * @param field field we're inspecting
				 * @param config the PopulateConfig
				 * @return true if we should show it */
				private boolean visible(Field field, PopulateConfig config) {
					if (config.filter == null) {
						return true;
					}
					return config.filter.contains(field.getName());
				}
			}).expandX().fill();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		String jsonStr = json.prettyPrint(rootDefinition);
		Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json").writeString(jsonStr, false);
	}

	public void saveField(FieldType type, Field field, Definition definition, VisTextField textField, PopulateConfig config) {
		try {
			if (type == FieldType.INT) {
				if (textField.getText().equals("")) {
					field.setInt(definition, 0);
				} else {
					field.setInt(definition, Integer.valueOf(textField.getText()));
				}
			} else if (type == FieldType.FLOAT) {
				if (textField.getText().equals("")) {
					field.setFloat(definition, 0);
				} else {
					field.setFloat(definition, Float.valueOf(textField.getText()));
				}
			} else {
				field.set(definition, textField.getText());
			}
			if (config.listener != null) {
				config.listener.invoke(field.getName());
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			System.out.println("null pointer");
		}
	}
}
