package com.paq.utils.constant;

import java.util.Arrays;

public enum FormatEnum {
    PDF(".pdf"),
    DOCX(".docx"),
    PPTX(".pptx"),
    ZIP(".zip"),
    MP4(".mp4");

    private final String extension;

    FormatEnum(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static FormatEnum fromFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Tên file không hợp lệ");
        }

        String normalizedFilename = filename.toLowerCase();
        return Arrays.stream(values())
                .filter(format -> normalizedFilename.endsWith(format.extension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Định dạng file không được hỗ trợ: " + filename));
    }
}
