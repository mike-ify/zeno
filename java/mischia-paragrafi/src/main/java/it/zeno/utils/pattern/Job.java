
package it.zeno.utils.pattern;

public interface Job extends Configurable,Startable{
	
	enum State {
		CONF,INPUT,VALIDATE_INPUT,
		EXEC,OUTPUT,VALIDATE_OUTPUT,
		SUCCESS,FAIL, AFTER_CONF_ARGS,
		BEFORE_CONF_ARGS,
		BEFORE_CONF, 
		AFTER_CONF
	}

	void input();
	boolean validateInput();
	void exec();
	void output();
	boolean validateOutput();
	void success();
	void fail(State state);
	
	State state(State state);
	default State state() {
		return State.SUCCESS;
	}
	
	@Override
	default void start() {
		conf();
		
		input();
		
		if(validateInput()) {
			exec();
		
			output();
			
			if(validateOutput()) {
			
				success();
				
				return;
			}
		}
		fail(state());
	}
}
