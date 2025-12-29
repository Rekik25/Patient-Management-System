package com.pm.billingservice.grpc;

import net.devh.boot.grpc.server.service.GrpcService;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import billing.BillingResponse;
import billing.BillingRequest;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);


    // Implement the gRPC service methods here
    @Override
    public void createBillingAccount(BillingRequest billingRequest,
        StreamObserver<BillingResponse> responseObserver){
            log.info("Received billing account creation request --> " + billingRequest.toString());
            // Here you would add your business logic to create a billing account

            BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

    }
}
