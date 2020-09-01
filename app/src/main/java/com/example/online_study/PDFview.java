package com.example.online_study;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class PDFview extends AppCompatActivity {
    WebView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_fview);
        pdfView = (WebView) findViewById(R.id.web);
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.setWebViewClient(new WebViewClient());

        if(Courses.y==1)
        {
            pdfView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=https://www.tutorialspoint.com/cplusplus/cpp_tutorial.pdf");

        }
        if(Courses.y==2)
        {
            pdfView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=https://www.tutorialspoint.com/java/java_tutorial.pdf");

        }
    }
}