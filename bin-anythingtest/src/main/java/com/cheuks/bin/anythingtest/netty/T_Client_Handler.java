package com.cheuks.bin.anythingtest.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class T_Client_Handler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		methodName("channelRegistered");
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		methodName("channelUnregistered");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		methodName("channelActive");
//		ctx.writeAndFlush("你好吗？");
		//		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		methodName("channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		methodName("channelRead");
		ByteBuf bb = (ByteBuf) msg;
		byte[]b=new byte[bb.readableBytes()];
		bb.readBytes(b);
		System.out.println(new String(b));
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		methodName("channelReadComplete");
		super.channelReadComplete(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		methodName("userEventTriggered");
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		methodName("channelWritabilityChanged");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		methodName("exceptionCaught");
		super.exceptionCaught(ctx, cause);
	}

	public void methodName(Object... o) {
		System.out.println("客户端-方法调用：" + o[0].toString());
	}

}