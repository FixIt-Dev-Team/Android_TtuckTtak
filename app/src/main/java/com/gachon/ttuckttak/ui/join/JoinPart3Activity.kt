package com.gachon.ttuckttak.ui.join

import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityJoinPart3Binding
import com.gachon.ttuckttak.ui.main.StartActivity
import com.gachon.ttuckttak.ui.terms.TermsPromoteActivity
import com.gachon.ttuckttak.ui.terms.TermsUseActivity
import com.gachon.ttuckttak.utils.RegexUtil

class JoinPart3Activity : BaseActivity<ActivityJoinPart3Binding>(ActivityJoinPart3Binding::inflate) {

    private var validNickname = false
    private var validPasswordFormat = false
    private var samePassword = false

    private var isLayoutVisible = false // layout alert 화면이 현재 보여지고 있는지

    override fun initAfterBinding() = with(binding) {
        setClickListener()
        setTouchListener()
        setFocusChangeListener()
        setCheckedChangeListener()
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        imagebuttonBack.setOnClickListener {
            finish()
        }

        // 가입하기 버튼을 클릭한 경우
        buttonJoin.setOnClickListener {
            showLayout()
        }

        // 시작하기 버튼을 클릭한 경우
        layoutAlert.buttonStart.setOnClickListener {
            // Todo: 회원가입 요청
            startNextActivity(StartActivity::class.java)
        }

        // 서비스 이용 약관를 클릭한 경우
        layoutAlert.textviewUseTerms.setOnClickListener {
            startNextActivity(TermsUseActivity::class.java)
        }


        layoutAlert.textviewPromoteTerms.setOnClickListener {
            startNextActivity(TermsPromoteActivity::class.java)
        }
    }

    // layout alert 화면 보여줄 때
    private fun showLayout() = with(binding.layoutAlert) {
        if (!isLayoutVisible) {
            root.visibility = View.VISIBLE
            root.translationY = root.height.toFloat()
            root.translationZ = Float.MAX_VALUE // 가장 큰 값을 줌으로써 인증하기 버튼 위로 나오게
            root.animate().translationY(0f).setDuration(300).start()
            isLayoutVisible = true
        }
    }

    // layout alert 화면 내릴 때
    private fun closeLayout() = with(binding.layoutAlert) {
        if (isLayoutVisible) {
            root.animate().translationY(root.height.toFloat()).setDuration(300).withEndAction {
                root.visibility = View.GONE
            }.start()
            isLayoutVisible = false
        }
    }

    private fun setTouchListener() = with(binding) {
        // layout alert 화면 외를 클릭 했을 때 layout alert 화면 내리기
        layoutRoot.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                closeLayout()
                return@setOnTouchListener true
            }
            false
        }

        // layout alert 화면은 클릭 되어도 그대로
        layoutAlert.root.setOnTouchListener { _, _ -> true }
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

    private fun setCheckedChangeListener() = with(binding.layoutAlert) {
        checkboxAgreeTerms.setOnCheckedChangeListener { _, isChecked ->
            checkboxUseTerms.isChecked = isChecked
            checkboxPromoteTerms.isChecked = isChecked
        }

        checkboxUseTerms.setOnCheckedChangeListener { _, isChecked ->
            buttonStart.isEnabled = isChecked
            updateAllCheckStatus()
        }

        checkboxPromoteTerms.setOnCheckedChangeListener { _, _ ->
            updateAllCheckStatus()
        }
    }

    private fun updateAllCheckStatus() = with(binding.layoutAlert) {
        checkboxAgreeTerms.setOnCheckedChangeListener(null)
        checkboxAgreeTerms.isChecked = checkboxUseTerms.isChecked && checkboxPromoteTerms.isChecked

        checkboxAgreeTerms.setOnCheckedChangeListener { _, isChecked ->
            checkboxUseTerms.isChecked = isChecked
            checkboxPromoteTerms.isChecked = isChecked
        }
    }
}