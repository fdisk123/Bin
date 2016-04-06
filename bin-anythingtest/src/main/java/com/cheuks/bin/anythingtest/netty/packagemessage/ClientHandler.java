package com.cheuks.bin.anythingtest.netty.packagemessage;

import com.cheuks.bin.anythingtest.netty.packagemessage.MsgBuf.MsgBody;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<MessagePackage<MsgBody>> {

	private ObjectCodec objectCodec = new DefaultObjectCoded();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessagePackage<MsgBody> msg) throws Exception {

		synchronized (client.class) {
			notifyAll();
		}
		try {
			System.out.println((String) objectCodec.encodeByByte(msg.getMessageBody().getResult().toByteArray()));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
	}

	public static void main(String[] args) {
		System.out.println("A".hashCode());
		System.out.println("AAAAAAAAA".hashCode());
		System.out.println("AAAAAAAAAA".hashCode());
		System.out.println("AAAAAAAAAAA".hashCode());
		System.out.println("AAAAAAAAAAAAA".hashCode());
	}

}
