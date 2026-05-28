package com.paq.utils.constant;

public enum FormatEnum {
    PDF(".pdf"),
    DOCX(".docx"),
    PPTX(".pptx"),
    ZIP(".zip");

    private final String extension;

    FormatEnum(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
