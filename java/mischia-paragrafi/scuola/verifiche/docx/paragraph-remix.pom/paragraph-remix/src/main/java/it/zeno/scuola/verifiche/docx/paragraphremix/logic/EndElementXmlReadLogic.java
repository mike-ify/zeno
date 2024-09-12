package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.EndXMLElement;
import it.zeno.utils.functions.ConsumerThrow;

public class EndElementXmlReadLogic{
	
	private EndXMLElement endElement;
	
	private ConsumerThrow<EndXMLElement>endPConsumer;
//	destXmlWriter.addXmlParagrafoEndLogic(xml);
	private ConsumerThrow<EndXMLElement>endSectPrConsumer;
//	destXmlWriter.writeXmlElement(xml);
	
	private ConsumerThrow<EndXMLElement>endBodyConsumer;
//	destXmlWriter.writeXmlElement(xml);
	private ConsumerThrow<EndXMLElement>endDocumentConsumer;
//	destXmlWriter.writeXmlElement(xml);
	private ConsumerThrow<EndXMLElement>endDefaultConsumer;
//	if(StartElementXmlReadLogic.secpr)
//		destXmlWriter.writeXmlElement(xml);
//	else
//		destXmlWriter.appendXmlElement(name,xml);

	public void logic() throws Exception {

		switch(endElement.getName()) {
		
			case "p":
				endPConsumer.accept(endElement);
			break;
			
			case "sectPr":
				endSectPrConsumer.accept(endElement);
			break;
			
			case "body":
				endBodyConsumer.accept(endElement);
			break;

			case "document":
				endDocumentConsumer.accept(endElement);
			break;
			
			default: 
				endDefaultConsumer.accept(endElement);
			break;
		}
	}

	public EndElementXmlReadLogic setEndElement(EndXMLElement endElement) {
		this.endElement = endElement;
		return this;
	}

	public EndElementXmlReadLogic setEndPConsumer(ConsumerThrow<EndXMLElement> endPConsumer) {
		this.endPConsumer = endPConsumer;
		return this;
	}

	public EndElementXmlReadLogic setEndSectPrConsumer(ConsumerThrow<EndXMLElement> endSectPrConsumer) {
		this.endSectPrConsumer = endSectPrConsumer;
		return this;
	}

	public EndElementXmlReadLogic setEndBodyConsumer(ConsumerThrow<EndXMLElement> endBodyConsumer) {
		this.endBodyConsumer = endBodyConsumer;
		return this;
	}

	public EndElementXmlReadLogic setEndDocumentConsumer(ConsumerThrow<EndXMLElement> endDocumentConsumer) {
		this.endDocumentConsumer = endDocumentConsumer;
		return this;
	}

	public EndElementXmlReadLogic setEndDefaultConsumer(ConsumerThrow<EndXMLElement> endDefaultConsumer) {
		this.endDefaultConsumer = endDefaultConsumer;
		return this;
	}
	
	public EndElementXmlReadLogic setEvent(XMLEvent event) {
		endElement.accept(event);
		return this;
	}
	public EndElementXmlReadLogic accept(XMLEventReader input) {
		endElement = new EndXMLElement(input);
		return this;
	}
	
}
