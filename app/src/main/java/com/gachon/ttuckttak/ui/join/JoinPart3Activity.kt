package com.gachon.ttuckttak.ui.join

import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityJoinPart3Binding
import com.gachon.ttuckttak.utils.RegexUtil


class JoinPart3Activity : BaseActivity<ActivityJoinPart3Binding>(ActivityJoinPart3Binding::inflate) {
    override fun initAfterBinding() = with(binding) {

        // 뒤로가기 버튼을 눌렀을 경우
        imagebuttonBack.setOnClickListener {
            finish()
        }
        
        edittextName.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    edittextName.setBackgroundResource(R.drawable.textbox_state_focused)
                } else {
                    val nickname = edittextName.text.toString()

                    if (RegexUtil.isValidNickname(nickname)) {
                        edittextName.setBackgroundResource(R.drawable.textbox_state_normal)
                    } else {
                        edittextName.setBackgroundResource(R.drawable.textbox_state_error)
                    }
                }
            }
        })

        edittextPassword.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    edittextPassword.setBackgroundResource(R.drawable.textbox_state_focused)
                    textviewPasswordError.visibility = View.INVISIBLE
                    textviewPasswordRule.visibility = View.INVISIBLE
                } else {
                    val password = edittextPassword.text.toString()

                    if (RegexUtil.isValidPw(password)) {
                        edittextPassword.setBackgroundResource(R.drawable.textbox_state_normal)
                        textviewPasswordError.visibility = View.INVISIBLE
                        textviewPasswordUsable.visibility = View.VISIBLE
                    } else {
                        edittextPassword.setBackgroundResource(R.drawable.textbox_state_error)
                        textviewPasswordError.visibility = View.VISIBLE
                    }
                }
            }
        })


        edittextPasswordCheck.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_focused)
                    textviewPasswordDiscordance.visibility = View.INVISIBLE
                } else {
                    val pwCheck = edittextPasswordCheck.text.toString()
                    val pw = edittextPassword.text.toString()

                    if (pw.equals(pwCheck)) {
                        edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_normal)
                        textviewPasswordDiscordance.visibility = View.INVISIBLE
                    } else {
                        edittextPasswordCheck.setBackgroundResource(R.drawable.textbox_state_error)
                        textviewPasswordDiscordance.visibility = View.VISIBLE
                    }
                }
            }
        })

        // 가입하기 버튼을 클릭한 경우
        buttonJoin.setOnClickListener {

        }
    }
}