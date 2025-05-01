package com.github.sysdepen.depen_api.services;


import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.repository.DocumentRepository;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UsuarioService userService;


    public Optional<Documents> findByUserIdAndDocumentType(Long userId, String documentType) {
        return documentRepository.findByUserIdAndDocumentType(userId, documentType);
    }


    public Documents save(Long userId, String documentType, String fileName) {
        try {
            String filePath = "uploads/" + userId + "/" + documentType + "/" + fileName;

            Usuario userCurrent = userService.findById(userId);

            // Verifica se já existe um registro para o usuário
            Documents userDocument = documentRepository.findByUser(userCurrent)
                    .orElseGet(() -> {
                        Documents newDoc = new Documents();
                        newDoc.setUser(userCurrent);
                        return newDoc;
                    });

            // Atualiza apenas o campo do tipo correspondente
            switch (documentType.toLowerCase()) {
                case "rg" -> userDocument.setFileRGPath(filePath);
                case "cpf" -> userDocument.setFileCPFPath(filePath);
                case "graup" -> userDocument.setFileGrauParentescoPath(filePath);
                case "endereco" -> userDocument.setFileEnderecoPath(filePath);
                case "foto" -> userDocument.setFileFotoPath(filePath);
                case "antcriminais" -> userDocument.setFileAntCriminaisPath(filePath);
                default -> throw new IllegalArgumentException("Tipo de documento não reconhecido: " + documentType);
            }

            userDocument.setDocumentType(documentType);
            userDocument.setUpdated_at(LocalDateTime.now());

            return documentRepository.save(userDocument);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("document not saved: " + e.getMessage());
        }
    }



    public List<Documents> findAll() {
            return documentRepository.findAll();
    }


    public Optional<Documents> findById(Long id) {
        if(documentRepository.findById(id).isEmpty()){
            throw new RuntimeException("document not found with id " + id);
        }
        return documentRepository.findById(id);
    }


    public Documents update(Documents documents) {
        return null;
    }


    public void deleteById(Long id) {

    }
}
