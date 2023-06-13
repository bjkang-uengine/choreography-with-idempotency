package choreography.with.idempotency.domain;

import choreography.with.idempotency.DeliveryApplication;
import choreography.with.idempotency.domain.DeliveryCancelled;
import choreography.with.idempotency.domain.DeliveryFailed;
import choreography.with.idempotency.domain.DeliveryStarted;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Delivery_table")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String orderId;

    private String productId;

    private String productName;

    private Integer qty;

    private String customerId;

    private String address;

    private String status;

    @PostPersist
    public void onPostPersist() {
        DeliveryStarted deliveryStarted = new DeliveryStarted(this);
        deliveryStarted.publishAfterCommit();

        DeliveryFailed deliveryFailed = new DeliveryFailed(this);
        deliveryFailed.publishAfterCommit();

        DeliveryCancelled deliveryCancelled = new DeliveryCancelled(this);
        deliveryCancelled.publishAfterCommit();
    }

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }

    public static void startDelivery(OrderCreated orderCreated) {
        //implement business logic here:

        /** Example 1:  new item 
        Delivery delivery = new Delivery();
        repository().save(delivery);

        DeliveryStarted deliveryStarted = new DeliveryStarted(delivery);
        deliveryStarted.publishAfterCommit();
        DeliveryFailed deliveryFailed = new DeliveryFailed(delivery);
        deliveryFailed.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderCreated.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);

            DeliveryStarted deliveryStarted = new DeliveryStarted(delivery);
            deliveryStarted.publishAfterCommit();
            DeliveryFailed deliveryFailed = new DeliveryFailed(delivery);
            deliveryFailed.publishAfterCommit();

         });
        */

    }

    public static void compensate(OrderRejected orderRejected) {
        //implement business logic here:

        /** Example 1:  new item 
        Delivery delivery = new Delivery();
        repository().save(delivery);

        DeliveryCancelled deliveryCancelled = new DeliveryCancelled(delivery);
        deliveryCancelled.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderRejected.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);

            DeliveryCancelled deliveryCancelled = new DeliveryCancelled(delivery);
            deliveryCancelled.publishAfterCommit();

         });
        */

    }
}
