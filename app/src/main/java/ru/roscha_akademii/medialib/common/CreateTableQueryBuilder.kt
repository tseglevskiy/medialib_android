package ru.roscha_akademii.medialib.common

import java.util.ArrayList
import java.util.HashSet

import ru.roscha_akademii.medialib.common.CreateTableQueryBuilder.SqlType.INTEGER

class CreateTableQueryBuilder(private val tableName: String?) {
    private var indexColumn: String? = null
    private var indexColumnType: SqlType? = null
    private val columns = ArrayList<String>()
    private val names = HashSet<String>()

    init {
        if (tableName == null || tableName.isEmpty()) {
            throw IllegalStateException("illegal table name")
        }
    }

    @JvmOverloads fun index(columnName: String?, type: SqlType = INTEGER): CreateTableQueryBuilder {
        if (columnName == null || columnName.isEmpty()) {
            throw IllegalStateException("illegal column name")
        }

        checkAndRememberColumnName(columnName)
        indexColumn = columnName
        indexColumnType = type

        return this
    }

    fun column(columnName: String?, type: SqlType?): CreateTableQueryBuilder {
        if (columnName == null || columnName.isEmpty()) {
            throw IllegalStateException("illegal column name")
        }
        if (type == null) {
            throw IllegalStateException("illegal column type")
        }

        checkAndRememberColumnName(columnName)
        columns.add(columnName + " " + type.name)

        return this
    }

    fun build(): String {
        val sb = StringBuilder()
        if (columns.size == 0) {
            throw IllegalStateException("empty column")
        }

        sb.append("create table ").append(tableName).append(" (")

        if (indexColumn != null) {
            //                sb.append(indexColumn).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb
                    .append(indexColumn)
                    .append(" ")
                    .append(indexColumnType!!.toString())
                    .append(" PRIMARY KEY, ")
        }

        sb.append(columns[0])
        columns.removeAt(0)

        for (c in columns) {
            sb.append(", ").append(c)
        }

        sb.append(")")

        return sb.toString()
    }

    private fun checkAndRememberColumnName(name: String) {
        if (names.contains(name)) {
            throw IllegalStateException("duplicate column " + name)
        }
        names.add(name)
    }

    enum class SqlType {
        STRING, INTEGER, REAL
    }
}



