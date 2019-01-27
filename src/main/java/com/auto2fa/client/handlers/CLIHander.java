package com.auto2fa.client.handlers;

import com.auto2fa.client.log.NetLogger;

import java.net.ServerSocket;
import java.util.Scanner;

public class CLIHander extends Thread {
	
	private Scanner scanner;
	private ServerSocket welcome;
	private NetLogger log;

	public CLIHander() {
		log = new NetLogger(CLIHander.class);
	}

	public void run(ServerSocket welcome) {
		scanner = new Scanner(System.in);
		this.welcome = welcome;
		waitForInput();
	}
	
	private void waitForInput() {
		String line = scanner.nextLine();
		
		boolean loop = true;
		if(line.toLowerCase().matches("stop|exit"))
			loop = false;
		else if(line.toLowerCase().matches("help"))
			log.info("\"stop | exit\" - stops the server\n");
		else
			log.info("Unknown command \"" + line + "\". Please type \"help\" for a list of commands");
		if(loop)
			waitForInput();
	}
	
}
