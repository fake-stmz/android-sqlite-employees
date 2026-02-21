package moe.stmz.employees

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import moe.stmz.employees.databinding.ItemEmployeeBinding

class EmployeeAdapter(
    private var list: MutableList<Employee>,
    private val onClick: (Employee) -> Unit,
    private val onLongClick: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemEmployeeBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmployeeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Количество штук разных зачем-то
    override fun getItemCount() = list.size

    // Жоская привязка данных к карточке
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = list[position]

        holder.binding.tvName.text = employee.name
        holder.binding.tvPosition.text = employee.position
        holder.binding.tvSalary.text = "ЗП: ${employee.salary}"

        // Обработка нажатия (probably для редактирования)
        holder.itemView.setOnClickListener {
            onClick(employee)
        }

        // Обработка зажатия (probably для удаления)
        holder.itemView.setOnLongClickListener {
            onLongClick(employee)
            true
        }
    }

    // Обновление данных
    fun updateList(newList: List<Employee>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}