package com.github.sysdepen.depen_api.services;


import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.repository.DocumentRepository;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        try{
            // Define o caminho do arquivo com base no tipo de documento
            String filePath = "uploads/" + userId + "/" + documentType + "/" + fileName;

            // Cria a entidade e salva no banco
            Documents userDocument = new Documents();
            Usuario userCurrent =  this.userService.findById(userId);
            userDocument.setUser(userCurrent);

            if(Objects.equals(documentType, "RG")) {
                userDocument.setFileRGPath(filePath);
            }else {
                var path =  userDocument.getFileRGPath();
                userDocument.setFileRGPath(path);
            }

            if(Objects.equals(documentType, "CPF")) {
                userDocument.setFileCPFPath(filePath);
            }else {
                var path =  userDocument.getFileCPFPath();
                userDocument.setFileCPFPath(path);
            }

            if (Objects.equals(documentType, "grauP")) {
                userDocument.setFileGrauParentescoPath(filePath);
            } else {
                var path =  userDocument.getFileGrauParentescoPath();
                userDocument.setFileGrauParentescoPath(path);
            }

            if(Objects.equals(documentType, "endereco")) {
                userDocument.setFileEnderecoPath(filePath);
            }else {
                var path =  userDocument.getFileEnderecoPath();
                userDocument.setFileEnderecoPath(path);
            }

            if(Objects.equals(documentType, "foto")) {
                userDocument.setFileFotoPath(filePath);
            }else {
                var path =  userDocument.getFileFotoPath();
                userDocument.setFileFotoPath(path);
            }

            userDocument.setDocumentType(documentType);

            return documentRepository.save(userDocument);
        }
        catch (Exception e){
            throw new RuntimeException("document not saved");
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
