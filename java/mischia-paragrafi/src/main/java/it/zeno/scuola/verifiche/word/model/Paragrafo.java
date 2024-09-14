package it.zeno.scuola.verifiche.word.model;

import java.util.ArrayList;
import java.util.List;

import it.zeno.utils.base.Get;

public class Paragrafo {

	public static final String XML_END = Get.prop("paragrafo.XML_END");
	public static final String XML_TEXT_END = Get.prop("paragrafo.XML_TEXT_END");
	public static final String XML_SOTTOLINEATO_GRASSETTO = Get.prop("paragrafo.XML_SOTTOLINEATO_GRASSETTO");
	public static final String XML_TEXT_START = Get.prop("paragrafo.XML_TEXT_START");
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
		StringBuilder sb = new StringBuilder();
		for(String xml : xmlElements) {
			if(xml.equals(" "))
				System.out.println(4);
			sb.append(xml);
		}return sb.toString();			
	}

    public Paragrafo empty() {
		xmlElements.clear();
		xmlElementNames.clear();
		testo.setLength(0);
		return this;
    }
    
    public Paragrafo setTesto(String testo) {
		this.testo.append(testo);
		return this;
	}

}
