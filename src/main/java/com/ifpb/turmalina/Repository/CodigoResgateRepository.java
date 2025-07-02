package com.ifpb.turmalina.Repository;

import com.ifpb.turmalina.Entity.CodigoResgate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodigoResgateRepository extends MongoRepository<CodigoResgate, String> {
}