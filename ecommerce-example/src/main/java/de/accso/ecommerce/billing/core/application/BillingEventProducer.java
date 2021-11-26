package de.accso.ecommerce.billing.core.application;

import de.accso.ecommerce.billing.api.BillCreatedEvent;
import de.accso.ecommerce.billing.api.PaymentDoneEvent;

import java.util.ArrayList;
import java.util.List;

public class BillingEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    private BillingMessaging messageBus;

    static {
        allEventTypes.add(BillCreatedEvent.class);
        allEventTypes.add(PaymentDoneEvent.class);
    }

    void produceBillCreatedEvent() {
        messageBus.send(new BillCreatedEvent());
    }

    void producePaymentDoneEvent() {
        messageBus.send(new PaymentDoneEvent());
    }
}
