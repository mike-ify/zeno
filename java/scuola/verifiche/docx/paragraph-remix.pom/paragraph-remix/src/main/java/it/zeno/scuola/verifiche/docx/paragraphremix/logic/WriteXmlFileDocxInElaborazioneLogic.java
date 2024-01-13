package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
		writeGrigliaRisposteCorrette();
		paragrafi = intestazione;
		writeParagrafi();
		paragrafi = domande;
		writeParagrafi();
		writeXmlElement(xml);
	}
public static void main(String[] args) throws FileNotFoundException, IOException {
	writeGrigliaRisposteCorrette();
}
	private static  void writeGrigliaRisposteCorrette() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = (XSSFSheet) wb.createSheet();

            // Set which area the table should be placed in
            AreaReference reference = wb.getCreationHelper().createAreaReference(
                    new CellReference(2, 2),new CellReference(4, 4));

            // Create
            XSSFTable table = sheet.createTable(reference); //creates a table having 3 columns as of area reference
            // but all of those have id 1, so we need repairing
            table.getCTTable().getTableColumns().getTableColumnArray(1).setId(2);
            table.getCTTable().getTableColumns().getTableColumnArray(2).setId(3);

            table.setName("Test");
            table.setDisplayName("Test_Table");

            // For now, create the initial style in a low-level way
            table.getCTTable().addNewTableStyleInfo();
            table.getCTTable().getTableStyleInfo().setName("TableStyleMedium2");

            // Style the table
            XSSFTableStyleInfo style = (XSSFTableStyleInfo) table.getStyle();
            style.setName("TableStyleMedium2");
            style.setShowColumnStripes(false);
            style.setShowRowStripes(true);
            style.setFirstColumn(false);
            style.setLastColumn(false);
            style.setShowRowStripes(true);
            style.setShowColumnStripes(true);

            // Set the values for the table
            XSSFRow row;
            XSSFCell cell;
            for (int i = 0; i < 3; i++) {
                // Create row
                row = sheet.createRow(i);
                for (int j = 0; j < 3; j++) {
                    // Create cell
                    cell = row.createCell(j);
                    if (i == 0) {
                        cell.setCellValue("Column" + (j + 1));
                    } else {
                        cell.setCellValue((i + 1.0) * (j + 1.0));
                    }
                }
            }

            // Save
            try (FileOutputStream fileOut = new FileOutputStream("ooxml-table.xlsx")) {
                wb.write(fileOut);
            }
            System.out.println("fine");
        }
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
