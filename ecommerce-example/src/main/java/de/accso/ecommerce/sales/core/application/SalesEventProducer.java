package de.accso.ecommerce.sales.core.application;

import de.accso.ecommerce.sales.api.*;

import java.util.ArrayList;
import java.util.List;

public class SalesEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    static {
        allEventTypes.add(CartUpdatedEvent.class);
        allEventTypes.add(CatalogChangedEvent.class);
        allEventTypes.add(OrderPlacedEvent.class);
        allEventTypes.add(SpecialSalesPhaseEndedEvent.class);
        allEventTypes.add(SpecialSalesPhaseStartedEvent.class);
    }
}
