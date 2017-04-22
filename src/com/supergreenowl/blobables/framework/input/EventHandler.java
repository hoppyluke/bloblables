package com.supergreenowl.blobables.framework.input;

import java.util.ArrayList;
import java.util.List;

import com.supergreenowl.blobables.framework.input.EventPool.EventFactory;


public abstract class EventHandler<T> {

	private static final byte POOL_SIZE = 100;
	
	protected EventPool<T> pool;
	protected ArrayList<T> buffer;
	protected ArrayList<T> events;
	
	/**
	 * Creates a new EventHandler.
	 * @param factory Factory to generate pooled event instances.
	 */
	public EventHandler(EventFactory<T> factory) {
		buffer = new ArrayList<T>();
		events = new ArrayList<T>();
		pool = new EventPool<T>(factory, POOL_SIZE);
	}
	
	/**
	 * Gets a list of all the events (in sequence) that have
	 * occurred since the last time that this method was called.
	 * @return Chronological list of events.
	 */
	public List<T> getEvents() {
		// protection as UI thread and game thread access this class
		synchronized(this) {
			int len = events.size();
			for(int i = 0; i < len; i++)
				pool.replace(events.get(i));
				
			events.clear();
			events.addAll(buffer);
			buffer.clear();
			return events;
		}
	}
}
