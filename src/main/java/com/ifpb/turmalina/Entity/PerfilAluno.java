package com.ifpb.turmalina.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "perfil_aluno")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class PerfilAluno {
    @Id
    private String alunoId;
    private String nome;
    private double pontuacaoGlobal;
    private int nivel; // 1 a 100
    private List<String> badges;
    private LocalDateTime ultimaAtualizacao;

    public PerfilAluno(String alunoId) {
        this.alunoId = alunoId;
        this.nome = "";
        this.pontuacaoGlobal = 0.0;
        this.nivel = 1; // Nível inicial
        this.badges = List.of(); // Lista vazia de badges
        this.ultimaAtualizacao = LocalDateTime.now(); // Data e hora atual
    }

    public String getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(String alunoId) {
        this.alunoId = alunoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPontuacaoGlobal() {
        return pontuacaoGlobal;
    }

    public void setPontuacaoGlobal(double pontuacaoGlobal) {
        this.pontuacaoGlobal = pontuacaoGlobal;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public List<String> getBadges() {
        return badges;
    }

    public void setBadges(List<String> badges) {
        this.badges = badges;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
