package com.example.proiect_android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proiect_android.activities.ExerciseActivity
import com.example.proiect_android.R
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.interfaces.onItemClickListener
import com.google.android.material.imageview.ShapeableImageView

class ExerciseRecyclerAdapter():
    RecyclerView.Adapter<ExerciseRecyclerAdapter.ExerciseRecyclerViewHolder>() {

    private lateinit var clickListener: onItemClickListener
    private var exerciseList = emptyList<Exercise>()
    private lateinit var context: Context
    private val exerciseKey = "exercise"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_list,
        parent, false)
        context = parent.context

        clickListener = object : onItemClickListener{
            override fun onItemClick(position: Int) {
                //Toast.makeText(requireActivity(), "You clicked on item no. $position", Toast.LENGTH_SHORT).show()

                val intent = Intent(context, ExerciseActivity::class.java)
                intent.putExtra(exerciseKey, exerciseList[position])
                context.startActivity(intent)
            }

        }

        return ExerciseRecyclerViewHolder(itemView, clickListener)
    }

    override fun getItemCount(): Int {
        return exerciseList.size;
    }

    override fun onBindViewHolder(holder: ExerciseRecyclerViewHolder, position: Int) {
        val currentItem = exerciseList[position]

        val imageId = context.resources.getIdentifier(currentItem.exerciseImage, "drawable", context.packageName)
        holder.exerciseImage.setImageResource(imageId)

        holder.exerciseName.text = currentItem.exerciseName
        holder.exerciseCategory.text = currentItem.exerciseCategory
    }

    class ExerciseRecyclerViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        val exerciseImage : ShapeableImageView = itemView.findViewById(R.id.exercise_image)
        val exerciseName : TextView = itemView.findViewById(R.id.exercise_name)
        val exerciseCategory: TextView = itemView.findViewById(R.id.exercise_category)

    }

    fun setData(exerciseList : List<Exercise>)
    {
        this.exerciseList = exerciseList
        notifyDataSetChanged()
    }

}