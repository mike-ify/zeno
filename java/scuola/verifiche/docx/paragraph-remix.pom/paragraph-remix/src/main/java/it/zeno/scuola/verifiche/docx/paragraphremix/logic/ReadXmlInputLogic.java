package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class ReadXmlInputLogic implements AutoCloseable{
	
	private XMLEventReader originXmlReader;
	private XMLEvent event;
	private StartXmlElementReadLogic startXmlReader;
	private EndElementXmlReadLogic endXmlReader;
	private XMLInputFactory xmlInputFactory;
	
	public ReadXmlInputLogic() throws IOException, XMLStreamException {
		startXmlReader = new StartXmlElementReadLogic();
		endXmlReader = new EndElementXmlReadLogic();

		xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    }
	
	public XMLEventReader getReader() {
		return originXmlReader;
	}
	
	public void logic() throws Exception {

		while (originXmlReader.hasNext()) {

			event = originXmlReader.nextEvent();

			if (event.isStartElement())
				startXmlReader.setEvent(event).logic();

			if (event.isEndElement())
				endXmlReader.setEvent(event).logic();
		}
	}
	
	public StartXmlElementReadLogic getStartXmlReader() {
		return startXmlReader;
	}
	
	public EndElementXmlReadLogic getEndXmlReader() {
		return endXmlReader;
	}
	
	@Override
	public void close() throws Exception {
		originXmlReader.close();	
	}
	
	public void init(Path fileDoxXmlIniziale) throws XMLStreamException, IOException {
		originXmlReader = xmlInputFactory
		.createXMLEventReader(
			new FileInputStream(fileDoxXmlIniziale.toFile())
		);
		
		startXmlReader.accept(originXmlReader);
		endXmlReader.accept(originXmlReader);
	}
	
}
