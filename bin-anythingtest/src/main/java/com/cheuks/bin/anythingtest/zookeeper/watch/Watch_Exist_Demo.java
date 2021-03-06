package com.cheuks.bin.anythingtest.zookeeper.watch;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Watch_Exist_Demo implements Watch {

	public static void main(String[] args) {

		Executors.newCachedThreadPool().execute(new listener());
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//		Thread thread = new Thread(new listener());
		//		thread.start();
	}

	static class listener implements Runnable {
		public void run() {
			try {
				final ZooKeeper zkClient = new ZooKeeper(ipAddress, 50000, null);

				//watchManager
				Field watchManager = ZooKeeper.class.getDeclaredField("watchManager");
				watchManager.setAccessible(true);

				//defaultWatcher
				Field defaultWatcher = null;

				Class[] cs = ZooKeeper.class.getDeclaredClasses();
				for (Class c : cs) {
					if (c.getName().contains("ZKWatchManager")) {
						defaultWatcher = c.getDeclaredField("defaultWatcher");
						defaultWatcher.setAccessible(true);
					}
				}

				Object watchManagerInstance = watchManager.get(zkClient);
				if (null != defaultWatcher)
					defaultWatcher.set(watchManagerInstance, new watch_listener("mmx节点侦听", zkClient));

				zkClient.exists("/mmx", true);
				//				zkClient.exists("/mmx", new watch_listener("mmx节点侦听", zkClient));
				//				zkClient.exists("mmx", new watch_listener("mmx节点侦听"), new StatCallback() {
				//
				//					public void processResult(int rc, String path, Object ctx, Stat stat) {
				//						System.out.println("asdfasdfasfasdfdsafdsafdsafds");
				//					}
				//				}, "context");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class watch_listener implements Watcher {

		private String name;
		private ZooKeeper zk;

		public void process(WatchedEvent event) {
			System.err.println(String.format("#%s", name));
			System.err.println(String.format("----------%s%s", "Path:", event.getPath()));
			System.err.println(String.format("----------%s%s", "State:", event.getState()));
			System.err.println(String.format("----------%s%s", "Type:", event.getType()));
			System.err.println(String.format("----------%s%s", "Wrapper:", event.getWrapper()));
			try {
				//				zk.exists(event.getPath(), new watch_listener(event.getPath() + "节点变化侦听", zk));
				zk.exists(event.getPath(), true);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public watch_listener(String name, ZooKeeper zk) {
			super();
			this.name = name;
			this.zk = zk;
		}
	}

}
