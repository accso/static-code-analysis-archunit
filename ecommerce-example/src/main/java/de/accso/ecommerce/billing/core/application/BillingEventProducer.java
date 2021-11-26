package de.accso.ecommerce.billing.core.application;

import de.accso.ecommerce.billing.api.*;

import java.util.ArrayList;
import java.util.List;

public class BillingEventProducer {
    private static List<Class> allEventTypes = new ArrayList<>();

    static {
        allEventTypes.add(BillCreatedEvent.class);
        allEventTypes.add(PaymentDoneEvent.class);
    }
}
