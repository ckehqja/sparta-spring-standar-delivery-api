package kr.sparta.deliveryapi.service;

import java.util.List;

import kr.sparta.deliveryapi.model.Delivery;
import kr.sparta.deliveryapi.model.enumtype.DeliveryStatus;

public interface Deliverable<T> {
	Delivery deliver(Long id);

	DeliveryStatus track(String trackingNumber);

	List<T> getAll();
}
