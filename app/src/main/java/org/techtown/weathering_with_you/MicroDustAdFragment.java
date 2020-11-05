package org.techtown.weathering_with_you;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.Objects;

public class MicroDustAdFragment extends Fragment {

    AdLoader.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        builder = new AdLoader.Builder(
                Objects.requireNonNull(this.getContext()), "ca-app-pub-3940256099942544/1044960115"
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_microdust_ad, container, false);
        return  rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView templateView = view.findViewById(R.id.microDustFragmentAd);
                templateView.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
