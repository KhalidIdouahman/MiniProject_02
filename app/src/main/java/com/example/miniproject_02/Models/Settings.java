package com.example.miniproject_02.Models;

public class Settings {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Settings(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
