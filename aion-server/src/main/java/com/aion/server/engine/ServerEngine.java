package com.aion.server.engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerEngine implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ServerEngine.class);
	private ServerSocket serverSocket;
	private Socket socket;
	private int port = 1978;
	private final AtomicLong connectionCount = new AtomicLong();
	private final AtomicLong activeConnectionCount = new AtomicLong();
	private volatile boolean running = false;
	private volatile boolean isShutdown = false;
	private static final ServerEngine instance = new ServerEngine();

	private ServerEngine() {
		/*Properties properties = new Properties();
		try {
			properties.load(ServerEngine.class.getResourceAsStream("application.properties"));
			this.port = Integer.parseInt(properties.getProperty("server.port"));
		} catch(IOException e) {
			LOG.error("[{}]",e);
		}*/
		run();
	}

	@Override
	public void run() {
		try {
			LOG.info("Starting game-server on port [{}]", port);
			serverSocket = new ServerSocket(port);
		} catch(IOException e) {
			LOG.error("{}", e);
		}
		while(!isShutdown) {
			synchronized(this) {
				try {
					socket = serverSocket.accept();
					LOG.info("New player is connected : [{}] [{}]", socket.getLocalSocketAddress(), socket.getRemoteSocketAddress());
					updateOnline(true);
					LOG.info("Current online is [{}] players", this.activeConnectionCount.get());
				} catch(IOException e) {
					LOG.error("I/O error: [{}]", e);
				}
			}
			Thread playerThread = new Server(socket);
			playerThread.start();
			try {
				Thread.sleep(1000);
				if(!playerThread.isAlive()){
					LOG.info("Player [{}] [{}] disconnected", socket.getLocalSocketAddress(), socket.getRemoteSocketAddress());
					updateOnline(false);
					LOG.info("Current online is [{}] players", this.activeConnectionCount.get());
				}
			} catch(InterruptedException e) {
				LOG.error("[{}]",e);
			}

		}

	}

	private synchronized void terminate() {
		if(isShutdown) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					LOG.info("Shutdown Hook is running");
				}
			});
			LOG.info("Server is terminating");
			try {
				serverSocket.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static ServerEngine getInstance() {
		return instance;
	}


	private void setShutdown(boolean shutdown) {
		this.isShutdown = shutdown;
	}

	private void updateOnline(boolean isConnected) {
		if(isConnected) {
			this.activeConnectionCount.incrementAndGet();
		}else {
			this.activeConnectionCount.decrementAndGet();
		}
	}

	public long getTotalConnectionsCount() {
		return this.connectionCount.get();
	}

	public void clearConnectionsCount() {
		this.connectionCount.set(0L);
	}

	public long getActiveConnectionsCount() {
		return this.activeConnectionCount.get();
	}

	public int getPort() {
		return this.serverSocket.getLocalPort();
	}

	public boolean isRunning() {
		return this.running;
	}
}
