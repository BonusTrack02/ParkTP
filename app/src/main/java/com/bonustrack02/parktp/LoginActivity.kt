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
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.loginBtnNaver.setOnClickListener {
            clickNaver()
        }
    }

    private fun clickNaver() {
        val clientId = resources.getString(R.string.naver_client_id)
        val clientSecret = resources.getString(R.string.naver_client_secret)
        NaverIdLoginSDK.initialize(this, clientId, clientSecret, "산책할 공원 어디?")

        NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                Toast.makeText(this@LoginActivity, "서버 에러 : $message", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Toast.makeText(this@LoginActivity, "네이버 로그인 실패 : $message", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                Toast.makeText(this@LoginActivity, "네이버 로그인 성공", Toast.LENGTH_SHORT).show()

                val accessToken = NaverIdLoginSDK.getAccessToken()

                val retrofit = RetrofitHelper.getInstance("https://openapi.naver.com")
                    .create(RetrofitService::class.java)
                    .getNaverIdUserInfo("Bearer $accessToken")
                    .enqueue(object : Callback<NaverUserInfoResponse> {
                        override fun onResponse(
                            call: Call<NaverUserInfoResponse>,
                            response: Response<NaverUserInfoResponse>
                        ) {
                            val userInfo = response.body()
                            val id = userInfo?.response?.id ?: ""
                            val email = userInfo?.response?.email ?: ""
                            Toast.makeText(this@LoginActivity, "$email", Toast.LENGTH_SHORT).show()
                            G.user = User(id, email)

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }

                        override fun onFailure(call: Call<NaverUserInfoResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "회원정보 가져오기 실패", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        })
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