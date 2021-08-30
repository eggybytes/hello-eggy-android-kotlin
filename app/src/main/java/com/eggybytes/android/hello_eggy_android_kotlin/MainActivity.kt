package com.eggybytes.android.hello_eggy_android_kotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.eggybytes.android.eggy_android.EggyClient
import com.eggybytes.android.eggy_android.EggyConfig
import com.eggybytes.android.eggy_android.EggyDevice
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var leftButton: Button
    private lateinit var rightButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextview: TextView

    private val questionBank = listOf(
        Question(R.string.question_one, R.string.left_answer_one, R.string.right_answer_one, true),
        Question(R.string.question_two, R.string.left_answer_two, R.string.right_answer_two, true),
        Question(R.string.question_three, R.string.left_answer_three, R.string.right_answer_three, false),
        Question(R.string.question_four, R.string.left_answer_four, R.string.right_answer_four, true)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        val config = EggyConfig("YOUR_API_TOKEN")
        val device = EggyDevice()
        EggyClient.start(config, device)
        registerForPushNotifications()

        setContentView(R.layout.activity_main)

        leftButton = findViewById(R.id.left_button)
        rightButton = findViewById(R.id.right_button)
        nextButton = findViewById(R.id.next_button)
        questionTextview = findViewById(R.id.question_text_view)

        leftButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        rightButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            moveNext()
        }

        questionTextview.setOnClickListener {
            moveNext()
        }

        setText()
    }

    private fun registerForPushNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = getString(R.string.default_notification_channel_id)
            if (manager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(channelId, getString(R.string.default_notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = getString(R.string.default_notification_channel_description)
                manager.createNotificationChannel(channel)
            }
        }

        Log.d(TAG, "fetching token")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            Thread {
                EggyClient.registerWithDeviceApi("", token)
            }.start()
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun moveNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
        setText()
    }

    private fun setText() {
        val questionTextResId = questionBank[currentIndex].questionResId
        questionTextview.setText(questionTextResId)

        val leftAnswerResId = questionBank[currentIndex].leftAnswerResId
        leftButton.setText(leftAnswerResId)

        val rightAnswerResId = questionBank[currentIndex].rightAnswerResId
        rightButton.setText(rightAnswerResId)
    }

    private fun checkAnswer(userClickedLeft: Boolean) {
        val answerShouldBeLeft = questionBank[currentIndex].answerIsLeft

        val messageResId = if (userClickedLeft == answerShouldBeLeft) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
