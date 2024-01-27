package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.ArrayList;
import java.util.List;

public class SpazioParagrafoDocx extends ParagrafoDocx{
	protected boolean spazio; 
	
	public SpazioParagrafoDocx(SpazioParagrafoDocx currentParagrafo, boolean spazio) {
		this(currentParagrafo);
		spazio = b;
	}
	
	public SpazioParagrafoDocx(SpazioParagrafoDocx p) {
		xmlElements = new ArrayList<>(p.xmlElements);
		testo = new StringBuilder(p.testo);
	}

	public SpazioParagrafoDocx() {
		xmlElementNames = new ArrayList<>();
		xmlElements = new ArrayList<>();
		testo = new StringBuilder();
	}
	public SpazioParagrafoDocx appendTxt(String txt) {
		this.testo.append(txt);
		return this;
	}
	
	public SpazioParagrafoDocx appendXmlElementName(String xmlElementName) {
		this.xmlElementNames.add(xmlElementName);
		return this;
	}	

	public SpazioParagrafoDocx appendXmlElement(String xmlElement) {
		this.xmlElements.add(xmlElement);
		return this;
	}
	
	public String toText() {
		return testo.toString();
	}	
	
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		for(String xml : xmlElements) {
			sb.append(xml);
		}
		return sb.toString();			
	}

    public SpazioParagrafoDocx empty() {
		xmlElements.clear();
		testo.setLength(0);
		return this;
    }
    
    public SpazioParagrafoDocx setTesto(String testo) {
		this.testo.append(testo);
		return this;
	}
    
	public boolean isSpazio() {
		return spazio;
	}

}
