package com.smart1.appsmartweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart1.appsmartweb.model.Block;

public interface BlockRepository extends JpaRepository<Block, Long>{
    // Encontra blocos disponíveis (sem ordem de produção associada) de uma cor específica
    @Query("SELECT b FROM Block b WHERE b.color = :color AND b.productionOrders IS NULL AND b.storageId IS NOT NULL ORDER BY b.position")
    List<Block> findAvailableBlocksByColor(int color);

    // Encontra a próxima posição disponível em um armazém específico
    @Query("SELECT COALESCE(MAX(b.position), 0) + 1 FROM Block b WHERE b.storageId.id = :storageId")
    int findNextAvailablePosition(Long storageId);
}
