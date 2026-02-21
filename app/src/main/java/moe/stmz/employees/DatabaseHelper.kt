package moe.stmz.employees

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "employees.db", null, 1) {

    // Создание таблицы
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE employees ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "position TEXT, " +
                "department TEXT, " +
                "salary REAL )"

        db.execSQL(createTable)
    }

    // Пересоздание при обновлении таблицы
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS employees")
        onCreate(db)
    }
}