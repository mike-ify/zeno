package it.zeno.scuola.verifiche.word;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Singleton;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockChainBatchCDI;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;
import it.zeno.utils.file.FILE;

@ApplicationScoped
@BlockChain("OriginXmlInputReadEventLoop")
public class OriginXmlInputReadEventLoop extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("StartXMLEventBlock")
	private Block startEvent;
	
	@Inject
	@BlockChain("StartXMLEventBlock")
	private Block endEvent;
	
	@Inject
	@BlockChain("in progress...")
	private Block next;
	
	@Override
	public void exec() {
		
		while (data.hasNextOriginXmlEvent()) {

			data.nextOriginXmlEventReader();

			if (data.isStartElement())
				startEvent.start();

			if (data.isEndElement())
				endEvent.start();
		}
	}
	
	@Override
	public void success() {
		next.start();
	}
	
}

