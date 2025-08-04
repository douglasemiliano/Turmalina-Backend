package com.ifpb.turmalina.service;

import com.ifpb.turmalina.DTO.Response.Response;
import com.ifpb.turmalina.Entity.*;
import com.ifpb.turmalina.Repository.DesafioAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DesafioAlunoService {


    @Autowired
    private DesafioAlunoRepository desafioAlunoRepository;

    @Autowired
    private PerfilAlunoService perfilAlunoService;

    @Autowired
    private BadgeService badgeService;


    public DesafioAluno buscarPorAlunoEdesafio(String alunoId, String desafioId) {
        return desafioAlunoRepository.findByAlunoIdAndDesafioId(alunoId, desafioId);
    }

    public List<DesafioAluno> buscarPorAlunoId(String alunoId) {
        return desafioAlunoRepository.findByAlunoId(alunoId);
    }

    public DesafioAluno salvar(DesafioAluno desafioAluno) {
        return this.desafioAlunoRepository.save(desafioAluno);
    }

    public ResponseEntity<Response<List<Premio>>> resgatarPremio(String alunoId, Desafio desafio) throws RuntimeException {
        DesafioAluno desafioAluno = buscarPorAlunoEdesafio(alunoId, desafio.getId());

        if (desafioAluno.getStatus().equals(DesafioStatus.REIVINDICADO)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(HttpStatus.NOT_FOUND.toString(), "Desafio não encontrado ou já resgatado.", null));
        }

        // Atualiza status para "RESGATADO"
        desafioAluno.setStatus(DesafioStatus.REIVINDICADO);
        salvar(desafioAluno);

        // Aplica o prêmio no perfil do aluno (xp, badges)

        PerfilAluno perfil = perfilAlunoService.getPerfilAluno(alunoId);

        for (Premio premio : desafio.getPremio()) {
            if (premio.getPontuacao() != null) {
                perfil.setPontuacaoGlobal(perfil.getPontuacaoGlobal() + premio.getPontuacao());
                perfilAlunoService.atualizarPerfilAluno(perfil);
            }
            if (premio.getBadgeId() != null) {
                badgeService.addBadgeToUser(premio.getBadgeId(), alunoId);
            }
        }

        return ResponseEntity.ok(new Response<>(HttpStatus.OK.toString(), "Prêmio resgatado com sucesso.", desafio.getPremio()));
    }

    public List<DesafioAluno> getDesafioalunoByCursoIdAndIdAluno(String idCurso, String idAluno) {
        List<DesafioAluno> desafios = desafioAlunoRepository.findByAlunoIdAndCursoId(idAluno, idCurso);
        return desafios;
    }
}
