package ru.roscha_akademii.medialib.common;

import java.util.ArrayList;
import java.util.HashSet;

import static ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.INTEGER;

public class CreateTableQueryBuilder {

    private String tableName;
    private String indexColumn = null;
    private SqlType indexColumnType = null;
    private ArrayList<String> columns = new ArrayList<>();
    private HashSet<String> names = new HashSet<>();

    public CreateTableQueryBuilder(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalStateException("illegal table name");
        }
        this.tableName = tableName;
    }

    public CreateTableQueryBuilder index(String columnName, SqlType type) {
        if (columnName == null || columnName.isEmpty()) {
            throw new IllegalStateException("illegal column name");
        }

        checkAndRememberColumnName(columnName);
        indexColumn = columnName;
        indexColumnType = type;

        return this;
    }

    public CreateTableQueryBuilder index(String columnName) {
        return index(columnName, INTEGER);
    }

    public CreateTableQueryBuilder column(String columnName, SqlType type) {
        if (columnName == null || columnName.isEmpty()) {
            throw new IllegalStateException("illegal column name");
        }
        if (type == null) {
            throw new IllegalStateException("illegal column type");
        }

        checkAndRememberColumnName(columnName);
        columns.add(columnName + " " + type.name());

        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        if (columns.size() == 0) {
            throw new IllegalStateException("empty column");
        }

        sb.append("create table ").append(tableName)
                .append(" (");

        if (indexColumn != null) {
//                sb.append(indexColumn).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb.append(indexColumn)
                    .append(" ")
                    .append(indexColumnType.toString())
                    .append(" PRIMARY KEY, ");
        }

        sb.append(columns.get(0));
        columns.remove(0);

        for (String c : columns) {
            sb.append(", ").append(c);
        }

        sb.append(")");

        return sb.toString();
    }

    private void checkAndRememberColumnName(String name) {
        if (names.contains(name)) {
            throw new IllegalStateException("duplicate column " + name);
        }
        names.add(name);
    }

    public enum SqlType {
        STRING, INTEGER, REAL
    }
}



