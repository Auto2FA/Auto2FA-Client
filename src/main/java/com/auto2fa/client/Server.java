package com.auto2fa.client;

import com.auto2fa.client.handlers.ChannelHandler;
import com.auto2fa.client.log.NetLogger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.net.InetSocketAddress;
import java.util.HashSet;

public class Server {
	
	public static final int PORT;
	public static final int TIMEOUT_SECONDS;
	private static Configurations configs;
	public static NetLogger log;

	static {
		log = new NetLogger(Server.class); // The file handler won't be updated, so we'll do it later in the static block
		configs = new Configurations();
		log.info("Checking config.properties for the port, timeout, and log folder");
		try {
			Configuration config = configs.properties("properties/config.properties");

			TIMEOUT_SECONDS = config.getInt("client_timeout_seconds");
			log.info("TIMEOUT_SECONDS = " + TIMEOUT_SECONDS);

			PORT = config.getInt("port");
			log.info("PORT = " + PORT);
		} catch (ConfigurationException e) {
			log.fatal("Could not initiate server - Invalid config file", e);
			throw new RuntimeException("Could not initiate server");
		}
	}

	private HashSet<String> authorized;

	public Server() throws InterruptedException {
		 authorized = new HashSet();

		EventLoopGroup group = new NioEventLoopGroup();

		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(group);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.localAddress(new InetSocketAddress("localhost", PORT));

			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					String ip = socketChannel.remoteAddress().getAddress().getHostAddress();
					log.netLog(ip, "has connected!");

					if(authorized.contains(ip)) {
						User user = new User(ip);
						log.info(user + " authenticated");
						try {
							socketChannel.pipeline().addLast(new ChannelHandler(user));
						} catch(Exception e) {
							socketChannel.close().sync();
						}

					} else {
						log.netLog(ip, "was not authorized");
						socketChannel.close().sync();
					}
				}
			});
			ChannelFuture channelFuture = serverBootstrap.bind().sync();
			log.info("Started socket server on port " + PORT);
			channelFuture.channel().closeFuture().sync();
		} catch(Exception e){
			log.fatal(e);
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) {
	   	try {
			new Server();
		} catch(InterruptedException ie) {
	   		log.fatal("Could not shutdown server gracefully", ie);
			System.exit(-1);
		}
	}
	
}
