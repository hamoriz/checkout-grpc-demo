package com.grpc.demo;

import com.blackrock.grpcdemo.CheckoutJobReply;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CheckoutExecutorManager {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutExecutorManager.class);
    private final Map<String, StreamObserver<CheckoutJobReply>> clientMap;

    public CheckoutExecutorManager() {
        this.clientMap = new ConcurrentHashMap<>();
    }

    /**
     * This will run a background thread to update the observers with new status. It is a single thread that maintains
     * what requests we have
     * TODO - on error, completed is not implemented as it is just to demonstrate the single thread approach
     */
    public void start() {
      Thread checkoutUpdater = new Thread(() -> {
          int i=0;
          logger.info("Checkout execution request handler started");
          while (true) {
              for (Map.Entry<String, StreamObserver<CheckoutJobReply>> entry : clientMap.entrySet()) {
                  String jobStatus = getStatusForJob(entry.getKey(),i);

                  CheckoutJobReply reply =CheckoutJobReply.newBuilder().setStatus(getStatusForJob(entry.getKey(),i)).build();
                entry.getValue().onNext(reply);
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
      })  ;
        checkoutUpdater.start();
    }

    public void registerCheckoutRequest(String client, StreamObserver<CheckoutJobReply> observer) {
        clientMap.put(client,observer);
    }

    /**
     * This can query the application/app specific checkout status -
     * we could do it in bulk mode for all the app/clients we have on the map
     * @param application
     * @return
     */
    private String getStatusForJob(String application, int status) {
        return application+" "+status;
    }

}
