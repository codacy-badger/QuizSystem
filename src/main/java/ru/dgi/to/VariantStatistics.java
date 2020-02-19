package ru.dgi.to;

public class VariantStatistics {

    private final String label;

    private final long value;

    public VariantStatistics(String label, long value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantStatistics that = (VariantStatistics) o;

        if (value != that.value) return false;
        return label != null ? label.equals(that.label) : that.label == null;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "VariantStatistics{" +
                "label='" + label + '\'' +
                ", value=" + value +
                '}';
    }
}
