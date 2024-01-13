package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

import it.zeno.scuola.verifiche.docx.utils.XML;

public class EndXMLElement extends XMLElement{
	private EndElement endElement;
	
	public EndXMLElement(XMLEventReader xmlReader) {
		super(xmlReader);
	}
	
	public EndXMLElement accept(XMLEvent xmlEvent) {
		endElement = xmlEvent.asEndElement();
		name = endElement.getName().getLocalPart();
		xml = XML.sanitize(endElement.toString());
		return this;
	}
	
	public EndElement getEndElement() {
		return endElement;
	}


	
}
