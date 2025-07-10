package com.smart1.appsmartweb.service.entities;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.repository.BlockRepository;

@Service
public class BlockService {
    @Autowired
    private BlockRepository blockRepository;

    public Block saveBlock(Block block) {
        return blockRepository.save(block);
    }

    public List<Block> listBlocks() {
        return blockRepository.findAll();
    }

    public Block findById(Long id) {
        Optional<Block> block = blockRepository.findById(id);
        return block.orElseThrow(() -> new RuntimeException("Block não encontrado"));
    }

    public void deleteById(Long id) {
        blockRepository.deleteById(id);
    }

    public Integer getLastPositionByStorageId(Long storageId) {
        // Criar pageable para limitar a 1 resultado (o último)
        PageRequest topOne = PageRequest.of(0, 1);

        List<Integer> positions = blockRepository.findPositionByStorageIdIdOrderByIdDesc(storageId, topOne);

        return positions.isEmpty() ? null : positions.get(0);
    }
}
