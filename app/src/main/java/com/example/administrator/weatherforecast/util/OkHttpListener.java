package com.example.administrator.weatherforecast.util;

import android.view.View;

public interface OkHttpListener {
    public void OnSuccess(String response);
    public void OnFail();
}
