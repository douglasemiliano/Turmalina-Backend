package com.ifpb.turmalina.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AlunoRankingDto {
    private String alunoId;
    private String nomeAluno;
    private int pontuacaoTotal;
    private int nivel;
    private String foto;

    public AlunoRankingDto(String alunoId, String nomeAluno, int pontuacaoTotal, int nivel, String foto) {
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.pontuacaoTotal = pontuacaoTotal;
        this.nivel = nivel;
        this.foto = foto;
    }

    public String getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(String alunoId) {
        this.alunoId = alunoId;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(int pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
