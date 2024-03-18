package se.lexicon.g48todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.lexicon.g48todoapi.domain.entity.Person;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     *     Find people who have no tasks
     */
    @Query("select p from Person p where size(p.tasks) = 0 ")
    List<Person> findIdlePeople();
}
