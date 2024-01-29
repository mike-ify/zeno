package it.zeno.utils.pattern;

public interface Job<T>{
	T setup() throws Exception;
	T input() throws Exception;
	T execute() throws Exception;
	T output() throws Exception;
	T next() throws Exception;
}
