package com.cheuks.bin.net.server.event;

import com.cheuks.bin.net.server.niothread.Attachment;
import com.cheuks.bin.net.util.ByteBufferUtil;
import com.cheuks.bin.net.util.ByteBufferUtil.DataPacket;
import com.cheuks.bin.net.util.Serializ;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class RmiWriteEvent implements WriteEvent {

	private Attachment attachment;
	private SocketChannel channel;

	public SelectionKey process(SelectionKey key, Serializ serializ) throws Throwable {
		attachment = (Attachment) key.attachment();
		channel = (SocketChannel) key.channel();

		DataPacket dataPacket = attachment.getAttachment();

		channel.write(ByteBufferUtil.newInstance().createPackageByByteBuffer(dataPacket.getData()));
		// throw new Throwable("channel读取失败");
		// 注册读
		if (dataPacket.getConnectType() == DataPacket.CONNECT_TYPE_SHORT)
			attachment.registerClose(key);
		else
			attachment.registerRead();
		return key;
	}
}
