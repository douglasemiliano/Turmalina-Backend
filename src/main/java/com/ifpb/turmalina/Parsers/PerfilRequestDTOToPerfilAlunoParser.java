package com.ifpb.turmalina.Parsers;

import com.ifpb.turmalina.DTO.PerfilRequestDTO;
import com.ifpb.turmalina.Entity.PerfilAluno;

public class PerfilRequestDTOToPerfilAlunoParser {

    public static PerfilAluno parse(PerfilRequestDTO perfilRequestDTO) {
        PerfilAluno perfil = new PerfilAluno(perfilRequestDTO.getAlunoId());
        perfil.setNome(perfilRequestDTO.getNomeAluno());
        perfil.setFoto(perfilRequestDTO.getFotoPerfil());
        perfil.setEmail(perfilRequestDTO.getEmail());
        return perfil;
    }
}
