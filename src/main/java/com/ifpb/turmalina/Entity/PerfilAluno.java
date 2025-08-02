package com.ifpb.turmalina.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private int pontuacaoGlobal;
    private int nivel; // 1 a 100
    private List<Badge> badges;
    private LocalDateTime ultimaAtualizacao;
    private String foto;
    private String email;

    public PerfilAluno(String alunoId) {
        this.alunoId = alunoId;
        this.nome = "";
        this.pontuacaoGlobal = 0;
        this.nivel = 1; // Nível inicial
        this.badges = new ArrayList<Badge>();// Lista vazia de badges
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

    public int getPontuacaoGlobal() {
        return pontuacaoGlobal;
    }

    public void setPontuacaoGlobal(int pontuacaoGlobal) {
        this.pontuacaoGlobal = pontuacaoGlobal;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "PerfilAluno{" +
                "alunoId='" + alunoId + '\'' +
                ", nome='" + nome + '\'' +
                ", pontuacaoGlobal=" + pontuacaoGlobal +
                ", nivel=" + nivel +
                ", badges=" + badges +
                ", ultimaAtualizacao=" + ultimaAtualizacao +
                ", foto='" + foto + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
