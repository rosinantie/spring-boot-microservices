package com.example.IP_Session_001.grpc;

import com.example.IP_Session_001.entity.Order;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class NotificationGrpcClient {

    @GrpcClient("notification-service")   // name must match the yaml key below
    private NotificationServiceGrpc.NotificationServiceBlockingStub stub;

    public void send(Order order) {
        OrderEvent request = OrderEvent.newBuilder()
                .setId(order.getId())
                .setClientId(order.getClientId())
                .setProductId(order.getProductId())
                .setQuantity(order.getQuantity())
                .setTotalPrice(order.getTotalPrice())
                .build();
        NotificationResponse response = stub.placeNotification(request);
        // log response.getStatus()
    }
}