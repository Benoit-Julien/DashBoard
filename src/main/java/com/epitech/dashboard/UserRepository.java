package com.epitech.dashboard;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByPseudo(final String pseudo);
}
