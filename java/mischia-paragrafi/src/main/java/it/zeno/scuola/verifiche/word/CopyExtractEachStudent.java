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
@BlockChain("copy-extract-student")
public class CopyExtractEachStudent extends BlockImpl{
	
	@Inject
	private QuestDocx data;
	
	@Inject
	@BlockChain("copy-docx-input")
	private Block loop;
	
	@Override
	public boolean conf() {
		data.setFileDocxInput(data.getFileDocxElab());
		
		String nameFileDocxElab = 
			FILE.nameLessExt(data.getFileDocxOrigin().getFileName())
			+ '-'
			+ data.countStudent()
			+ ".docx";
		
		data.setFileDocxElab(data.getDirElab().resolve(nameFileDocxElab));
		
		return true;
	}
	
	@Override
	public void success() {
		if(data.nextStudent() <= data.getStudentiNu()) {
			loop.start();
		}else{
			try(Block next = CDI.current().select(PreparaXmlInput.class).get()){
				next.start();
			} catch (Exception e) {
				throw Log.error(e);
			}
		}
	}
}
