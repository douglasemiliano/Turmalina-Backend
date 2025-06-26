package com.ifpb.turmalina.Entity;


import com.ifpb.turmalina.DTO.AlunoRankingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ranking_curso")
public class Ranking {
    @Id
    private String cursoId;
    private List<AlunoRankingDto> alunos;
    private LocalDateTime ultimaAtualizacao;

    public String getCursoId() {
        return cursoId;
    }

    public void setCursoId(String cursoId) {
        this.cursoId = cursoId;
    }

    public List<AlunoRankingDto> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<AlunoRankingDto> alunos) {
        this.alunos = alunos;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "cursoId='" + cursoId + '\'' +
                ", alunos=" + alunos.toString() +
                ", ultimaAtualizacao=" + ultimaAtualizacao +
                '}';
    }
}
