package com.cheuks.bin.anythingtest.zookeeper.watch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Watcher_Demo_1 {

	public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		ZooKeeper zkClient = new ZooKeeper(ServerList.getList(), 5000, new Watcher() {

			public void process(WatchedEvent event) {
				System.out.println(event);
				countDownLatch.countDown();
			}

		});
		countDownLatch.await();
		Stat stat;
		if (null == (stat = zkClient.exists("/listening_path", false)))
			zkClient.create("/listening_path", new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zkClient.create("/listening_path/ip", new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		Thread.sleep(15000);
		// zkClient.delete("/listening_path", -1);
		zkClient.close();
	}

}
