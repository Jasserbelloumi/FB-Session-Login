package com.jsr.fblogin;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private EditText cookieInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        cookieInput = findViewById(R.id.cookie_input);
        Button btnLogin = findViewById(R.id.btn_login);

        // إعدادات المتصفح ليعمل مثل الموبايل الحقيقي
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36");
        
        webView.setWebViewClient(new WebViewClient());

        btnLogin.setOnClickListener(v -> {
            String rawCookies = cookieInput.getText().toString().trim();
            if (!rawCookies.isEmpty()) {
                injectCookiesAndLoad(rawCookies);
            } else {
                Toast.makeText(this, "Please paste your cookies!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void injectCookiesAndLoad(String cookieString) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookies(null);

        // تقسيم الكوكيز وحقنها في نطاق فيسبوك
        String[] cookieArray = cookieString.split(";");
        for (String cookie : cookieArray) {
            cookieManager.setCookie("https://www.facebook.com", cookie.trim());
        }

        cookieManager.flush();
        webView.loadUrl("https://m.facebook.com");
        Toast.makeText(this, "Session Injected! Loading...", Toast.LENGTH_LONG).show();
    }
}
