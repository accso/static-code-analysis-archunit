package de.accso.ecommerce.billing.core.application;

import de.accso.ecommerce.common.Event;

public interface BillingMessaging {
    public void send(Event event);
}
