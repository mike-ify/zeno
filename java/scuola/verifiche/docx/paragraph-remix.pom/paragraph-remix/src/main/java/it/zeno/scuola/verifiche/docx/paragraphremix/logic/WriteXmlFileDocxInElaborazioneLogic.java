package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.Domanda;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Paragrafo;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Risposta;

public class WriteXmlFileDocxInElaborazioneLogic implements AutoCloseable{
	private static final Logger LOG = LoggerFactory.getLogger(WriteXmlFileDocxInElaborazioneLogic.class);
	
	private BufferedWriter documentXmlWriter;
	
	private Paragrafo currentParagrafo = new Paragrafo(); 
	private List<Paragrafo> paragrafi = new ArrayList<>(); 

	private Domanda lastDomanda;

	public static boolean firstDomanda;

	public WriteXmlFileDocxInElaborazioneLogic() throws IOException {
		firstDomanda = true;
	}
	public WriteXmlFileDocxInElaborazioneLogic init(Path documentXmlPath) throws IOException {
		documentXmlWriter = Files.newBufferedWriter(documentXmlPath,StandardOpenOption.TRUNCATE_EXISTING);
		return this;
	}
	public WriteXmlFileDocxInElaborazioneLogic writeXmlElement(String xml) throws IOException{
		documentXmlWriter.append(xml);
		documentXmlWriter.append("\n");
		return this;
	}

    public WriteXmlFileDocxInElaborazioneLogic writeXmlDocumentStart(String xml) throws IOException {
		writeXmlElement("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
		.writeXmlElement(xml);
		
		return this;
    }    
	
	public WriteXmlFileDocxInElaborazioneLogic writeXmlBodyStart(String body) throws IOException {
		return writeXmlElement(body);
    }

	public void appendTestoParagrafo(String xml, String testo) {
		currentParagrafo.appendXmlElement(xml).appendTxt(testo);

		if(testo.length() > 0 && Risposta.isRispostaStart(testo)) {
			testo = Risposta.tryRimuoviCrocetta(testo);
//			Domanda domanda = (Domanda)paragrafi.get(paragrafi.size() - 1);
//			domanda.addRisposta(currentParagrafo);
		}
		currentParagrafo.appendXmlElement(testo);
	}

	public void appendXmlElement(String name, String xml) {
		currentParagrafo
		.appendXmlElementName(name)
		.appendXmlElement(xml);
	}

    public void appendXmlTabStart(String xml) {
		currentParagrafo
		.appendXmlElement(xml)
		.appendTxt("\t");
    }

    public void addXmlParagrafoEndLogic(String xml) throws IOException {
		currentParagrafo.appendXmlElement(xml);
		
		String pTxt = currentParagrafo.toText();
		LOG.debug(pTxt);	
		Matcher matcherRisposta = Risposta.matcherRisposta(pTxt);
				
		if(Domanda.isDomanda(pTxt)) {
			if(firstDomanda) {
				writeParagrafi();
				firstDomanda = false;
			}
			paragrafi.add(new Domanda(currentParagrafo));
			lastDomanda = (Domanda) paragrafi.get(paragrafi.size() - 1);
		}
		else
		if(matcherRisposta.matches()) {
			//TODO: griglia risposte corrette
			//String crocetta = matcherRisposta.group(1).trim();
			lastDomanda.addRisposta(currentParagrafo);
		}
		else if(firstDomanda)//paragrafi intestazione
			paragrafi.add(new Paragrafo(currentParagrafo));
		else
			paragrafi.add(new Paragrafo(currentParagrafo,true));
		currentParagrafo.empty();
    }

    private void writeParagrafi() throws IOException {
    	for(Paragrafo p : paragrafi) {
    		writeXmlElement(p.toXml());
    		if(p instanceof Domanda) 
    			for(Risposta r : Domanda.class.cast(p).getRisposte()) 
    				writeXmlElement(r.toXml());
    	}
    	documentXmlWriter.flush();
    	paragrafi.clear();
    }

	public void writeXmlSectPrStart(String xml) throws IOException {
		Domanda d;
		List<Paragrafo>spazi = new ArrayList<>();
		List<Paragrafo>paragrafi2 = new ArrayList<>();
		for(Paragrafo p : paragrafi) {
			if(p instanceof Domanda) {
				d = (Domanda)p;
				Collections.shuffle(d.getRisposte());
			}else {
				spazi.add(p);
			}
		}
		
		for(Paragrafo p : spazi)
			paragrafi.remove(p);
		
		Collections.shuffle(paragrafi);	
		int l = paragrafi.size();
		for(int x = 0; x < l; x++) {
			paragrafi2.add(paragrafi.get(x));
			paragrafi2.add(spazi.get(0));
			paragrafi2.add(spazi.get(0));
		}
		paragrafi = paragrafi2;
		writeParagrafi();
		writeXmlElement(xml);
	}

	public void close() throws IOException {
		currentParagrafo.empty(); 
		paragrafi.clear(); 
		firstDomanda = true;
		documentXmlWriter.close();
	}

}
