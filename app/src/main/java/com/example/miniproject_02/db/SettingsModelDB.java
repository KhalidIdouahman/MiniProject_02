package com.example.miniproject_02.db;

public class SettingsModelDB {

    public static class Color {
        public static final String TABLE_NAME = "color";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CODE = "code";
    }

    public static class Settings {
        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_NAME = "setting_name";
        public static final String COLUMN_VALUE = "value";
    }

}
