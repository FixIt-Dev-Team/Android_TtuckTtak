package com.gachon.ttuckttak.ui.setting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsProfileBinding
import kotlinx.coroutines.launch
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.ui.login.ResetPwActivity
import dagger.hilt.android.AndroidEntryPoint

import com.gachon.ttuckttak.ui.setting.SettingsProfileViewmodel.NavigateTo.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class SettingsProfileActivity : BaseActivity<ActivitySettingsProfileBinding>(
    ActivitySettingsProfileBinding::inflate,
    TransitionMode.HORIZONTAL
) {

    private val viewModel: SettingsProfileViewmodel by viewModels()

    private val permission = Manifest.permission.READ_MEDIA_IMAGES

    private var isLayoutVisible = false // layout alert 화면이 현재 보여지고 있는지

    override fun initAfterBinding() = with(binding) {
        binding.viewmodel = viewModel
        binding.layoutAlert.viewmodel = viewModel
        setObservers()
        setClickListener()
        setFocusListener()
        setTouchListener()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@SettingsProfileActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Before -> finish()
                    is ResetPw -> startNextActivity(ResetPwActivity::class.java)
                }
            }
        }

        viewModel.profile.observe(this@SettingsProfileActivity) { profile ->
            if (profile != null) {
                if (profile.userName != null) {
                    binding.edittextNickname.setText(profile.userName)
                }

                if (profile.email != null) {
                    binding.textviewUserEmail.text = profile.email
                }

                if (profile.profileImgUrl != null) { // 사용자의 프로필 이미지가 있는 경우 Glide를 이용해 프로필 이미지를 설정한다
                    Glide.with(this@SettingsProfileActivity)
                        .load(profile.profileImgUrl) // 사용자의 프로필 이미지를 Load
                        .placeholder(R.drawable.img_profile) // 사용자의 프로필 로딩을 시작하기 전에 보여줄 이미지 설정
                        .error(R.drawable.img_profile) // 리소스를 불러오다가 에러가 발생했을 때 보여줄 이미지를 설정
                        .fallback(R.drawable.img_profile) // Load할 url이 null인 경우 등 비어있을 때 보여줄 이미지를 설정
                        .into(binding.imageviewProfile)
                }
            }

            // Todo: 첫 로그인 + 네트워크 문제로 사용자의 프로필을 불러올 수 없을 때 어떻게 화면에 보여줄지 기획에 물어볼 것
            //  즉 profile == null일 때 어떻게 처리할 것인지
        }

        viewModel.nicknameErrorMessage.observe(this@SettingsProfileActivity) { message ->
            binding.layoutNickname.setBackgroundResource(R.drawable.textbox_state_error)
            binding.textviewNicknameErrorMessage.visibility = View.VISIBLE
            binding.textviewNicknameErrorMessage.text = message
        }

        viewModel.changeDetected.observe(this@SettingsProfileActivity) { value ->
            binding.imagebuttonSave.isEnabled = value
        }

        // 일회성 show toast
        lifecycleScope.launch {
            viewModel.showToastEvent.collect { message ->
                showToast(message)
            }
        }
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            if (viewModel.changeDetected.value == true) { // 변경 사항이 존재한다면
                showLayout() // 변경하지 않고 나가겠냐는 화면을 보여주고

            } else { // 변경 사항이 존재하지 않는다면
                finish() // 해당 화면 종료
            }
        }

        // 프로필 사진 변경을 클릭하는 경우
        layoutProfile.setOnClickListener {
            changeImage.launch(permission)
        }
    }

    private fun setFocusListener() = with(binding) {
        edittextNickname.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                layoutNickname.setBackgroundResource(R.drawable.textbox_state_normal)
                textviewNicknameErrorMessage.visibility = View.INVISIBLE

            } else {
                val nickname = edittextNickname.text.toString()
                viewModel.checkNicknameAvailable(nickname)
            }
        }
    }

    private val changeImage =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) { // 권한이 주어지지 않은 경우
                showToast("프로필 이미지 변경을 위해선 갤러리 권한이 필요합니다.")

            } else { // 권한이 주어진 경우
                val pick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                readImage.launch(pick)
            }
        }

    private val readImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imgUri = it.data?.data

                binding.imageviewProfile.setImageURI(imgUri)
                viewModel.setNewProfileImg(getImageFromUri(imgUri!!))
            }
        }

    private fun getImageFromUri(uri: Uri): MultipartBody.Part {
        val path = getRealPathFromURI(uri)
        val file = File(path)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", "newProfileImg", requestFile)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        cursor.close()

        return path
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

}