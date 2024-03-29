package it.zeno.scuola.verifiche.docx.paragraphremix.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Questionario extends Paragrafo{
	public static final String XML_START = "<w:p w14:paraId=\"03E5D41E\" w14:textId=\"759FB500\" w:rsidR=\"00CE44F0\" w:rsidRPr=\"007F4FF0\" w:rsidRDefault=\"00CE44F0\" w:rsidP=\"00CE44F0\">"
	+ "<w:pPr><w:spacing w:before=\"40\" w:after=\"0\" w:line=\"240\" w:lineRule=\"auto\"/>"
	+ "<w:ind w:left=\"993\" w:hanging=\"993\"/><w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
	+ "<w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr></w:pPr><w:r w:rsidRPr=\"007F4FF0\">"
	+ "<w:rPr><w:rFonts w:ascii=\"Consolas\" w:hAnsi=\"Consolas\" w:cstheme=\"minorHAnsi\"/>"
	+ "<w:sz w:val=\"24\"/><w:szCs w:val=\"24\"/></w:rPr><w:t xml:space=\"preserve\">";

	private static boolean scanQuestionario = false;
	private Integer numeroQuestionario;

	private static Pattern PATTERN_QUESTIONARIO = Pattern.compile("Questionario .(\\d+)");

	private static int countQuestionario = 1;
	
	public Questionario(Paragrafo p) {
		super(p);
	}
	public Questionario() {
		super();
	}
	public Integer getNumeroQuestionario() {
		return numeroQuestionario;
	}
	public void setNumeroQuestionario(String strNumeroBaseQuestionario) {
		int numeroBaseQuestionario = Integer.parseInt(strNumeroBaseQuestionario);
		numeroQuestionario = numeroBaseQuestionario + countQuestionario ++;
	}
	public static Questionario checkQuestionario(String p) {
		Questionario q = null;
		if(!scanQuestionario ) {
			Matcher questionarioMatcher = PATTERN_QUESTIONARIO.matcher(p); 
			if(questionarioMatcher.matches()) {
				q = new Questionario();
				q.setNumeroQuestionario(questionarioMatcher.group(1));
				q.setTesto("Questionario: #" + q.getNumeroQuestionario());
				q.appendXmlElement(XML_START)
				 .appendXmlElement(q.toText())
				 .appendXmlElement(XML_END)
				;
			}
		}
		return q;
	}
}
