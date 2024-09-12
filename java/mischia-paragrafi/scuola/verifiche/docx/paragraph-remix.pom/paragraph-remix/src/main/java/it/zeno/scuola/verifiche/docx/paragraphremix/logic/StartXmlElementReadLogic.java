package it.zeno.scuola.verifiche.docx.paragraphremix.logic;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;

import it.zeno.scuola.verifiche.docx.paragraphremix.model.StartXMLElement;
import it.zeno.utils.functions.ConsumerThrow;

public class StartXmlElementReadLogic{
	
	private StartXMLElement startElement;
	
	private ConsumerThrow<StartXMLElement>startDocumentConsumer;
	private ConsumerThrow<StartXMLElement>startBodyConsumer;
	private ConsumerThrow<StartXMLElement>startTextConsumer;
	private ConsumerThrow<StartXMLElement>startTabConsumer;
	private ConsumerThrow<StartXMLElement>startSectPrConsumer;
	private ConsumerThrow<StartXMLElement>startDefaultConsumer;
	
	public void logic() throws Exception {
		
		switch (startElement.getName()) {
			
			case "document": 
				startDocumentConsumer.accept(startElement);
				break;
			
			case "body": 
				startBodyConsumer.accept(startElement);
				break;
			
			case "t":
				startTextConsumer.accept(startElement);
				break;
			
			case "tab":
				startTabConsumer.accept(startElement);
				break;
	
			case "sectPr":
				startSectPrConsumer.accept(startElement);
				break;
			
			default: 
				startDefaultConsumer.accept(startElement);
				break;
		}
	}
	public StartXmlElementReadLogic setStartElement(StartXMLElement startElement) {
		this.startElement = startElement;
		return this;
	}
	public StartXmlElementReadLogic setStartDocumentConsumer(ConsumerThrow<StartXMLElement> startDocumentConsumerThrow) {
		this.startDocumentConsumer = startDocumentConsumerThrow;
		return this;
	}
	public StartXmlElementReadLogic setStartBodyConsumer(ConsumerThrow<StartXMLElement> startBodyConsumerThrow) {
		this.startBodyConsumer = startBodyConsumerThrow;
		return this;
	}
	public StartXmlElementReadLogic setStartTextConsumer(ConsumerThrow<StartXMLElement> startTextConsumerThrow) {
		this.startTextConsumer = startTextConsumerThrow;
		return this;
	}
	public StartXmlElementReadLogic setStartTabConsumer(ConsumerThrow<StartXMLElement> startTabConsumerThrow) {
		this.startTabConsumer = startTabConsumerThrow;
		return this;
	}
	public StartXmlElementReadLogic setStartSectPrConsumer(ConsumerThrow<StartXMLElement> startSectPrConsumerThrow) {
		this.startSectPrConsumer = startSectPrConsumerThrow;
		return this;
	}
	public StartXmlElementReadLogic setStartDefaultConsumer(ConsumerThrow<StartXMLElement> startDefaultConsumerThrow) {
		this.startDefaultConsumer = startDefaultConsumerThrow;
		return this;
	}
	public StartXmlElementReadLogic setEvent(XMLEvent event) {
		startElement.accept(event);
		return this;
	}
	public StartXmlElementReadLogic accept(XMLEventReader input) {
		startElement = new StartXMLElement(input);
		return this;
	}
}
