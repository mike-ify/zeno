package it.zeno.scuola.verifiche.word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Singleton;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;

@ApplicationScoped
@BlockChain("copy-docx-input")
public class CopyFileDocxInput extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("estrai-xml-input")
	private Block next;
	
	@Override
	public void output() {
		
		Path source = data.getFileDocxInput();
		Path dest = data.getFileDocxElab();
		
		try {
			Files.copy(source, dest);
		} catch (IOException e) {
			throw Log.error(e);
		}
	}
}
