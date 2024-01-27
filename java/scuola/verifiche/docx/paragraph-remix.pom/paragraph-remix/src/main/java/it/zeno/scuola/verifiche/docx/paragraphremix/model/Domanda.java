package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import it.zeno.utils.Str;


public class Domanda extends ParagrafoDocx{

	public final static Pattern PATTERN_CODICE_DOMANDA = Pattern.compile("^(D\\d{2}):.+$");

	public static final String XML_START = "<w:p w:rsidRPr='00493E32' w:rsidRDefault='00493E32' w:rsidR='00493E32' w:rsidP='00493E32' w14:paraId='2602AAE6' w14:textId='13F23426'>"
	+ "<w:pPr><w:spacing w:line='240' w:before='40' w:after='0' w:lineRule='auto'></w:spacing>"
	+ "<w:ind w:left='851' w:hanging='851'></w:ind><w:rPr><w:rFonts w:hAnsi='Consolas' w:cstheme='minorHAnsi' w:ascii='Consolas'></w:rFonts>"
	+ "<w:b></w:b><w:sz w:val='24'></w:sz><w:szCs w:val='24'></w:szCs><w:u w:val='single'></w:u></w:rPr></w:pPr><w:r w:rsidRPr='003135E8'>"
	+ "<w:rPr><w:rFonts w:hAnsi='Consolas' w:cstheme='minorHAnsi' w:ascii='Consolas'></w:rFonts>"
	+ "<w:b></w:b><w:sz w:val='24'></w:sz><w:szCs w:val='24'></w:szCs>"
	+ "</w:rPr><w:t xml:space=\"preserve\">";
	public static final String XML_TEXT_START = "<w:r><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
			+ "<w:b/><w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr><w:tab/><w:t>";

	private String codiceDomanda;
	private String codiceRiordinatoDomanda;
	private String testoDomanda;
	private List<Risposta> risposte; 
		
	public Domanda(ParagrafoDocx p) {
		super(p);
		risposte = new ArrayList<>();
	}
	
	public Domanda setTestoDomanda(String testoDomanda) {
		this.testoDomanda = testoDomanda;
		return this;
	}

	public String getCodiceDomanda() {
		return codiceDomanda;
	}
	
	public Domanda setCodiceDomanda(String codiceDomanda) {
		this.codiceDomanda = codiceDomanda;
		return this;
	}
	
	public String getTestoDomanda(){
		return testoDomanda;
	}
	
	public String getTestoDomandaFull(){
		return toText();
	}

	public int addRisposta(ParagrafoDocx currentParagrafo) {
		int ord = risposte.size();
		Risposta r = new Risposta(currentParagrafo, this, ord);
		risposte.add(r);
		return risposte.size() - 1;
	}

    public static boolean isDomanda(String pTxt) {
        return Domanda.PATTERN_CODICE_DOMANDA.matcher(pTxt).matches();
    }

    public List<Risposta> getRisposte() {
        return risposte;
    }
    
	public Domanda setCodice(String codice) {
		codiceDomanda = codice;
		return this;
	}

	public ParagrafoDocx setCodiceRiordinato(String codice) {
		codiceRiordinatoDomanda = codice;
		return this;
	}

	public static void sanitize(Domanda domanda, int domandaNu) {
		
		String text = domanda.toText();
		
		int tabi = text.indexOf('\t');
		
		StringBuilder codice = new StringBuilder(text.substring(0,tabi))
		.append('D')
		.append(domandaNu < 10 ? '0' : Str.VOID)
		.append(domandaNu)
		.append(':');
		
		text = text.substring(tabi + 1);
		
		domanda
		.empty();
		
		domanda
		.setCodiceRiordinato(codice.toString())
		.setTesto(text)
		.appendXml(Domanda.XML_START)
		.appendXml(codice)
		.appendXml(ParagrafoDocx.XML_TEXT_END)
		.appendXml(ParagrafoDocx.XML_TAB)
		.appendXml(Domanda.XML_TEXT_START)
		.appendXml(text)
		.appendXml(ParagrafoDocx.XML_END);
		
		Risposta risposta;
		for(int i = 0, len = domanda.getRisposte().size(); i < len; i++) {
			risposta = domanda.getRisposte().get(i);
			Risposta.sanitize(risposta, i);
		}
	}	
}
