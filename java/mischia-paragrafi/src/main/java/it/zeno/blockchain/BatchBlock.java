package it.zeno.blockchain;

import java.util.List;

public abstract class BatchBlock extends BlockImpl{
	@Override
	public void conf(List<String>args) {
		state(State.BEFORE_CONF_ARGS);
		if(args.size() == 0 || !tryConfArgs(args))
			conf();
		state(State.AFTER_CONF_ARGS);
	}
	
	protected boolean tryConfArgs(List<String> args) {
		if(args.size() == 1) {
			if(!confArg(args.get(0)))
				return false;
		} else {
			if(!confArgs(args))
				return false;
		}
		
		return true;
	}

	protected abstract boolean confArgs(List<String> args);

	protected abstract boolean confArg(String string);


	@Override
	public void conf() {
		state(State.BEFORE_CONF);
		if(!tryConfLocal()) 
			if(!tryConfSystem()) 
				throw new RuntimeException("cartella di input");
		state(State.AFTER_CONF);
	}

	protected boolean tryConfSystem() {
		if(!tryConfSystemProperties())
			if(!tryConfSystemEnv())
				return false;
		return true;
	}

	protected abstract boolean tryConfSystemEnv();

	protected abstract boolean tryConfSystemProperties();

	protected boolean tryConfLocal() {
		if(!tryConfLocalDir())
			if(!tryConfLocalProperties())
				return false;
		return true;
	}

	protected abstract boolean tryConfLocalProperties();

	protected abstract boolean tryConfLocalDir();
}
