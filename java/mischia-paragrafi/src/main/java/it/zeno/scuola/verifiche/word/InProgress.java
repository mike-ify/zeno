package it.zeno.scuola.verifiche.word;

import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.utils.base.Log;

@BlockChain("?")
public class InProgress extends BlockImpl{
	
	@Override
	public void success() {
		Log.info("Work in progress...");
	}
}
