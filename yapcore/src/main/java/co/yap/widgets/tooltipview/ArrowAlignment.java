package co.yap.widgets.tooltipview;

public enum ArrowAlignment {
    START(0), CENTER(1), END(2), ANCHORED_VIEW(3), CUSTOM(4);

    private final int value;

    ArrowAlignment(int value) {
        this.value = value;
    }

    public static ArrowAlignment getAlignment(int value) {
        for (ArrowAlignment alignment : values()) {
            if (value == alignment.getValue()) {
                return alignment;
            }
        }
        throw new IllegalArgumentException("No matching ArrowAlignment with value: " +value);
    }

    public int getValue() {
        return value;
    }
}
