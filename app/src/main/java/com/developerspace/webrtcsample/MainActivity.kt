package com.developerspace.webrtcsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.developerspace.webrtcsample.databinding.ActivityStartBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Constants.isIntiatedNow = true
        Constants.isCallEnded = true
        binding.startMeeting.setOnClickListener {
            if (binding.meetingId.text.toString().trim().isNullOrEmpty())
                binding.meetingId.error = "Please enter meeting id"
            else {
                db.collection("calls")
                    .document(binding.meetingId.text.toString())
                    .get()
                    .addOnSuccessListener {
                        if (it["type"] == "OFFER" || it["type"] == "ANSWER" || it["type"] == "END_CALL") {
                            binding.meetingId.error = "Please enter new meeting ID"
                        } else {
                            val intent = Intent(this@MainActivity, RTCActivity::class.java)
                            intent.putExtra("meetingID", binding.meetingId.text.toString())
                            intent.putExtra("isJoin", false)
                            startActivity(intent)
                        }
                    }
                    .addOnFailureListener {
                        binding.meetingId.error = "Please enter new meeting ID"
                    }
            }
        }
        binding.joinMeeting.setOnClickListener {
            if (binding.meetingId.text.toString().trim().isNullOrEmpty())
                binding.meetingId.error = "Please enter meeting id"
            else {
                val intent = Intent(this@MainActivity, RTCActivity::class.java)
                intent.putExtra("meetingID", binding.meetingId.text.toString())
                intent.putExtra("isJoin", true)
                startActivity(intent)
            }
        }
    }
}