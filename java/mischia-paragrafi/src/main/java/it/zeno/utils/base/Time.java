package it.zeno.utils.base;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;

public class Time {
	private Time() {}
	
	static private Instant start(String name) {
		Instant now = Instant.now();
		Get.prop().put(
			name,
			now
		);
		return now;
	}
	
	static public Instant start() {
		return start(Thread.currentThread().getName());
	}

	public static long diffMillis() {
		String name = Thread.currentThread().getName();
		Temporal start = (Temporal) Get.prop().get(name);
		Instant end = start(name);
		long ms = Duration.between(start, end).toMillis();
		System.out.println("diff ms " + ms);
		return ms;
	}

}
