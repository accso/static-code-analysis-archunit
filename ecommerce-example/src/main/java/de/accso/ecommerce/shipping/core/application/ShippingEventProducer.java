package de.accso.ecommerce.shipping.core.application;

import de.accso.ecommerce.shipping.api.*;

import java.util.ArrayList;
import java.util.List;

public class ShippingEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    static {
        allEventTypes.add(DeliveryDeliveredEvent.class);
        allEventTypes.add(DeliveryPreparedEvent.class);
        allEventTypes.add(DeliveryRetourEvent.class);
        allEventTypes.add(DeliverySentOutEvent.class);
    }
}
