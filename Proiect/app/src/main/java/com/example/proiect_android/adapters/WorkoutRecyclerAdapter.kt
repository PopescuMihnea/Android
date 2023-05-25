package com.example.proiect_android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proiect_android.R
import com.example.proiect_android.activities.ModifyWorkoutActivity
import com.example.proiect_android.activities.WorkoutActivity
import com.example.proiect_android.interfaces.Communicator
import com.example.proiect_android.interfaces.onItemClickListener
import com.example.proiect_android.model.Workout
import com.google.android.material.button.MaterialButton

class WorkoutRecyclerAdapter(private val communicator : Communicator): RecyclerView.Adapter<WorkoutRecyclerAdapter.WorkoutRecyclerViewHolder>() {

    private lateinit var clickListener: onItemClickListener
    private lateinit var deleteClickListener: onItemClickListener
    private lateinit var modifyClickListener: onItemClickListener
    private var workoutList = emptyList<Workout>()
    private lateinit var context: Context
    private val workoutKey = "workout"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.workout_list, parent, false)
        context = parent.context

        clickListener = object : onItemClickListener{
            override fun onItemClick(
                position: Int,
            ) {

                val intent = Intent(context, WorkoutActivity::class.java)
                intent.putExtra(workoutKey, workoutList[position])
                context.startActivity(intent)
            }

        }

        deleteClickListener = object : onItemClickListener{
            override fun onItemClick(position: Int) {
                communicator.passWorkout(workoutList[position])
            }

        }

        modifyClickListener = object : onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(context, ModifyWorkoutActivity::class.java)
                intent.putExtra(workoutKey, workoutList[position])
                context.startActivity(intent)
            }

        }

        return WorkoutRecyclerViewHolder(itemView, clickListener,
            deleteClickListener, modifyClickListener)
    }

    override fun onBindViewHolder(
        holder: WorkoutRecyclerViewHolder,
        position: Int
    ) {
        val currentItem = workoutList[position]
        holder.workoutName.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class WorkoutRecyclerViewHolder
        (
         itemView: View,
         listener: onItemClickListener,
         deleteListener: onItemClickListener,
         modifyListener: onItemClickListener
    ): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

            val deleteButton : MaterialButton = itemView.findViewById(R.id.delete_workout_button)
            val editButton : MaterialButton = itemView.findViewById(R.id.edit_workout_button)

            deleteButton.setOnClickListener {
                deleteListener.onItemClick(adapterPosition)
            }

            editButton.setOnClickListener {
                modifyListener.onItemClick(adapterPosition)
            }
        }

        val workoutName : TextView = itemView.findViewById(R.id.workout_name)
    }

    fun setData(workoutList : List<Workout>)
    {
        this.workoutList = workoutList
        notifyDataSetChanged()
    }
}