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

    public AlunoRankingDto(String alunoId, String nomeAluno, int pontuacaoTotal, int nivel) {
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.pontuacaoTotal = pontuacaoTotal;
        this.nivel = nivel;
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
}
