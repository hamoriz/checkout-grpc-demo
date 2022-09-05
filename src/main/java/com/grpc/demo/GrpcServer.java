package com.grpc.demo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
  private static final Logger logger = LoggerFactory.getLogger(GrpcServer.class.getName());

  private static final Integer DEFAULT_PORT = 54321;

  Server server;

  public GrpcServer(BindableService service) {
    this.server = ServerBuilder.forPort(DEFAULT_PORT)
        .addService(service)
        .build();
  }

  public void start() throws IOException {
    this.server.start();
    logger.info("Server started, listening on " + DEFAULT_PORT);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown
        // hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        try {
          GrpcServer.this.stop();
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() throws InterruptedException {
    if (this.server != null) {
      this.server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  public void blockUntilShutdown() throws InterruptedException {
    if (this.server != null) {
      this.server.awaitTermination();
    }
  }

}
