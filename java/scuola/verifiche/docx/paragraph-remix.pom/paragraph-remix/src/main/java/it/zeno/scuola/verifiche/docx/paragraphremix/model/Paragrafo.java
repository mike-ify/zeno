package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.ArrayList;
import java.util.List;

public class Paragrafo {
	protected StringBuilder testo; 
	protected List<String> xmlElements; 
	protected List<String> xmlElementNames; 
	protected boolean spazio; 
	public boolean isSpazio() {
		return spazio;
	}
	public Paragrafo(Paragrafo currentParagrafo, boolean b) {
		this(currentParagrafo);
		spazio = b;
	}
	
	public Paragrafo(Paragrafo p) {
		xmlElementNames = new ArrayList<>(p.xmlElementNames);
		xmlElements = new ArrayList<>(p.xmlElements);
		testo = new StringBuilder(p.testo);
	}

	public Paragrafo() {
		xmlElementNames = new ArrayList<>();
		xmlElements = new ArrayList<>();
		testo = new StringBuilder();
	}
	public Paragrafo appendTxt(String txt) {
		this.testo.append(txt);
		return this;
	}
	
	public Paragrafo appendXmlElementName(String xmlElementName) {
		this.xmlElementNames.add(xmlElementName);
		return this;
	}	

	public Paragrafo appendXmlElement(String xmlElement) {
		this.xmlElements.add(xmlElement);
		return this;
	}
	
	public String toText() {
		return testo.toString();
	}	
	
	public String toXml() {
		//\n
		return String.join("",xmlElements);
	}

    public void empty() {
		xmlElements.clear();
		xmlElementNames.clear();
		testo.setLength(0);
    }
}
