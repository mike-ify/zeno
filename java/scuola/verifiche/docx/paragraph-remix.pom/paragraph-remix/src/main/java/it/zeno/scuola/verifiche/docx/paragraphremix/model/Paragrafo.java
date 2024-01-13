package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.ArrayList;
import java.util.List;

public class Paragrafo {
	public static final String XML_END = "</w:t></w:r></w:p>";
	public static final String XML_TAB = "<w:r w:rsidR=\"00CE44F0\">"
	+ "<w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
	+ "<w:b/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr><w:tab/></w:r>";
	public static final String XML_TEXT_END = "</w:t></w:r>";
	public static final String XML_S = "<w:p w14:paraId=\"2F965704\" w14:textId=\"77777777\" w:rsidR=\"00493E32\" w:rsidRDefault=\"00493E32\" w:rsidP=\"00CE44F0\"><w:pPr><w:spacing w:before=\"40\" w:after=\"0\" w:line=\"240\" w:lineRule=\"auto\"/><w:ind w:left=\"851\" w:hanging=\"851\"/><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr></w:pPr></w:p>";
	public static final String XML_SOTTOLINEATO_GRASSETTO = "<w:r w:rsidRPr=\"0044569F\"><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/><w:b/><w:bCs/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/><w:u w:val=\"single\"/></w:rPr><w:t>rossi</w:t></w:r>";
	
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
