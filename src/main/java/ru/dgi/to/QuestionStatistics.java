package ru.dgi.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class QuestionStatistics extends BaseTo {

    private final String name;

    private final int number;

    private final int answerTypeId;

    @JsonProperty("content")
    private final List<VariantStatistics> variantStatistics;

    public QuestionStatistics(Integer id, String name, int number, int answerTypeId, List<VariantStatistics> variantStatistics) {
        super(id);
        this.name = name;
        this.number = number;
        this.answerTypeId = answerTypeId;
        this.variantStatistics = variantStatistics;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getAnswerTypeId() {
        return answerTypeId;
    }

    public List<VariantStatistics> getVariantStatistics() {
        return variantStatistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionStatistics that = (QuestionStatistics) o;

        if (number != that.number) return false;
        if (answerTypeId != that.answerTypeId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return variantStatistics != null ? variantStatistics.equals(that.variantStatistics) : that.variantStatistics == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + number;
        result = 31 * result + answerTypeId;
        result = 31 * result + (variantStatistics != null ? variantStatistics.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuestionStatistics{" +
                "name='" + name + '\'' +
                ", number='" + number +
                ", answerTypeId='" + answerTypeId +
                ", variantStatistics=" + variantStatistics +
                '}';
    }
}
