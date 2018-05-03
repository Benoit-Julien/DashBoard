package com.epitech.dashboard;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WidgetRepository extends MongoRepository<Widget, String> {
    List<Widget>    findWidgetsByOwner(User owner);
}
