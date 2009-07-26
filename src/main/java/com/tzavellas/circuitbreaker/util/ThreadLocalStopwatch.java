package com.tzavellas.circuitbreaker.util;

public class ThreadLocalStopwatch {
	
	private static Long ZERO = Long.valueOf(0);
	
	private static class LongThreadLocal extends ThreadLocal<Long> {
		@Override
		protected Long initialValue() {
			return ZERO;
		}
	}
	
	private ThreadLocal<Long> beginning = new LongThreadLocal();
	private ThreadLocal<Long> end = new LongThreadLocal();
	private String operation;
	
	public ThreadLocalStopwatch() {
		operation = "operation";
	}
	
	public ThreadLocalStopwatch(String operation) {
		this.operation = operation;
	}
	
	public void start() {
		if (beginning.get() != 0) {
			throw new IllegalStateException("A Stopwatch cannot be re-started!");
		}
		beginning.set(System.nanoTime());
	}
	
	public void stop() {
		if (beginning.get() == 0) {
			throw new IllegalStateException("You cannot stop a Stopwatch that has not been started!");
		}
		end.set(System.nanoTime());
	}

	/**
	 * Reset the timer
	 */
	public void reset() {
		beginning.set(ZERO);
		end.set(ZERO);
	}
	
	/**
	 * Get the duration in nanoseconds
	 */
	public long duration() {
		long beginning = this.beginning.get();
		long end = this.end.get();
		if (beginning == 0 || end == 0) {
			throw new IllegalStateException("You must start and then stop a Stopwatch to get the duration!");
		}
		return end - beginning;
	}
	
	@Override
	public String toString() {
		return operation + " took (ns): " + duration();
	}
}
