package com.paq.pojo.response;

public class ResEnumDTO {

    private String name;
    private String label;

    public ResEnumDTO() {
    }

    public ResEnumDTO(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
