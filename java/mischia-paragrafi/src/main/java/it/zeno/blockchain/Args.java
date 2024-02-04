package it.zeno.blockchain;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class Args{
	@Inject
	protected List<String>args;
	protected int size = 0;
	
	@PostConstruct
	protected void init() {
		size = args.size();
	}
	
	public boolean validate() {
		if(size == 0)
			return false;
		return true;
	}
	
	public List<String> getArgs() {
		return args;
	}
	
	public int getArgsSize(){
		return size;
	}
	
	public boolean isMoreThanOne() {
		return size > 1;
	}
	
	public String first() {
		if(validate())
			return args.get(0);
		return null;	
	}
	public String second() {
		if(size > 1)
			return args.get(1);
		return null;	
	}
}
