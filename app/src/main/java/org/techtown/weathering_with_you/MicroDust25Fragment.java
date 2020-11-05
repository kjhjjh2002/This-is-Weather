package org.techtown.weathering_with_you;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MicroDust25Fragment extends Fragment {

    private int microDustGradeImage;
    private int microDustGrade;

    public static MicroDust25Fragment newInstance(int image, int grade) {

        Bundle args = new Bundle();
        args.putInt("image", image);
        args.putInt("grade", grade);

        MicroDust25Fragment fragment = new MicroDust25Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        microDustGradeImage = getArguments().getInt("image", 0);
        microDustGrade = getArguments().getInt("grade", 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_micro25, container, false
        );
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView micro25GradeImageView = view.findViewById(R.id.micro25GradeImageView);
        micro25GradeImageView.setImageResource(microDustGradeImage);

        TextView micro25GradeTextView = view.findViewById(R.id.micro25GradeTextView);
        micro25GradeTextView.setText("현재 "+"초미세먼지 "+getGradeText(microDustGrade));
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
