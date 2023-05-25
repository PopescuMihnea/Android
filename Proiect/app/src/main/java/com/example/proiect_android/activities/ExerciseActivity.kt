package com.example.proiect_android.activities

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import com.example.proiect_android.R
import com.example.proiect_android.model.Exercise
import com.example.proiect_android.viewmodel.ExercisesViewModel
import com.google.android.material.button.MaterialButton
import java.io.File


class ExerciseActivity : AppCompatActivity() {
    private lateinit var exerciseName : TextView
    private lateinit var exerciseImage : ImageView
    private lateinit var exerciseCategory : TextView
    private lateinit var exerciseVideo : VideoView
    private lateinit var exerciseDescription : TextView
    private lateinit var shareButton : MaterialButton
    private lateinit var playButton : MaterialButton
    private lateinit var scrollView : ScrollView
    private lateinit var recordButton : Button
    private lateinit var viewButton : Button
    private lateinit var deleteButton : Button
    private lateinit var checkButton : MaterialButton
    private lateinit var exerciseViewModel : ExercisesViewModel
    private var initCheckButtonX : Float = 0f
    private val exerciseKey = "exercise"
    private val videoUriKey = "videoUri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)


        //navigation (navigation graph)
        val bundle : Bundle? = intent.extras
        val exercise = bundle!!.getParcelable<Exercise>(exerciseKey)


        //view binding
        exerciseName = findViewById(R.id.exercise_name)
        exerciseImage = findViewById(R.id.exercise_image)
        exerciseCategory = findViewById(R.id.exercise_category)
        exerciseVideo = findViewById(R.id.exercise_video)
        exerciseDescription = findViewById(R.id.exercise_description)
        shareButton = findViewById(R.id.share_button)
        playButton = findViewById(R.id.play_button)
        scrollView = findViewById(R.id.scroll_view)
        recordButton = findViewById(R.id.record_video_button)
        viewButton = findViewById(R.id.view_video_button)
        deleteButton = findViewById(R.id.delete_video_button)
        checkButton = findViewById(R.id.check_button)

        exerciseViewModel = ViewModelProvider(this)[ExercisesViewModel::class.java]

        exerciseName.text = exercise!!.exerciseName

        val imageId = resources.getIdentifier(exercise.exerciseImage, "drawable", packageName)
        exerciseImage.setImageResource(imageId)
        exerciseCategory.text = getString(R.string.exercise_category_string, exercise.exerciseCategory)
        exerciseDescription.text = exercise.exerciseDescription

        val videoId = resources.getIdentifier(exercise.exerciseVideo, "raw", packageName)
        val offlineUri = Uri.parse("android.resource://$packageName/${videoId}")
        val mediaController = MediaController(this)
        mediaController.setAnchorView(exerciseVideo)
        exerciseVideo.setVideoURI(offlineUri)

        scrollView.viewTreeObserver.addOnScrollChangedListener { mediaController.hide() }

        shareButton.setOnClickListener {
            val shareString = "${exercise.exerciseName}\nMuscle groups: ${exercise.exerciseCategory}\nVideo guide: ${exercise.exerciseShareVideo}"

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, shareString)
            intent.type="text/plain"

            val chooser = Intent.createChooser(intent, "Share information about this exercise using:")
            startActivity(chooser)

        }

        playButton.setOnClickListener{
            playButton.visibility = View.INVISIBLE
            exerciseVideo.setMediaController(mediaController)
            exerciseVideo.start()
        }

        if (exercise.exerciseUserVideo != null)
        {
            val uri = Uri.parse(exercise.exerciseUserVideo)
            Log.d("STATE", "Uri ${uri.path}")
            if (checkContentExists(uri))
            {
                addButtons()
            }
        }

        val getActionRecord = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val uri = it?.data?.data.toString()
            if (uri != "null")
            {
                exercise.exerciseUserVideo = uri
                exerciseViewModel.updateExercise(exercise)

                addButtons()

                val intent = Intent(this, UserVideoActivity::class.java)
                intent.putExtra(videoUriKey, uri)
                startActivity(intent)
            }
        }

        val getActionDelete = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult())
        {
            if (it != null)
            {
                when(it.resultCode)
                {
                    Activity.RESULT_OK -> Toast.makeText(this, "File deleted from gallery", Toast.LENGTH_SHORT).show()
                    Activity.RESULT_CANCELED -> Toast.makeText(this, "File not deleted from gallery", Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

                removeButtons()
                deleteAnimation()
            }
        }

        recordButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if (intent.resolveActivity(packageManager) != null)
            {
                getActionRecord.launch(intent)
            }
        }

        viewButton.setOnClickListener{
            val intent = Intent(this, UserVideoActivity::class.java)
            intent.putExtra(videoUriKey, exercise.exerciseUserVideo)
            startActivity(intent)
        }

        deleteButton.setOnClickListener{
            val uri = Uri.parse(exercise.exerciseUserVideo)
            exercise.exerciseUserVideo = null

            exerciseViewModel.updateExercise(exercise)

            val pi = MediaStore.createDeleteRequest(contentResolver, arrayListOf(uri))
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(pi.intentSender).build()
                getActionDelete.launch(intentSenderRequest)
            } catch (e: SendIntentException) {
                e.printStackTrace()
            }
        }

        scrollView.smoothScrollTo(0, 0)
    }

    private fun addButtons()
    {
        recordButton.text = getString(R.string.update_video)
        viewButton.visibility = View.VISIBLE
        deleteButton.visibility = View.VISIBLE
        deleteButton.isClickable = true
        initCheckButtonX = checkButton.x
    }

    private fun removeButtons()
    {
        recordButton.text = getString(R.string.record_video)
        viewButton.visibility = View.GONE
        deleteButton.isClickable = false
    }

    private fun checkContentExists(uri : Uri): Boolean {
        val projection : Array<String> = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(uri, projection, null, null)
        cursor.use{
            cursor ->
            if (cursor != null )
            {
                if (cursor.moveToFirst())
                {
                    val path = cursor.getString(0)
                    return File(path).exists()
                }

            }
            return false
        }
    }

    private fun deleteAnimation()
    {
        val animButtonHide = ObjectAnimator.ofFloat(deleteButton, View.ALPHA, deleteButton.alpha, 0f)
        animButtonHide.duration = 1000
        animButtonHide.interpolator = LinearInterpolator()
        animButtonHide.doOnEnd{
            deleteButton.text = getString(R.string.remove_video)
            deleteButton.backgroundTintList = getColorStateList(R.color.black)
            deleteButton.visibility = View.INVISIBLE
            deleteButton.alpha = 1f

            checkButton.visibility = View.INVISIBLE
            checkButton.x = initCheckButtonX
        }

        val animHideCheck = ObjectAnimator.ofFloat(checkButton, View.ALPHA, viewButton.alpha, 0f)
        animHideCheck.duration = 1000
        animHideCheck.interpolator = LinearInterpolator()
        animHideCheck.doOnEnd {
            animButtonHide.start()
        }

        val animMoveCheck = ObjectAnimator.ofFloat(checkButton, View.TRANSLATION_X, initCheckButtonX, deleteButton.x - initCheckButtonX)
        animMoveCheck.duration = 1000
        animMoveCheck.interpolator = LinearInterpolator()
        animMoveCheck.doOnEnd {
            animHideCheck.start()
        }

        val animShowCheck = ObjectAnimator.ofFloat(checkButton, View.ALPHA, 0f, 0f, 1f)
        animShowCheck.duration = 1000
        animShowCheck.interpolator = LinearInterpolator()
        animShowCheck.doOnEnd {

        }

        val animButtonShow = ObjectAnimator.ofFloat(deleteButton, View.ALPHA,0f, 0f, 1f)
        deleteButton.text = ""
        deleteButton.backgroundTintList = getColorStateList(R.color.green)
        animButtonShow.duration = 1000
        animButtonShow.interpolator = LinearInterpolator()
        animButtonShow.doOnEnd {
            checkButton.visibility = View.VISIBLE
            animShowCheck.start()
            animMoveCheck.start()
        }

        animButtonShow.start()

    }
}