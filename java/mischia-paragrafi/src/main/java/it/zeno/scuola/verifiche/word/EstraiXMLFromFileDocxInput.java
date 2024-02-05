package it.zeno.scuola.verifiche.word;

import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.file.Zip;

@ApplicationScoped
@BlockChain("estrai-xml-input")
public class EstraiXMLFromFileDocxInput extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("copy-docx-for-studentnu")
	private Block next;
	
	@Override
	public void output() {
		Path dest = data.getDirElab().resolve(data.getNameFileDocxElabNoExt());
		if(!dest.toFile().mkdir())
			throw new RuntimeException("impossibile creare dir " + dest.toString());
		Zip.extract(data.getFileDocxElab(), dest);
	}
}
