package com.ifpb.turmalina.Repository;
import com.ifpb.turmalina.Entity.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends MongoRepository<Badge, String> {
    List<Badge> findByCreatedBy(String userId);
}
