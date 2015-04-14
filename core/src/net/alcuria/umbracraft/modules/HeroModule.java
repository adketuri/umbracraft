package net.alcuria.umbracraft.modules;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import net.alcuria.umbracraft.definitions.HeroDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextArea;

public class HeroModule extends Module{

	@Override
	public String getTitle() {
		return "Heroes";
	}

	@Override
	public void populate(Table content) {
		content.add(new Table(){
			{
				ArrayList<Field> fieldList = new ArrayList<Field>();
				Class<HeroDefinition> tmpClass = HeroDefinition.class;
				fieldList.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
				for (final Field field : fieldList){
					add(new Table(){
						{
							add(new VisLabel(field.getName()));
							add(new VisTextArea(""));
						}
					}).row();
				}
			}
		}).expand().fill();
	}


}
