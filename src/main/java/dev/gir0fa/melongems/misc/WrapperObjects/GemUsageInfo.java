package dev.gir0fa.melongems.misc.WrapperObjects;

public class GemUsageInfo {

    private final String gemName;
    private final int level;

    public GemUsageInfo(String gemName, int level) {
        this.gemName = gemName;
        this.level = level;
    }

    public String getName() {
        return gemName;
    }

    public int getLevel() {
        return level;
    }
}
