package moe.stmz.employees

import android.content.ContentValues
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

    // CRUD операции

    // C - Добавлени сотрудника
    fun insertEmployee(employee: Employee) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", employee.name)
            put("position", employee.position)
            put("department", employee.department)
            put("salary", employee.salary)
        }
        db.insert("employees", null, values)
        db.close()
    }

    // R1 - Получение всех сотрудников
    fun getAllEmployees() : List<Employee> {
        val list = mutableListOf<Employee>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM employees", null)

        if (cursor.moveToFirst()) {
            do {
                val employee = Employee(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    position = cursor.getString(2),
                    department = cursor.getString(3),
                    salary = cursor.getDouble(4)
                )
                list.add(employee)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }

    // R2 - Получение конкретного сотрудника
    fun getEmployee(id: Int) : Employee? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM employees WHERE id=?",
            arrayOf(id.toString())
        )

        var employee: Employee? = null

        if (cursor.moveToFirst()) {
            employee = Employee(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                position = cursor.getString(2),
                department = cursor.getString(3),
                salary = cursor.getDouble(4)
            )
        }

        cursor.close()
        db.close()

        return employee
    }

    // U - Обновление информации о сотруднике
    fun updateEmployee(employee: Employee) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", employee.name)
            put("position", employee.position)
            put("department", employee.department)
            put("salary", employee.salary)
        }

        db.update("employees", values, "id = ?", arrayOf(employee.id.toString()))
        db.close()
    }

    // D - Удаление (СМЕЕЕЕЕЕЕРТЬ) сотрудника
    fun deleteEmployee(id: Int) {
        val db = writableDatabase
        db.delete("employees", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}