package moe.stmz.employees

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "employees.db", null, 1) {

    // Создание таблицы
    override fun onCreate(db: SQLiteDatabase) {
        // SQL запрос на создание таблицы сотрудников
        val createTable = "CREATE TABLE employees ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "position TEXT, " +
                "department TEXT, " +
                "salary REAL )"

        // Выполнение SQL скрипта
        db.execSQL(createTable)
    }

    // Пересоздание при обновлении таблицы
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Удаление таблицы, если она есть
        db.execSQL("DROP TABLE IF EXISTS employees")
        // Создание таблицы заново
        onCreate(db)
    }

    // CRUD операции

    // C - Добавление сотрудника
    fun insertEmployee(employee: Employee) {
        val db = writableDatabase
        // Значения для вставки
        val values = ContentValues().apply {
            put("name", employee.name)
            put("position", employee.position)
            put("department", employee.department)
            put("salary", employee.salary)
        }

        // Запрос на вставку (INSERT) в таблицу сотрудников
        db.insert("employees", null, values)
        db.close()
    }

    // R1 - Получение всех сотрудников
    fun getAllEmployees() : List<Employee> {
        val list = mutableListOf<Employee>()
        val db = readableDatabase
        // Запрос всех записей из таблицы сотрудников
        val cursor = db.rawQuery("SELECT * FROM employees", null)

        if (cursor.moveToFirst()) { // Перемещение курсора на первую запись
            do {
                // Создание объекта сотрудника из записи в БД
                val employee = Employee(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    position = cursor.getString(2),
                    department = cursor.getString(3),
                    salary = cursor.getDouble(4)
                )
                // Добавление сотрудника в список
                list.add(employee)
            } while (cursor.moveToNext()) // Перемещение к следующей записи
        }

        cursor.close()
        db.close()

        return list
    }

    // R2 - Получение конкретного сотрудника
    fun getEmployee(id: Int) : Employee? {
        val db = readableDatabase
        // Получение только одной записи из БД по id
        val cursor = db.rawQuery(
            "SELECT * FROM employees WHERE id=?",
            arrayOf(id.toString())
        )

        var employee: Employee? = null

        if (cursor.moveToFirst()) {
            // Заполнение объекта сотрудника
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
        // Обновленные данные
        val values = ContentValues().apply {
            put("name", employee.name)
            put("position", employee.position)
            put("department", employee.department)
            put("salary", employee.salary)
        }

        // Запрос на обновление (UPDATE) данных в БД
        db.update("employees", values, "id = ?", arrayOf(employee.id.toString()))
        db.close()
    }

    // D - Удаление (СМЕЕЕЕЕЕЕРТЬ) сотрудника
    fun deleteEmployee(id: Int) {
        val db = writableDatabase
        // Запрос на удаление (DELETE) данных из БД
        db.delete("employees", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}