package com.ifpb.turmalina.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "desafios")
@Getter
@Setter
@AllArgsConstructor
@Data
public class Desafio {
    @Id
    private String id;
    private String cursoId;
    private String createdBy;
    private String titulo;
    private String descricao;
    private List<Premio> premio;
    private boolean ativo;
    private LocalDateTime dataFinal;
    private List<WinCondition> winCondition;

    public Desafio() {
    }

    public Desafio(String cursoId, String createdBy, String titulo, String descricao, List<Premio> premio, boolean ativo, LocalDateTime dataFinal, List<WinCondition> winCondition) {
        this.cursoId = cursoId;
        this.createdBy = createdBy;
        this.titulo = titulo;
        this.descricao = descricao;
        this.premio = premio;
        this.ativo = ativo;
        this.dataFinal = dataFinal;
        this.winCondition = winCondition;
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

    public List<Premio> getPremio() {
        return premio;
    }

    public void setPremio(List<Premio> premio) {
        this.premio = premio;
    }

    public List<WinCondition> getWinCondition() {
        return winCondition;
    }

    public void setWinCondition(List<WinCondition> winCondition) {
        this.winCondition = winCondition;
    }
}
