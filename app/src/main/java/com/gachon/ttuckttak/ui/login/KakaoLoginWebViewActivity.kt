package com.gachon.ttuckttak.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.WebviewBinding

// FixMe: 현재 웹 뷰에 카카오 로그인 화면 띄우는데, 카카오 연동해서 할 수 있으면 해볼 것
//  근데 카카오 연동하면 access token 받을 것 같은데 이 부분 고민해볼 것

class KakaoLoginWebViewActivity : BaseActivity<WebviewBinding>(WebviewBinding::inflate) {

    private val restApiKey = BuildConfig.KAKAO_REST_API_KEY
    private val redirectUri = BuildConfig.KAKAO_REDIRECT_URI

    @SuppressLint("SetJavaScriptEnabled")
    override fun initAfterBinding() = with(binding) {
        setupWebView(webView)
        loadAuthUrl(webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavaScriptInterface(), "JSInterface")
        webView.webViewClient = createWebViewClient()
    }

    private fun createWebViewClient() = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return request.url?.let { handleUrlOverride(it.toString()) } ?: false
        }

        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return handleUrlOverride(url)
        }
    }

    private fun handleUrlOverride(url: String): Boolean {
        return if (url.startsWith(redirectUri)) {
            val authCode = Uri.parse(url).getQueryParameter("code")
            Log.d("AUTH_CODE", "Auth code: $authCode")
            returnResult(authCode)
            true
        } else {
            false
        }
    }

    private fun returnResult(authCode: String?) {
        val resultIntent = Intent().apply {
            putExtra("authCode", authCode)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun loadAuthUrl(webView: WebView) {
        val authUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=$restApiKey&redirect_uri=$redirectUri"
        Log.i("URL", authUrl)
        webView.loadUrl(authUrl)
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @Suppress("unused")
        fun receiveCode(authCode: String) {
            Log.d("AUTH_CODE", "Auth code: $authCode")
        }
    }
}