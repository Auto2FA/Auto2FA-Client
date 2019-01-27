package com.auto2fa.client.util;

import java.net.Socket;
import java.util.ArrayList;

public class SocketHelper {

	public static int getSocketIndex(ArrayList<Socket> sockets, Socket socket) {
		for(int i = 0;i<sockets.size();i++)
			if(sockets.get(i) == socket)
				return i;
		return -1;
	}

}
