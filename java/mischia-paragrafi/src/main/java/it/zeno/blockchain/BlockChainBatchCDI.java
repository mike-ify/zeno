package it.zeno.blockchain;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Producer;
import javax.inject.Inject;

import org.jboss.weld.environment.se.Weld;

import it.zeno.utils.base.Log;
import it.zeno.utils.pattern.Configurable;
import it.zeno.utils.pattern.Startable;

@ApplicationScoped
public class BlockChainBatchCDI implements Startable,Configurable{
	
    private static List<String> a;

	public static void main(String[] args) {
        try {
        	a = Arrays.asList(args);
        	Weld.newInstance().initialize();
        	CDI.current()
        	.select(BlockChainBatchCDI.class)
        	.get().start();
		} catch (Throwable e) {
			throw Log.error(e);
		}finally {
			Log.info("fine");
		}
    }
    
    @Inject
    @BlockChain
    private Block block;
    
	@Override
	public void start() throws Exception {
		block.start();
	}
	
	@Produces
	public List<String> getArgs(){
		return a;
	}
}
