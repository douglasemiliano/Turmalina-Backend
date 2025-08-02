package com.ifpb.turmalina.Repository;

import com.ifpb.turmalina.Entity.Desafio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesafioRepository extends MongoRepository<Desafio, String> {

}