package ru.dragomirov.taskschedule.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Tasks",
        indexes = {
                @Index(name = "idx_task_author", columnList = "author"),
                @Index(name = "idx_task_status", columnList = "status")
        }
)
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "expirationData")
    private LocalDateTime expirationData;

}
