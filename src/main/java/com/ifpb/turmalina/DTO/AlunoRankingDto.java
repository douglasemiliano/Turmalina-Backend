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
    private double pontuacaoTotal;
    private double percentual;

    public AlunoRankingDto(String alunoId, String nomeAluno, double pontuacaoTotal, double percentual) {
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.pontuacaoTotal = pontuacaoTotal;
        this.percentual = percentual;
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

    public double getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(double pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public double getPercentual() {
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }
}
