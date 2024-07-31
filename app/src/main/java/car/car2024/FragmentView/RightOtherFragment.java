package car.car2024.FragmentView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import car.car2024.ActivityView.R;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import car.car2024.ViewAdapter.OtherAdapter;
import car.car2024.ViewAdapter.Other_Landmark;

public class RightOtherFragment extends Fragment {
    private final List<Other_Landmark> otherList = new ArrayList<>();
    Context minstance = null;

    public static RightOtherFragment getInstance() {
        return RightZigbeeHolder.mInstance;
    }

    private static class RightZigbeeHolder {
        @SuppressLint("StaticFieldLeak")
        private static final RightOtherFragment mInstance = new RightOtherFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        minstance = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_other_fragment, container, false);
        initFruits();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        OtherAdapter adapter = new OtherAdapter(otherList, getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initFruits() {
        otherList.clear();

        Other_Landmark setApple = new Other_Landmark("设置预设位", R.mipmap.default_position);
        otherList.add(setApple);

        Other_Landmark callApple = new Other_Landmark("调用预设位", R.mipmap.default_position);
        otherList.add(callApple);

        Other_Landmark setCameraTime = new Other_Landmark("设置预设位转动次数", R.mipmap.default_position);
        otherList.add(setCameraTime);

        Other_Landmark setBrightness = new Other_Landmark("设置亮度", R.mipmap.tft_lcd);
        otherList.add(setBrightness);

        Other_Landmark setContrast = new Other_Landmark("设置对比度", R.mipmap.tft_lcd);
        otherList.add(setContrast);

        Other_Landmark setSaturability = new Other_Landmark("设置饱和度", R.mipmap.tft_lcd);
        otherList.add(setSaturability);

        Other_Landmark setChroma = new Other_Landmark("设置色度", R.mipmap.tft_lcd);
        otherList.add(setChroma);

        Other_Landmark cameraParameter = new Other_Landmark("预设", R.mipmap.tft_lcd);
        otherList.add(cameraParameter);
    }
}

