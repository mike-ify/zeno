package it.zeno.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipd {
	
	public static void extract(Path zipFilePath, Path destDir) {
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
	
	public static void create(Path dirDaComprimere, Path nomeFileZip) throws IOException {
	    Path p = Files.createFile(nomeFileZip);
	    try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
	        Files.walk(dirDaComprimere)
	          .filter(path -> !Files.isDirectory(path))
	          .forEach(path -> {
	              ZipEntry zipEntry = new ZipEntry(dirDaComprimere.relativize(path).toString());
	              try {
	                  zs.putNextEntry(zipEntry);
	                  //Files.copy(path, zs);
	                  zs.closeEntry();
	            } catch (IOException e) {
	                System.err.println(e);
	            }
	          });
	    }
	}
	
	public static void main(String[] args) throws IOException {
		String pIO = "C:\\Users\\HP\\dev\\projects\\amici\\zeno\\apps\\java\\scuola\\verifiche\\docx\\paragraph-remix.pom\\paragraph-remix.cmd\\io";
		
		Zipd.create(
			Paths.get(pIO + "/log"),
			Paths.get(pIO + "/log.zip")
		);
	}

}
