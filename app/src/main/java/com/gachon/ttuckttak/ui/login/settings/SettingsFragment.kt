package com.gachon.ttuckttak.ui.login.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), View.OnClickListener {

    private val binding: FragmentSettingsBinding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼을 누르는 경우
        backButton.setOnClickListener {
            //findNavController().navigate()
        }

        // 프로필 버튼을 누르는 경우
        buttonProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsProfileFragment)
        }

        // 알림 설정 버튼을 누르는 경우
        alertSetting.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsAlertFragment)
        }

        // 고객 센터 버튼을 누르는 경우
        consumerCenter.setOnClickListener {
            //findNavController().navigate()
        }

        // 리뷰 및 평가하기 버튼을 누르는 경우
        review.setOnClickListener {
            //findNavController().navigate()
        }

        // 도움 및 설명 버튼을 누르는 경우
        help.setOnClickListener {
            //findNavController().navigate()
        }

        // 이용 수칙 버튼을 누르는 경우
        use.setOnClickListener {
            //findNavController().navigate()
        }

        // 로그아웃 버튼을 누르는 경우
        logoutButton.setOnClickListener {
            //findNavController().navigate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}
