package ru.dgi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@NoArgsConstructor
@Getter
@Setter
public class Variant extends NamedEntity {
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "variants")
    private List<Question> questions;
}
