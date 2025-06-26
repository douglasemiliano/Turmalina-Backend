package com.ifpb.turmalina.service;

import com.ifpb.turmalina.Entity.PerfilAluno;
import com.ifpb.turmalina.Repository.PerfilAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PerfilAlunoService {

    @Autowired
    private PerfilAlunoRepository repositorio;

    List<PerfilAluno> repositoriio = new ArrayList<>();

    public PerfilAluno atualizarPerfilAluno(String alunoId, String nomeAluno, double novaPontuacao, List<String> novasBadges){
        PerfilAluno perfil = this.repositorio.findById(alunoId).orElse(new PerfilAluno(alunoId));


        perfil.setNome(nomeAluno);

        perfil.setPontuacaoGlobal(perfil.getPontuacaoGlobal() + novaPontuacao);
        perfil.setNivel(calcularNivel(perfil.getPontuacaoGlobal()));

        Set<String> badges = new HashSet<>(perfil.getBadges());
        badges.addAll(novasBadges);
        perfil.setBadges(new ArrayList<>(badges));

        perfil.setUltimaAtualizacao(LocalDateTime.now());
        repositorio.save(perfil);

        return perfil;
    }

    public PerfilAluno criarPerfilAluno(PerfilAluno perfilAluno) {
        return repositorio.save(perfilAluno);
    }

    public PerfilAluno buscarPerfilAlunoPorId(String alunoId) {
        return repositoriio.stream()
                .filter(perfil -> perfil.getAlunoId().equals(alunoId))
                .findFirst()
                .orElse(new PerfilAluno(alunoId));
    }

    public PerfilAluno getPerfilAluno(String alunoId) {
        return repositorio.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Perfil do aluno não encontrado"));
    }

    private int calcularNivel(double pontuacao) {
        return (int) Math.min(100, Math.floor(pontuacao / 10)); // Exemplo: 1000 pts = nível 100
    }
}
