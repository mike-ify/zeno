package it.zeno.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Dir {

	public static boolean empty(Path dir) throws IOException {
		remove(dir);
		return dir.toFile().mkdir();
	}

	public static Path mk(Path basePath, String name) throws IOException {
		Path p = basePath.resolve(name);
		p.toFile().mkdir();
		empty(p);
		return p;
	}
	
	public static void remove(Path dir) throws IOException {
		if(Files.exists(dir))
			Files.walk(dir)
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.forEach(File::delete);
	}
	
}
