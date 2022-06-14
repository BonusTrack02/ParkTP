package com.bonustrack02.parktp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bonustrack02.parktp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    val binding : ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val intent = Intent(this, MainActivity::class.java)

        binding.loginBtnNonlogin.setOnClickListener {
            startActivity(intent)
            finish()
        }

        binding.loginBtnGoogle.setOnClickListener {
            clickGoogle()
            startActivity(intent)
            finish()
        }

        binding.loginBtnKakao.setOnClickListener {
            clickKakao()
        }
    }

    private fun clickKakao() {

        val callback : (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()

                UserApiClient.instance.me { user, error ->
                    if (user != null) {
                        var id = user.id.toString()
                        var email = user.kakaoAccount?.email ?: ""

                        G.user = User(id, email)

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun clickGoogle() {
        val idToken = getString(R.string.google_id_token)
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(idToken)
            .requestEmail()
            .build()

        val intent = GoogleSignIn.getClient(this, signInOptions).signInIntent
        resultLauncher.launch(intent)
    }

    val resultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val intent = it.data
        val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

        var account = task.result
        val email = account.email

    }
}