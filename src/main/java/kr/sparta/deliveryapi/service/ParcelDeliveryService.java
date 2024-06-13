package kr.sparta.deliveryapi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sun.nio.sctp.IllegalReceiveException;

import kr.sparta.deliveryapi.model.Delivery;
import kr.sparta.deliveryapi.model.Parcel;
import kr.sparta.deliveryapi.model.enumtype.DeliveryStatus;
import kr.sparta.deliveryapi.model.enumtype.ItemType;
import kr.sparta.deliveryapi.repository.DeliveryRepository;
import kr.sparta.deliveryapi.repository.ParcelRepository;

@Service
public class ParcelDeliveryService implements Deliverable<Parcel> {
	private final DeliveryRepository deliveryRepository;
	private final ParcelRepository parcelRepository;

	public ParcelDeliveryService(DeliveryRepository deliveryRepository, ParcelRepository parcelRepository) {
		this.deliveryRepository = deliveryRepository;
		this.parcelRepository = parcelRepository;
	}

	private String generateTrackingNo(String description) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
			+ String.valueOf(description.hashCode()).substring(0, 4);
	}

	@Override
	public Delivery deliver(Long id) {
		final Parcel parcel = parcelRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);

		final String trackingNo = generateTrackingNo(parcel.getDescription());
		final Delivery delivery = Delivery.builder()
			.trackingNumber(trackingNo)
			.itemType(ItemType.PARCEL)
			.status(DeliveryStatus.SHIPPED)
			.itemId(parcel.getId())
			.name(parcel.getDescription())
			.build();

		deliveryRepository.save(delivery);

		return delivery;
	}

	@Override
	public DeliveryStatus track(String trackingNumber) {
		return deliveryRepository.findById(trackingNumber)
			.map(Delivery::getStatus)
			.orElseThrow(IllegalReceiveException::new);
	}

	@Override
	public List<Parcel> getAll() {
		return parcelRepository.findAll();
	}
}
