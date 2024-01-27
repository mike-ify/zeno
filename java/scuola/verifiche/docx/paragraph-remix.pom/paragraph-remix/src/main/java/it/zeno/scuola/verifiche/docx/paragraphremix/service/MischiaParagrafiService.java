package it.zeno.scuola.verifiche.docx.paragraphremix.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.zeno.scuola.verifiche.docx.paragraphremix.logic.ReadXmlInputLogic;
import it.zeno.scuola.verifiche.docx.paragraphremix.logic.WriteXLSXGrigliaRisultatiLogic;
import it.zeno.scuola.verifiche.docx.paragraphremix.logic.WriteXmlFileDocxInElaborazioneLogic;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.AlunnoParagrafoDocx;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Domanda;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.ParagrafoDocx;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.QuestionarioParagrafoDocx;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Risposta;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.StrutturaMischiaParagrafi;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.XMLElement;
import it.zeno.utils.functions.ConsumerThrow;
import it.zeno.utils.interfaces.Service;

public class MischiaParagrafiService implements Service,AutoCloseable{
	private final static Logger LOG = LoggerFactory.getLogger(MischiaParagrafiService.class);
	private Instant startAt;
	private StrutturaMischiaParagrafi struttura;
	private final ReadXmlInputLogic xmlReader;
	private final WriteXmlFileDocxInElaborazioneLogic xmlWriter = new WriteXmlFileDocxInElaborazioneLogic();
	private boolean fineParagrafiRead = false;
	private WriteXLSXGrigliaRisultatiLogic xlsxWriter;
	
	private ParagrafoDocx currentParagrafo = new ParagrafoDocx(); 
	private List<ParagrafoDocx> paragrafi = new ArrayList<>(); 
	private Domanda lastDomanda;
	
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
		
		xlsxWriter = new WriteXLSXGrigliaRisultatiLogic(
			struttura.getPathXlsxRisultati()
		);
    }
	
	public void run() throws Exception {
		
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
			xmlWriter.writeXmlElement(startElement.getXml());
		})
		.setStartTextConsumer(startElement -> {
			XMLEvent event = xmlReader.getReader().nextEvent();
			currentParagrafo.appendTxt(event.asCharacters().getData());
		})
		.setStartTabConsumer(startElement -> 
			currentParagrafo.appendTxt("\t")
		)
		.setStartSectPrConsumer(startElement -> {
			fineParagrafiRead = true;
			
			List<ParagrafoDocx>intestazione = new ArrayList<>();
			List<ParagrafoDocx>domande = new ArrayList<>();
			
			Domanda domanda;
			for(ParagrafoDocx paragrafo : paragrafi) {
				if(paragrafo instanceof Domanda) {
					domanda = (Domanda)paragrafo;
					Collections.shuffle(domanda.getRisposte());
					domande.add(domanda);
				}else
					intestazione.add(paragrafo);
			}

			paragrafi = intestazione;
			writeParagrafi();
			
			Collections.shuffle(domande);
			paragrafi = domande;
			sanitizeParagrafi();
			writeParagrafi();
			
			QuestionarioParagrafoDocx questionario = intestazione.stream()
			.filter(i -> i instanceof QuestionarioParagrafoDocx)
			.map(QuestionarioParagrafoDocx.class::cast)
			.findFirst().get();
			
			xlsxWriter.setParagrafi(paragrafi);
			xlsxWriter.setQuestionario(questionario);
			xlsxWriter.logic();
			
			xmlWriter.writeXmlElement(startElement.getXml());
		})
		
		.setDefaultConsumer(xmlDefaultConsumer);
	}
	
	private void sanitizeParagrafi() {
		ParagrafoDocx paragrafo;
		for(int i = 0, len = paragrafi.size();i < len; i++) {
			paragrafo = paragrafi.get(i);
			if(paragrafo instanceof Domanda)
				Domanda.sanitize((Domanda)paragrafo,i);
		}
	}
	
	private void writeParagrafi() throws IOException {
    	
    	for(ParagrafoDocx paragrafo : paragrafi) {
    		
    		xmlWriter.writeXmlElement(paragrafo.toXml());
    		
    		if(paragrafo instanceof Domanda) {
    			
    			Domanda domanda = (Domanda)paragrafo;
    			
    			for(Risposta risposta : domanda.getRisposte()) 
    				xmlWriter.writeXmlElement(risposta.toXml());
    			
    			xmlWriter.writeXmlElement(ParagrafoDocx.XML_S);
    			xmlWriter.writeXmlElement(ParagrafoDocx.XML_S);
    		}
    	}
    	
    	xmlWriter.flush();
    	
    	paragrafi.clear();
    }
	
	private void registerXmlEndEvents() {
		xmlReader
		.getEndXmlReader()
		.setEndPConsumer(endElement -> {
			
			String pTxt = currentParagrafo.toText();
			
			LOG.info(pTxt);	
			
			Matcher matcherRisposta = Risposta.matcherRisposta(pTxt);
			
			Optional<QuestionarioParagrafoDocx> questionario = QuestionarioParagrafoDocx
			.checkQuestionario(pTxt);
			
			if(Domanda.isDomanda(pTxt))
				paragrafi.add(lastDomanda = new Domanda(currentParagrafo));
			
			else if(matcherRisposta.matches()) 
				lastDomanda.addRisposta(currentParagrafo);
			
			else if(questionario.isPresent())
				paragrafi.add(questionario.get());
			
			else if(pTxt.trim().startsWith("Cognome"))
				paragrafi.add(new AlunnoParagrafoDocx(currentParagrafo));
			
			else if(lastDomanda == null)
				paragrafi.add(new ParagrafoDocx(currentParagrafo));
			
			currentParagrafo.empty();
		})
		.setEndElementConsumer(endElement -> {
			xmlWriter.writeXmlElement(endElement.getXml());
		})
		.setDefaultConsumer(xmlDefaultConsumer);
	}
	
	private ConsumerThrow<XMLElement>xmlDefaultConsumer = el -> {
		if(fineParagrafiRead)
			xmlWriter.writeXmlElement(el.getXml());
		else
			currentParagrafo.appendXml(el.getXml());
	};
	
	public void endTimeCount() {
		LOG.info("Esegito in {} millisecondi",Duration.between(startAt, Instant.now()).toMillis());
	}

	@Override
	public void close() throws Exception {
		struttura.rimuoviCartellaElaborazione();
		currentParagrafo.empty(); 
		paragrafi.clear(); 
		lastDomanda = null;
		endTimeCount();
	}	
}
