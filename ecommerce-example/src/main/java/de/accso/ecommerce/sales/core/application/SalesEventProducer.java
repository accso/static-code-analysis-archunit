package de.accso.ecommerce.sales.core.application;

import de.accso.ecommerce.sales.api.*;
import de.accso.ecommerce.shipping.core.application.ShippingMessaging;

import java.util.ArrayList;
import java.util.List;

public class SalesEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    private ShippingMessaging messageBus;       // this is wrong, tests will fail
                                                // fix by changing type to: SalesMessaging messageBus

    static {
        allEventTypes.add(CartUpdatedEvent.class);
        allEventTypes.add(CatalogChangedEvent.class);
        allEventTypes.add(OrderPlacedEvent.class);
        allEventTypes.add(SpecialSalesPhaseEndedEvent.class);
        allEventTypes.add(SpecialSalesPhaseStartedEvent.class);
    }

    void produceCartUpdatedEvent() {
        messageBus.send(new CartUpdatedEvent());
    }

    void produceCatalogChangedEvent() {
        messageBus.send(new CatalogChangedEvent());
    }

    void produceOrderPlacedEvent() {
        messageBus.send(new OrderPlacedEvent());
    }

    void produceSpecialSalesPhaseEndedEvent() {
        messageBus.send(new SpecialSalesPhaseEndedEvent());
    }

    void produceSpecialSalesPhaseStartedEvent() {
        messageBus.send(new SpecialSalesPhaseStartedEvent());
    }
}
