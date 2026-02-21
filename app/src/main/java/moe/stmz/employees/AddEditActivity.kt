package moe.stmz.employees

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import moe.stmz.employees.databinding.ActivityAddEditBinding

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var db: DatabaseHelper

    private var employeeId: Int = -1
    private var employee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // Получаем id (если он есть)
        employeeId = intent.getIntExtra("employee_id", -1)

        // Если id != -1 → это редактирование
        if (employeeId != -1) {

            employee = db.getEmployee(employeeId)

            employee?.let {
                binding.etName.setText(it.name)
                binding.etPosition.setText(it.position)
                binding.etDepartment.setText(it.department)
                binding.etSalary.setText(it.salary.toString())
            }
        }

        // Кнопка сохранить
        binding.btnSave.setOnClickListener {

            val name = binding.etName.text.toString()
            val position = binding.etPosition.text.toString()
            val department = binding.etDepartment.text.toString()
            val salaryText = binding.etSalary.text.toString()

            if (name.isEmpty() ||
                position.isEmpty() ||
                department.isEmpty() ||
                salaryText.isEmpty()) {

                Toast.makeText(this,
                    "Заполните все поля",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val salary = salaryText.toDouble()

            if (employeeId == -1) {
                // Добавление
                db.insertEmployee(
                    Employee(
                        name = name,
                        position = position,
                        department = department,
                        salary = salary
                    )
                )
            } else {
                // Обновление
                db.updateEmployee(
                    Employee(
                        id = employeeId,
                        name = name,
                        position = position,
                        department = department,
                        salary = salary
                    )
                )
            }

            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}