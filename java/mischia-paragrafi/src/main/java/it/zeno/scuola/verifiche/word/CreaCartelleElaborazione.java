package it.zeno.scuola.verifiche.word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;
import it.zeno.utils.file.Dir;

@BlockChain("cartelle-elaborazione")
public class CreaCartelleElaborazione extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("copy-docx-input")
	private Block next;
	
	@Override
	public boolean conf() {
		try {
			Path elabDir = data.getDirDocxInput().resolve("temp");
			Dir.empty(elabDir);
			data.setDirElab(elabDir);
			
			Path resultDir = data.getDirDocxInput().resolve("result");
			Dir.empty(resultDir);
			data.setDirResult(resultDir);
			
			return Files.exists(elabDir) && Files.exists(resultDir);
		} catch (IOException e) {
			Log.error(e);
		}
		
		throw new RuntimeException("impossibile creare le cartelle di elaborazione");
	}
	
	@Override
	public void output() {
		data.setFileDocxElab(data.getDirElab().resolve(data.getFileDocxInput().getFileName()));
	}
}
