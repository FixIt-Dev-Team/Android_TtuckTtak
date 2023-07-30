package com.gachon.ttuckttak.ui.join

import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityJoinPart3Binding

class JoinPart3Activity : BaseActivity<ActivityJoinPart3Binding>(ActivityJoinPart3Binding::inflate) {
    override fun initAfterBinding() = with(binding) {
        // 가입하기 버튼 비활성화
        buttonJoin.setClickable(false)
        // 비밀번호 입력창 비활성화
        edittextPassword.setClickable(false)
        // 비밀번호 확인 입력창 비활성화
        edittextPasswordCheck.setClickable(false)

        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            // 이전 화면으로 이동
            startActivityWithClear(JoinPart2Activity::class.java)
        }

        // 닉네임 입력창을 눌렀을 경우 -- textbox 색 변경하기
        edittextName.setBackgroundResource(R.drawable.box_input_text)
        // 입력받은 닉네임 값 받기
        val nikname = edittextName.text.toString()

        // 닉네임 중복, 적합 여부 확인 -- 임시설정
        // 닉네임이 중복이거나 부적합한 경우
        if(true) {
            // 닉네임 입력 textbox 색 변경
            edittextName.setBackgroundResource(R.drawable.box_error_text)

            // error message visible -- 임시설정
            // 중복된 코드 입력 시
            if(true) { textviewOverlapName.visibility = View.VISIBLE }
            // 부적합한 코드 입력 시
            else { textviewInvalidName.visibility = View.VISIBLE }
        }
        else {
            // 닉네임이 적합하는 경우 -- textbox 색 변경하기
            edittextName.setBackgroundResource(R.drawable.box_input_text)
            // 비밀번호 입력창 활성화
            edittextPassword.setClickable(true)
            // 비밀번호 입력창을 누르는 경우
            edittextPassword.setBackgroundResource(R.drawable.box_input_text)

            // 비밀번호 적합 여부 확인 -- 임시설정
            // 적합하는 경우
            /*if(true) {
                // 사용가능한 비밀번호 visible
                textviewPasswordUsable.visibility = View.VISIBLE

                // 비밀번호 확인 입력창 활성화
                layoutPasswordCheck.setClickable(true)

                // 비밀번호 확인 textbox 색 변경
                layoutPasswordCheck.setBackgroundColor(R.drawable.box_input_text)

                // Q. 비밀번호 일치 여부 확인해야 하는지..?


                // 가입하기 버튼 활성화
                buttonJoin.setClickable(true)
                // 가입하기 버튼 색 변경
                buttonJoin.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.main_theme_blue))
                // 가입하기 버튼 글자 색 변경
                buttonJoin.setTextColor(getColor(R.color.white))

                // 가입하기 버튼 누르는 경우
                buttonJoin.setOnClickListener {
                    // 가입하기 버튼 숨기기
                    buttonJoin.visibility = View.VISIBLE
                    // 약관 동의 창 띄우기
                    layoutAlert.visibility = View.VISIBLE
                    // 서비스 약관 동의 -- Q.몰르겟어요
                    radiobuttonAgreeTerms.setOnCheckedChangeListener { buttonView, isChecked -> }

                    // 서비스 이용 약관 text를 눌렀을 경우 -- 해당 액티비티로 이동
                    textviewUseTerms.setOnClickListener {
                        startNextActivity(TermsUseActivity::class.java)
                    }

                    // 서비스 홍보 약관 text를 눌렀을 경우 -- 해당 액티비티로 이동
                    textviewPromoteTerms.setOnClickListener {
                        startNextActivity(TermsPromoteActivity::class.java)
                    }
                }
            }
            else {
                // 비밀번호가 적합하지 않은 경우 -- 비밀번호 적합 조건 visible
                textviewPasswordError.visibility = View.VISIBLE

                // 비밀번호 textbox 색 변경하기
                layoutJoinPassword.setBackgroundResource(R.drawable.box_error_text)
            }*/
        }



    }
}