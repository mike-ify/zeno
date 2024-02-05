package it.zeno.blockchain;

import java.util.Optional;

import it.zeno.utils.base.Log;

public abstract class BlockImpl implements Block{
	protected State st;
	
	@Override
	public State state() {
		return st = Optional.ofNullable(st).orElse(State.CONF);
	}
	
	@Override
	public State state(State state) {
		st = state;
		Log.info(st.name());
		return st;
	}
	
	protected void init() {}
	
	
}
