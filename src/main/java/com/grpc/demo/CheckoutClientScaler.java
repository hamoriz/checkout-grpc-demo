package com.grpc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutClientScaler {
  private static final Logger logger = LoggerFactory.getLogger(CheckoutClientScaler.class.getName());



  public void run() {

    for (int i = 0; i < 1000; i++) {
      startConsumer("Client"+String.valueOf(i));
    }
  }

  private void startConsumer(String i) {
    Thread consumer = new Thread(new CheckoutIndividualClient(i));
    consumer.start();
  }

}
