package com.phildev.front.mls.service;


import com.phildev.front.mls.dto.UserDTO;
import com.phildev.front.mls.model.Structure;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.StructureRepository;
import com.phildev.front.mls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
    public class UtilisateurService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private StructureRepository structureRepository;

        public List<UserDTO> afficheUtilisateurs(){
            List<UserDTO> userDTOS = new ArrayList<>();
            List<User> users = userRepository.findAll();
            List<Structure> structures = structureRepository.findAll();
            Map<Integer, String> structureAndName = new HashMap<>();
            structures.forEach(structure -> {
                structureAndName.put(structure.getId(), structure.getName());
            });
            for (User user : users) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFirstname(user.getFirstName());
                userDTO.setLastname(user.getLastName());
                userDTO.setStructureName(structureAndName.get(user.getStructureId()));
                userDTO.setEmail(user.getEmail());
                userDTO.setRole(user.getRole());
                userDTOS.add(userDTO);
            }
            return userDTOS;
        }
    }
