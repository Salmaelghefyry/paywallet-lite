package com.paylogic.paywalletlite.repository.wallet;

import com.paylogic.paywalletlite.domain.wallet.WalletConfig;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class WalletConfigRepositoryImpl implements WalletConfigRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public WalletConfig save(WalletConfig config) {
        if (config.getConfigId() == null) {
            entityManager.persist(config);
            return config;
        }
        return entityManager.merge(config);
    }

    @Override
    public Optional<WalletConfig> findById(UUID configId) {
        return Optional.ofNullable(entityManager.find(WalletConfig.class, configId));
    }
}