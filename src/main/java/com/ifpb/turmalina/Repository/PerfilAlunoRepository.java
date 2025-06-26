package com.ifpb.turmalina.Repository;


import com.ifpb.turmalina.Entity.PerfilAluno;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilAlunoRepository extends MongoRepository<PerfilAluno, String> {

    Optional<PerfilAluno> findByAlunoId(String alunoId);
}
