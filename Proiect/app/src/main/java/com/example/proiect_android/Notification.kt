package com.example.proiect_android

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.LiveFolders.INTENT
import androidx.core.app.NotificationCompat
import com.example.proiect_android.activities.MainActivity
import com.example.proiect_android.activities.WorkoutActivity
import com.example.proiect_android.model.Workout
import com.example.proiect_android.utils.Constants.APP_URI
import com.example.proiect_android.utils.Constants.PACKAGE_NAME

const val notificationId = 1
const val channelId = "channel1"

class Notification: BroadcastReceiver() {

    private val workoutKey = "workout"

    override fun onReceive(context: Context, intent: Intent) {
        val workout = intent.getParcelableExtra<Workout>(workoutKey)
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("$APP_URI/workout/${workout!!.id}")
        )
        clickIntent.setPackage(PACKAGE_NAME)

        val clickPendingIntent = TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(clickIntent)
            //addParentStack(WorkoutActivity::class.java)
            //addNextIntent(clickIntent)
            getPendingIntent(1, flags)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.dumbbell_gym_svgrepo_com)
            .setContentTitle("Scheduled workout")
            .setContentText("See workout: ${workout!!.name}")
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }
}