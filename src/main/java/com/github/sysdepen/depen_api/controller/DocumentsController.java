package com.github.sysdepen.depen_api.controller;


import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.services.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/documents")
@Validated
@CrossOrigin("*")
public class DocumentsController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<Documents>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Documents>> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(documentService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
    }

    @GetMapping("/documents")
    public ResponseEntity<Resource> getDocumentByUserAndType(
            @RequestParam Long userId,
            @RequestParam String documentType,
            HttpServletRequest request) {
        // Busca o documento no banco de dados pelo userId e documentType
        Documents document = documentService.findByUserIdAndDocumentType(userId, documentType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado"));

        // Obtém o caminho completo do arquivo no sistema de arquivos
        File file = new File(document.getFileRGPath());

        if (!file.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado no servidor");
        }

        try {
            // Cria o recurso para o arquivo
            Resource resource = new UrlResource(file.toURI());

            // Determina o tipo de conteúdo do arquivo
            String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // Tipo padrão se não identificado
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao carregar o arquivo", e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("userId") Long userId,
                                         @RequestParam("files") List<MultipartFile> files,
                                         @RequestParam("documentType") String documentType) throws IOException {
        // Define o diretório base para o usuário
        String baseDir = System.getProperty("user.dir") + "/uploads/";
        File userDir = new File(baseDir + userId);

        // Verifica se a pasta do userId já existe
        if (!userDir.exists()) {
            userDir.mkdirs(); // Cria a pasta do userId, se não existir
        }

        // Define a subpasta para o tipo de documento
        File documentTypeDir = new File(userDir, documentType);

        // Verifica se a pasta do tipo de documento já existe
        if (documentTypeDir.exists()) {
            return ResponseEntity.badRequest().body("O tipo de documento '" + documentType + "' já foi enviado para este usuário.");
        }

        // Cria a subpasta para o tipo de documento
        documentTypeDir.mkdirs();

        List<Documents> savedDocuments = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            File targetFile = new File(documentTypeDir, fileName);

            // Transfere o arquivo para o sistema de arquivos
            file.transferTo(targetFile);

            // Salva o caminho do arquivo no banco de dados
            Documents userDocument = documentService.save(userId, documentType, fileName);
            savedDocuments.add(userDocument);
        }

        return ResponseEntity.ok(savedDocuments);
    }

    @PutMapping
    public ResponseEntity<Documents> update(@RequestBody Documents documents) {
        return ResponseEntity.status(HttpStatus.OK).body(documentService.update(documents));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        documentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
