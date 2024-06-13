package kr.sparta.deliveryapi.service;

import com.sun.nio.sctp.IllegalReceiveException;
import kr.sparta.deliveryapi.model.Delivery;
import kr.sparta.deliveryapi.model.Parcel;
import kr.sparta.deliveryapi.model.enumtype.DeliveryStatus;
import kr.sparta.deliveryapi.model.Food;
import kr.sparta.deliveryapi.model.enumtype.ItemType;
import kr.sparta.deliveryapi.repository.DeliveryRepository;
import kr.sparta.deliveryapi.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FoodDeliveryService implements Deliverable<Food>{
    private final FoodRepository foodRepository;
    private final DeliveryRepository deliveryRepository;

    public FoodDeliveryService(FoodRepository foodRepository, DeliveryRepository deliveryRepository) {
        this.foodRepository = foodRepository;
        this.deliveryRepository = deliveryRepository;
    }

    private String generateTrackingNo(String description) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + String.valueOf(description.hashCode()).substring(0, 4);
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
    public DeliveryStatus track(String trackingNumber) {
        return deliveryRepository.findById(trackingNumber)
            .map(Delivery::getStatus)
            .orElseThrow(IllegalReceiveException::new);
    }

    @Override
    public List<Food> getAll() {
        return foodRepository.findAll();
    }

}
