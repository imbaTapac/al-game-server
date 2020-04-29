package com.aion.server.utils;

import java.util.concurrent.atomic.AtomicLong;

public final class OnlineUtils {
	private static final AtomicLong currentOnline = new AtomicLong(0);

	private OnlineUtils() {
		throw new IllegalStateException("Util class");
	}

	public static long getCurrentOnline() {
		return currentOnline.get();
	}

	public static void updateOnline(boolean isNewConnection) {
		if(isNewConnection) {
			currentOnline.incrementAndGet();
		} else {
			currentOnline.decrementAndGet();
		}
	}
}
