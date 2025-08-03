package com.ifpb.turmalina.Entity;

public class Premio {
    private Integer pontuacao; // opcional, pode ser nulo
    private String badgeId; // opcional, pode ser nulo

    public Premio() {}

    public Premio(Integer pontuacao) {
        this.pontuacao = pontuacao;
        this.badgeId = null;
    }

    public Premio(String badgeId) {
        this.badgeId = badgeId;
        this.pontuacao = null;
    }

    public Premio(Integer pontuacao, String badgeId) {
        this.pontuacao = pontuacao;
        this.badgeId = badgeId;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }
}

