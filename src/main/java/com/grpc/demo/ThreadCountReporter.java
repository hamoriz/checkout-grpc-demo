package com.grpc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadCountReporter implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(ThreadCountReporter.class.getName());

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(1000);
        logger.info("Thread count: " + Thread.activeCount());
      } catch (InterruptedException ex) {
        return;
      }
    }
  }
}
