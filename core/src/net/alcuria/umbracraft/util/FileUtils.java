package net.alcuria.umbracraft.util;

import java.io.File;
import java.util.HashSet;

import com.badlogic.gdx.utils.Array;

public class FileUtils {

	public static Array<String> getFilesAt(String path, boolean recursive) {
		Array<String> files = new Array<String>();
		walk(path, files, "", recursive);
		return files;
	}

	private static void walk(String path, Array<String> lines, String prefix, boolean recursive) {

		File rootFile = new File(path);
		File[] list = rootFile.listFiles();

		if (list == null) {
			return;
		}

		HashSet<String> used = new HashSet<>();
		for (File f : list) {
			if (f.isDirectory() && recursive) {
				walk(f.getAbsolutePath(), lines, prefix + f.getName() + "\\", recursive);
			} else {
				String nameWithoutExtension = f.getName().replaceFirst("[.][^.]+$", "");
				if (nameWithoutExtension == null || nameWithoutExtension.length() < 1) {
					continue;
				}
				if (used.contains(nameWithoutExtension)) {
					nameWithoutExtension = f.getName().replace('.', '_');
				}
				lines.add(nameWithoutExtension);
			}
		}
	}

}
