package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.ArrayList;
import java.util.List;

public class ParagrafoDocx {
	public static final String XML_END = "</w:t></w:r></w:p>";
	public static final String XML_TAB = "<w:r w:rsidR=\"00CE44F0\">"
	+ "<w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
	+ "<w:b/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr><w:tab/></w:r>";
	public static final String XML_TEXT_START = "<w:r w:rsidRPr=\"007F4FF0\"><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr><w:t xml:space=\"preserve\">";
	public static final String XML_TEXT_END = "</w:t></w:r>";
	public static final String XML_S = "<w:p w14:paraId=\"2F965704\" w14:textId=\"77777777\" w:rsidR=\"00493E32\" w:rsidRDefault=\"00493E32\" w:rsidP=\"00CE44F0\"><w:pPr><w:spacing w:before=\"40\" w:after=\"0\" w:line=\"240\" w:lineRule=\"auto\"/><w:ind w:left=\"851\" w:hanging=\"851\"/><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr></w:pPr></w:p>";
	public static final String XML_SOTTOLINEATO_GRASSETTO = "<w:r w:rsidRPr=\"0044569F\"><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/><w:b/><w:bCs/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/><w:u w:val=\"single\"/></w:rPr><w:t  xml:space=\"preserve\">";
	
	protected StringBuilder testo; 
	protected StringBuilder xml; 
	
	public ParagrafoDocx(ParagrafoDocx p) {
		xml = new StringBuilder(p.xml);
		testo = new StringBuilder(p.testo);
	}

	public ParagrafoDocx() {
		xml = new StringBuilder();
		testo = new StringBuilder();
	}
	public ParagrafoDocx appendTxt(String txt) {
		testo.append(txt);
		return this;
	}
	
	public ParagrafoDocx appendXml(CharSequence xml) {
		this.xml.append(xml);
		return this;
	}
	
	public String toText() {
		return testo.toString();
	}	
	
	public String toXml() {
		return xml.toString();			
	}

    public ParagrafoDocx empty() {
		xml.setLength(0);
		testo.setLength(0);
		return this;
    }
    
    public ParagrafoDocx setTesto(String testo) {
		this.testo.append(testo);
		return this;
	}
}
