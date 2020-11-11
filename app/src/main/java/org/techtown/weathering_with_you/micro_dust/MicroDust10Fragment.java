package org.techtown.weathering_with_you.micro_dust;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.weathering_with_you.R;

public class MicroDust10Fragment extends Fragment {

    private int microDustGradeImage;
    private int microDustGrade;

    public static MicroDust10Fragment newInstance(int image, int grade) {
        Bundle args = new Bundle();
        args.putInt("image", image);
        args.putInt("grade", grade);

        MicroDust10Fragment fragment = new MicroDust10Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        microDustGradeImage = getArguments().getInt("image", 0);
        microDustGrade = getArguments().getInt("grade", 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_micro10, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView micro10GradeImageView = view.findViewById(R.id.micro10GradeImageView);
        micro10GradeImageView.setImageResource(microDustGradeImage);

        TextView micro10GradeTextView = view.findViewById(R.id.micro10GradeTextView);
        micro10GradeTextView.setText( "현재 "+"미세먼지 "+getGradeText(microDustGrade));
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private String getGradeText(int grade){

        String text = "";
        switch (grade){
            case 1:
                text = "좋음";
                break;
            case 2:
                text = "보통";
                break;
            case 3:
                text = "나쁨";
                break;
            case 4:
                text = "매무나쁨";
                break;
            default:
                text = "위험함 정보가 없음";
                break;
        }

        return text;
    }
}
