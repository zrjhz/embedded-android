package car.car2024.FragmentView;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import car.car2024.ActivityView.R;
import car.car2024.ViewAdapter.ZigbeeAdapter;
import car.car2024.ViewAdapter.Zigbee_Landmark;

public class RightZigbeeFragment extends Fragment {

    private List<Zigbee_Landmark> ZigbeeList = new ArrayList<Zigbee_Landmark>();
    Context minstance = null;

    // private RightZigbeeFragment(){}

    public static RightZigbeeFragment getInstance() {
        return RightZigbeeHolder.mInstance;
    }

    private static class RightZigbeeHolder {
        private static final RightZigbeeFragment mInstance = new RightZigbeeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        minstance = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_zigbee_fragment, container, false);
        initZigbees();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ZigbeeAdapter adapter = new ZigbeeAdapter(ZigbeeList, getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initZigbees() {
        ZigbeeList.clear();
        Zigbee_Landmark apple = new Zigbee_Landmark("闸门", R.mipmap.barrier_gate);
        ZigbeeList.add(apple);
        Zigbee_Landmark banana = new Zigbee_Landmark("数码管", R.mipmap.nixie_tube);
        ZigbeeList.add(banana);
        Zigbee_Landmark orange = new Zigbee_Landmark("语音播报", R.mipmap.voice_broadcast);
        ZigbeeList.add(orange);
        Zigbee_Landmark watermelon = new Zigbee_Landmark("无线充电", R.mipmap.maglev);
        ZigbeeList.add(watermelon);

        Zigbee_Landmark pear1 = new Zigbee_Landmark("TFT显示器1", R.mipmap.tft_lcd);
        ZigbeeList.add(pear1);

        Zigbee_Landmark pear2 = new Zigbee_Landmark("TFT显示器2", R.mipmap.tft_lcd);
        ZigbeeList.add(pear2);


        Zigbee_Landmark stereo_garage = new Zigbee_Landmark("立体车库", R.mipmap.garage);
        ZigbeeList.add(stereo_garage);
        Zigbee_Landmark traffic_light = new Zigbee_Landmark("智能交通灯", R.mipmap.traffic_light);
        ZigbeeList.add(traffic_light);
    }

}
