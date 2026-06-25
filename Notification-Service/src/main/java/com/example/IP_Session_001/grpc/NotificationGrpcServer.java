package com.example.IP_Session_001.grpc;

import com.example.IP_Session_001.dto.OrderEventDTO;
import com.example.IP_Session_001.model.Notification;
import com.example.IP_Session_001.service.NotificationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * gRPC entry point for placing notifications.
 *
 * This is the fourth delivery architecture (sync + binary + contract-first),
 * alongside the REST controller, and the Kafka and RabbitMQ consumers.
 * It listens on the dedicated gRPC port (see grpc.server.port) and simply
 * adapts the protobuf request to {@link OrderEventDTO}, then reuses the same
 * {@link NotificationService#placeNotification} logic as every other path.
 */
@Slf4j
@GrpcService                 // registers this as a gRPC endpoint (NOT @RestController)
@RequiredArgsConstructor
public class NotificationGrpcServer
        extends NotificationServiceGrpc.NotificationServiceImplBase {

    private final NotificationService notificationService;   // reuse existing service

    @Override
    public void placeNotification(OrderEvent request,
                                  StreamObserver<NotificationResponse> responseObserver) {

        log.info("[gRPC] Received notification request for order: {}", request.getId());

        // protobuf message -> existing DTO
        OrderEventDTO dto = new OrderEventDTO();
        dto.setId(request.getId());
        dto.setClientId(request.getClientId());
        dto.setProductId(request.getProductId());
        dto.setQuantity(request.getQuantity());
        dto.setTotalPrice(request.getTotalPrice());

        // same business logic as the REST / Kafka / RabbitMQ paths
        Notification notification = notificationService.placeNotification(dto);

        NotificationResponse response = NotificationResponse.newBuilder()
                .setStatus("SENT")
                .setMessage("Notification placed for order " + request.getId()
                        + " (notification id: " + notification.getMongoId() + ")")
                .build();

        responseObserver.onNext(response);   // send the reply
        responseObserver.onCompleted();      // close the stream

        log.info("[gRPC] Notification processed for order {}", request.getId());
    }
}