package de.dafuqs.chalk.common.pattern;

import net.minecraft.util.StringIdentifiable;

public enum ChalkPatterns implements StringIdentifiable {
    NORMAL("normal"),
    SKULL("skull");
    private final String name;
    ChalkPatterns(String name) {
        this.name = name;
    }
    public String toString() {
        return this.name;
    }
    public String asString() {
        return this.name;
    }
}