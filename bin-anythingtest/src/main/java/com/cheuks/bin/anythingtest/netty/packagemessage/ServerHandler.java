package com.cheuks.bin.anythingtest.netty.packagemessage;

import com.cheuks.bin.anythingtest.netty.packagemessage.HandlerCenter.people;
import com.cheuks.bin.anythingtest.netty.packagemessage.MsgBuf.MsgBody;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<MessagePackage<MsgBody>> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessagePackage<MsgBody> msg) throws Exception {
		HandlerCenter.join(new people<ChannelHandlerContext>(msg.getMessageBody(), ctx));
	}

}
