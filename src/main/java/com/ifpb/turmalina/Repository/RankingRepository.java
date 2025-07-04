package com.ifpb.turmalina.Repository;

import com.ifpb.turmalina.Entity.Ranking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends MongoRepository<Ranking, String> {
    Ranking findByCursoId(String cursoId);
}