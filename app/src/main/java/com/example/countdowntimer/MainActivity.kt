package com.example.countdowntimer

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countdowntimer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }
    private var isPlaying = false
    private var firstPlay = true
    private var milliseconds = 0
    private lateinit var timer: CountDownTimer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun playPause(view: View) {
        if (isPlaying){
            isPlaying = false
            binding.btnStartpause.text = getString(R.string.start)
            timer.cancel()

        } else {

            if (binding.etTimeSeconds.text.isEmpty()) {
                Toast.makeText(this, R.string.invalid_time, Toast.LENGTH_SHORT).show()
            } else {
                if (binding.etTimeMinutes.text.isEmpty()){
                    milliseconds = 0
                } else {
                    milliseconds = (binding.etTimeMinutes.text.toString().toInt()) * 60 * 1000
                }
                milliseconds += (binding.etTimeSeconds.text.toString().toInt()) * 1000
                timer = object : CountDownTimer(milliseconds.toLong(), 1000){
                    override fun onTick(millisUntilFinished: Long){
                        if (millisUntilFinished >= 60000){
                            binding.etTimeMinutes.setText("" + millisUntilFinished / 60000)
                            Log.i(TAG, "minutes ${millisUntilFinished / 60000}")
                        } else {
                            binding.etTimeMinutes.setText("")
                        }
                        binding.etTimeSeconds.setText("" + (millisUntilFinished % 60000) / 1000)

                    }

                    override fun onFinish() {
                        binding.btnDelete.visibility = View.GONE
                        binding.btnStartpause.text = getString(R.string.start)
                        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)

                        val anim = ObjectAnimator.ofInt(binding.etTimeSeconds, "textColor", R.color.blue_mid, R.color.red_mid,
                                R.color.blue_mid)
                        anim.duration = 1500
                        anim.setEvaluator(ArgbEvaluator())
                        anim.repeatMode = ValueAnimator.REVERSE

                        anim.start()

                    }
                }.start()
                    isPlaying = true
                    binding.btnStartpause.text = getString(R.string.pause)
                    binding.btnDelete.visibility = View.VISIBLE
            }
        }
    }

    fun deleteTimer(view: View) {
        if (isPlaying){
            isPlaying = false
        }
        timer.cancel()
        binding.etTimeMinutes.setText("")
        binding.etTimeSeconds.setText("")
        binding.btnDelete.visibility = View.GONE
        firstPlay = true
    }
}