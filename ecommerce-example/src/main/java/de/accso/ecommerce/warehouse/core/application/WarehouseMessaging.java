package de.accso.ecommerce.warehouse.core.application;

import de.accso.ecommerce.common.Event;

public interface WarehouseMessaging {
    public void send(Event event);
}
