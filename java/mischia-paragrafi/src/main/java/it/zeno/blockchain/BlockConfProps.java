package it.zeno.blockchain;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import it.zeno.utils.base.Get;
import it.zeno.utils.base.Log;

@BlockChain("block-conf-props")
public class BlockConfProps extends BlockImpl {

	@Override
	public boolean conf() {
		Optional<Path> path;
		try {
			path = Files.walk(Paths.get(""))
			.filter(p -> p.getFileName().toString().endsWith(".properties"))
			.findFirst();
		} catch (IOException e) {
			throw Log.error(e);
		}
		
		if(path.isEmpty())
			return false;
		
		try(InputStream is = Files.newInputStream(path.get())){
			Get.prop().load(is);
			return true;
		}catch(Exception e) {
			Log.error(e);
			return false;
		}
	}

}
