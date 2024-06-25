package com.phildev.front.mls.service;

import com.phildev.front.mls.model.Structure;
import com.phildev.front.mls.repository.StructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StructureService {

    @Autowired
    private StructureRepository structureRepository;

    public String getStructureNameById(Integer id){
        Optional<Structure> structure = structureRepository.findById(id);
        return structure.map(Structure::getName).orElse(null);

    }
}
