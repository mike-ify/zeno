package it.zeno.scuola.verifiche.word;

import javax.inject.Inject;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.scuola.verifiche.word.model.StartXMLElement;
import it.zeno.utils.functions.ConsumerThrow;


@BlockChain("StartXMLEventBlock")
public class StartXMLEventBlock extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("start-document")
	private Block document;
	
	private ConsumerThrow<StartXMLElement>startBodyConsumer;
	private ConsumerThrow<StartXMLElement>startTextConsumer;
	private ConsumerThrow<StartXMLElement>startTabConsumer;
	private ConsumerThrow<StartXMLElement>startSectPrConsumer;
	private ConsumerThrow<StartXMLElement>startDefaultConsumer;
	
	@Override
	public boolean conf() {
		
		StartXMLElement startElement = new StartXMLElement(data.getOriginXmlEventReader());
		
		data.setStartXMLElement(startElement);
		
		return true;
	}
	
	public void succes() throws Exception {
		switch (data.getStartElementName()) {
			
			case "document": 
				document.start();
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

	public StartXMLEventBlock setStartDocumentConsumer(ConsumerThrow<StartXMLElement> startDocumentConsumerThrow) {
		this.startDocumentConsumer = startDocumentConsumerThrow;
		return this;
	}
	public StartXMLEventBlock setStartBodyConsumer(ConsumerThrow<StartXMLElement> startBodyConsumerThrow) {
		this.startBodyConsumer = startBodyConsumerThrow;
		return this;
	}
	public StartXMLEventBlock setStartTextConsumer(ConsumerThrow<StartXMLElement> startTextConsumerThrow) {
		this.startTextConsumer = startTextConsumerThrow;
		return this;
	}
	public StartXMLEventBlock setStartTabConsumer(ConsumerThrow<StartXMLElement> startTabConsumerThrow) {
		this.startTabConsumer = startTabConsumerThrow;
		return this;
	}
	public StartXMLEventBlock setStartSectPrConsumer(ConsumerThrow<StartXMLElement> startSectPrConsumerThrow) {
		this.startSectPrConsumer = startSectPrConsumerThrow;
		return this;
	}
	public StartXMLEventBlock setStartDefaultConsumer(ConsumerThrow<StartXMLElement> startDefaultConsumerThrow) {
		this.startDefaultConsumer = startDefaultConsumerThrow;
		return this;
	}
	public StartXMLEventBlock setEvent(XMLEvent event) {
		startElement.accept(event);
		return this;
	}
	public StartXMLEventBlock accept(XMLEventReader input) {
		startElement = new StartXMLElement(input);
		return this;
	}
}
