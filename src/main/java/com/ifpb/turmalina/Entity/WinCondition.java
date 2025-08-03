package com.ifpb.turmalina.Entity;

import java.util.ArrayList;
import java.util.List;

public class WinCondition {
    private TipoWinCondition tipo;
    private int quantidade;
    private List<String> estado = new ArrayList<String>();
    private String itemId;

    public WinCondition(){}

    public WinCondition(TipoWinCondition tipo, int quantidade, List<String> estado, String itemId) {
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.estado = estado;
        this.itemId = itemId;
    }

    public TipoWinCondition getTipo() {
        return tipo;
    }

    public void setTipo(TipoWinCondition tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<String> getEstado() {
        return estado;
    }

    public void setEstado(List<String> estado) {
        this.estado = estado;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
