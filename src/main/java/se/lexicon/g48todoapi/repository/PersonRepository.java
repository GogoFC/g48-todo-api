package se.lexicon.g48todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.g48todoapi.domain.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Todo: Define required methods
}
