package com.gachon.ttuckttak.ui.login.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.gachon.ttuckttak.databinding.FragmentSettingsProfileBinding
import com.gachon.ttuckttak.R

class SettingsProfileFragment : Fragment(), View.OnClickListener {

    private val binding: FragmentSettingsProfileBinding by lazy { FragmentSettingsProfileBinding.inflate(layoutInflater) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 버튼을 누르는 경우
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsProfileFragment_to_settingsFragment)
        }

        // 변경 사항이 일어난 경우

        // 변경 사항이 일어난 후, 저장을 하려는 경우
        checkPointFrame.setOnClickListener {
            checkPointCircle.setImageResource(R.drawable.ic_check_blue)
        }

        // 프로필 사진 변경 버튼을 클릭하는 경우
        imgProfilePlus.setOnClickListener {
            //findNavController().navigate()
        }

        // 닉네임 변경 버튼을 누르는 경우
        goNikname.setOnClickListener {
            //findNavController().navigate()
        }

        // 닉네임을 입력하는 경우
        inputNikname.setOnClickListener {
            // 입력한 nikname 가져오기
            val nikname = inputNikname.text.toString()

            // nikname 입력 여부 확인
            if(nikname.isBlank()) {
                Toast.makeText(requireContext(), "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else {
                // nikname 중복 검사

                // 중복 검사 뒤, 중복이 아닐 경우, 관련 화면으로 전환
                goNikname.setOnClickListener {
                    //findNavController().navigate()
                }

                // 중복 검사 뒤, 중복일 경우, 에러 메세지 출력
                //Toast.makeText(requireContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 이메일을 입력하는 경우
        inputEmail.setOnClickListener {
            // 입력한 email 가져오기
            val email = inputEmail.text.toString()
            // email 중복 검사
            // 중복 검사 뒤, 중복일 경우, 에러 메세지 출력
            //Toast.makeText(requireContext(), "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
        }

        // 비밀번호 재설정 버튼을 누르는 경우
        passwordResetButton.setOnClickListener {
            // 입력한 nikname과 email 가져오기
            val nikname = inputNikname.text.toString()
            val email = inputEmail.text.toString()

            // 닉네임과 이메일이 공백일 경우, 에러 메세지 출력
            if(nikname.isBlank() || email.isBlank()) {
                Toast.makeText(requireContext(), "닉네임/이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
            else {
                //findNavController().navigate()
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