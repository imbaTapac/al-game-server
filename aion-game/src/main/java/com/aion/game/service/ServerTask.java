package com.aion.game.service;

import com.aion.game.listener.ServerListener;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServerTask extends Service<ServerListener> {

	public ServerTask(){
	}

	@Override
	protected Task<ServerListener> createTask() {
		return new Task<ServerListener>() {
			@Override
			protected ServerListener call(){
				return ServerListener.getInstance();
			}
		};
	}
}
