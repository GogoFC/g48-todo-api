package se.lexicon.g48todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.g48todoapi.domain.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Todo: Define required methods

}
