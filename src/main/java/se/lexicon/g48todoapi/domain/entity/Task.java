package se.lexicon.g48todoapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "person")
@ToString(exclude = "person")
@Builder

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Task(String title, String description, LocalDate deadline, Person person) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.person = person;
    }

    public Task(String title, String description, LocalDate deadline, boolean done, Person person) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
        this.person = person;
    }

    public Task(String title, String description, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }
}
