package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.EndXMLElement;
import it.zeno.utils.functions.ConsumerThrow;

public class EndElementXmlReadLogic extends ElementXmlReadLogic{
	
	private EndXMLElement endElement;
	
	private ConsumerThrow<EndXMLElement>endPConsumer;
	private ConsumerThrow<EndXMLElement>endSectPrConsumer;
	private ConsumerThrow<EndXMLElement>endBodyConsumer;
	private ConsumerThrow<EndXMLElement>endDocumentConsumer;

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
				defaultConsumer.accept(endElement);
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

	public EndElementXmlReadLogic setEndElementConsumer(ConsumerThrow<EndXMLElement> end) {
		this.endElementConsumer = end;
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
