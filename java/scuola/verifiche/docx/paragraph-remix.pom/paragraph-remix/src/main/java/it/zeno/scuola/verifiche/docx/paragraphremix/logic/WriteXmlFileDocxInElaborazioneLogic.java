package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFTableStyleInfo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.Alunno;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Domanda;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Paragrafo;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Questionario;
import it.zeno.scuola.verifiche.docx.paragraphremix.model.Risposta;
import it.zeno.utils.functions.ConsumerThrow;

public class WriteXmlFileDocxInElaborazioneLogic implements AutoCloseable{
	private static final Logger LOG = LoggerFactory.getLogger(WriteXmlFileDocxInElaborazioneLogic.class);
	
	private BufferedWriter documentXmlWriter;
	
	private Paragrafo currentParagrafo = new Paragrafo(); 
	private List<Paragrafo> paragrafi = new ArrayList<>(); 

	private Domanda lastDomanda;
	private ConsumerThrow<Alunno> alunnoConsumer;
	public void setAlunnoConsumer(ConsumerThrow<Alunno> alunnoConsumer) {
		this.alunnoConsumer = alunnoConsumer;
	}
	public WriteXmlFileDocxInElaborazioneLogic init(Path documentXmlPath) throws IOException {
		documentXmlWriter = Files.newBufferedWriter(documentXmlPath,StandardOpenOption.TRUNCATE_EXISTING);
		
//		examples\temp\crocette1-rossi-mario\word\document.xml
		String s = documentXmlPath.getParent().getParent().getFileName().toString();
		int i1 = s.indexOf('-');
		int i2 = s.lastIndexOf('-');
		final String CO = s.substring(i1 + 1, i2);
		final String NO = s.substring(i2 + 1);
		final String CL = s.substring(0,i1);
		setAlunnoConsumer(alunno -> {
			alunno.setNome(NO);
			alunno.setCognome(CO);
			alunno.setClasse(CL);
		});
		return this;
	}
	public WriteXmlFileDocxInElaborazioneLogic writeXmlElement(String xml) throws IOException{
		documentXmlWriter.append(xml);
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
		currentParagrafo.appendTxt(testo);

		if(testo.length() > 0 && Risposta.isRispostaStart(currentParagrafo.toText())) {
			testo = Risposta.tryRimuoviCrocetta(currentParagrafo,testo);
//			Domanda domanda = (Domanda)paragrafi.get(paragrafi.size() - 1);
//			domanda.addRisposta(currentParagrafo);
			xml = "<w:t xml:space=\"preserve\">";
		}
		currentParagrafo.appendXmlElement(xml).appendXmlElement(testo);
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

    public void addXmlParagrafoEndLogic(String xml) throws Exception {
		currentParagrafo.appendXmlElement(xml);
		
		String pTxt = currentParagrafo.toText();
		LOG.debug(pTxt);	
		Matcher matcherRisposta = Risposta.matcherRisposta(pTxt);
		
		Questionario q = Questionario.checkQuestionario(pTxt);
		
		if(Domanda.isDomanda(pTxt)) {
			paragrafi.add(new Domanda(currentParagrafo));
			lastDomanda = (Domanda) paragrafi.get(paragrafi.size() - 1);
		}
		else
		if(matcherRisposta.matches()) {
			//TODO: griglia risposte corrette
			//String crocetta = matcherRisposta.group(1).trim();
			lastDomanda.addRisposta(currentParagrafo);
		}
		else if(q != null) {
			paragrafi.add(q);
		}
		else if(pTxt.trim().startsWith("Cognome")) {
			paragrafi.add(new Alunno(currentParagrafo,alunnoConsumer));
		}
		else if(lastDomanda == null) {
			paragrafi.add(new Paragrafo(currentParagrafo));
		}
		currentParagrafo.empty();
    }

    private void writeParagrafi() throws IOException {
    	for(Paragrafo p : paragrafi) {
    		writeXmlElement(p.toXml());
    		if(p instanceof Domanda) {
    			for(Risposta r : Domanda.class.cast(p).getRisposte()) 
    				writeXmlElement(r.toXml());
    			writeXmlElement(Paragrafo.XML_S);
    			writeXmlElement(Paragrafo.XML_S);
    		}
    	}
    	documentXmlWriter.flush();
    	paragrafi.clear();
    }

	public void writeXmlSectPrStart(String xml) throws IOException {
		Domanda d;
		List<Paragrafo>intestazione = new ArrayList<>();
		List<Paragrafo>domande = new ArrayList<>();
		for(Paragrafo p : paragrafi) {
			if(p instanceof Domanda) {
				d = (Domanda)p;
				Collections.shuffle(d.getRisposte());
				domande.add(d);
			}else
				intestazione.add(p);
		}
		
		Collections.shuffle(domande);	
		paragrafi = domande;
		sanitizeParagrafi();
		Questionario q = intestazione.stream()
		.filter(i-> i instanceof Questionario)
		.map(Questionario.class::cast)
		.findFirst().get();
		writeGrigliaRisposteCorrette(q);
		paragrafi = intestazione;
		writeParagrafi();
		paragrafi = domande;
		writeParagrafi();
		writeXmlElement(xml);
	}
	private  void writeGrigliaRisposteCorrette(Questionario q) throws FileNotFoundException, IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        XSSFSheet spreadsheet = workbook.createSheet(" Student Data "); 
        XSSFRow row; 
        int rowid = 0; 
        
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);                 

        Cell cell;
        row = spreadsheet.createRow(rowid++); 
        row = spreadsheet.createRow(rowid++); 
        int cellid = 0; 
        row.createCell(cellid++)
        .setCellValue(""); 
        cell = row.createCell(cellid++);
        cell.setCellValue(q.getNumeroQuestionario().toString());
        cell.setCellStyle(style);
        for (Paragrafo p : paragrafi) { 
        	Domanda d = (Domanda)p;
            List<String> s = new ArrayList<>();
            for (Risposta r : d.getRisposte().stream().filter(r->r.isValid()).collect(Collectors.toList())) { 
            	s.add(r.getLettera());
            } 
            row.createCell(cellid++)
            .setCellValue(String.join("",s)); 
        } 
  
        FileOutputStream out = new FileOutputStream(new File("examples/out/risultati.xlsx")); 
        workbook.write(out); 
        out.close(); 
	}
	private void sanitizeParagrafi() {
		int domanda = 1,risposta = 0;
		
        String text,codice;
        int tabi = 0;
		for(Paragrafo p : paragrafi) {
			
			text = p.toText();
			
			if(p instanceof Domanda) {
				tabi = text.indexOf('\t');
				codice = text.substring(0,tabi);
				codice = "D" + (domanda < 10 ? "0" : "") + domanda++ + ":";
				text = text.substring(tabi + 1);
				Domanda.class.cast(p)
				.setCodiceRiordinato(codice)
				.empty()
				.setTesto(text)
				.appendXmlElement(Domanda.XML_START)
				.appendXmlElement(codice)
				.appendXmlElement(Paragrafo.XML_TEXT_END)
				.appendXmlElement(Paragrafo.XML_TAB)
				.appendXmlElement(Domanda.XML_TEXT_START)
				.appendXmlElement(text)
				.appendXmlElement(Paragrafo.XML_END);
				risposta = 0;
				for(Risposta r : Domanda.class.cast(p).getRisposte()) {
					text = r.toText();
					
					tabi = text.indexOf('\t');
					codice = text.substring(0,tabi);
					r.empty();
					r.setCodiceRisposta(codice);
					r.setCodiceRiordinatoRisposta(Character.toString(Risposta.getAalfabeto(risposta++)) + "[ ]");
					text = text.substring(tabi + 1);
					
					r.appendXmlElement(Risposta.XML_START)
					.appendXmlElement(r.getCodiceRiordinatoRisposta())
					.appendXmlElement(Paragrafo.XML_TEXT_END)
					.appendXmlElement(Paragrafo.XML_TAB)
					.appendXmlElement(Risposta.XML_TEXT_START)
					.appendXmlElement(text)
					.appendXmlElement(Paragrafo.XML_END);
				
				}
			}
		}
	}
	public void close() throws IOException {
		currentParagrafo.empty(); 
		paragrafi.clear(); 
		documentXmlWriter.close();
		lastDomanda = null;
	}

}
