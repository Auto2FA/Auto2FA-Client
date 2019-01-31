package com.auto2fa.client.handlers;

import com.auto2fa.client.User;
import com.auto2fa.client.log.NetLogger;
import com.auto2fa.client.util.AuthUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ChannelHandler extends ChannelInboundHandlerAdapter {

	private NetLogger log;
	private User user;


	public ChannelHandler(User user) {
		log = new NetLogger(this.getClass());
		this.user = user;
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf inBuffer = (ByteBuf)msg;
		String received = inBuffer.toString(CharsetUtil.UTF_8).replaceAll("\r\n", "");
		log.channelLog(ctx, "Channel received: " + received);
		// Website server should just be sending us the e-mail and we'll send back the 2FA code if we have that e-mail
		if(received.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // matches an e-mail
			// TODO: SEND BLUETOOTH
			// For now we'll just implement 2FA here
			String authKey = "temp";
			if(received.equals("mike@test.io"))
				authKey = AuthUtil.generateCurrentNumberString("FY5OI6M5VCFPFCDA7DKZGPBDTZ6DZQTD"); // This is temporary!
			sendString(ctx, authKey);
		} else {
			log.channelLog(ctx, "didn't receive a valid e-mail. Sending back INVALID");
			sendString(ctx, "INVALID");
			closeConnection(ctx);
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		channelReadComplete(ctx);
		ctx.close().sync();
	}

	public ChannelFuture sendString(ChannelHandlerContext ctx, String message) {
		log.info("Sending" + message);
		return ctx.write(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
	}

	private ChannelFuture closeConnection(ChannelHandlerContext ctx) throws Exception {
		channelReadComplete(ctx);
		return ctx.channel().close();
	}

}
