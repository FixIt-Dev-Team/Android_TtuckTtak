package com.gachon.ttuckttak.ui.join

import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.SignUpReq
import com.gachon.ttuckttak.data.remote.service.AuthService
import com.gachon.ttuckttak.data.remote.service.MemberService
import com.gachon.ttuckttak.databinding.ActivityJoinPart3Binding
import com.gachon.ttuckttak.ui.main.StartActivity
import com.gachon.ttuckttak.ui.terms.TermsPromoteActivity
import com.gachon.ttuckttak.ui.terms.TermsUseActivity
import com.gachon.ttuckttak.utils.RegexUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class JoinPart3Activity : BaseActivity<ActivityJoinPart3Binding>(ActivityJoinPart3Binding::inflate, TransitionMode.HORIZONTAL) {

    private var validNickname = false
    private val email: String by lazy { intent.getStringExtra("email")!! }
    @Inject lateinit var userManager: UserManager
    @Inject lateinit var tokenManager: TokenManager
    @Inject lateinit var authService: AuthService
    @Inject lateinit var memberService: MemberService

    private var isLayoutVisible = false // layout alert 화면이 현재 보여지고 있는지

    override fun initAfterBinding() = with(binding) {
        setClickListener()
        setTouchListener()
        setFocusChangeListener()
        setCheckedChangeListener()
    }

    private fun setClickListener() = with(binding) {
        imagebuttonBack.setOnClickListener { finish() }
        buttonJoin.setOnClickListener { showLayout() }

        // 시작하기 버튼을 클릭한 경우
        layoutAlert.buttonStart.setOnClickListener { handleStartButtonClick() }
        layoutAlert.textviewUseTerms.setOnClickListener { startNextActivity(TermsUseActivity::class.java) }
        layoutAlert.textviewPromoteTerms.setOnClickListener { startNextActivity(TermsPromoteActivity::class.java) }
    }

    private fun handleStartButtonClick() = with(binding) {
        val nickname = edittextName.text.toString()
        val password = edittextPassword.text.toString()
        val adProvision = layoutAlert.checkboxPromoteTerms.isChecked

        performSignUp(nickname, password, adProvision)
    }

    private fun performSignUp(nickname: String, password: String, adProvision: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = authService.signUp(SignUpReq(email, password, nickname, adProvision))
                Log.i("response", response.toString())

                handleSignUpResponse(response)

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("JoinPart3Activity", "서버 통신 오류: ${e.message}")
                    showToast("회원가입 요청 실패")
                }
            }
        }
    }

    private suspend fun handleSignUpResponse(response: BaseResponse<LoginRes>) {
        with(response) {
            if (isSuccess) {
                userManager.saveUserIdx(response.data!!.userIdx)
                tokenManager.saveToken(response.data.tokenInfo)
                startNextActivity(StartActivity::class.java)

            } else {
                showErrorMessage(response)
            }
        }
    }

    private suspend fun showErrorMessage(response: BaseResponse<LoginRes>) = with(binding) {
        withContext(Dispatchers.Main) {
            closeLayout()
            when (response.code) {
                409 -> {
                    edittextName.apply {
                        setBackgroundResource(R.drawable.textbox_state_error)
                        setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_red))
                    }
                    textviewNicknameErrorMessage.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.overlap_nickname)
                    }
                }
                else -> showToast(response.message)
            }
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
        // layout 밖을 클릭 했을 때 layout 내리기
        layoutRoot.setOnTouchListener { _, event -> closeLayoutOnTouchOutside(event) }

        // layout 내부를 클릭 했을 땐 그대로
        layoutAlert.root.setOnTouchListener { _, _ -> true }
    }

    // 레이아웃 밖을 터치했을 때 레이아웃 닫기
    private fun closeLayoutOnTouchOutside(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            closeLayout()
            return true
        }
        return false
    }

    // Todo: 로직 검증 필요
    //  해당 부분은 로직 변경 시 수정될 수 있어 리팩토링 진행 X
    private fun setFocusChangeListener() = with(binding) {

        // Todo: 해당 부분 로직 고민
        //  서버와 연결되지 않은 상태일 때 버튼이 계속 비활성 되어 있음. 사용자 측면에서 볼 때 동의 다 하고 나서 통신이 되지 않는게 낫지 않나 싶음
        //  하여 해당 부분은 논의 후 수정
        edittextName.setOnFocusChangeListener { _, hasFocus ->
            textviewNicknameErrorMessage.visibility = View.INVISIBLE

            if (hasFocus) {
                edittextName.apply {
                    setBackgroundResource(R.drawable.textbox_state_focused)
                    setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_black))
                }

            } else {
                val nickname = edittextName.text.toString()

                if (nickname.isEmpty()) {
                    edittextName.setBackgroundResource(R.drawable.textbox_state_normal)
                    validNickname = false

                } else if (RegexUtil.isValidNicknameFormat(nickname)) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try { // 서버에 닉네임 사용 가능 여부 확인
                            lifecycleScope.launch(Dispatchers.Main) {
                                val response = memberService.checkNickname(nickname)
                                Log.i("response", response.toString())

                                if (response.isSuccess && response.data!!.isAvailable) { // 사용 가능한 닉네임인 경우
                                    validNickname = true
                                    edittextName.setBackgroundResource(R.drawable.textbox_state_normal)

                                } else { // 사용 가능하지 않은 닉네임인 경우
                                    validNickname = false
                                    edittextName.setBackgroundResource(R.drawable.textbox_state_error)
                                    textviewNicknameErrorMessage.apply {
                                        visibility = View.VISIBLE
                                        text = getString(R.string.overlap_nickname)
                                    }
                                }
                            }

                        } catch (e: Exception) {
                            runOnUiThread {
                                validNickname = false
                                Log.e("JoinPart3Activity", "서버 통신 오류: ${e.message}")
                                showToast("닉네임 사용 가능 요청 실패")
                            }
                        }
                    }

                } else {
                    validNickname = false
                    edittextName.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewNicknameErrorMessage.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.invalid_nickname)
                    }
                }

                updateJoinButton()
            }
        }

        edittextPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                edittextPassword.setBackgroundResource(R.drawable.textbox_state_focused)
                textviewPasswordMessage.apply {
                    text = getString(R.string.password_rule)
                    setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_black))
                    gravity = Gravity.CENTER
                }

            } else {
                val password = edittextPassword.text.toString()

                if (password.isEmpty()) { // 비어있는 경우
                    edittextPassword.setBackgroundResource(R.drawable.textbox_state_normal)
                    textviewPasswordMessage.apply {
                        text = getString(R.string.password_rule)
                        setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_black))
                        gravity = Gravity.CENTER
                    }

                } else if (RegexUtil.isValidPwFormat(password)) { // 비밀번호 형식에 맞는 경우
                    edittextPassword.setBackgroundResource(R.drawable.textbox_state_normal)
                    textviewPasswordMessage.apply {
                        text = getString(R.string.password_usable)
                        setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.main_theme_subcyan))
                        gravity = Gravity.END
                    }

                } else { // 비밀번호 형식에 맞지 않는 경우
                    edittextPassword.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewPasswordMessage.apply {
                        text = getString(R.string.invalid_pw_format)
                        setTextColor(ContextCompat.getColor(this@JoinPart3Activity, R.color.general_theme_red))
                        gravity = Gravity.END
                    }
                }

                updateJoinButton()
            }
        }

        edittextPasswordCheck.setOnFocusChangeListener { _, hasFocus ->
            textviewCheckPasswordMessage.visibility = View.INVISIBLE

            if (hasFocus) {
                edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_focused)

            } else {
                val pw = edittextPassword.text.toString()
                val pwCheck = edittextPasswordCheck.text.toString()

                if (pwCheck.isEmpty() || pw == pwCheck) { // 비어있는 경우, 입력한 pw와 동일한 경우
                    edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_normal)

                } else { // 입력한 pw와 동일하지 않는 경우
                    edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewCheckPasswordMessage.visibility = View.VISIBLE
                }

                updateJoinButton()
            }
        }
    }

    private fun updateJoinButton() = with(binding) {
        val password = edittextPassword.text.toString()
        val passwordCheck = edittextPasswordCheck.text.toString()

        buttonJoin.isEnabled = (validNickname && RegexUtil.isValidPwFormat(password) && (password == passwordCheck))
    }

    private fun setCheckedChangeListener() = with(binding.layoutAlert) {
        setUpAgreeTermsCheckedChangeListener()
        setUpUseTermsCheckedChangeListener()
        setUpPromoteTermsCheckedChangeListener()
    }

    private fun setUpAgreeTermsCheckedChangeListener() = with(binding.layoutAlert) {
        checkboxAgreeTerms.setOnCheckedChangeListener { _, isChecked ->
            checkboxUseTerms.isChecked = isChecked
            checkboxPromoteTerms.isChecked = isChecked
        }
    }

    private fun setUpUseTermsCheckedChangeListener() = with(binding.layoutAlert) {
        checkboxUseTerms.setOnCheckedChangeListener { _, isChecked ->
            buttonStart.isEnabled = isChecked
            updateAllCheckStatus()
        }
    }

    private fun setUpPromoteTermsCheckedChangeListener() = with(binding.layoutAlert) {
        checkboxPromoteTerms.setOnCheckedChangeListener { _, _ -> updateAllCheckStatus() }
    }

    private fun updateAllCheckStatus() = with(binding.layoutAlert) {
        checkboxAgreeTerms.setOnCheckedChangeListener(null)
        checkboxAgreeTerms.isChecked = checkboxUseTerms.isChecked && checkboxPromoteTerms.isChecked

        setUpAgreeTermsCheckedChangeListener()
    }
}