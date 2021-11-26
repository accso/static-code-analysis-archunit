package de.accso.ecommerce.shipping.core.application;

import de.accso.ecommerce.shipping.api.DeliveryDeliveredEvent;
import de.accso.ecommerce.shipping.api.DeliveryPreparedEvent;
import de.accso.ecommerce.shipping.api.DeliveryRetourEvent;
import de.accso.ecommerce.shipping.api.DeliverySentOutEvent;

import java.util.ArrayList;
import java.util.List;

public class ShippingEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    private ShippingMessaging messageBus;

    static {
        allEventTypes.add(DeliveryDeliveredEvent.class);
        allEventTypes.add(DeliveryPreparedEvent.class);
        allEventTypes.add(DeliveryRetourEvent.class);
        allEventTypes.add(DeliverySentOutEvent.class);
    }

    void produceDeliveryDeliveredEvent() {
        messageBus.send(new DeliveryDeliveredEvent());
    }

    void produceDeliveryPreparedEvent() {
        messageBus.send(new DeliveryPreparedEvent());
    }

    void produceDeliveryRetourEvent() {
        messageBus.send(new DeliveryRetourEvent());
    }

    void produceDeliverySentOutEvent() {
        messageBus.send(new DeliverySentOutEvent());
    }
}
