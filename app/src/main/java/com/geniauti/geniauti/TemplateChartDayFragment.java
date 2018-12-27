package com.geniauti.geniauti;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geniauti.geniauti.compactcalendarview.domain.Event;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplateChartDayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateChartDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateChartDayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    private SimpleDateFormat sdf, sdfTime;
    private Calendar cal;
    private String DateandTime;

    private OnFragmentInteractionListener mListener;
    private BarChart chartFrequency;
    private HorizontalBarChart chartReasons, chartTypes, chartLocations;
    private ArrayList<String> xLabelsReasons, xLabelsTypes, xLabelsLocations;

    private List<BarEntry> yFrequency = new ArrayList<>();
    private List<BarEntry> yReasons = new ArrayList<>();
    private List<BarEntry> yTypes = new ArrayList<>();
    private List<BarEntry> yLocations = new ArrayList<>();

    private int hour0 = 0, hour1 = 0, hour2 = 0, hour3 = 0, hour4 = 0, hour5 = 0, hour6 = 0, hour7 = 0, hour8 = 0, hour9 = 0, hour10 = 0, hour11 = 0, hour12 = 0, hour13 = 0, hour14 = 0, hour15 = 0, hour16 = 0, hour17 = 0, hour18 = 0, hour19 = 0, hour20 = 0, hour21 = 0, hour22 = 0, hour23 = 0;
    private int intensity0 = 0, intensity1 = 0, intensity2 = 0, intensity3 = 0, intensity4 = 0, intensity5 = 0, intensity6 = 0, intensity7 = 0, intensity8 = 0, intensity9 = 0, intensity10 = 0, intensity11 = 0, intensity12 = 0, intensity13 = 0, intensity14 = 0, intensity15 = 0, intensity16 = 0, intensity17 = 0, intensity18 = 0, intensity19 = 0, intensity20 = 0, intensity21 = 0, intensity22 = 0, intensity23 = 0;
    private int interest = 0, demand = 0, selfstimulation = 0, taskevation = 0, reasonEtc = 0;
    private int selfharm = 0, harm = 0, destruction = 0, breakaway = 0, sexual = 0, typeEtc = 0;
    private int home = 0, mart = 0, restaurant = 0, school = 0, locationEtc = 0;
    private int dayNumber = 0, dayIntensity = 0;
    private double dayTime = 0.0;
    private String intensity;

    TextView mDayNumber;
    TextView mDayTime;
    TextView mDayIntensity;

    private int colorIntensity1, colorIntensity2, colorIntensity3, colorIntensity4, colorIntensity5;

    public static ArrayList<Behavior> behaviorData;
    public static int positionNum;
    private int getCount = ChartDayFragment.adapter.getCount();

    private int diff;

    public TemplateChartDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment TemplateChartDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateChartDayFragment newInstance(int position, ArrayList<Behavior> behaviors) {
        TemplateChartDayFragment fragment = new TemplateChartDayFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        behaviorData = behaviors;
        positionNum = position;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v != null) {
            if ((ViewGroup) v.getParent() != null)
                ((ViewGroup) v.getParent()).removeView(v);
            return v;
        }

        v = inflater.inflate(R.layout.fragment_template_chart_day, container, false);

        sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN);
        sdfTime = new SimpleDateFormat("aa hh", Locale.KOREAN);
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1*diff);
        DateandTime = sdf.format(cal.getTime());

        colorIntensity5 = Color.parseColor("#2dc76d");
        colorIntensity4 = Color.parseColor("#cc2dc76d");
        colorIntensity3 = Color.parseColor("#992dc76d");
        colorIntensity2 = Color.parseColor("#662dc76d");
        colorIntensity1 = Color.parseColor("#332dc76d");

        // Behavior ArrayList
        for(int i = 0; i < behaviorData.size(); i++) {
            Behavior behavior =  behaviorData.get(i);

            Date startTime = behavior.start_time;

            if(sdf.format(startTime).equals(DateandTime)){
                // frequency

                String sTime = sdfTime.format(startTime);
                int iTime = Integer.parseInt(sTime.substring(3,5));
                if(sTime.substring(0,2).equals("오후")) {
                    if(!sTime.substring(3,5).equals("12")) {
                        iTime = iTime + 12;
                    }
                } else {
                    if(sTime.substring(3,5).equals("12")) {
                        iTime = iTime - 12;
                    }
                }
                switch(iTime) {
                    case 0:
                        hour0 += 1;
                        intensity0 += behavior.intensity;
                        break;
                    case 1:
                        hour1 += 1;
                        intensity1 += behavior.intensity;
                        break;
                    case 2:
                        hour2 += 1;
                        intensity2 += behavior.intensity;
                        break;
                    case 3:
                        hour3 += 1;
                        intensity3 += behavior.intensity;
                        break;
                    case 4:
                        hour4 += 1;
                        intensity4 += behavior.intensity;
                        break;
                    case 5:
                        hour5 += 1;
                        intensity5 += behavior.intensity;
                        break;
                    case 6:
                        hour6 += 1;
                        intensity6 += behavior.intensity;
                        break;
                    case 7:
                        hour7 += 1;
                        intensity7 += behavior.intensity;
                        break;
                    case 8:
                        hour8 += 1;
                        intensity8 += behavior.intensity;
                        break;
                    case 9:
                        hour9 += 1;
                        intensity9 += behavior.intensity;
                        break;
                    case 10:
                        hour10 += 1;
                        intensity10 += behavior.intensity;
                        break;
                    case 11:
                        hour11 += 1;
                        intensity11 += behavior.intensity;
                        break;
                    case 12:
                        hour12 += 1;
                        intensity12 += behavior.intensity;
                        break;
                    case 13:
                        hour13 += 1;
                        intensity13 += behavior.intensity;
                        break;
                    case 14:
                        hour14 += 1;
                        intensity14 += behavior.intensity;
                        break;
                    case 15:
                        hour15 += 1;
                        intensity15 += behavior.intensity;
                        break;
                    case 16:
                        hour16 += 1;
                        intensity16 += behavior.intensity;
                        break;
                    case 17:
                        hour17 += 1;
                        intensity17 += behavior.intensity;
                        break;
                    case 18:
                        hour18 += 1;
                        intensity18 += behavior.intensity;
                        break;
                    case 19:
                        hour19 += 1;
                        intensity19 += behavior.intensity;
                        break;
                    case 20:
                        hour20 += 1;
                        intensity20 += behavior.intensity;
                        break;
                    case 21:
                        hour21 += 1;
                        intensity21 += behavior.intensity;
                        break;
                    case 22:
                        hour22 += 1;
                        intensity22 += behavior.intensity;
                        break;
                    case 23:
                        hour23 += 1;
                        intensity23 += behavior.intensity;
                        break;
                }

                // number
                dayNumber += 1;

                // time
                long timeDiff = behavior.end_time.getTime() - behavior.start_time.getTime();
                dayTime = dayTime + (timeDiff/(1000*60));

                // intensity
                dayIntensity += behavior.intensity;

                // Reasons
                HashMap<String, Object> reason = (HashMap<String, Object>) behavior.reason;
                HashMap.Entry<String,Object> entryRaason = reason.entrySet().iterator().next();

                // Color Code
                switch(entryRaason.getKey()) {
                    case "관심":
                        interest += 1;
                        break;
                    case "자기자극":
                        selfstimulation += 1;
                        break;
                    case "과제회피":
                        taskevation += 1;
                        break;
                    case "요구":
                        demand += 1;
                        break;
                    case "기타":
                        reasonEtc += 1;
                        break;
                }

                // Types
                HashMap<String, Object> type = (HashMap<String, Object>) behavior.type;
                HashMap.Entry<String,Object> entryType = type.entrySet().iterator().next();

                // Color Code
                switch(entryType.getKey()) {
                    case "selfharm":
                        selfharm += 1;
                        break;
                    case "harm":
                        harm += 1;
                        break;
                    case "destruction":
                        destruction += 1;
                        break;
                    case "breakaway":
                        breakaway += 1;
                        break;
                    case "sexual":
                        sexual += 1;
                        break;
                    case "etc":
                        typeEtc += 1;
                        break;
                }

                // Locations
                switch(behavior.place) {
                    case "집":
                        home += 1;
                        break;
                    case "마트":
                        mart += 1;
                        break;
                    case "식당":
                        restaurant += 1;
                        break;
                    case "학교":
                        school += 1;
                        break;
                }

            }
        }

        // Frequency
        chartFrequency = v.findViewById(R.id.chart_day_frequency);
        chartFrequency.getDescription().setEnabled(false);
        chartFrequency.getLegend().setEnabled(false);
        chartFrequency.setScaleEnabled(false);
        chartFrequency.setTouchEnabled(false);

        XAxis xAxisFrequency = chartFrequency.getXAxis();
        xAxisFrequency.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisFrequency.setDrawGridLines(false);

        YAxis yAxisLeftFrequency = chartFrequency.getAxisLeft();
        yAxisLeftFrequency.setEnabled(false);
        yAxisLeftFrequency.setStartAtZero(true);

        YAxis yAxisRightFrequency = chartFrequency.getAxisRight();
        yAxisRightFrequency.setEnabled(true);
        yAxisRightFrequency.mAxisMinimum = 0;
        yAxisRightFrequency.setStartAtZero(true);

        if(dayNumber == 0) {
            yAxisRightFrequency.mAxisMaximum = 2;
        } else {
            List<Integer> list = Arrays.asList(hour0, hour1, hour2, hour3, hour4, hour5, hour6, hour7, hour8, hour9, hour10,
                    hour11, hour12, hour13, hour14, hour15, hour16, hour17, hour18, hour19, hour20, hour21, hour22, hour23);

            int max = Collections.max(list);
            if(max == 1) {
                yAxisRightFrequency.setAxisMaximum(2);
            } else {
                yAxisRightFrequency.mAxisMaximum = max;
                yAxisRightFrequency.setLabelCount(max);
            }
        }

        yFrequency.add(new BarEntry(0, hour0));
        yFrequency.add(new BarEntry(1, hour1));
        yFrequency.add(new BarEntry(2, hour2));
        yFrequency.add(new BarEntry(3, hour3));
        yFrequency.add(new BarEntry(4, hour4));
        yFrequency.add(new BarEntry(5, hour5));
        yFrequency.add(new BarEntry(6, hour6));
        yFrequency.add(new BarEntry(7, hour7));
        yFrequency.add(new BarEntry(8, hour8));
        yFrequency.add(new BarEntry(9, hour9));
        yFrequency.add(new BarEntry(10, hour10));
        yFrequency.add(new BarEntry(11, hour11));
        yFrequency.add(new BarEntry(12, hour12));
        yFrequency.add(new BarEntry(13, hour13));
        yFrequency.add(new BarEntry(14, hour14));
        yFrequency.add(new BarEntry(15, hour15));
        yFrequency.add(new BarEntry(16, hour16));
        yFrequency.add(new BarEntry(17, hour17));
        yFrequency.add(new BarEntry(18, hour18));
        yFrequency.add(new BarEntry(19, hour19));
        yFrequency.add(new BarEntry(20, hour20));
        yFrequency.add(new BarEntry(21, hour21));
        yFrequency.add(new BarEntry(22, hour22));
        yFrequency.add(new BarEntry(23, hour23));

        int[] colors = new int[] {colorIntensity(intensity0, hour0), colorIntensity(intensity1, hour1), colorIntensity(intensity2, hour2), colorIntensity(intensity3, hour3), colorIntensity(intensity4, hour4), colorIntensity(intensity5, hour5), colorIntensity(intensity6, hour6), colorIntensity(intensity7, hour7), colorIntensity(intensity8, hour8), colorIntensity(intensity9, hour9),
                colorIntensity(intensity10, hour10), colorIntensity(intensity11, hour11), colorIntensity(intensity12, hour12), colorIntensity(intensity13, hour13), colorIntensity(intensity14, hour14), colorIntensity(intensity15, hour15), colorIntensity(intensity16, hour16), colorIntensity(intensity17, hour17), colorIntensity(intensity18, hour18), colorIntensity(intensity19, hour19),
                colorIntensity(intensity20, hour20), colorIntensity(intensity21, hour21), colorIntensity(intensity22, hour22), colorIntensity(intensity23, hour23)};

        BarDataSet setFrequency = new BarDataSet(yFrequency, "");
        setFrequency.setDrawValues(false);
        setFrequency.setColors(colors);
        BarData dataFrequency = new BarData(setFrequency);
        chartFrequency.setData(dataFrequency);

        // Textview
        TextView dayDate = v.findViewById(R.id.chart_day_date);
        dayDate.setText(DateandTime);

        mDayNumber = v.findViewById(R.id.chart_day_number);
        mDayTime = v.findViewById(R.id.chart_day_time);
        mDayIntensity = v.findViewById(R.id.chart_day_intensity);
        mDayNumber.setText(String.valueOf(dayNumber));

        if(dayNumber != 0) {
            mDayTime.setText(String.valueOf(Math.round((dayTime / dayNumber)*10)/10.0));
        } else {
            mDayTime.setText("0");
        }

        if(dayNumber == 0) {
            intensity = "없음";
        } else {
            switch(Math.round(dayIntensity / dayNumber)) {
                case 1:
                    intensity = "매우 약함";
                    break;
                case 2:
                    intensity = "약함";
                    break;
                case 3:
                    intensity = "보통";
                    break;
                case 4:
                    intensity = "강함";
                    break;
                case 5:
                    intensity = "매우 강함";
                    break;
            }
        }
        mDayIntensity.setText(intensity);

        // Reasons
        chartReasons = v.findViewById(R.id.chart_day_reasons);
        chartReasons.getDescription().setEnabled(false);
        chartReasons.getLegend().setEnabled(false);
        chartReasons.setScaleEnabled(false);
        chartReasons.setTouchEnabled(false);

        xLabelsReasons = new ArrayList<>();
        xLabelsReasons.add("기타");
        xLabelsReasons.add("과제회피");
        xLabelsReasons.add("자기자극");
        xLabelsReasons.add("요구");
        xLabelsReasons.add("관심");

        XAxis xAxisReasons = chartReasons.getXAxis();
        xAxisReasons.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisReasons.setDrawGridLines(false);
        xAxisReasons.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelsReasons.get((int) value);
            }

        });

        YAxis yAxisLeftReasons = chartReasons.getAxisLeft();
        yAxisLeftReasons.setStartAtZero(true);
        yAxisLeftReasons.setEnabled(false);
        yAxisLeftReasons.setLabelCount(5, false);
        yAxisLeftReasons.setAxisMaxValue(5);

        YAxis yAxisRightReasons = chartReasons.getAxisRight();
        yAxisRightReasons.setStartAtZero(true);
        yAxisRightReasons.setLabelCount(5, false);
        yAxisRightReasons.setAxisMaxValue(5);

        yReasons.add(new BarEntry(0, reasonEtc));
        yReasons.add(new BarEntry(1, taskevation));
        yReasons.add(new BarEntry(2, selfstimulation));
        yReasons.add(new BarEntry(3, demand));
        yReasons.add(new BarEntry(4, interest));

        BarDataSet setReasons = new BarDataSet(yReasons, "");
        setReasons.setColors(Color.parseColor("#2dc76d"));
        setReasons.setDrawValues(false);
        setReasons.setValueTextSize(12);
        BarData dataReasons = new BarData(setReasons);
        chartReasons.setData(dataReasons);
        chartReasons.setFitBars(true);

        // Types
        chartTypes = v.findViewById(R.id.chart_day_types);
        chartTypes.getDescription().setEnabled(false);
        chartTypes.getLegend().setEnabled(false);
        chartTypes.setScaleEnabled(false);
        chartTypes.setTouchEnabled(false);

        xLabelsTypes = new ArrayList<>();
        xLabelsTypes.add("기타");
        xLabelsTypes.add("성적");
        xLabelsTypes.add("이탈");
        xLabelsTypes.add("파괴");
        xLabelsTypes.add("타해");
        xLabelsTypes.add("자해");

        XAxis xAxisTypes = chartTypes.getXAxis();
        xAxisTypes.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisTypes.setDrawGridLines(false);
        xAxisTypes.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelsTypes.get((int) value);
            }

        });

        YAxis yAxisLeftTypes = chartTypes.getAxisLeft();
        yAxisLeftTypes.setStartAtZero(true);
        yAxisLeftTypes.setEnabled(false);
        yAxisLeftTypes.setLabelCount(5, false);
        yAxisLeftTypes.setAxisMaxValue(5);

        YAxis yAxisRightTypes = chartTypes.getAxisRight();
        yAxisRightTypes.setStartAtZero(true);
        yAxisRightTypes.setLabelCount(5, false);
        yAxisRightTypes.setAxisMaxValue(5);

        yTypes.add(new BarEntry(0, typeEtc));
        yTypes.add(new BarEntry(1, sexual));
        yTypes.add(new BarEntry(2, breakaway));
        yTypes.add(new BarEntry(3, destruction));
        yTypes.add(new BarEntry(4, harm));
        yTypes.add(new BarEntry(5, selfharm));

        BarDataSet setTypes = new BarDataSet(yTypes, "");
        setTypes.setColors(Color.parseColor("#2dc76d"));
        setTypes.setDrawValues(false);
        setTypes.setValueTextSize(12);
        BarData dataTypes = new BarData(setTypes);
        chartTypes.setData(dataTypes);
        chartTypes.setFitBars(true);

        // Locations
        chartLocations = v.findViewById(R.id.chart_day_locations);
        chartLocations.getDescription().setEnabled(false);
        chartLocations.getLegend().setEnabled(false);
        chartLocations.setScaleEnabled(false);
        chartLocations.setTouchEnabled(false);

        xLabelsLocations = new ArrayList<>();
        xLabelsLocations.add("기타");
        xLabelsLocations.add("학교");
        xLabelsLocations.add("식당가가");
        xLabelsLocations.add("마트");
        xLabelsLocations.add("집");

        XAxis xAxisLocations = chartLocations.getXAxis();
        xAxisLocations.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisLocations.setDrawGridLines(false);
        xAxisLocations.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelsLocations.get((int) value);
            }

        });

        YAxis yAxisLeftLocations = chartLocations.getAxisLeft();
        yAxisLeftLocations.setStartAtZero(true);
        yAxisLeftLocations.setEnabled(false);
        yAxisLeftLocations.setLabelCount(5, false);
        yAxisLeftLocations.setAxisMaxValue(5);

        YAxis yAxisRightLocations = chartLocations.getAxisRight();
        yAxisRightLocations.setStartAtZero(true);
        yAxisRightLocations.setLabelCount(5, false);
        yAxisRightLocations.setAxisMaxValue(5);

        yLocations.add(new BarEntry(0, locationEtc));
        yLocations.add(new BarEntry(1, school));
        yLocations.add(new BarEntry(2, restaurant));
        yLocations.add(new BarEntry(3, mart));
        yLocations.add(new BarEntry(4, home));

        BarDataSet setLocations = new BarDataSet(yLocations, "");
        setLocations.setColors(Color.parseColor("#2dc76d"));
        setLocations.setDrawValues(false);
        setLocations.setValueTextSize(12);
        BarData dataLocations = new BarData(setLocations);
        chartLocations.setData(dataLocations);
        chartLocations.setFitBars(true);

        return v;
    }

    public int colorIntensity(int intensity, int number) {

        if(number != 0) {
            switch (Math.round(intensity / number)) {
                case 1:
                    return colorIntensity1;
                case 2:
                    return colorIntensity2;
                case 3:
                    return colorIntensity3;
                case 4:
                    return colorIntensity4;
                case 5:
                    return colorIntensity5;
                default:
                    return 0;
            }
        }

        return 0;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            //화면에 실제로 보일때
            int currentItem = ChartDayFragment.viewPager.getCurrentItem();
            if(currentItem + 1 == getCount) {
                diff = 0;
            }
        }
        else
        {
            //preload 될때(전페이지에 있을때)
            diff = getCount - positionNum - 1;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
