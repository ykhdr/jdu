package ru.nsu.fit.ykhdr.jdu.printformat;

import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.exception.DuIOException;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.utils.SizeConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileRepresentation {
    private static final Map<String, String> templateValuesMap = new HashMap<>() {{
        put("indent", "");
        put("file", "");
        put("size", "");
        put("target", "");
    }};

    private static final String INDENT = "  ";
    private static final StringSubstitutor SUBSTITUTOR = new StringSubstitutor(templateValuesMap);

    public static @NotNull String format(@NotNull FormattingTemplates template, @NotNull DuFile file, int depth) {
        templateValuesMap.replace("indent", INDENT.repeat(depth));
        templateValuesMap.replace("file", file.name());
        templateValuesMap.replace("size", size(file));

        if (template == FormattingTemplates.SYM_TEMPLATE) {
            templateValuesMap.replace("target", symlinkTarget(file));
        }

        return SUBSTITUTOR.replace(template.format);
    }

    private static @NotNull String size(@NotNull DuFile file) {
        return SizeConverter.convertToString(file.size());
    }

    private static @NotNull String symlinkTarget(@NotNull DuFile link) {
        try {
            return Files.readSymbolicLink(link.path()).toString();
        } catch (IOException e) {
            throw new DuIOException(e);
        }
    }
}
