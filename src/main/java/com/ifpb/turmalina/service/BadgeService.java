package com.ifpb.turmalina.service;

import com.ifpb.turmalina.Entity.Badge;
import com.ifpb.turmalina.Entity.PerfilAluno;
import com.ifpb.turmalina.Repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {


    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private PerfilAlunoService perfilAlunoService;

    public Badge criarBadge(Badge badge){
        return badgeRepository.save(badge);
    }

    public Badge buscarBadgePorId(String id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge não encontrada"));
    }

    public Badge atualizarBadge(String id, Badge badgeAtualizada) {
        Badge badgeExistente = buscarBadgePorId(id);
        badgeExistente.setNome(badgeAtualizada.getNome());
        badgeExistente.setDescricao(badgeAtualizada.getDescricao());
        return badgeRepository.save(badgeExistente);
    }

    public List<Badge> getBadgesByUserId(String idUser) {
        List<Badge> badges = badgeRepository.findByCreatedBy(idUser);
        if (badges.isEmpty()) {
            throw new RuntimeException("Nenhuma badge encontrada para o usuário com ID: " + idUser);
        }
        return badges;
    }

    public String addBadgeToUser(String idBadge, String idUser) {
        PerfilAluno aluno = this.perfilAlunoService.getPerfilAluno(idUser);
        Badge badge = this.buscarBadgePorId(idBadge);
        if (aluno.getBadges().stream().anyMatch(b -> b.getId().equals(idBadge))) {
            return "Badge já atribuída ao usuário.";
        }
        aluno.getBadges().add(badge);
        this.perfilAlunoService.atualizarPerfilAluno(aluno);
        return "Badge atribuída com sucesso ao usuário.";
    }
}
