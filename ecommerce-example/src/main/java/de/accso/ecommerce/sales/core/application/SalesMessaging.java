package de.accso.ecommerce.sales.core.application;

import de.accso.ecommerce.common.Event;

public interface SalesMessaging {
    public void send(Event event);
}
