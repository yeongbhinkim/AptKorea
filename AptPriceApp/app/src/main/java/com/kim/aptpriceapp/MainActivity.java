package com.kim.aptpriceapp;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.wvLayout);
        mWebView.loadUrl("http://10.0.2.2:9080"); //연결할 url

//        mWebView.loadUrl("https://www.naver.com/"); //연결할 url

        mWebView.setWebViewClient(new WebViewClient()); // 현재 앱을 나가서 새로운 브라우저를 열지 않도록 함.

        mWebSettings = mWebView.getSettings(); // 웹뷰에서 webSettings를 사용할 수 있도록 함.
        mWebSettings.setJavaScriptEnabled(true); //웹뷰에서 javascript를 사용하도록 설정
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); //멀티윈도우 띄우는 것
        mWebSettings.setAllowFileAccess(true); //파일 엑세스
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그
        mWebSettings.setUseWideViewPort(true); //화면 사이즈 맞추기
        mWebSettings.setSupportZoom(true); // 화면 줌 사용 여부
        mWebSettings.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
        mWebSettings.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
        mWebSettings.setDefaultFixedFontSize(14); //기본 고정 글꼴 크기, value : 1~72 사이의 숫자
        mWebSettings.setAllowContentAccess(true);//웹뷰를 통해 Content URL 에 접근 사용 여부


    }
}