package com.ifpb.turmalina.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "Desafios")
@Getter
@Setter
public class Desafio {
    private String id;
    private String titulo;
    private String descricao;
    private int pontos;
    private String cursoId;
    private boolean ativo;
    private Badge badge;
    private String createdBy;
    private LocalDateTime dataFinal;

    public Desafio() {
    }

    public Desafio(String id, String titulo, String descricao, int pontos, String cursoId, boolean ativo, Badge badge, String createdBy, LocalDateTime dataFinal) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.pontos = pontos;
        this.cursoId = cursoId;
        this.ativo = ativo;
        this.badge = badge;
        this.createdBy = createdBy;
        this.dataFinal = dataFinal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public String getCursoId() {
        return cursoId;
    }

    public void setCursoId(String cursoId) {
        this.cursoId = cursoId;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }
}
