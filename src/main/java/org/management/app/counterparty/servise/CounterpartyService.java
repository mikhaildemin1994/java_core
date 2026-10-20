package org.management.app.counterparty.servise;

import org.management.app.counterparty.model.Counterparty;

public interface CounterpartyService {
    Counterparty getCounterpartyEntityById (Long counterpartyId);
}
