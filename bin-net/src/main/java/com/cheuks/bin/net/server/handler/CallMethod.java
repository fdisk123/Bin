package com.cheuks.bin.net.server.handler;

import com.cheuks.bin.net.util.ByteBufferUtil;
import com.cheuks.bin.net.util.ByteBufferUtil.DataPacket;
import com.cheuks.bin.net.util.DefaultSerializImpl;
import com.cheuks.bin.net.util.Serializ;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class CallMethod implements Cloneable {

	protected SocketChannel channel;
	protected Serializ defaultSerializ = new DefaultSerializImpl();
	protected Socket s;
	SocketChannel sc;
	private String ip;
	private int port;
	private boolean shortConnect;
	private long connectionDateTime;
	private long timeOut;

	public CallMethod(String ip, int port, boolean shortConnect, int timeOut, Serializ serializ) {
		super();
		this.ip = ip;
		this.port = port;
		this.shortConnect = shortConnect;
		this.timeOut = timeOut;
		if (null != serializ)
			this.defaultSerializ = serializ;
	}

	private boolean timeOutChecked(long lastConnectionTime, long timeOut) {
		return System.currentTimeMillis() - lastConnectionTime < timeOut;
	}

	public SocketChannel getConnection() throws IOException {
		try {
			//			if (null != sc && sc.isConnected() && sc.isOpen() && timeOutChecked(this.connectionDateTime, this.timeOut))
			//				return sc;
			sc = SocketChannel.open();
			sc.connect(new InetSocketAddress(this.ip, this.port));
			return sc;
		} finally {
			connectionDateTime = System.currentTimeMillis();
		}
	}

	public Object call(String path, String methodName, Object... params) throws NumberFormatException, Throwable {
		try {
			MessageInfo messageInfo = new MessageInfo();
			messageInfo.setPath(path).setMethod(methodName);
			messageInfo.setParams(params);
			sc = getConnection();
			sc.write(ByteBufferUtil.newInstance().createPackageByByteBuffer(DataPacket.SERVICE_TYPE_RMI, DataPacket.CONNECT_TYPE_LONG, defaultSerializ.serializ(messageInfo)));
			messageInfo = this.defaultSerializ.toObject(ByteBufferUtil.newInstance().getData(sc, false).getData());
			//			messageInfo = MessageCenter.newInstance().working(ip, port, messageInfo, defaultSerializ);
			if (null != messageInfo.getThrowable())
				throw messageInfo.getThrowable();
			return messageInfo.getResult();
		} finally {
			if (shortConnect) {
				sc = null;
			}
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
