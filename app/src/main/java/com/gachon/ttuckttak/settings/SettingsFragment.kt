package com.gachon.ttuckttak.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.navigation.fragment.findNavController
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_change_settings)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼을 누르는 경우
        binding.iconBackButton.setOnClickListener {
            //findNavController().navigate()
        }

        // 프로필 버튼을 누르는 경우
        binding.profileFrameBox.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsProfileFragment)
        }

        // 알림 설정 버튼을 누르는 경우
        binding.alertSettingsFrame.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_settingsAlertFragment)
        }

        // 고객 센터 버튼을 누르는 경우
        binding.consumerCenterFrame.setOnClickListener {
            //findNavController().navigate()
        }

        // 리뷰 및 평가하기 버튼을 누르는 경우
        binding.reviewAndEvaluate.setOnClickListener {
            //findNavController().navigate()
        }

        // 도움 및 설명 버튼을 누르는 경우
        binding.helpAndExplain.setOnClickListener {
            //findNavController().navigate()
        }

        // 이용 수칙 버튼을 누르는 경우
        binding.usingRules.setOnClickListener {
            //findNavController().navigate()
        }

        // 로그아웃 버튼을 누르는 경우
        binding.logoutText.setOnClickListener {
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
