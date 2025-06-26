package com.ifpb.turmalina.service;

import com.ifpb.turmalina.Entity.Ranking;
import com.ifpb.turmalina.Repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    public Ranking salvarRanking(Ranking ranking) {
        return rankingRepository.save(ranking);
    }

    public Ranking buscarRankingPorCursoId(String cursoId) {
        Ranking ranking = rankingRepository.findByCursoId(cursoId);
        if (ranking == null) {
            ranking = new Ranking();
            ranking.setCursoId(cursoId);
            ranking.setAlunos(new ArrayList<>());
            ranking.setUltimaAtualizacao(LocalDateTime.now());
            return salvarRanking(ranking);
        }
        return ranking;
    }

}
