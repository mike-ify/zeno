package it.zeno.blockchain;

import javax.inject.Inject;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import it.zeno.utils.base.Log;
import it.zeno.utils.pattern.Startable;
	
public class BatchCDIBlockChain implements Startable{

    public static void main(String[] args) {
    	
    	WeldContainer container = new Weld().initialize();
        try {
        	container
        	.select(BatchCDIBlockChain.class)
        	.get()
        	.start();
		} catch (Throwable e) {
			throw Log.error(e);
		}finally {
			container.shutdown();
		}
    }

    @Inject
    @BlockChain
    private Block block;
    
	@Override
	public void start() throws Exception {
		block.start();
	}
}
