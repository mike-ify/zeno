package it.zeno.scuola.verifiche.word.model;

import javax.enterprise.context.Dependent;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import it.zeno.utils.file.XML;

@Dependent
public class StartXMLElement extends XMLElement{
	private StartElement startElement;
	
	public StartXMLElement(XMLEventReader xmlReader) {
		super(xmlReader);
	}
	
	public StartXMLElement accept(XMLEvent xmlEvent) {
		startElement = xmlEvent.asStartElement();
		name = startElement.getName().getLocalPart();
		xml = XML.sanitize(startElement.toString());
		return this;
	}
	
	public StartElement getStartElement() {
		return startElement;
	}
}
