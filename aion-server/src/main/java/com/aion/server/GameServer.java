package com.aion.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aion.server.engine.ServerEngine;


public class GameServer {
	private static final Logger LOG = LoggerFactory.getLogger(GameServer.class);

	public static void main(String[] args) {
		ServerEngine.getInstance().run();
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			LOG.error("[{}]", e);
			Thread.currentThread().interrupt();
		}
	}
}
