package ru.dgi.to;

import ru.dgi.model.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class QuestWithResult extends BaseTo {
    private final Status status;

    private final String name;

    private final boolean isActive;

    public QuestWithResult(Integer id, Status status, String name, boolean isActive) {
        super(id);
        this.status = status;
        this.name = name;
        this.isActive = isActive;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestWithResult that = (QuestWithResult) o;
        return status == that.status &&
                isActive == that.isActive &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, name, isActive);
    }

    @Override
    public String toString() {
        return "QuestWithResult{" +
                "id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
