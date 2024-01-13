package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.message.StringFormattedMessage;

import it.zeno.utils.functions.ConsumerThrow;

public class Alunno extends Paragrafo{
	public static final String XML_START = "<w:p w14:paraId=\"03E5D41E\" w14:textId=\"759FB500\" w:rsidR=\"00CE44F0\" w:rsidRPr=\"007F4FF0\" w:rsidRDefault=\"00CE44F0\" w:rsidP=\"00CE44F0\">"
	+ "<w:pPr><w:spacing w:before=\"40\" w:after=\"0\" w:line=\"240\" w:lineRule=\"auto\"/>"
	+ "<w:ind w:left=\"993\" w:hanging=\"993\"/><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
	+ "<w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr></w:pPr><w:r w:rsidRPr=\"007F4FF0\">"
	+ "<w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
	+ "<w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr><w:t>";
	private String nome;
	private String cognome;
	private String classe;
	private String data;
	
	public Alunno(String nomeCognomeSeparatiConHash) {
		setDataNow();
		int indexOfHash = nomeCognomeSeparatiConHash.indexOf('#');
		nome = nomeCognomeSeparatiConHash.substring(0,indexOfHash);
		cognome = nomeCognomeSeparatiConHash.substring(indexOfHash + 1);
	}
	
	private void setDataNow() {
		data = DateFormat
		.getDateInstance(DateFormat.LONG, Locale.ITALY)
		.format(new Date());
	}

	public Alunno(Paragrafo p, ConsumerThrow<Alunno> alunnoConsumer) throws Exception {
		super(p);
		setDataNow();
		alunnoConsumer.accept(this);
		String txt = testo.toString().replace('\t', ' ');
		txt = MessageFormat.format(
			"Cognome {0} Nome {1} Classe {2} Data {3}",
			cognome,nome,classe,data
		);
		empty();
		setTesto(txt);
		appendXmlElement(XML_START);
		appendXmlElement(txt);
		appendXmlElement(XML_END);
	}


	@Override
	public String toString() {
		return cognome.concat("-").concat(nome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}
}
