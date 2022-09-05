package com.grpc.demo;

public class CheckoutAsyncServer {
  public static void main(String... args) {
    try {
      startThreadCountReporter();
      GrpcServer server = new GrpcServer(new CheckoutAsyncService(new CheckoutExecutorManager()));

      server.start();
      server.blockUntilShutdown();
    } catch (Exception ex) {
      return;
    }
  }

  private static void startThreadCountReporter() {
    Thread reporter = new Thread(new ThreadCountReporter());
    reporter.start();
  }

}
