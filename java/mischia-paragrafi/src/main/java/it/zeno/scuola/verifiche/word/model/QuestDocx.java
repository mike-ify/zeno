package it.zeno.scuola.verifiche.word.model;

import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Singleton;

@Singleton
public class QuestDocx {
	private Path dirDocxInput;
	private String strDirDocxInput;
	private Integer studentiNu;

	public QuestDocx() {
		this.studentiNu = 25;
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
	}

	public String getStrDirDocxInput() {
		return strDirDocxInput;
	}

	public void setStrDirDocxInput(String strDirDocxInput) {
		this.strDirDocxInput = strDirDocxInput;
	}
}