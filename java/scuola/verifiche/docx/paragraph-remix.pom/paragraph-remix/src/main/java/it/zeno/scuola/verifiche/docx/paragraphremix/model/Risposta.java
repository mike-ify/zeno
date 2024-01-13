package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Risposta extends Paragrafo{

	private final static Pattern PATTERN_RISPOSTA_START = Pattern.compile("^[A-Z]\\[(\\s|X)");
	public final static Pattern PATTERN_CODICE_RISPOSTA_VALIDA = Pattern.compile("^[A-Z]\\[X");
	public final static Pattern PATTERN_CODICE_RISPOSTA_COMPOSE = Pattern.compile("^([A-Z]?)(\\[?)(X?)$");
	public final static Pattern PATTERN_RISPOSTA = Pattern.compile("^[A-Z]\\[(\\s|X).{0,}$");
	
	private Domanda parent;
	private int ordine;
	private String codiceRisposta;
	private String testoRisposta;
	private boolean valid = false;

	public static boolean isRispostaStart(String testoParagrafo){
		return PATTERN_RISPOSTA_START.matcher(testoParagrafo).matches();
	}	
	
	public static Matcher matcherRisposta(String testoParagrafo){
		return PATTERN_RISPOSTA.matcher(testoParagrafo);
	}

	public Risposta(Paragrafo p,Domanda d, int o) {
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
		return codiceRisposta;
	}

	public void setCodiceRisposta(String codiceRisposta) {
		this.codiceRisposta = codiceRisposta;
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

    public static String tryRimuoviCrocetta(Paragrafo currentParagrafo, String testo) {
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

}
