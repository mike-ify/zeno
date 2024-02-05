package it.zeno.scuola.verifiche.word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.inject.Inject;

import it.zeno.blockchain.Args;
import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.blockchain.BlockImpl;
import it.zeno.scuola.verifiche.word.model.QuestDocx;
import it.zeno.utils.base.Log;

@BlockChain
public class QuestDocxConf extends BlockImpl{
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
			
			data.setDirDocxInput(args.first());
			
			if(args.isMoreThanOne())
				data.setStudentiNu(Integer.parseInt(args.second()));
			
			try {
				return isValidPath();
			} catch (IOException e) {
				throw Log.error(e);
			}
		}
		
		//caso che non trova parametri nell'applicazione
		try {
			//1. cerca il file docx di input nella cartella corrente
			if(cercaFileDocx(""))
				return true;

			//2. cerca nelle variabili di ambiente la dir contenente il docx
			String envDir = System.getenv("QUEST_DOCX_HOME");
			if(envDir != null && cercaFileDocx(envDir)) 
				return true;
			
			//3. cerca nelle property jvm
			envDir = System.getProperty("QUEST_DOCX_HOME");
			if(envDir != null && cercaFileDocx(envDir)) 
				return true;
			
		} catch (IOException e) {
			Log.error(e);	
		}
		
		throw new RuntimeException("File di input non trovato");
	}
	
	private boolean cercaFileDocx(String dir) throws IOException {
		Optional<Path> opt = Files
		.walk(Paths.get(dir))
		.filter(p -> !Files.isDirectory(p))
		.filter(p -> p.getFileName().toString().endsWith(".docx"))
		.findAny();
		
		if(opt.isPresent()) {
			data.setFileDocxOrigin(opt.get());
			return true;
		}
		
		return false;
	}

	private boolean isValidPath() throws IOException {
		if(data.getStrDirDocxInput() != null) {
			if(Files.isDirectory(data.getDirDocxInput())) {
				Optional<Path> opt = Files.walk(data.getDirDocxInput())
				.filter(p1 -> p1.getFileName().toString().endsWith(".docx"))
				.findAny();
				if(opt.isEmpty())
					return false;
				data.setFileDocxOrigin(opt.get());
			}
			return Files.exists(data.getDirDocxInput()) && 
			Files.exists(data.getFileDocxInput());
		}
		return false;
	}
}
