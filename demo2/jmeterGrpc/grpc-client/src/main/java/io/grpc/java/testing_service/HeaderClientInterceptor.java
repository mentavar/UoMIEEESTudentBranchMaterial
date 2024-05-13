package io.grpc.java.testing_service;

import io.grpc.*;

public class HeaderClientInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                /* put custom header */

                if (headers != null) {
                    Metadata fixedHeaders = new Metadata();
                    Metadata.Key<String> key = Metadata.Key.of("header", Metadata.ASCII_STRING_MARSHALLER);
                    fixedHeaders.put(key, "hiiii");
                    headers.merge(fixedHeaders);
                }
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        /**
                         * if you don't need receive header from server, you can use
                         * {@link io.grpc.stub.MetadataUtils attachHeaders} directly to send header
                         */
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
