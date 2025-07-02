package com.ifpb.turmalina.service;

import com.ifpb.turmalina.Entity.Badge;
import com.ifpb.turmalina.Entity.CodigoResgate;
import com.ifpb.turmalina.Entity.PerfilAluno;
import com.ifpb.turmalina.Repository.BadgeRepository;
import com.ifpb.turmalina.Repository.CodigoResgateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BadgeService {


    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private CodigoResgateRepository codigoResgateRepository;

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

    public String gerarCodigoResgate(String badgeId) {
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        LocalDateTime expiraEm = LocalDateTime.now().plusHours(24);
        CodigoResgate redeemCode = new CodigoResgate(code, badgeId, expiraEm);
        codigoResgateRepository.save(redeemCode);
        return code;
    }

    public Badge resgatarBadgeComCodigo(String code, String userId) {
        CodigoResgate redeemCode = codigoResgateRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Código inválido."));
        if (redeemCode.getDataExpiracao().isBefore(LocalDateTime.now())) {
            codigoResgateRepository.deleteById(code);
            throw new RuntimeException("Código expirado.");
        }
        addBadgeToUser(redeemCode.getBadgeId(), userId);

        //codigoResgateRepository.deleteById(code); // Código só pode ser usado uma vez
        return buscarBadgePorId(redeemCode.getBadgeId());
    }
}
