package it.zeno.utils;

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

public class IO {

	public static boolean dirEmpty(Path dir) throws IOException {
		dirRemove(dir);
		return dir.toFile().mkdir();
	}

	public static Path mkdir(Path basePath, String name) throws IOException {
		Path p = basePath.resolve(name);
		p.toFile().mkdir();
		IO.dirEmpty(p);
		return p;
	}
	
	public static void dirRemove(Path dir) throws IOException {
		if(Files.exists(dir))
			Files.walk(dir)
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.forEach(File::delete);
	}
	
	public static void zipExplode(Path zipFilePath, Path destDir) {
		File dir = destDir.toFile();
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath.toFile());
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void zipImplode(Path sourceDirPath, Path zipFilePath) throws IOException {
	    Path p = Files.createFile(zipFilePath);
	    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
	        Files.walk(sourceDirPath)
	          .filter(path -> !Files.isDirectory(path))
	          .forEach(path -> {
	              ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
	              try {
	                  zs.putNextEntry(zipEntry);
	                  Files.copy(path, zs);
	                  zs.closeEntry();
	            } catch (IOException e) {
	                System.err.println(e);
	            }
	          });
	    }
	}

}
