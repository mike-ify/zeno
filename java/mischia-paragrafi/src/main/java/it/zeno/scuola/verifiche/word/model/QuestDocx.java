package it.zeno.scuola.verifiche.word.model;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Singleton;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import it.zeno.utils.base.Log;
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
	private Path fileXMLInput;
	private XMLEventReader originXmlEventReader;
	private XMLEvent event;
	private StartXMLElement startElement;
	

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

	public void setFileXMLInput(Path p) {
		fileXMLInput = p;
	}
	
	public Path getFileXMLInput() {
		return fileXMLInput;
	}

	public void setOriginXmlEventReader(XMLEventReader xmlEventReader) {
		this.originXmlEventReader = xmlEventReader;
	}

	public XMLEventReader getOriginXmlEventReader() {
		// TODO Auto-generated method stub
		return originXmlEventReader;
	}

	public void nextOriginXmlEventReader() {
		try {
			event = originXmlEventReader.nextEvent();
		} catch (XMLStreamException e) {
			throw Log.error(e);
		}
	}

	public XMLEvent getEvent() {
		// TODO Auto-generated method stub
		return event;
	}

	public boolean isStartElement() {
		// TODO Auto-generated method stub
		return event.isStartElement();
	}

	public boolean isEndElement() {
		// TODO Auto-generated method stub
		return event.isEndElement();
	}

	public boolean hasNextOriginXmlEvent() {
		// TODO Auto-generated method stub
		return originXmlEventReader.hasNext();
	}

	public void setStartXMLElement(StartXMLElement startElement) {
		this.startElement = startElement;
	}

	public String getStartElementName() {
		// TODO Auto-generated method stub
		return startElement.getName();
	}
}