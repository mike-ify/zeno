package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import it.zeno.scuola.verifiche.docx.utils.XML;


public class StartElementXmlReader{
	public static boolean secpr = false;

	public void logic(XMLEvent event,DocumentoWriter destDocument, NextXmlEventReaderFunction xmlEventReader ) throws Exception {
		StartElement startElement = event.asStartElement();
		String name = startElement.getName().getLocalPart();
		String xml = XML.sanitize(startElement.toString());
		
		switch (name) {
		
		case "document": {
			secpr = false;
			destDocument.writeXmlDocumentStart(xml);
			break;
		}
		
		case "body": {
			destDocument.writeXmlBodyStart(xml);
			break;
		}		
		
		case "t":{
			event = xmlEventReader.next();
			destDocument.appendTestoParagrafo(xml,event.asCharacters().getData());
			break;
		}
		
		case "tab":{
			destDocument.appendXmlTabStart(xml);
			break;
		}	

		case "sectPr":{
			secpr = true;
			destDocument.writeXmlSectPrStart(xml);
			break;
		}	
		
		default: {
			if(secpr)
				destDocument.writeXmlElement(xml);
			else
				destDocument.appendXmlElement(name,xml);
			break;
		}
	}
	}

	

}
