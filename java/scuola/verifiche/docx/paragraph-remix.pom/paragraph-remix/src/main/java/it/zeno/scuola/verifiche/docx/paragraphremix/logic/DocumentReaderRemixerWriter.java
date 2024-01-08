package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class DocumentReaderRemixerWriter{

	private XMLEventReader originXmlReader;
	private DocumentoWriter destXmlWriter;
	private XMLEvent event;

	private StartElementXmlReader startXmlReader;
	private EndElementXmlReader endXmlReader;
	private XMLInputFactory xmlInputFactory;
	
	public DocumentReaderRemixerWriter() throws IOException, XMLStreamException {
		this.startXmlReader = new StartElementXmlReader();
		this.endXmlReader = new EndElementXmlReader();

		xmlInputFactory = XMLInputFactory.newInstance();
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
	}

	public DocumentReaderRemixerWriter setDestXmlWriter(Path docxCopyedPathExploded) throws IOException {
		Path documentXmlCopyed = docxCopyedPathExploded.resolve("word/document.xml");
				
		Files.newInputStream(documentXmlCopyed, StandardOpenOption.TRUNCATE_EXISTING);
		this.destXmlWriter = new DocumentoWriter(documentXmlCopyed);
		return this;
	}
	
	public DocumentReaderRemixerWriter createOriginXmlReader(String originXmlFile) throws FileNotFoundException, XMLStreamException {
		this.originXmlReader = xmlInputFactory.createXMLEventReader(new FileInputStream(Paths.get(originXmlFile).toFile()));
		return this;
	}
	
	public DocumentReaderRemixerWriter logic() throws Exception {

		while (originXmlReader.hasNext()) {

			event = originXmlReader.nextEvent();

			if (event.isStartElement())
				startXmlReader.logic(event, destXmlWriter, originXmlReader::nextEvent);

			if (event.isEndElement())
				endXmlReader.logic(event, destXmlWriter);
		}
		
		close();
		
		return this;
	}

	private void close() throws XMLStreamException, IOException {
		originXmlReader.close();
		destXmlWriter.close();
	}

}
