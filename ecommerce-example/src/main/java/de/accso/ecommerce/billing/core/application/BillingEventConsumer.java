package de.accso.ecommerce.billing.core.application;

import de.accso.ecommerce.shipping.api.DeliveryRetourEvent;
import de.accso.ecommerce.shipping.api.DeliverySentOutEvent;

public class BillingEventConsumer {
    void consumeDeliverySentOutEvent(DeliverySentOutEvent event) {
        // nope
    }

    void consumeDeliveryRetourEvent(DeliveryRetourEvent event) {
        // nope
    }
}
