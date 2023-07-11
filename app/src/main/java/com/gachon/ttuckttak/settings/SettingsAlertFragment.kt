package com.gachon.ttuckttak.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gachon.ttuckttak.databinding.FragmentSettingsAlertBinding
import com.gachon.ttuckttak.R


class SettingsAlertFragment : Fragment(), View.OnClickListener {

    //Switch eventFunctionSwitch;

    private lateinit var binding: FragmentSettingsAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsAlertBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼을 누르는 경우
        binding.iconBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsAlertFragment_to_settingsFragment)
        }

        // switch를 누르는 경우
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}