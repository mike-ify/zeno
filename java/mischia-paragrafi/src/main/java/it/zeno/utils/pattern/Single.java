package it.zeno.utils.pattern;

public abstract class Single<T>{
	
	static final protected Object sync = new Object();
	
	abstract protected Object readResolve();
}
