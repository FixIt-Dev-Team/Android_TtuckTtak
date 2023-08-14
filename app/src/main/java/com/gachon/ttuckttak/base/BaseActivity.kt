package com.gachon.ttuckttak.base

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gachon.ttuckttak.R

// AppCompatActivity를 상속받아 Activity 역할을 하는 가장 기본적인 Activity
// 자동적으로 뷰 바인딩을 해주기 때문에 BaseActivity를 상속받아 필요한 것들만 넘겨주면 자동적으로 onCreate() 수행되면서 뷰 바인딩이 된다.
abstract class BaseActivity<T : ViewBinding>(
    private val inflate: (LayoutInflater) -> T,
    private val transitionMode: TransitionMode = TransitionMode.HORIZONTAL
) : AppCompatActivity() {

    protected lateinit var binding: T
        private set

    private var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?

        when (transitionMode) {
            TransitionMode.HORIZONTAL -> overridePendingTransition(R.anim.slide_horizontal_enter, R.anim.none)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.slide_vertical_enter, R.anim.none)
            else -> Unit
        }

        initAfterBinding()
    }

    // 뷰 바인딩이 끝나고 호출되는 추상 메소드
    // BaseActivity를 상속받은 activity는 이 메소드를 정의해줌으로써 자동적으로 실행되게 해준다.
    protected abstract fun initAfterBinding()

    // Toast message를 message만 넣어서 불러오도록
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 이전 Activity는 남기고, 다음 activity를 켜주는 역할
    fun startNextActivity(activity: Class<*>?) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    // 이전 Activity를 모두 날려주고, 다음 activity를 켜주는 역할
    fun startActivityWithClear(activity: Class<*>?) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun finish() {
        super.finish()
        when (transitionMode) {
            TransitionMode.HORIZONTAL -> overridePendingTransition(R.anim.none, R.anim.slide_horizontal_exit)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.slide_vertical_exit)
            else -> Unit
        }
    }

    // 키보드 숨기기
    // 디바이스에서 키보드가 뜨는 것을 자동적으로 숨겨주는 역할
    private fun hideKeyboard(v: View) {
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }

    // 화면 클릭하여 키보드 숨기기 및 포커스 제거
    // 터치 이벤트를 처리하는 함수 override
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (it.action == MotionEvent.ACTION_DOWN) { // 손가락이 화면에 닿았을 때의 액션인 경우
                val currentView = currentFocus
                if (currentView is EditText) { // 현재 포커스가 있는 뷰가 EditText일 경우
                    handleFocusChangeForEditText(currentView, it) // EditText 외부를 터치했을 때 포커스 처리
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    // EditText 외부를 터치했을 때 포커스를 변경하고 키보드를 숨기는 method
    private fun handleFocusChangeForEditText(view: EditText, motionEvent: MotionEvent) {
        val viewRect = Rect()
        view.getGlobalVisibleRect(viewRect)

        // EditText 외부 영역을 터치한 경우 포커스 제거 및 소프트 키보드 숨기기
        if (!viewRect.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
            hideKeyboardAndClearFocus(view)
        }
    }

    // 포커스 제거 및 소프트 키보드 숨기는 method
    private fun hideKeyboardAndClearFocus(view: EditText) {
        view.clearFocus()
        hideKeyboard(view)
    }

    enum class TransitionMode {
        NONE, HORIZONTAL, VERTICAL
    }
}