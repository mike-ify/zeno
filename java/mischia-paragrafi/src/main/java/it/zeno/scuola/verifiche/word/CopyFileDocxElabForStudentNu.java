package it.zeno.scuola.verifiche.word;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;
import it.zeno.utils.file.FILE;

@ApplicationScoped
@BlockChain("copy-docx-for-studentnu")
public class CopyFileDocxElabForStudentNu extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("?")
	private Block next;
	@Inject
	@BlockChain("copy-docx-input")
	private Block loop;
	
	
	@Override
	public boolean conf() {
		data.setFileDocxInput(data.getFileDocxElab());
		
		String nameFileDocxElab = 
			FILE.nameLessExt(data.getFileDocxOrigin().getFileName())
			+ data.countStudent()
			+ ".docx";
		
		data.setFileDocxElab(data.getDirElab().resolve(nameFileDocxElab));
		
		return true;
	}
	
	@Override
	public void success() {
		if(data.nextStudent() <= data.getStudentiNu()) {
			loop.start();
		}else
			next.start();
	}
}
