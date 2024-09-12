package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import it.zeno.scuola.verifiche.docx.utils.XML;

abstract class XMLElement {
	protected XMLEventReader xmlReader;
	protected String name;
	protected String xml;
	
	public XMLElement(XMLEventReader xmlReader) {
		this.xmlReader = xmlReader;
	}

	public XMLEventReader getXmlReader() {
		return xmlReader;
	}

	public String getName() {
		return name;
	}

	public String getXml() {
		return xml;
	}
	
}
