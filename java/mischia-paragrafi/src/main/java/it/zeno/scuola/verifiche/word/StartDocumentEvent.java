package it.zeno.scuola.verifiche.word;

import javax.inject.Inject;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;

@BlockChain("start-document")
public class StartDocumentEvent extends BlockImpl {

	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("start-document")
	private Block document;
	
	@Override
	public boolean conf() {
		return data.disableFineParagrafiRead();
		//xmlWriter.writeXmlDocumentStart(startElement.getXml());
	}
}
