package com.grpc.demo;

import com.blackrock.grpcdemo.CheckoutJobReply;
import com.blackrock.grpcdemo.CheckoutJobRequest;
import com.blackrock.grpcdemo.StreamingCheckoutGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;

public class CheckoutAsyncService extends StreamingCheckoutGrpc.StreamingCheckoutImplBase {
  private static final Logger logger = LoggerFactory.getLogger(CheckoutAsyncService.class.getName());

  private CheckoutExecutorManager checkoutExecutorManager;

  public CheckoutAsyncService(CheckoutExecutorManager checkoutExecutorManager) {
    this.checkoutExecutorManager = checkoutExecutorManager;
    checkoutExecutorManager.start();
  }

  @Override
  public void subscribeCheckoutJob(CheckoutJobRequest request, StreamObserver<CheckoutJobReply> responseObserver) {
    logger.debug("Registering a new D2 request");
    checkoutExecutorManager.registerCheckoutRequest(request.getApplication(),responseObserver);
  }
}
