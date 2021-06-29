package com.jibs.neo4j.practice.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.jibs.neo4j.practice.domain.Gateway;

@Repository
public interface GatewayRepository extends Neo4jRepository<Gateway, Long> {

}
