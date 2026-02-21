package moe.stmz.employees

data class Employee(
    val id: Int = 0,            // Идентификатор
    val name: String,           // Имя
    val position: String,       // Должность
    val department: String,     // Отдел
    val salary: Double          // Зарплата
)
