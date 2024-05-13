package io.grpc.java.testing_service.service;

import io.grpc.java.testing_service.APIResponse;
import io.grpc.java.testing_service.LoginRequest;
import io.grpc.java.testing_service.grpc.UserGrpc;
import io.grpc.stub.StreamObserver;


public class UserService extends UserGrpc.UserImplBase {
    @Override
    public void login(LoginRequest request, StreamObserver<APIResponse> responseObserver) {

        String username = request.getUsername();
        String password = request.getPassword();

        APIResponse.Builder response = APIResponse.newBuilder();

        if(username.equals(password)){
            //success message
            response.setResponseCode(1).setResponseMessage("Success");

        } else  {
            //failure message
            response.setResponseCode(0).setResponseMessage("Invalid username or password");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

//    @Override
//    public void logout(Empty request, StreamObserver<User.APIResponse> responseObserver) {
//
//    }
}
