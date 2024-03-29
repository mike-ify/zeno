package it.zeno.scuola.verifiche.docx.paragraphremix.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.zeno.scuola.verifiche.docx.paragraphremix.logic.ReadXmlInputLogic;
import it.zeno.scuola.verifiche.docx.paragraphremix.logic.WriteXLSXGrigliaRisultatiLogic;
import it.zeno.scuola.verifiche.docx.paragraphremix.logic.WriteXmlFileDocxInElaborazioneLogic;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.StrutturaMischiaParagrafi;
import it.zeno.utils.interfaces.Service;

public class MischiaParagrafiService implements Service,AutoCloseable{
	private final static Logger LOG = LoggerFactory.getLogger(MischiaParagrafiService.class);

	private Instant startAt;
	
	private StrutturaMischiaParagrafi struttura;
	private final ReadXmlInputLogic xmlReader;
	private final WriteXmlFileDocxInElaborazioneLogic xmlWriter;

	private boolean fineParagrafiRead = false;

	private WriteXLSXGrigliaRisultatiLogic xlsxWriter;
	
	public MischiaParagrafiService(
		String strPathBase,
		String nomeFileDocxIniziale,
		String[]alunni
	) throws IOException, XMLStreamException {
		startAt = Instant.now();
		
		struttura = new StrutturaMischiaParagrafi()
		.setPathBase(strPathBase)
		.setNomeFileDocxInput(nomeFileDocxIniziale)
		.setAlunni(alunni);
		
		xmlReader = new ReadXmlInputLogic();
		
		registerXmlStartEvents();
		registerXmlEndEvents();
		
		xmlWriter = new WriteXmlFileDocxInElaborazioneLogic();
		xlsxWriter = new WriteXLSXGrigliaRisultatiLogic(
			struttura.getPathXlsxRisultati()
		);
    }
	
	public void run() throws Exception {
		
		xmlWriter.setQConsumer(x -> {
			xlsxWriter.setParagrafi(x.getParagrafi());
			xlsxWriter.setQuestionario(x.getQuestionario());
			xlsxWriter.logic();
		});
		
		struttura.eachAlunni(alunno -> {
			
			struttura
			.copiaFileDocxInputPerElaborazione(alunno)
			.estraiFileDocxInElaborazione();
			
			xmlWriter.init(struttura.getPathFileDocxXMLDaScrivere());
			xmlReader.init(struttura.getPathFileDocxXMLDaLeggere());

			xmlReader.logic();
			
			xmlReader.close();
			xmlWriter.close();
			
			struttura
			.spostaXmlElaborato(alunno);
		});
		
		xlsxWriter.close();
	}

	private void registerXmlStartEvents() {
		xmlReader
		.getStartXmlReader()
		.setStartDocumentConsumer(startElement -> {
			fineParagrafiRead = false;
			xmlWriter.writeXmlDocumentStart(startElement.getXml());
		})
		.setStartBodyConsumer(startElement -> {
			fineParagrafiRead = false;
			xmlWriter.writeXmlBodyStart(startElement.getXml());
		})
		.setStartTextConsumer(startElement -> {
			XMLEvent event = xmlReader.getReader().nextEvent();
			xmlWriter.appendTestoParagrafo(startElement.getXml(),event.asCharacters().getData());
		})
		.setStartTabConsumer(startElement -> {
			xmlWriter.appendXmlTabStart(startElement.getXml());
		})
		.setStartSectPrConsumer(startElement -> {
			fineParagrafiRead = true;
			xmlWriter.writeXmlSectPrStart(startElement.getXml());
		})
		.setStartDefaultConsumer(startElement -> {
			if(fineParagrafiRead)
				xmlWriter.writeXmlElement(startElement.getXml());
			else
				xmlWriter.appendXmlElement(startElement.getName(),startElement.getXml());
		});
	}
	
	private void registerXmlEndEvents() {
		xmlReader
		.getEndXmlReader()
		.setEndPConsumer(startElement -> {
			xmlWriter.addXmlParagrafoEndLogic(startElement.getXml());
		})
		.setEndPConsumer(startElement -> {
			xmlWriter.addXmlParagrafoEndLogic(startElement.getXml());
		})
		.setEndSectPrConsumer(startElement -> {
			xmlWriter.writeXmlElement(startElement.getXml());
		})
		.setEndBodyConsumer(startElement -> {
			xmlWriter.writeXmlElement(startElement.getXml());
		})
		.setEndDocumentConsumer(startElement -> {
			xmlWriter.writeXmlElement(startElement.getXml());
		})
		.setEndDefaultConsumer(startElement -> {
			if(fineParagrafiRead)
				xmlWriter.writeXmlElement(startElement.getXml());
			else
				xmlWriter.appendXmlElement(startElement.getName(),startElement.getXml());
		});
	}
	
	public void endTimeCount() {
		LOG.info("Esegito in {} millisecondi",Duration.between(startAt, Instant.now()).toMillis());
	}

	@Override
	public void close() throws Exception {
		struttura.rimuoviCartellaElaborazione();
		endTimeCount();
	}	
}
