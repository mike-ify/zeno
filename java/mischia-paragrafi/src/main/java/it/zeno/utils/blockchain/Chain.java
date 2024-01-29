package it.zeno.utils.blockchain;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import it.zeno.utils.base.Instance;
import it.zeno.utils.base.Str;
import it.zeno.utils.pattern.Job;
import it.zeno.utils.pattern.Single;

public class Chain extends Single implements Job<Chain>{
	private static Chain chain;
	private Map<String,String>properties;
	
	private Block[]blocks;
	
	private Chain() {}
	
	public static Chain get() {
		if(chain == null)
			synchronized(sync) {
				if(chain == null)
					chain = new Chain();
			}
		return chain;
	}

	@Override
	public Chain setup() throws Exception {
		URL url = Thread.currentThread().getContextClassLoader().getResource("");
		Files.walk(Paths.get(url.toURI()))
		.forEach(p -> {
			System.out.println(p.getFileName().toString());
		});
		
		return this;
	}
	
	public static void main(String[] args) throws Exception {
		Chain chain = new Chain();
		chain.setup();
	}

	@Override
	public Chain input() throws Exception {
		return this;
	}

	@Override
	public Chain execute() throws Exception {
		return this;
	}

	@Override
	public Chain output() throws Exception {
		return this;
	}

	@Override
	public Chain next() throws Exception {
		return this;
	}


}
