package com.smart1.appsmartweb.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart1.appsmartweb.model.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {
    // Mantém a busca original por cor no estoque (storageId = 1)
    @Query("SELECT b FROM Block b WHERE b.color = :color AND b.productionOrder IS NULL AND b.storageId.id = 1 ORDER BY b.position")
    List<Block> findAvailableBlocksByColor(int color);

    // Busca posições ocupadas na expedição (storageId = 2)
    @Query("SELECT b.position FROM Block b WHERE b.storageId.id = 2")
    List<Integer> findOccupiedPositionsInExpedicao();

    // Mapa completo de posições
    @Query("SELECT b.position, b.color FROM Block b WHERE b.storageId.id = :storageId ORDER BY b.position")
    List<Object[]> findPositionColorMapByStorageId(int storageId);

    // Método para encontrar a primeira posição livre
    default int findFirstFreePosition(int storageId) {
        System.out.println("[findFirstFreePosition] Iniciando busca por posição livre no storageId: " + storageId);

        // 1. Busca posições ocupadas
        System.out.println("[findFirstFreePosition] Consultando posições ocupadas...");
        List<Object[]> occupiedPositions = findPositionColorMapByStorageId(storageId);
        System.out.println("[findFirstFreePosition] Encontradas " + occupiedPositions.size() + " posições ocupadas");

        // 2. Inicializa mapa de posições
        int[] positionMap = new int[28];
        System.out.println("[findFirstFreePosition] Mapa de posições inicializado (28 slots)");

        // 3. Preenche o mapa com cores
        for (Object[] data : occupiedPositions) {
            int position = (int) data[0];
            int color = (int) data[1];

            if (position >= 1 && position <= 28) {
                System.out.println("[findFirstFreePosition] Preenchendo posição " + position + " com cor " + color);
                positionMap[position - 1] = color;
            } else {
                System.out.println("[findFirstFreePosition] AVISO: Posição inválida ignorada: " + position);
            }
        }

        // 4. Busca primeira posição livre
        System.out.println("[findFirstFreePosition] Procurando primeira posição livre...");
        for (int i = 0; i < positionMap.length; i++) {
            if (positionMap[i] == 0) {
                System.out.println("[findFirstFreePosition] Posição livre encontrada: " + (i + 1));
                return i + 1;
            }
        }

        // 5. Caso todas ocupadas
        System.out.println("[findFirstFreePosition] AVISO: Nenhuma posição livre encontrada (armazém cheio)");
        return -1;
    }

    // Método para obter o array completo de posições
    default int[] getPositionMap(int storageId) {
        System.out.println("[getPositionMap] Gerando mapa completo para storageId: " + storageId);

        // 1. Busca posições ocupadas
        System.out.println("[getPositionMap] Consultando posições ocupadas...");
        List<Object[]> occupiedPositions = findPositionColorMapByStorageId(storageId);
        System.out.println("[getPositionMap] Encontradas " + occupiedPositions.size() + " posições ocupadas");

        // 2. Inicializa mapa
        int[] positionMap = new int[28];
        System.out.println("[getPositionMap] Mapa de posições inicializado com 28 slots (0=livre)");

        // 3. Preenche o mapa
        for (Object[] data : occupiedPositions) {
            int position = (int) data[0];
            int color = (int) data[1];

            if (position >= 1 && position <= 28) {
                System.out.println("[getPositionMap] Mapeando posição " + position + " -> cor " + color);
                positionMap[position - 1] = color;
            } else {
                System.out.println("[getPositionMap] AVISO: Posição inválida " + position + " ignorada");
            }
        }

        // 4. Exibe resumo
        System.out.println("[getPositionMap] Mapa final gerado: " + Arrays.toString(positionMap));
        return positionMap;
    }


    @Query("SELECT b.position FROM Block b WHERE b.storageId.id = 1 AND b.color = :color AND b.productionOrder IS NULL")
    List<Integer> findAvailablePositionsByColor(int color);

    @Query("SELECT b.position FROM Block b WHERE b.storageId.id = :storageId")
    List<Integer> findOccupiedPositionsByStorageId(Long storageId);


    default int findFirstFreePositionByColorAndStorage(int color, Long storageId) {
        List<Integer> posicoesLivres = findAvailablePositionsByColor(color);
        if (!posicoesLivres.isEmpty()) {
            return posicoesLivres.get(0);
        }
        throw new RuntimeException("Não há blocos disponíveis da cor " + color + " no estoque");
    }
}
