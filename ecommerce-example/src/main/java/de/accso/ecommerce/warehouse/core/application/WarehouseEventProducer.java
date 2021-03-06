package de.accso.ecommerce.warehouse.core.application;

import de.accso.ecommerce.warehouse.api.NewGoodsReceivedEvent;
import de.accso.ecommerce.warehouse.api.ProductRunsOutOfStockEvent;

import java.util.ArrayList;
import java.util.List;

public class WarehouseEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    private WarehouseMessaging messageBus;

    static {
        allEventTypes.add(NewGoodsReceivedEvent.class);
        allEventTypes.add(ProductRunsOutOfStockEvent.class);
    }

    void produceNewGoodsReceivedEvent() {
        messageBus.send(new NewGoodsReceivedEvent());
    }

    void produceProductRunsOutOfStockEvent() {
        messageBus.send(new ProductRunsOutOfStockEvent());
    }
}
