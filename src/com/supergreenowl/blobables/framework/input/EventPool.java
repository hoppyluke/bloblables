package com.supergreenowl.blobables.framework.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows pooling and re-use of object instances.
 * @author luke
 *
 * @param <T>
 */
public class EventPool<T> {

	public interface EventFactory<T> {
		public T create();
	}
	
	private final List<T> free;
	private final int maxSize;
	private final EventFactory<T> factory;
	
	public EventPool(EventFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		free = new ArrayList<T>(maxSize);
	}
	
	/**
	 * Retrieves and event from the pool.
	 * @return
	 */
	public T retrieve() {
		T event = null;
		
		if(free.size() == 0)
			event = factory.create();
		else
			event = free.remove(free.size() - 1);
		
		return event;
	}
	
	/**
	 * Returns an event to the pool for future use.
	 * @param event
	 */
	public void replace(T event) {
		if(free.size() < maxSize) free.add(event);
	}
}
