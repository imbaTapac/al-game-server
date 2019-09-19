package com.aion.game.listener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aion.game.controller.LoginFormController;


public class ServerListener implements Runnable{
	private static final Logger LOG = LoggerFactory.getLogger(ServerListener.class);

	private InetAddress ip;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;

	private static final ServerListener serverListener = new ServerListener();

	private ServerListener(){
		run();
	}

	@Override
	public void run() {
		try {
			LOG.info("Trying to connect");
			ip = InetAddress.getByName("localhost");
			socket = new Socket(ip,1978);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			if(socket.isConnected()){
				LOG.info("Successfully connected to game-server");
			}
		} catch(UnknownHostException e) {
			LOG.error("[{}]",e);
		} catch(IOException ie) {
			LOG.error("[{}]",ie);
		}finally {
			try {
				socket.close();
				dis.close();
				dos.close();
			} catch(IOException e) {
				LOG.error("[{}]",e);
			}
		}
		while(socket.isConnected()){
			try {
				Thread.sleep(5000);
				LoginFormController.getInstance().handleServerStatus(socket);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}

			LOG.info("Keeping Player thread on");
			if(!socket.isConnected()){
				LOG.info("Lost connection to game-server");
			}
		}
	}

	public static ServerListener getInstance(){
		return serverListener;
	}

	public Socket getSocket(){
		return this.socket;
	}

	public synchronized void terminate(){
		LOG.info("Player disconnect");
		try {
			socket.close();
			dos.close();
			dis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
