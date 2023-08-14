package com.gachon.ttuckttak.ui.setting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.databinding.ActivitySettingsProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.ProfileDto
import com.gachon.ttuckttak.data.remote.dto.UserInfoRes
import com.gachon.ttuckttak.ui.login.ResetPwActivity
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception

class SettingsProfileActivity : BaseActivity<ActivitySettingsProfileBinding>(ActivitySettingsProfileBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this@SettingsProfileActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@SettingsProfileActivity) }

    private val permission = Manifest.permission.READ_MEDIA_IMAGES

    private var newNickname = false
    private var newImage = false


    override fun initAfterBinding() = with(binding) {
        // 서버로부터 사용자 정보 갱신
        edittextNickname.setText(userManager.getUserName())
        textviewUserEmail.text = userManager.getUserMail()
        if (userManager.getUserImageUrl().isNullOrEmpty()) {
            imageProfile.setImageDrawable(AppCompatResources.getDrawable(this@SettingsProfileActivity, R.drawable.img_profile))
        } else {
            Glide.with(this@SettingsProfileActivity)
                .load(userManager.getUserImageUrl())
                .into(imageProfile)
        }

        setClickListener()
        setFocusListener()
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 누르는 경우
        buttonBack.setOnClickListener {
            // 저장 필요한 상태
            if (newNickname || newImage) {
                mainFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
            } else {
                // 저장 필요하지 않은 상태
                finish()
            }
        }

        // 팝업 뒤로 가기 버튼
        buttonBackTrue.setOnClickListener {
            // getProfile(userManager.getUserIdx()!!, tokenManager.getAccessToken()!!)
            finish()
        }

        // 저장 버튼
        imagebuttonSave.setOnClickListener {
            if (newImage) {
                // 이미지 갱신 있는 경우
                // TODO("이미지 리사이징 및 사진 업로드 문제 확인")
                updateProfile(tokenManager.getAccessToken()!!)
                newImage = false
                newNickname = false
            } else if (newNickname) {
                // 닉네임만 갱신하는 경우
                updateNickname(tokenManager.getAccessToken()!!)
                newNickname = false
            }
        }

        // 프로필 사진 변경 버튼을 클릭하는 경우
        buttonProfilePlus.setOnClickListener {
            changeImage.launch(permission)
        }

        // 비밀번호 재설정 버튼
        buttonPasswordReset.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = TtukttakServer.changePw(userManager.getUserMail()!!)

                    withContext(Dispatchers.Main) {
                        // 전송 성공
                        if (response.isSuccess) {
                            val data = response.data!!
                            Log.i(TAG, "이메일 전송: ${data.sendSuccess}")

                            // 비밀번호 재설정 activity 실행
                            val intent = Intent(this@SettingsProfileActivity, ResetPwActivity::class.java)
                            intent.putExtra("email", userManager.getUserMail())
                            startActivity(intent)
                        } else {
                            Log.e(TAG, "비밀번호 재설정 실패")
                            Log.e(TAG, "${response.code} ${response.message}")
                            showToast("비밀번호 재설정 실패")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e(TAG, "서버 통신 오류: ${e.message}")
                        showToast("비밀번호 재설정 실패")
                    }
                }
            }
        }
    }

    private fun setFocusListener() = with(binding) {
        edittextNickname.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                layoutNickname.setBackgroundResource(R.drawable.textbox_state_normal)
                textviewOverlapNickname.visibility = View.INVISIBLE
            }
            else {
                val nickname = edittextNickname.text.toString()

                // 닉네임이 기준에 맞지 않는 경우
                if(nickname.length < 4 || nickname.length > 12){
                    layoutNickname.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewOverlapNickname.visibility = View.VISIBLE
                    textviewOverlapNickname.text = getString(R.string.invalid_nickname)
                }
                else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val response = TtukttakServer.checkNickname(nickname)
                            Log.i("response", response.toString())

                            if (response.isSuccess) {
                                // 닉네임이 중복되는 경우
                                if(!response.data!!.isAvailable) {
                                    layoutNickname.setBackgroundResource(R.drawable.textbox_state_error)
                                    textviewOverlapNickname.visibility = View.VISIBLE
                                    textviewOverlapNickname.text = getString(R.string.overlap_nickname)
                                }

                                // 닉네임이 적절한 경우
                                newNickname = true
                                userManager.saveUserName(nickname)
                            } else {
                                showToast("DB Error")
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Log.e("error", "서버 통신 오류: ${e.message}")
                                showToast("닉네임 중복 확인 실패")
                            }
                        }
                    }
                }

            }
        }
    }

    private val readImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val imgUri = data?.data

            binding.imageProfile.setImageURI(imgUri)

            newImage = true
            userManager.saveUserImagePath(getRealPathFromURI(imgUri!!))
        }
    }
    private val changeImage = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        // 권한이 주어지지 않은 경우
        if (!isGranted) {
            showToast("프로필 이미지 변경을 위해선 갤러리 권한이 필요합니다.")
            finish()
        }
        // 권한이 주어진 경우
        val pick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        readImage.launch(pick)
    }
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        cursor.close()

        return path
    }


//    private fun getProfile(userId: String, token: String) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val response = TtukttakServer.getUserInfo(userId, token)
//
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccess) {
//                        val data = response.data!!
//                        Log.i(TAG, "userName: ${data.userName}")
//                        Log.i(TAG, "userMail: ${data.email}")
//                        Log.i(TAG, "userImgUrl: ${data.profileImgUrl}")
//                        Log.i(TAG, "accountType: ${data.accountType}")
//                        saveProfile(data)
//                    } else {
//                        Log.e(TAG, "유저 정보 취득 실패")
//                        Log.e(TAG, "${response.code} ${response.message}")
//                        showToast("유저 정보 취득 실패")
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Log.e(TAG, "서버 통신 오류: ${e.message}")
//                    showToast("유저 정보 취득 실패")
//                }
//            }
//        }
//    }
//
//    private fun saveProfile(data: UserInfoRes) {
//        userManager.saveUserName(data.userName)
//        userManager.saveUserMail(data.email)
//        userManager.saveUserImageUrl(data.profileImgUrl)
//    }

    private fun updateNickname(token: String) = with(binding) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = TtukttakServer.updateUserInfo(authCode = token,
                    reqDto = ProfileDto(userManager.getUserIdx()!!, userManager.getUserName()!!, userManager.getUserImageUrl()!!),
                    file = null)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!
                        Log.i(TAG, "Upadated: ${data.isSuccess}")
                        imagebuttonSave.setColorFilter(ContextCompat.getColor(this@SettingsProfileActivity, R.color.main_theme_blue))
                    } else {
                        Log.e(TAG, "유저 정보 업데이트 실패")
                        Log.e(TAG, "${response.code} ${response.message}")
                        showToast("유저 정보 업데이트 실패")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "서버 통신 오류: ${e.message}")
                    showToast("유저 정보 업데이트 실패")
                }
            }
        }
    }
    private fun updateProfile(token: String) = with(binding) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val file = File(userManager.getUserImagePath()!!)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val response = TtukttakServer.updateUserInfo(authCode = token,
                    reqDto = ProfileDto(userManager.getUserIdx()!!, userManager.getUserName()!!, userManager.getUserImageUrl()!!),
                    file = MultipartBody.Part.create(requestFile))

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!
                        Log.i(TAG, "Upadated: ${data.isSuccess}")
                        imagebuttonSave.setColorFilter(ContextCompat.getColor(this@SettingsProfileActivity, R.color.main_theme_blue))
                    } else {
                        Log.e(TAG, "유저 정보 업데이트 실패")
                        Log.e(TAG, "${response.code} ${response.message}")
                        showToast("유저 정보 업데이트 실패")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "서버 통신 오류: ${e.message}")
                    showToast("유저 정보 업데이트 실패")
                }
            }
        }
    }

    companion object {
        const val TAG = "PROFILE"
    }
}