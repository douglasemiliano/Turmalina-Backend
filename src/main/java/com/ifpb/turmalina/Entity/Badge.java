package com.ifpb.turmalina.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Badges")
public class Badge {
    @Id
    private String id;
    private String nome;
    private String descricao;
    private LocalDateTime dataObtida;
    private String imagem;
    private String createdBy;

    public Badge(String nome, String descricao, String imagem, String createdBy) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataObtida = LocalDateTime.now();
        this.imagem = imagem;
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataObtida() {
        return dataObtida;
    }

    public void setDataObtida(LocalDateTime dataObtida) {
        this.dataObtida = dataObtida;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}