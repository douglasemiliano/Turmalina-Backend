package com.ifpb.turmalina.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "CodigosResgate")
public class CodigoResgate {
    @Id
    private String codigo;
    private String badgeId;
    private LocalDateTime dataExpiracao;

    public CodigoResgate(String codigo, String badgeId, LocalDateTime dataExpiracao) {
        this.codigo = codigo;
        this.badgeId = badgeId;
        this.dataExpiracao = dataExpiracao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}
