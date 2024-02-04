package it.zeno.scuola.verifiche.word;

import javax.inject.Inject;

import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;

@BlockChain("cartelle-elaborazione")
public class CreaCartelleElaborazione extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Override
	public boolean conf() {
		return false;
	}
	
}
