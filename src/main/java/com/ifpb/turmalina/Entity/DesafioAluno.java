package com.ifpb.turmalina.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "desafios_aluno")
@Getter
@Setter
@AllArgsConstructor
@Data
public class DesafioAluno {
    @Id
    private String id;
    private String alunoId;        // ID do aluno (vindo do Google Classroom, por exemplo)
    private String desafioId;      // ID do desafio associado
    private String cursoId;       // ID do curso associado ao desafio
    private DesafioStatus status;  // INCOMPLETO, COMPLETO, REIVINDICADO
    private LocalDateTime dataConclusao;
    private LocalDateTime dataReivindicacao;

    public DesafioAluno(){}

    public DesafioAluno(String alunoId, String desafioId, String cursoId, DesafioStatus status, LocalDateTime dataConclusao, LocalDateTime dataReivindicacao) {
        this.alunoId = alunoId;
        this.desafioId = desafioId;
        this.cursoId = cursoId;
        this.status = status;
        this.dataConclusao = dataConclusao;
        this.dataReivindicacao = dataReivindicacao;
    }

    public String getId() {
        return id;
    }

    public String getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(String alunoId) {
        this.alunoId = alunoId;
    }

    public String getDesafioId() {
        return desafioId;
    }

    public void setDesafioId(String desafioId) {
        this.desafioId = desafioId;
    }

    public DesafioStatus getStatus() {
        return status;
    }

    public void setStatus(DesafioStatus status) {
        this.status = status;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public LocalDateTime getDataReivindicacao() {
        return dataReivindicacao;
    }

    public void setDataReivindicacao(LocalDateTime dataReivindicacao) {
        this.dataReivindicacao = dataReivindicacao;
    }

    public String getCursoId() {
        return cursoId;
    }

    public void setCursoId(String cursoId) {
        this.cursoId = cursoId;
    }
}
