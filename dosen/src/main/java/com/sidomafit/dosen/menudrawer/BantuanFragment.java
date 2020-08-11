package com.sidomafit.dosen.menudrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sidomafit.dosen.MainActivity;
import com.sidomafit.dosen.R;

public class BantuanFragment extends Fragment {

    private WebView bantuan;
    private String URL;

    public BantuanFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bantuan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_bantuan);

        URL = "https://sidoma-dosen.web.app/";
        bantuan = view.findViewById(R.id.webView);
        bantuan.getSettings().setJavaScriptEnabled(true);
        bantuan.loadUrl(URL);
        bantuan.setWebViewClient(new WebViewClient());
    }
}
