package kr.sparta.deliveryapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.sparta.deliveryapi.model.Delivery;
import kr.sparta.deliveryapi.model.Parcel;
import kr.sparta.deliveryapi.model.enumtype.DeliveryStatus;
import kr.sparta.deliveryapi.model.enumtype.ItemType;
import kr.sparta.deliveryapi.repository.DeliveryRepository;
import kr.sparta.deliveryapi.repository.ParcelRepository;

@Service
public class ParcelDeliveryService extends AbstractDelivery<Parcel> {
	private final ParcelRepository parcelRepository;

	public ParcelDeliveryService(DeliveryRepository deliveryRepository, ParcelRepository parcelRepository) {
		super(deliveryRepository);
		this.parcelRepository = parcelRepository;
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
	public List<Parcel> getAll() {
		return parcelRepository.findAll();
	}
}
