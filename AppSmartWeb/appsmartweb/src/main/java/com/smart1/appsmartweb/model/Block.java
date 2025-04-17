package com.smart1.appsmartweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int position;

    private int color;

    @ManyToOne
    @JoinColumn(name = "StorageId")
    private Storage storageId;

    @ManyToOne
    @JoinColumn(name = "ProductionOrder", referencedColumnName = "productionOrder")
    private Orders productionOrders;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Storage getStorageId() {
        return storageId;
    }

    public void setStorageId(Storage storageId) {
        this.storageId = storageId;
    }

    public Orders getProductionOrders() {
        return productionOrders;
    }

    public void setProductionOrders(Orders productionOrders) {
        this.productionOrders = productionOrders;
    }
}