package it.zeno.scuola.verifiche.docx.utils;

import java.util.regex.Pattern;

public class XML {
    public static final Pattern PATTERN_RAW_XML_START = Pattern.compile("<(/?)\\[.+\\]:");

    public static String sanitize(String xml) {
		
		if(xml.indexOf('<') > -1)
			return PATTERN_RAW_XML_START.matcher(xml).replaceAll("<$1");
		
		if(xml.equals("ENDDOCUMENT"))
			return"";
		
		return xml;
	}
}
