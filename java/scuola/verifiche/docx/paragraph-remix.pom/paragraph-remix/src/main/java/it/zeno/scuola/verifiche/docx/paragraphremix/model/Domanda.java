package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Domanda extends Paragrafo{

	public final static Pattern PATTERN_CODICE_DOMANDA = Pattern.compile("^(D\\d{2}):.+$");

	private String codiceDomanda;
	private String testoDomanda;
	private List<Risposta> risposte; 
		
	public Domanda(Paragrafo p) {
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

	public int addRisposta(Paragrafo currentParagrafo) {
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
	
}
