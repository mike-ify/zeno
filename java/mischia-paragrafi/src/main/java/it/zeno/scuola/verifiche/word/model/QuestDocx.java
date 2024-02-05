package it.zeno.scuola.verifiche.word.model;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Singleton;

import it.zeno.utils.file.FILE;

@Singleton
public class QuestDocx {
	private Path dirDocxInput;
	private Path fileDocxInput;
	private String strDirDocxInput;
	private Integer studentiNu;
	private String name;
	private Path dirElab;
	private Path dirResult;
	private Path fileDocxElab;
	private Integer currentStudentNu;
	private Path fileDocxOrigin;
	private String nameFileDocxElab;
	private String nameFileDocxElabNoExt;
	

	public QuestDocx() {
		this.studentiNu = 25;
		this.currentStudentNu = 1;
	}

	public Integer getStudentiNu() {
		return studentiNu;
	}

	public void setStudentiNu(Integer studentiNu) {
		this.studentiNu = studentiNu;
	}

	public Path getDirDocxInput() {
		return dirDocxInput;
	}

	public void setDirDocxInput(Path dirDocxInput) {
		this.dirDocxInput = dirDocxInput;
		strDirDocxInput = dirDocxInput.toString();
	}

	public String getStrDirDocxInput() {
		return strDirDocxInput;
	}

	public void setDirDocxInput(String strDirDocxInput) {
		this.strDirDocxInput = strDirDocxInput;
		setDirDocxInput(Paths.get(strDirDocxInput));
	}

	public Path getFileDocxInput() {
		return fileDocxInput;
	}

	public void setFileDocxInput(Path fileDocxInput) {
		this.fileDocxInput = fileDocxInput;
		name = fileDocxInput.getFileName().toString();
	}
	
	public void setFileDocxOrigin(Path fileDocxInput) {
		this.fileDocxOrigin = fileDocxInput;
		this.fileDocxInput = fileDocxInput;
	}
	public Path getFileDocxOrigin() {
		return fileDocxOrigin;
	}
	public void setDirElab(Path elabDir) {
		dirElab = elabDir;
	}

	public void setDirResult(Path resultDir) {
		dirResult = resultDir;
	}

	public Path getDirElab() {
		return dirElab;
	}

	public void setFileDocxElab(Path path) {
		fileDocxElab = path;
		nameFileDocxElab = path.getFileName().toString();
		nameFileDocxElabNoExt = FILE.nameLessExt(path);
	}
	public String getNameFileDocxElab() {
		return nameFileDocxElab;
	}
	public String getNameFileDocxElabNoExt() {
		return nameFileDocxElabNoExt;
	}
	public Path getFileDocxElab() {
		return fileDocxElab;
	}

	public Integer countStudent() {
		return currentStudentNu;
	}
	public Integer nextStudent() {
		return currentStudentNu++;
	}


}