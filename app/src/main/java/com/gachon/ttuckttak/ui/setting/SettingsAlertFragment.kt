package com.gachon.ttuckttak.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gachon.ttuckttak.databinding.FragmentSettingsAlertBinding
import com.gachon.ttuckttak.R

class SettingsAlertFragment : Fragment(), View.OnClickListener {

    private val binding: FragmentSettingsAlertBinding by lazy { FragmentSettingsAlertBinding.inflate(layoutInflater) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼을 누르는 경우
        backButtonFrame.setOnClickListener {
           val mIntent = Intent(getActivity(), SettingsActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(mIntent)
        }

        // switch를 누르는 경우
        eventAndFunctionPushSwitch.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked) {
                // switch가 체크된 경우
            }
            else {
                // switch가 체크되지 않은 경우
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}