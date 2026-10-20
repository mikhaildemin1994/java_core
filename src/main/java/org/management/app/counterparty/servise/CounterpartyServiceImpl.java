package org.management.app.counterparty.servise;

import lombok.RequiredArgsConstructor;
import org.management.app.counterparty.model.Counterparty;
import org.management.app.counterparty.repository.CounterpartyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterpartyServiceImpl implements CounterpartyService{
    private final CounterpartyRepository counterpartyRepository;

    @Override
    public Counterparty getCounterpartyEntityById(Long counterpartyId) {
        return counterpartyRepository.findById(counterpartyId).orElseThrow();
    }
}
