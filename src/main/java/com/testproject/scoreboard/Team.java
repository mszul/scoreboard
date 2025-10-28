package com.testproject.scoreboard;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public record Team(@NotNull Locale locale) {

    @Override
    @NotNull
    public String toString() {
        return locale.getDisplayCountry();
    }
}
