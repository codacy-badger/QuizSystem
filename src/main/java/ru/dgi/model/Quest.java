package ru.dgi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Quest extends NamedEntity {

    @NotNull
    @Column(name = "create_date", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "is_active")
    private boolean isActive = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quest", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quest")
    private List<Result> results;
}
