package ru.nsu.fit.ykhdr.jdu.printformat;

import org.jetbrains.annotations.NotNull;

public enum FormattingTemplates {
    DIR_TEMPLATE("${indent}/${file} [${size}]"),
    FILE_TEMPLATE("${indent}${file} [${size}]"),
    SYM_TEMPLATE("${indent}${file}@ -> ${target} [${size}]");

    public final String format;

    FormattingTemplates(@NotNull String format) {
        this.format = format;
    }
}
