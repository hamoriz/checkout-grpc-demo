package com.grpc.demo;

import java.util.Iterator;

import com.blackrock.grpcdemo.CheckoutJobReply;
import com.blackrock.grpcdemo.CheckoutJobRequest;
import com.blackrock.grpcdemo.StreamingCheckoutGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class CheckoutIndividualClient implements Runnable {
  private static final Integer DEFAULT_PORT = 54321;
  private static final String DEFAULT_HOST = "localhost";

  private static final Logger logger = LoggerFactory.getLogger(CheckoutIndividualClient.class.getName());
  private final String id;

  public CheckoutIndividualClient(String id) {
    this.id=id;
  }
  @Override
  public void run() {
    ManagedChannel channel = ManagedChannelBuilder.forAddress(DEFAULT_HOST, DEFAULT_PORT).usePlaintext().build();
    StreamingCheckoutGrpc.StreamingCheckoutBlockingStub stub = StreamingCheckoutGrpc.newBlockingStub(channel);

    CheckoutJobRequest request = CheckoutJobRequest.newBuilder().setApplication(id).build();
    Iterator<CheckoutJobReply> ticks;
    try {
      ticks = stub.subscribeCheckoutJob(request);
      logger.info("gRPC Client subscribed for: "+request.getApplication());
    } catch (StatusRuntimeException ex) {
      logger.info("gRPC Client failed: {0}", ex.getStatus());
      return;
    }

    while (ticks.hasNext()) {
      CheckoutJobReply tick = ticks.next();
      logger.info(tick.getStatus());
    }


  }
}
