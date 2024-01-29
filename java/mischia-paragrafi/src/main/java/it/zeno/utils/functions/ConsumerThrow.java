package it.zeno.utils.functions;

@FunctionalInterface
public interface ConsumerThrow<T>{
    void accept(T t) throws Exception;
}