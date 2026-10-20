package com.smart1.appsmartweb.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart1.appsmartweb.model.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("SELECT b FROM Block b WHERE b.color = :color AND b.productionOrder IS NULL AND b.storageId.id = 1 ORDER BY b.position")
    List<Block> findAvailableBlocksByColor(@Param("color") int color);

    @Query("SELECT b.position FROM Block b WHERE b.storageId.id = 2")
    List<Integer> findOccupiedPositionsInExpedicao();

    @Query("SELECT b.position, b.color FROM Block b WHERE b.storageId.id = :storageId ORDER BY b.position")
    List<Object[]> findPositionColorMapByStorageId(@Param("storageId") int storageId);

    default int findFirstFreePosition(int storageId) {
        int[] positionMap = getPositionMap(storageId);
        for (int i = 0; i < positionMap.length; i++) {
            if (positionMap[i] == 0) {
                return i + 1;
            }
        }
        return -1;
    }

    default int[] getPositionMap(int storageId) {
        List<Object[]> occupiedPositions = findPositionColorMapByStorageId(storageId);
        int[] positionMap = new int[28];
        for (Object[] data : occupiedPositions) {
            int position = (int) data[0];
            int color = (int) data[1];
            if (position >= 1 && position <= 28) {
                positionMap[position - 1] = color;
            }
        }
        return positionMap;
    }

    @Query("SELECT b.position FROM Block b WHERE b.storageId.id = 1 AND b.color = :color AND b.productionOrder IS NULL ORDER BY b.position ASC")
    List<Integer> findAvailablePositionsByColor(@Param("color") int color);

    @Query("SELECT b.position FROM Block b WHERE b.storageId.id = :storageId")
    List<Integer> findOccupiedPositionsByStorageId(@Param("storageId") Long storageId);

    default int findFirstFreePositionByColorAndStorage(int color, Long storageId) {
        List<Integer> posicoesLivres = findAvailablePositionsByColor(color);
        if (!posicoesLivres.isEmpty()) {
            return posicoesLivres.get(0);
        }
        throw new RuntimeException("Não há blocos disponíveis da cor " + color + " no estoque");
    }

    default int findFirstFreePositionByColorExcluding(int color, Set<Integer> excludedPositions) {
        List<Integer> posicoesLivres = findAvailablePositionsByColor(color);
        for (Integer pos : posicoesLivres) {
            if (excludedPositions == null || !excludedPositions.contains(pos)) {
                return pos;
            }
        }
        throw new RuntimeException("Não há blocos suficientes da cor " + color + " no estoque");
    }

    @Query("SELECT b FROM Block b WHERE b.color = :color AND b.storageId.id = :storageId ORDER BY b.position ASC")
    List<Block> findBlocksByColorAndStorageId(@Param("color") int color, @Param("storageId") Long storageId);

    @Query("SELECT b FROM Block b WHERE b.storageId.id = :storageId AND b.position = :position")
    Optional<Block> findByStorageIdAndPosition(@Param("storageId") Long storageId, @Param("position") int position);

    Optional<Block> findByProductionOrder_ProductionOrderAndStorageId_Id(Long productionOrder, Long storageId);

    @Query("SELECT b.position FROM Block b WHERE b.storageId.id = :storageId ORDER BY b.id DESC")
    List<Integer> findPositionByStorageIdIdOrderByIdDesc(@Param("storageId") Long storageId, Pageable pageable);
}
