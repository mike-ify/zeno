package it.zeno.utils.file;

import java.nio.file.Path;

public class FILE {

	public static String nameLessExt(Path p) {
		String name = p.getFileName().toString();
		return name.substring(0,name.lastIndexOf('.'));
	}

}
