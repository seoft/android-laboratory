package kr.co.seoft.webview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val webView by lazy { findViewById<WebView>(R.id.webView) }

    private val bt1 by lazy { findViewById<Button>(R.id.bt1) }
    private val bt2 by lazy { findViewById<Button>(R.id.bt2) }
    private val bt3 by lazy { findViewById<Button>(R.id.bt3) }
    private val bt4 by lazy { findViewById<Button>(R.id.bt4) }
    private val bt5 by lazy { findViewById<Button>(R.id.bt5) }

    private val printText by lazy { findViewById<TextView>(R.id.printText) }

    private val url = "file:///android_asset/sample.html"
    private val initState = """{
    "isWriter": false,
    "inApp": true,
    "language": "ko"
}
""".trimIndent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWebView()
        initListener()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
        }

        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(WebAppInterface(this), "AppBridge")
        webView.loadUrl(url)
    }

    private fun initListener() {
        bt1.setOnClickListener {
            webView.loadUrl("javascript:initState('$initState')")
        }
        bt2.setOnClickListener {
            webView.loadUrl("javascript:onBackFromApp()")
        }
        bt3.setOnClickListener { }
        bt4.setOnClickListener { }
        bt5.setOnClickListener { }
    }


    inner class WebAppInterface(private val mContext: Context) {

        @JavascriptInterface
        fun onShowToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }

        @JavascriptInterface
        fun onPageChanged(page: String) {
            printText.text = "onPageChanged : [$page]"
        }

    }

}