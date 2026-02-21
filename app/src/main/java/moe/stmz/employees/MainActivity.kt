package moe.stmz.employees

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import moe.stmz.employees.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подключаем ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        // Настройка RecyclerView
        adapter = EmployeeAdapter(mutableListOf(),
            onClick = { employee ->
                // Переход к редактированию
                val intent = Intent(this, AddEditActivity::class.java)
                intent.putExtra("employee_id", employee.id)
                startActivity(intent)
            },
            onLongClick = { employee ->
                // Подтверждение удаления
                AlertDialog.Builder(this)
                    .setTitle("Удаление")
                    .setMessage("Удалить сотрудника?")
                    .setPositiveButton("Да") { _, _ ->
                        db.deleteEmployee(employee.id)
                        loadData()
                    }
                    .setNegativeButton("Нет", null)
                    .show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Кнопка добавления
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val employees = db.getAllEmployees()
        adapter.updateList(employees)
    }
}