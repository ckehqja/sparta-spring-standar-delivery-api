package kr.sparta.deliveryapi.service;

import java.util.List;

import kr.sparta.deliveryapi.model.Common;
import kr.sparta.deliveryapi.model.Delivery;
import kr.sparta.deliveryapi.model.enumtype.DeliveryStatus;

public interface Deliverable<T extends Common> {
	Delivery deliver(Long id);

	DeliveryStatus track(String trackingNumber);

	List<T> getAll();
}
