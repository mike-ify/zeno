package it.zeno.scuola.verifiche.word;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import it.zeno.blockchain.BatchBlock;
import it.zeno.blockchain.Block;
import it.zeno.blockchain.BlockChain;
import it.zeno.utils.base.Get;
import it.zeno.utils.base.Log;

@BlockChain
public class CercaQuestDocx extends BatchBlock{
	private Path pDirDocxInput;
	private String sDirDocxInput;
	private List<String> args;
	
	@Override
	protected boolean tryConfSystemEnv() {
		sDirDocxInput = System.getenv("QUEST_DOCX_HOME");
		return trySetPath();
	}

	@Override
	protected boolean tryConfSystemProperties() {
		sDirDocxInput = System.getProperty("quest.docx.home");
		return trySetPath();
	}

	private boolean trySetPath() {
		if(sDirDocxInput != null) {
			pDirDocxInput = Paths.get(sDirDocxInput);
			return Files.exists(pDirDocxInput);
		}
		return false;
	}

	@Override
	protected boolean tryConfLocalProperties() {
		try(InputStream is = Files.newInputStream(Paths.get("quest-docx.properties"))){
			Get.prop().load(is);
			sDirDocxInput = Get.prop("dir.quest.docx");
			return trySetPath();
		}catch(Exception e) {
			Log.error(e);
			return false;
		}
	}

	@Override
	protected boolean tryConfLocalDir() {
		try {
			sDirDocxInput = Files
			.walk(Paths.get(""))
			.filter(p -> !Files.isDirectory(p))
			.map(p->p.getFileName().toString())
			.filter(s -> s.endsWith(".docx"))
			.findFirst()
			.orElse(null);
			
			return trySetPath();
		} catch (IOException e) {
			Log.error(e);
			return false;
		}
	}

	@Override
	protected boolean confArgs(List<String> args) {
		this.args = args;
		return confArg(args.get(0));
	}

	@Override
	protected boolean confArg(String arg) {
		if(arg.endsWith(".docx")) {
			pDirDocxInput = Paths.get(arg).getParent();
			sDirDocxInput = pDirDocxInput.toString();
		}else {
			sDirDocxInput = arg;
			pDirDocxInput = Paths.get(sDirDocxInput);
		}
		return Files.exists(pDirDocxInput);
	}

}
