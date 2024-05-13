package io.grpc.java.testing_service;

import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.concurrent.TimeUnit;

public class TestingServiceClient {

    public ManagedChannel channel;
    public UserGrpc.UserBlockingStub stub;
    public LoginRequest request;
    public Metadata header;

    public static void main(String args[]) {
    }

    public TestingServiceClient(String host, int port) {
        this.channel = createChannel(host, port);
        this.stub = createBlockingStub(this.channel);
    }

    public ManagedChannel createChannel(String host, int port) {
        channel = NettyChannelBuilder.forAddress(host, port).usePlaintext().build();
        return channel;
    }

    public UserGrpc.UserBlockingStub createBlockingStub(ManagedChannel channel) {
        stub = UserGrpc.newBlockingStub(channel);
        return stub;
    }

    public TestingServiceClient createLoginRequest(String username, String password) {
        request = LoginRequest.newBuilder().setUsername(username).setPassword(password).build();
        return this;
    }

    public APIResponse sendRequestToService(TestingServiceClient client, String username, String password) {
        client = client.createLoginRequest(username, password);

        APIResponse serverResponse = stub.withInterceptors(new HeaderClientInterceptor())
                .login(request);
        return serverResponse;
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

}
