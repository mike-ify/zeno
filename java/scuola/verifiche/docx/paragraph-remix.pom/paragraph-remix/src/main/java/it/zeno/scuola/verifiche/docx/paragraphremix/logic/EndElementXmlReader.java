package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import java.io.IOException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

import it.zeno.scuola.verifiche.docx.utils.XML;


public class EndElementXmlReader{

	public void logic(XMLEvent event, DocumentoWriter destXmlWriter) throws IOException {

		EndElement endElement = event.asEndElement();
		String name = endElement.getName().getLocalPart();
		String xml = XML.sanitize(endElement.toString());
		switch (name) {
		
			case "p":
				destXmlWriter.addXmlParagrafoEndLogic(xml);
			break;
			
			case "sectPr":{
				destXmlWriter.writeXmlElement(xml);
				break;
			}
			
			case "body":{
				destXmlWriter.writeXmlElement(xml);
				break;
			}

			case "document":{
				destXmlWriter.writeXmlElement(xml);
				break;
			}
			
			default: 
				if(StartElementXmlReader.secpr)
					destXmlWriter.writeXmlElement(xml);
				else
					destXmlWriter.appendXmlElement(name,xml);
			break;
		}
		
	}

}
