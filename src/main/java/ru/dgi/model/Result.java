package ru.dgi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.SafeHtml;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"quest_id", "username"})})
@NoArgsConstructor
@Getter
@Setter
public class Result extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Quest quest;

    @SafeHtml
    private String username;

    @SafeHtml
    private String fullname;

    @NotNull
    @Column(name = "answer_start", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime answerStart = LocalDateTime.now();

    @Column(name = "answer_modified")
    private LocalDateTime answerModified;

    @NotNull
    private Status status;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "result")
    private List<Answer> answers;

    public Result (Quest quest, String username, String fullname, LocalDateTime answerStart, LocalDateTime answerModified, Status status) {
        this(null, quest, username, fullname, answerStart, answerModified, status);
    }

    public Result(Integer id, Quest quest, String username, String fullname, LocalDateTime answerStart, LocalDateTime answerModified, Status status) {
        super(id);
        this.quest = quest;
        this.username = username;
        this.fullname = fullname;
        this.answerStart = answerStart;
        this.answerModified = answerModified;
        this.status = status;
    }
}
