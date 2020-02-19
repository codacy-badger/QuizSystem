package ru.dgi.to;

public class AnswerTo extends BaseTo {

    private final String answerText;

    public AnswerTo(Integer id, String answerText) {
        super(id);
        this.answerText = answerText;
    }

    public String getAnswerText() {
        return answerText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerTo answerTo = (AnswerTo) o;

        return answerText != null ? answerText.equals(answerTo.answerText) : answerTo.answerText == null;
    }

    @Override
    public int hashCode() {
        return answerText != null ? answerText.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AnswerTo{" +
                "id=" + id +
                ", answerText='" + answerText + '\'' +
                '}';
    }
}
