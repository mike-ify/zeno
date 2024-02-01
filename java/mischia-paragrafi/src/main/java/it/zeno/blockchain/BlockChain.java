package it.zeno.blockchain;

import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Target({FIELD, TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BlockChain {
	String value() default "block-start";
}
