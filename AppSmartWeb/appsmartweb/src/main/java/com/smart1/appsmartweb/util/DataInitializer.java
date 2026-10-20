package com.smart1.appsmartweb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.model.Storage;
import com.smart1.appsmartweb.repository.StorageRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    public static final long STOCK_STORAGE_ID = 1L;
    public static final int STOCK_CAPACITY = 28;

    public static final long EXPEDITION_STORAGE_ID = 2L;
    public static final int EXPEDITION_CAPACITY = 12;

    private final StorageRepository storageRepository;

    public DataInitializer(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @PostConstruct
    public void init() {
        if (!storageRepository.existsById(STOCK_STORAGE_ID)) {
            Storage stock = new Storage(STOCK_STORAGE_ID, "Stock", STOCK_CAPACITY);
            storageRepository.save(stock);
            log.info("Storage inicializado: id={}, name='Stock', capacity={}", STOCK_STORAGE_ID, STOCK_CAPACITY);
        }

        if (!storageRepository.existsById(EXPEDITION_STORAGE_ID)) {
            Storage expedition = new Storage(EXPEDITION_STORAGE_ID, "Expedition", EXPEDITION_CAPACITY);
            storageRepository.save(expedition);
            log.info("Storage inicializado: id={}, name='Expedition', capacity={}", EXPEDITION_STORAGE_ID, EXPEDITION_CAPACITY);
        }
    }
}
