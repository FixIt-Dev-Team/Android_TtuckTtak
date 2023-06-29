package com.gachon.ttuckttak.ui.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.WebviewBinding

// FixMe: 현재 웹 뷰에 카카오 로그인 화면 띄우는데, 카카오 연동해서 할 수 있으면 해볼 것

class KakaoLoginWebViewActivity : BaseActivity<WebviewBinding>(WebviewBinding::inflate) {

    private val restApiKey = BuildConfig.KAKAO_REST_API_KEY
    private val redirectUri = BuildConfig.KAKAO_REDIRECT_URI

    override fun initAfterBinding() = with(binding) {
        webView.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(MyJavaScriptInterface(), "JSInterface")
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url?.startsWith(redirectUri) == true) {
                        val authCode = Uri.parse(url).getQueryParameter("code")
                        Log.d("AUTH_CODE", "Auth code: $authCode")

                        val resultIntent = Intent().apply {
                            putExtra("authCode", authCode)
                        }

                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
        }

        val authUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=$restApiKey&redirect_uri=$redirectUri"
        Log.i("URL", authUrl)
        webView.loadUrl(authUrl)
    }

    class MyJavaScriptInterface {

        @JavascriptInterface
        fun receiveCode(authCode: String) {
            Log.d("AUTH_CODE", "Auth code: $authCode")
        }
    }
}