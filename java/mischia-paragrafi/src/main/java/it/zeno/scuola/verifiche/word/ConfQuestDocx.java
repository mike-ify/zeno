package it.zeno.scuola.verifiche.word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import it.zeno.blockchain.Args;
import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;
import it.zeno.utils.pattern.ProducesData;
import jdk.internal.classfile.Annotation;
import jdk.internal.classfile.AnnotationElement;

@BlockChain
public class ConfQuestDocx extends BlockImpl implements ProducesData<QuestDocx>{
	@Inject
	private QuestDocx data;
	
	@Inject
	private Args args;	
	
	@Inject
	@BlockChain("cartelle-elaborazione")
	private Block next;	
	
	@Override
	public boolean conf() {
		//caso parametri passati all'applicazione
		if(args.validate()) {
			
			data.setStrDirDocxInput(args.first());
			
			if(args.isMoreThanOne())
				data.setStudentiNu(Integer.parseInt(args.second()));
			
			return isValidPath();
		}
		
		//caso che non trova parametri nell'applicazione
		try {
			//1. cerca il file docx di input nella cartella corrente
			if(cercaDir(""))
				return true;

			//2. cerca nelle variabili di ambiente la dir contenente il docx
			String envDir = System.getenv("QUEST_DOCX_HOME");
			if(envDir != null) 
				return cercaDir(envDir);
			
			//3. cerca nelle property jvm
			envDir = System.getProperty("QUEST_DOCX_HOME");
			if(envDir != null) 
				return cercaDir(envDir);
			
		} catch (IOException e) {
			Log.error(e);	
		}
		
		return false;
	}
	
	private boolean cercaDir(String dir) throws IOException {
		Optional<Path> opt = Files
		.walk(Paths.get(dir))
		.filter(p -> !Files.isDirectory(p))
		.filter(p -> p.getFileName().toString().endsWith(".docx"))
		.findAny();
		
		if(opt.isPresent()) {
			data.setDirDocxInput(opt.get());
			data.setStrDirDocxInput(data.getDirDocxInput().toString());
			return true;
		}
		
		return false;
	}

	private boolean isValidPath() {
		if(data.getStrDirDocxInput() != null) {
			data.setDirDocxInput(Paths.get(data.getStrDirDocxInput()));
			return Files.exists(data.getDirDocxInput());
		}
		return false;
	}
	
	@Override
	public QuestDocx produce() {
		return data;
	}
	
	@Override
	public void success() {
		next.start();
	}

}
