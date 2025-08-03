package com.ifpb.turmalina.Repository;

import com.ifpb.turmalina.Entity.DesafioAluno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesafioAlunoRepository extends MongoRepository<DesafioAluno, String> {

    DesafioAluno findByAlunoIdAndDesafioId(String alunoId, String desafioId);

    List<DesafioAluno> findByAlunoId(String alunoId);

    List<DesafioAluno> findByAlunoIdAndCursoId(String idAluno, String idCurso);
}