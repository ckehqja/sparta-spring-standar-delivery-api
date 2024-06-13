package kr.sparta.deliveryapi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sun.nio.sctp.IllegalReceiveException;

import kr.sparta.deliveryapi.model.Common;
import kr.sparta.deliveryapi.model.Delivery;
import kr.sparta.deliveryapi.model.enumtype.DeliveryStatus;
import kr.sparta.deliveryapi.repository.DeliveryRepository;

public abstract class AbstractDelivery<T extends Common> implements Deliverable<T> {
	protected final DeliveryRepository deliveryRepository;

	protected AbstractDelivery(DeliveryRepository deliveryRepository) {
		this.deliveryRepository = deliveryRepository;
	}

	protected String generateTrackingNo(String description) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
			+ String.valueOf(description.hashCode()).substring(0, 4);
	}

	@Override
	public DeliveryStatus track(String trackingNumber) {
		return deliveryRepository.findById(trackingNumber)
			.map(Delivery::getStatus)
			.orElseThrow(IllegalReceiveException::new);
	}

}
