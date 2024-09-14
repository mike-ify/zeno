package it.zeno.scuola.verifiche.word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;

@ApplicationScoped
public class XMLProducers {
	@Inject
	private QuestDocx data;

	@Inject
	private XMLInputFactory xmlInputFactory;
	
	@Produces
	public XMLStreamReader getXMLStreamReader() {
		try {
			return xmlInputFactory.createXMLStreamReader(new FileInputStream(data.getFileXMLInput().toFile()));
		} catch (FileNotFoundException | XMLStreamException e) {
			throw Log.error(e);
		}
	}	
	
	@Produces
	public XMLInputFactory getXMLInputFactory() {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		return xmlInputFactory;
	}
}
