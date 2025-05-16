package com.smart1.appsmartweb.util;

import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.model.Storage;
import com.smart1.appsmartweb.repository.StorageRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final StorageRepository storageRepository;

    public DataInitializer(StorageRepository storageRepository){
        this.storageRepository = storageRepository;
    }

    @PostConstruct
    public void init(){
        if(storageRepository.count() == 0) {
            Storage stock = new Storage();
            stock.setId(1L);
            stock.setName("Stock");
            stock.setCapacity(28);
            storageRepository.save(stock);

            Storage expedition = new Storage();
            expedition.setId(2L);
            expedition.setName("Expedition");
            expedition.setCapacity(12);
            storageRepository.save(expedition);
        }
    }
}
