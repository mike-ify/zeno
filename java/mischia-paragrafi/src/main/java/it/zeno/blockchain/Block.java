package it.zeno.blockchain;

import it.zeno.utils.pattern.Job;

public interface Block extends Job{
	default String getName() {
		return getClass()
		.getAnnotation(BlockChain.class)
		.value();
	}
	
	@Override
	default void input() {
	}

	@Override
	default boolean validateInput() {
		return false;
	}

	@Override
	default void exec() {
	}

	@Override
	default void output() {
	}

	@Override
	default boolean validateOutput() {
		return false;
	}

	@Override
	default void success() {
	}

	@Override
	default void fail(State state) {
	}

	@Override
	default State state() {
		return null;
	}

	@Override
	default void conf() {
	}
}
