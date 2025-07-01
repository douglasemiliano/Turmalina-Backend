package com.ifpb.turmalina.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PerfilRequestDTO {
    private String alunoId;
    private String nomeAluno;
    private String fotoPerfil;
    private String email;

    public PerfilRequestDTO(String alunoId, String nomeAluno, String fotoPerfil, String email) {
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.fotoPerfil = fotoPerfil;
        this.email = email;
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

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
