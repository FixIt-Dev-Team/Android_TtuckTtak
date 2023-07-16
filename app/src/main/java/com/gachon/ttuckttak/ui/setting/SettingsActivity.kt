package com.gachon.ttuckttak.ui.setting

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.databinding.ActivitySettingsBinding
import com.gachon.ttuckttak.ui.login.LoginActivity

class SettingsActivity : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy {ActivitySettingsBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding) {
            // 뒤로가기 버튼을 누르는 경우 - 이전 화면으로 이동 <임시설정>
            //backButtonFrame.setOnClickListener { onReplaceFragment() }

            // 프로필 박스의 화살표를 누르는 경우 - 프로필 설정 프래그먼트로 이동
            buttonProfileFrame.setOnClickListener { onReplaceFragment(SettingsProfileFragment()) }

            // 알림 설정 버튼을 누르는 경우 - 알림 설정 프래그먼트로 이동
            alertSetting.setOnClickListener { onReplaceFragment(SettingsAlertFragment()) }

            // 고객센터 버튼을 누르는 경우 - 고객센터 프래그먼트로 이동 <임시 설정>
            //consumerCenter.setOnClickListener { onReplaceFragment() }

            // 리뷰 및 평가하기 버튼을 누르는 경우 - 해당 프래그먼트로 이동 <임시 설정>
            //review.setOnClickListener { onReplaceFragment() }

            // 도움 및 설명 버튼을 누르는 경우 - 해당 프래그먼트로 이동 <임시 설정>
            //help.setOnClickListener { onReplaceFragment() }

            // 이용 수칙 버튼을 누르는 경우 - 해당 프래그먼트로 이동 <임시 설정>
            //use.setOnClickListener { onReplaceFragment() }

            // 로그아웃 버튼을 누르는 경우 - LoginActivity로 이동
            /*logoutButton.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
              }
             */
        }
    }

    // 해당 프래그먼트로 대체하는 함수
    private fun onReplaceFragment(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.onboarding_, fragment)
        }.commit()
    }
}