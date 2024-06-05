package com.example.interest1.ui.screens.settings.interests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.interest1.R
import com.example.interest1.models.InterestModel
import kotlinx.android.synthetic.main.choose_view.view.*

class InterestsAdapter(private val listItems: List<InterestModel>) : RecyclerView.Adapter<InterestsAdapter.ChooseViewHolder>() {

    class ChooseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkboxItem: CheckBox = view.checkboxItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_view, parent, false)

        val holder = ChooseViewHolder(view)
        holder.checkboxItem.setOnClickListener {
            val current = listItems[holder.adapterPosition].isChosen
            listItems[holder.adapterPosition].isChosen = !current
        }
        return holder
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.checkboxItem.text = listItems[position].name
        holder.checkboxItem.isChecked = listItems[position].isChosen
    }

    override fun getItemCount(): Int = listItems.size

    fun getData(): Map<String, String> {
        val userInterestsMap = hashMapOf<String, String>()
        for (i in listItems.indices) {
            val current = listItems[i]
            if (current.isChosen) {
                userInterestsMap[current.id] = current.name
            }
        }
        return userInterestsMap
    }

}