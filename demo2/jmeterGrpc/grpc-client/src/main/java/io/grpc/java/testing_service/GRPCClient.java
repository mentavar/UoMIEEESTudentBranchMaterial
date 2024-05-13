package io.grpc.java.testing_service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.java.testing_service.UserGrpc;


public class GRPCClient {

    public static void main(String[] args){
                                                                                      // maybe here use useSecurity()
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091).usePlaintext().build();

        //stub from proto file

        UserGrpc.UserBlockingStub stub =  UserGrpc.newBlockingStub(channel);
        LoginRequest request = LoginRequest.newBuilder().setUsername("Hello").setPassword("Hello").build();

        APIResponse response = stub.login(request);

        System.out.println("Response : "+ response.getResponseMessage());

    }
}
