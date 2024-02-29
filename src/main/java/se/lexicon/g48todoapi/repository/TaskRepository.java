package se.lexicon.g48todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.g48todoapi.domain.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //todo: select tasks contain title
    //todo: select tasks by person id
    //todo: select tasks by status
    //todo: select tasks by date between start and end
    //todo: select all unassigned tasks
    //todo: select all unfinished tasks
    //todo: select all unfinished and overdue tasks

}
