package com.grpc.demo;

import com.blackrock.grpcdemo.CheckoutJobReply;
import com.blackrock.grpcdemo.CheckoutJobRequest;
import com.blackrock.grpcdemo.StreamingCheckoutGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutSyncService extends StreamingCheckoutGrpc.StreamingCheckoutImplBase {

  private Logger logger = LoggerFactory.getLogger(CheckoutSyncService.class);

  @Override
  public void subscribeCheckoutJob(CheckoutJobRequest request, StreamObserver<CheckoutJobReply> responseObserver) {
    int i=0;
    logger.info("Checkout execution request handler started");
    while (true) {
     {
        String jobStatus = getStatusForApplicationCheckoutJob(request.getApplication(),i);

         CheckoutJobReply reply =CheckoutJobReply.newBuilder().setStatus(getStatusForApplicationCheckoutJob(request.getApplication(),i)).build();
        responseObserver.onNext(reply);
        logger.info("jobStatus for client updated:"+jobStatus);
      }
      i++;
      try {
        logger.info("Jenkins query executed");
        Thread.sleep(5000);
      } catch (InterruptedException ex) {
        return;
      }
    }
  }

  private String getStatusForApplicationCheckoutJob(String client, int status) {
    return client+" "+status;
  }
}
