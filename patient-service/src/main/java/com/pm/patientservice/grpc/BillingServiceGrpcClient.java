package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import billing.BillingServiceGrpc.BillingServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BillingServiceGrpcClient {
    private final BillingServiceBlockingStub blockingStub;
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);

    public BillingServiceGrpcClient(
        @Value("${billing.service.address:localhost}") String serverAddress,
        @Value("${billing.service.grpc.port:9001}") int serverPort
    ) { 
        log.info("Connecting to Billing gRPC Service at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
            .usePlaintext()
            .build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount (String patientId, String name, String email) {
        log.info("Creating billing account for patientId: {}, name: {}, email: {}", patientId, name, email);
        BillingRequest request = BillingRequest.newBuilder()
            .setPatientId(patientId)
            .setName(name)
            .setEmail(email)
            .build();
        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Received billing account creation response via GRPC: {}", response.toString());
        return response;
    }   
}
