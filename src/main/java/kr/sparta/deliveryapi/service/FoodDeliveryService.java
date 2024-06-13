package kr.sparta.deliveryapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.sparta.deliveryapi.model.Delivery;
import kr.sparta.deliveryapi.model.Food;
import kr.sparta.deliveryapi.model.enumtype.DeliveryStatus;
import kr.sparta.deliveryapi.model.enumtype.ItemType;
import kr.sparta.deliveryapi.repository.DeliveryRepository;
import kr.sparta.deliveryapi.repository.FoodRepository;

@Service
public class FoodDeliveryService extends AbstractDelivery<Food> {
	private final FoodRepository foodRepository;

	public FoodDeliveryService(FoodRepository foodRepository, DeliveryRepository deliveryRepository) {
		super(deliveryRepository);
		this.foodRepository = foodRepository;
	}

	@Override
	public Delivery deliver(Long id) {
		final Food food = foodRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);

		final String trackingNo = generateTrackingNo(food.getName());
		final Delivery delivery = Delivery.builder()
			.trackingNumber(trackingNo)
			.itemType(ItemType.FOOD)
			.status(DeliveryStatus.SHIPPED)
			.itemId(food.getId())
			.name(food.getName())
			.build();

		deliveryRepository.save(delivery);

		return delivery;
	}

	@Override
	public List<Food> getAll() {
		return foodRepository.findAll();
	}

}
