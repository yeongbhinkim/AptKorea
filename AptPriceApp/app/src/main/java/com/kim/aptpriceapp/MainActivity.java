package com.kim.aptpriceapp;

import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.http.SslError;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. WebView 초기화
        mWebView = findViewById(R.id.wvLayout);
        mWebSettings = mWebView.getSettings();

        // 2. WebView 설정
        mWebSettings.setJavaScriptEnabled(true); // JavaScript 사용 허용
        mWebSettings.setLoadWithOverviewMode(true); // 콘텐츠가 화면 크기에 맞게 조정되도록 설정
        mWebSettings.setUseWideViewPort(true); // Viewport 메타태그 사용 허용
        mWebSettings.setSupportZoom(true); // 줌 기능 사용 허용
        mWebSettings.setBuiltInZoomControls(true); // 화면 확대/축소 컨트롤 제공
        mWebSettings.setDisplayZoomControls(false); // 화면 확대/축소 버튼 비활성화 (터치로 확대 가능)
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 캐시 사용 안함
        mWebSettings.setAllowFileAccess(true); // 파일 접근 허용
        mWebSettings.setAllowContentAccess(true); // Content URL에 접근 허용
        mWebSettings.setDomStorageEnabled(true);  // LocalStorage 활성화


        // 3. WebViewClient 설정 (네트워크 및 SSL 오류 처리)
        mWebView.setWebViewClient(new WebViewClient() {
            // 페이지 로딩 중 오류가 발생했을 때
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(MainActivity.this, "페이지 로딩 오류: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                view.loadData("<html><body><h2>네트워크 오류가 발생했습니다.</h2></body></html>", "text/html", "UTF-8");
            }

            // SSL 인증서 오류 발생 시 처리
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // SSL 오류를 무시하고 계속 진행 (테스트 목적으로만 사용)
                // 실제 배포 시에는 이 코드를 제거하고, 사용자에게 오류를 알리는 것이 좋습니다.
            }
        });

        // 4. URL 로드 (앱 내에서 열도록 설정)
        mWebView.loadUrl("https://aptkorea.store/");
    }
}
