package ru.dragomirov.taskschedule.core.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dragomirov.taskschedule.auth.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Tasks",
        indexes = {
                @Index(name = "idx_task_author_id", columnList = "author_id"),
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

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "expirationData")
    private LocalDateTime expirationData;

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
