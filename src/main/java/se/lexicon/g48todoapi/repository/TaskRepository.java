package se.lexicon.g48todoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.lexicon.g48todoapi.domain.entity.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //todo: select tasks contain title
    List<Task> findByTitleContains(String title);

    //todo: select tasks by person id
    List<Task> findByPerson_Id(Long personId);

    //todo: select tasks by status
    List<Task> findByDone(boolean done);

    //todo: select tasks by date between start and end(two ways)
    @Query("select t from Task t where t.deadline between :from and :to")
    List<Task> selectTasksBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);
    List<Task> findByDeadlineBetween(LocalDate from, LocalDate to);

    //todo: select all unassigned tasks
    List<Task> findByPersonIsNull();

    //todo: select all unfinished tasks
    @Query("select t from Task t where t.done = false")
    List<Task> selectUnFinishedTasks();

    //todo: select all unfinished and overdue tasks
    @Query("select t from Task t where t.done = false and current_date > t.deadline ")
    List<Task> selectUnFinishedAndOverdueTasks();

}
