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

    public PerfilAluno atualizarPontuacaoGlobal(String alunoId, int pontuacao) {
        Optional<PerfilAluno> perfil = this.repositorio.findById(alunoId);

        System.err.println("Atualizando pontuação global do aluno: " + alunoId + " com pontuação: " + pontuacao);
        if(perfil.isPresent()) {
            if(perfil.get().getAtividadesConcluidas() < pontuacao) {
                perfil.get().setAtividadesConcluidas(perfil.get().getAtividadesConcluidas() + pontuacao);
            } else {
                perfil.get().setAtividadesConcluidas(pontuacao);
            }
            perfil.get().setUltimaAtualizacao(LocalDateTime.now());
            System.err.println("nivel: " + calcularNivel(perfil.get().getAtividadesConcluidas()));
            perfil.get().setNivel(calcularNivel(pontuacao + perfil.get().getPontuacaoGlobal()));
            repositorio.save(perfil.get());
        }

        return perfil.orElseThrow(() -> new RuntimeException("Perfil do aluno não encontrado"));

    }

    private int calcularNivel(int pontuacao) {
        return pontuacao / 10 > 0 ? (pontuacao / 10) : 1;
    }
}
