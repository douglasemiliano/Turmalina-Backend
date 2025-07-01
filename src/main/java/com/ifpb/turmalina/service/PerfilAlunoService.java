package com.ifpb.turmalina.service;

import com.ifpb.turmalina.Entity.PerfilAluno;
import com.ifpb.turmalina.Repository.PerfilAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PerfilAlunoService {

    @Autowired
    private PerfilAlunoRepository repositorio;

    List<PerfilAluno> repositoriio = new ArrayList<>();

    public void atualizarPerfilAluno(PerfilAluno perfilAluno) {
        perfilAluno.setUltimaAtualizacao(LocalDateTime.now());
        boolean existe = this.repositorio.existsById(perfilAluno.getAlunoId());

        if (existe) {
            repositorio.save(perfilAluno);
        } else {
            throw new RuntimeException("Perfil do aluno não encontrado");
        }

    }

    public PerfilAluno criarPerfilAluno(PerfilAluno perfilAluno) {
        Optional<PerfilAluno> perfilExistente = repositorio.findById(perfilAluno.getAlunoId());
        if (perfilExistente.isEmpty()) {
            return repositorio.save(perfilAluno);
        }
        return perfilExistente.get();
    }

    public PerfilAluno getPerfilAluno(String alunoId) {
        return repositorio.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Perfil do aluno não encontrado"));
    }

    public void atualizarPontuacaoGlobal(String alunoId, double pontuacao) {
        Optional<PerfilAluno> perfil = this.repositorio.findById(alunoId);

        if(perfil.isPresent()) {
            perfil.get().setPontuacaoGlobal(perfil.get().getPontuacaoGlobal() + pontuacao);
            perfil.get().setNivel(calcularNivel(perfil.get().getPontuacaoGlobal()));
            perfil.get().setUltimaAtualizacao(LocalDateTime.now());
            repositorio.save(perfil.get());
        }

    }

    private int calcularNivel(double pontuacao) {
        return (int) Math.min(100, Math.floor(pontuacao / 10)); // Exemplo: 1000 pts = nível 100
    }
}
