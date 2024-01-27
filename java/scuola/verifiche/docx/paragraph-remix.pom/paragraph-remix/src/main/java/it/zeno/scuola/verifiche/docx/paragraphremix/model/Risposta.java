package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.zeno.utils.Str;

public class Risposta extends ParagrafoDocx{

	private final static Pattern PATTERN_RISPOSTA_START = Pattern.compile("^[A-Z]\\[(\\s|X)");
	public final static Pattern PATTERN_CODICE_RISPOSTA_VALIDA = Pattern.compile("^[A-Z]\\[X");
	public final static Pattern PATTERN_CODICE_RISPOSTA_COMPOSE = Pattern.compile("^([A-Z]?)(\\[?)(X?)$");
	public final static Pattern PATTERN_RISPOSTA = Pattern.compile("^[A-Z]\\[(\\s|X).{0,}$");
	public final static String XML_START = "<w:p w:rsidRPr='003135E8' w:rsidRDefault='00493E32' w:rsidR='00493E32' w:rsidP='00493E32' w14:paraId='1CE1F724' w14:textId='3FDCA43A'>"
			+ "<w:pPr><w:spacing w:line='240' w:before='40' w:after='0' w:lineRule='auto'></w:spacing>"
			+ "<w:ind w:left='851' w:hanging='851'></w:ind>"
			+ "<w:rPr><w:rFonts w:hAnsi='Consolas' w:cstheme='minorHAnsi' w:ascii='Consolas'></w:rFonts>"
			+ "<w:sz w:val='24'></w:sz><w:szCs w:val='24'></w:szCs>"
			+ "</w:rPr></w:pPr><w:proofErr w:type='gramStart'></w:proofErr>"
			+ "<w:r w:rsidRPr='003135E8'><w:rPr><w:rFonts w:hAnsi='Consolas' w:cstheme='minorHAnsi' w:ascii='Consolas'></w:rFonts>"
			+ "<w:sz w:val='24'></w:sz><w:szCs w:val='24'></w:szCs>"
			+ "</w:rPr><w:t xml:space=\"preserve\">";
	public static final String XML_TEXT_START = "<w:r w:rsidRPr=\"003135E8\"><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
			+ "<w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr><w:t xml:space=\"preserve\">";
	
	private Domanda parent;
	private int ordine;
	private String codiceRisposta;
	private String codiceRiordinatoRisposta;
	private String testoRisposta;
	private boolean valid = false;

	public static boolean isRispostaStart(String testoParagrafo){
		return PATTERN_RISPOSTA_START.matcher(testoParagrafo).matches();
	}	
	
	public static Matcher matcherRisposta(String testoParagrafo){
		return PATTERN_RISPOSTA.matcher(testoParagrafo);
	}

	public Risposta(ParagrafoDocx p,Domanda d, int o) {
		super(p);
		parent = d;
		ordine = o;
	}

	public int getOrdine() {
		return ordine;
	}

	public Domanda getDomanda() {
		return parent;
	}

	public String getCodiceRisposta() {
		return ""+codiceRisposta.charAt(0);
	}

	public void setCodiceRisposta(String codiceRisposta) {
		this.codiceRisposta = codiceRisposta;
		valid = codiceRisposta.indexOf('X') > -1;
	}
	
	public void setCodiceRiordinatoRisposta(String codiceRisposta) {
		this.codiceRiordinatoRisposta = codiceRisposta;
	}

	public String getTestoRisposta() {
		return testoRisposta;
	}

	public String getTestoRispostaFull() {
		return toText();
	}

	public void setTestoRisposta(String testoRisposta) {
		this.testoRisposta = testoRisposta;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

    public static String tryRimuoviCrocetta(ParagrafoDocx currentParagrafo, String testo) {
		if(PATTERN_CODICE_RISPOSTA_VALIDA.matcher(currentParagrafo.toText()).matches()) {
			Matcher m = PATTERN_CODICE_RISPOSTA_COMPOSE.matcher(testo);
			StringBuilder r = new StringBuilder();
			int c = 1,t = m.groupCount() - 1;
			for(;c <= t;c++) {
				try {
					String g = m.group(c);
					r.append(g);
				}catch(Exception e) {}
			}
			r.append(' ');
			testo = m.replaceAll(r.toString());
		}
        return testo;
    }

	public String getCodiceRiordinatoRisposta() {
		// TODO Auto-generated method stub
		return codiceRiordinatoRisposta;
	}

	public String getLettera() {
		return Character.toString(codiceRiordinatoRisposta.charAt(0));
	}

	public static void sanitize(Risposta risposta, int rispostaNu) {

		String text = risposta.toText();
		
		int tabi = text.indexOf('\t');
		String codice = text.substring(0,tabi);
		
		risposta
		.empty();
		
		risposta
		.setCodiceRisposta(codice);
		
		risposta
		.setCodiceRiordinatoRisposta(
			Character.toString(
				Str.getAalfabeto(rispostaNu)
			) + "[ ]"
		);
		
		text = text.substring(tabi + 1);
		
		risposta
		.appendXml(Risposta.XML_START)
		.appendXml(risposta.getCodiceRiordinatoRisposta())
		.appendXml(ParagrafoDocx.XML_TEXT_END)
		.appendXml(ParagrafoDocx.XML_TAB)
		.appendXml(Risposta.XML_TEXT_START)
		.appendXml(text)
		.appendXml(ParagrafoDocx.XML_END);
				
	}

}
