package com.example.proiect_android.fragments

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.example.proiect_android.Notification
import com.example.proiect_android.R
import com.example.proiect_android.channelId
import com.example.proiect_android.model.Workout
import com.example.proiect_android.notificationId
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class WorkoutNotification : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var timePicker : TimePicker
    private lateinit var datePicker: DatePicker
    private val workoutKey = "workout"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitButton = view.findViewById(R.id.submitButton)
        timePicker = view.findViewById(R.id.timePicker)
        datePicker = view.findViewById(R.id.datePicker)

        createNotificationChannel()
        submitButton.setOnClickListener {
            scheduleNotification()
        }

    }

    private fun createNotificationChannel() {
        val name = "Workout channel"
        val desc = "Notifications for workouts"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        channel.description = desc
        val notificationManager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleNotification() {
        val intent = Intent(requireActivity().applicationContext, Notification::class.java)

        val bundle = this.arguments
        val workout = bundle!!.getParcelable<Workout>(workoutKey)!!
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        intent.putExtra(workoutKey, workout)

        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity().applicationContext,
            notificationId,
            intent,
            flags
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        showAlert(time, workout)
    }

    private fun showAlert(time: Long, workout: Workout) {
        val date = Date(time)
        val dateFormat = DateFormat.getLongDateFormat(requireActivity().applicationContext)
        val timeFormat = DateFormat.getTimeFormat(requireActivity().applicationContext)

        AlertDialog.Builder(context)
            .setTitle("Notification scheduled")
            .setMessage("Title: Scheduled workout \n" +
                    "Message: 'See workout: ${workout.name}' \n " +
                    "At: ${dateFormat.format(date)}  ${timeFormat.format(date)}")
            .setPositiveButton("Ok"){_,_->}
            .show()
    }

    private fun getTime(): Long {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_notification, container, false)
    }

}