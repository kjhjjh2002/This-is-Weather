package org.techtown.weathering_with_you.micro_dust;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MicroDustFragmentAdapter extends FragmentStateAdapter {

    public int count;

    int micro10Image;
    int micro10Grade;

    int micro25Image;
    int micro25Grade;

    public MicroDustFragmentAdapter(FragmentActivity fragmentActivity, int count,
                                    int micro10Image, int micro10Grade,
                                    int micro25Image, int micro25Grade){
        super(fragmentActivity);

        this.count = count;

        this.micro10Image = micro10Image;
        this.micro10Grade = micro10Grade;

        this.micro25Image = micro25Image;
        this.micro25Grade = micro25Grade;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        // 좌우로 슬라이드 되는 뷰페이저
        if (index == 0)
            return MicroDust25Fragment.newInstance(micro10Image, micro10Grade);
        else if (index == 1)
            return MicroDust10Fragment.newInstance(micro25Image, micro25Grade);
        else return new MicroDustAdFragment();
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position){
        return position % count;
    }
}
