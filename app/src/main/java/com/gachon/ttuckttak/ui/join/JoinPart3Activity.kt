package com.gachon.ttuckttak.ui.join

import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityJoinPart3Binding
import com.gachon.ttuckttak.utils.RegexUtil

class JoinPart3Activity : BaseActivity<ActivityJoinPart3Binding>(ActivityJoinPart3Binding::inflate) {

    private var validNickname = false
    private var validPasswordFormat = false
    private var samePassword = false

    override fun initAfterBinding() = with(binding) {
        setClickListener()
        setFocusChangeListener()
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        imagebuttonBack.setOnClickListener {
            finish()
        }

        // 가입하기 버튼을 클릭한 경우
        buttonJoin.setOnClickListener {

        }
    }

    // Todo: 로직 검증 필요
    private fun setFocusChangeListener() = with(binding) {
        edittextName.setOnFocusChangeListener { _, hasFocus ->
            validNickname = false
            textviewNicknameErrorMessage.visibility = View.INVISIBLE

            if (hasFocus) {
                edittextName.setBackgroundResource(R.drawable.textbox_state_focused)

            } else {
                val nickname = edittextName.text.toString()

                if (nickname.isEmpty()) {
                    edittextName.setBackgroundResource(R.drawable.textbox_state_normal)

                } else if (RegexUtil.isValidNicknameFormat(nickname)) {
                    validNickname = true // Todo: 디자인 수정 없는 경우 사용 가능한 닉네임인지 서버에 요청 후 사용 가능하면 valid nickname을 true로 변경
                    edittextName.setBackgroundResource(R.drawable.textbox_state_normal)

                } else {
                    edittextName.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewNicknameErrorMessage.visibility = View.VISIBLE
                    textviewNicknameErrorMessage.text = getString(R.string.invalid_nikname)
                }
            }

            updateJoinButton()
        }

        edittextPassword.setOnFocusChangeListener { _, hasFocus ->
            validPasswordFormat = false

            if (hasFocus) {
                edittextPassword.setBackgroundResource(R.drawable.textbox_state_focused)
                textviewPasswordMessage.text = getString(R.string.password_rule)
                textviewPasswordMessage.setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_black))
                textviewPasswordMessage.gravity = Gravity.CENTER

            } else {
                val password = edittextPassword.text.toString()

                if (password.isEmpty()) { // 비어있는 경우
                    edittextPassword.setBackgroundResource(R.drawable.textbox_state_normal)
                    textviewPasswordMessage.text = getString(R.string.password_rule)
                    textviewPasswordMessage.setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_black))
                    textviewPasswordMessage.gravity = Gravity.CENTER

                } else if (RegexUtil.isValidPwFormat(password)) { // 비밀번호 형식에 맞는 경우
                    validPasswordFormat = true
                    edittextPassword.setBackgroundResource(R.drawable.textbox_state_normal)
                    textviewPasswordMessage.text = getString(R.string.password_usable)
                    textviewPasswordMessage.setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.main_theme_subcyan))
                    textviewPasswordMessage.gravity = Gravity.END

                } else { // 비밀번호 형식에 맞지 않는 경우
                    edittextPassword.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewPasswordMessage.text = getString(R.string.invalid_pw_format)
                    textviewPasswordMessage.setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_red))
                    textviewPasswordMessage.gravity = Gravity.END
                }
            }

            updateJoinButton()
        }

        edittextPasswordCheck.setOnFocusChangeListener { _, hasFocus ->
            samePassword = false
            textviewCheckPasswordMessage.visibility = View.INVISIBLE

            if (hasFocus) {
                edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_focused)

            } else {
                val pw = edittextPassword.text.toString()
                val pwCheck = edittextPasswordCheck.text.toString()

                if (pwCheck.isEmpty()) { // 비어있는 경우
                    edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_normal)

                } else if (pw == pwCheck) { // 입력한 pw와 동일한 경우
                    samePassword = true
                    edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_normal)

                } else { // 입력한 pw와 동일하지 않는 경우
                    edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewCheckPasswordMessage.visibility = View.VISIBLE
                }
            }

            updateJoinButton()
        }
    }

    private fun updateJoinButton() = with(binding) {
        buttonJoin.isEnabled = (validNickname && validPasswordFormat && samePassword)
    }
}