package nanoquiz.util;

import java.util.LinkedList;

public class AsyncProvider<T> {
	private LinkedList<T> list = new LinkedList<>();

	public synchronized void provide(T data) {
		list.add(data);
		notify();
	}

	public synchronized T await() throws InterruptedException {
		T data = list.poll();
		while(data == null) {
			wait();
			data = list.poll();
		}
		return data;
	}
}
