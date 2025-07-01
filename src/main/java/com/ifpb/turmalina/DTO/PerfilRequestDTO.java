package com.ifpb.turmalina.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PerfilRequestDTO {
    private String alunoId;
    private String nome;
    private String foto;
    private String email;

    public PerfilRequestDTO(String alunoId, String nome, String foto, String email) {
        this.alunoId = alunoId;
        this.nome = nome;
        this.foto = foto;
        this.email = email;
    }

    public String getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(String alunoId) {
        this.alunoId = alunoId;
    }

    public String getNomeAluno() {
        return nome;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nome = nomeAluno;
    }

    public String getFotoPerfil() {
        return foto;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.foto = fotoPerfil;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
