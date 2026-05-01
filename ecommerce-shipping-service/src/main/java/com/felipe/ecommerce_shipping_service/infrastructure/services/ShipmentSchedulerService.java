package com.felipe.ecommerce_shipping_service.infrastructure.services;

import com.felipe.ecommerce_shipping_service.core.domain.ShipmentStatus;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipment.ShipmentEntity;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.repositories.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class ShipmentSchedulerService {
  private final TaskScheduler taskScheduler;
  private final ShipmentRepository shipmentRepository;
  private final Map<UUID, ScheduledFuture<?>> scheduledShipments = new ConcurrentHashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(ShipmentSchedulerService.class);

  public ShipmentSchedulerService(TaskScheduler taskScheduler, ShipmentRepository shipmentRepository) {
    this.taskScheduler = taskScheduler;
    this.shipmentRepository = shipmentRepository;
  }

  public void scheduleShipmentToStatusMutation(ShipmentEntity shipment) {
    schedule(shipment, () -> mutateToOutForDelivery(shipment), shipment.getUpdatedAt());
  }

  private void schedule(ShipmentEntity shipment, Runnable task, LocalDateTime scheduleAt) {
    LocalDateTime mutateAt = scheduleAt.plusMinutes(5);
    Instant instantMutateAt = mutateAt.atZone(ZoneId.systemDefault()).toInstant();
    ScheduledFuture<?> future = this.taskScheduler.schedule(task, instantMutateAt);
    this.scheduledShipments.put(shipment.getId(), future);
    logger.info(
      "Scheduling Shipment -> shipmentId: {} - currentStatus: {} - mutateAt: {}",
      shipment.getId(), shipment.getStatus(), mutateAt
    );
    logger.debug("schedule - Scheduled shipments quantity: {}", this.scheduledShipments.size());
  }

  private void cancelScheduled(UUID shipmentId) {
    ScheduledFuture<?> scheduledShipment = this.scheduledShipments.get(shipmentId);

    if (scheduledShipment != null) {
      scheduledShipment.cancel(true);
      this.scheduledShipments.remove(shipmentId);
      logger.info("Cancelling scheduled shipment -> shipmentId: {}", shipmentId);
      logger.debug("cancelScheduled - Scheduled shipments quantity: {}", this.scheduledShipments.size());
    }
  }

  @EventListener(ApplicationReadyEvent.class)
  private void scheduleShipmentsOnStartUp() {
    logger.info("=== Scheduling shipments to mutate ===");
    Random random = new Random();

    List<ShipmentEntity> shipments = this.shipmentRepository.findAllWithStatusNotEqualTo(ShipmentStatus.DELIVERED);
    shipments.forEach(shipment -> {
      // Generates random scheduling time to prevent shipments from being modified at the same time
      LocalDateTime scheduleAt = LocalDateTime.now().plusMinutes(random.nextInt(1, 6));
      if (shipment.getStatus() == ShipmentStatus.PREPARING) {
        schedule(shipment, () -> mutateToOutForDelivery(shipment), scheduleAt);
      } else {
        schedule(shipment, () -> mutateToDelivered(shipment), scheduleAt);
      }
    });
    logger.info("Scheduled shipments to mutate -> quantity: {}", this.scheduledShipments.size());
  }

  private void mutateToOutForDelivery(ShipmentEntity shipment) {
    this.shipmentRepository.findById(shipment.getId()).ifPresent(foundShipment -> {
      foundShipment.setStatus(ShipmentStatus.OUT_FOR_DELIVERY);
      foundShipment.setShippedAt(LocalDateTime.now());

      ShipmentEntity updatedShipment = this.shipmentRepository.save(foundShipment);
      this.scheduledShipments.remove(updatedShipment.getId());
      logger.info("Mutating Shipment \"{}\" status to OUT_FOR_DELIVERY", updatedShipment.getId());

      schedule(updatedShipment, () -> mutateToDelivered(updatedShipment), updatedShipment.getUpdatedAt());

      // TODO: send to email kafka topic
    });
  }

  private void mutateToDelivered(ShipmentEntity shipment) {
    this.shipmentRepository.findById(shipment.getId()).ifPresent(foundShipment -> {
      foundShipment.setStatus(ShipmentStatus.DELIVERED);
      foundShipment.setDeliveredAt(LocalDateTime.now());

      ShipmentEntity updatedShipment = this.shipmentRepository.save(foundShipment);
      this.scheduledShipments.remove(updatedShipment.getId());
      logger.info("Mutating Shipment \"{}\" status to DELIVERED", updatedShipment.getId());
      logger.debug("mutateToDelivered - Scheduled shipments quantity: {}", this.scheduledShipments.size());

      // TODO: send to email kafka topic
    });
  }
}
