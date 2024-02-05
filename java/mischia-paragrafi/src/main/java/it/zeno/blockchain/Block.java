package it.zeno.blockchain;

import java.lang.reflect.Field;

import it.zeno.utils.base.Log;
import it.zeno.utils.pattern.Configurable;
import it.zeno.utils.pattern.Startable;

public interface Block extends Configurable,Startable{
	default String getName() {
		return getClass()
		.getAnnotation(BlockChain.class)
		.value();
	}

	enum State {
		CONF,INPUT,VALIDATE_INPUT,
		EXEC,OUTPUT,VALIDATE_OUTPUT,
		SUCCESS,FAIL, AFTER_CONF_ARGS,
		BEFORE_CONF_ARGS,
		BEFORE_CONF, 
		AFTER_CONF
	}

	default void input() {}
	default boolean validateInput() {return true;}
	default void exec() {}
	default void output() {}
	default boolean validateOutput() {return true;}
	default void success() {
		try {
			Field field = getClass()
			.getDeclaredField("next");
			field.setAccessible(true);
			
			Object ctx = field.get(this);
			
			ctx.getClass()
			.getMethod("start")
			.invoke(ctx);
			
		} catch (Exception e) {
			Log.error(e);
		}
	}
	default void fail(State state) {}
	
	State state(State state);
	default State state() {
		return State.SUCCESS;
	}
	
	@Override
	default void start() {
		Log.info("start {}",getClass().getAnnotation(BlockChain.class).value());
		if(conf()) {
			input();
			if(validateInput()) {
				exec();
				output();
				if(validateOutput()) {
					success();
					return;
				}
			}
		}
		fail(state());
	}

}
