package com.github.sysdepen.depen_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import lombok.Data;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_documents")
public class Documents {

    public Documents() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256)
    private String documentType;


    private String fileRGPath;


    private String fileCPFPath;


    private String fileGrauParentescoPath;


    private String fileAntCriminaisPath;


    private String fileEnderecoPath;


    private String fileFotoPath;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @OneToOne
    @JsonIgnoreProperties
    private Usuario user;


    @Transient
    private MultipartFile arquivo;

    public void setName(String testDocument) {
    }
}
