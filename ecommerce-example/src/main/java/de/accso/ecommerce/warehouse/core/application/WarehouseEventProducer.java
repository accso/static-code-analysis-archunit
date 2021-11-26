package de.accso.ecommerce.warehouse.core.application;

import de.accso.ecommerce.warehouse.api.*;

import java.util.ArrayList;
import java.util.List;

public class WarehouseEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    static {
        allEventTypes.add(NewGoodsReceivedEvent.class);
        allEventTypes.add(ProductRunsOutOfStockEvent.class);
    }
}
