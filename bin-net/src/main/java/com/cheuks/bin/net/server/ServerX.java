package com.cheuks.bin.net.server;

public class ServerX {
	public static void main(String[] args) throws Throwable {
		//		Logger.getDefault().setErrorWrite(false).setInfoWrite(true);
		//		ExecutorService mananger = Executors.newFixedThreadPool(10);
		//		SelectorThread st;
		//		//				mananger.execute(new SelectorThread(2L, 10086, 10087, 10088, 10089));
		//		mananger.execute(st = new SelectorThread(2L, 10010));
		//		mananger.execute(new ReleaseListThread());
		//		mananger.execute(new ReleaseQueueThread(5000));
		//		mananger.execute(new AttachmentListThread(5));
		//		mananger.execute(new ReaderThreadMananger());
		//		mananger.execute(new WriterThreadManange());
		//		st.addListener(10086, 10087, 10088, 10089);

		Server server = NioServer.newInstance();
		server.addService(10088, 10087, 10089, 10086).setTimeOut(60000).start(20);
	}
}
