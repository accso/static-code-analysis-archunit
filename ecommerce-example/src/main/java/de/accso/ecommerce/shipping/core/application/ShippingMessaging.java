package de.accso.ecommerce.shipping.core.application;

import de.accso.ecommerce.common.Event;

public interface ShippingMessaging {
    public void send(Event event);

}
