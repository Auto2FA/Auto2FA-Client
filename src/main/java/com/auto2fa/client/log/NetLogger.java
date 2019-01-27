package com.auto2fa.client.log;

import com.auto2fa.client.User;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
/**
 * Logging wrapper for Logger
 */
public class NetLogger {

	private Logger log;

	public NetLogger(Class clazz) {
		log = Logger.getLogger(clazz);
	}

	public void channelLog(ChannelHandlerContext ctx, String msg) {
		netLog(((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress(), msg);
	}

	public void netLog(String ipHash, String msg) {
		log.info("[" + ipHash + "] " + msg);
	}

	public void info(String msg) {
		log.info(msg);
	}

	public void fatal(String msg) {
		log.fatal(msg);
	}

	public void fatal(String msg, Throwable throwable) {
		log.fatal(msg, throwable);
	}

	public void fatal(Throwable throwable) {
		log.fatal(throwable);
	}

	public void scanLog(User user, String msg) {
		log.info(user + " > " + msg);
	}
	
	public void exceptionLog(String msg, Exception e) {
		log.fatal("[ERROR] " + msg + " -> " + e.getMessage());
	}
	
}
