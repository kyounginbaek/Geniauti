package com.geniauti.geniauti;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplateChartYearFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateChartYearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateChartYearFragment extends Fragment {
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

    private TemplateChartMonthFragment.OnFragmentInteractionListener mListener;
    private BarChart chartFrequency;
    private HorizontalBarChart chartReasons, chartTypes, chartLocations;
    private ArrayList<String> xLabelsFrequency, xLabelsReasons, xLabelsTypes, xLabelsLocations;

    private List<BarEntry> yFrequency = new ArrayList<>();
    private List<BarEntry> yReasons = new ArrayList<>();
    private List<BarEntry> yTypes = new ArrayList<>();
    private List<BarEntry> yLocations = new ArrayList<>();

    private int january = 0, february = 0, march = 0, afril = 0, may = 0, june = 0, july = 0, august = 0, september = 0, october = 0, november = 0, december = 0;
    private int interest = 0, demand = 0, selfstimulation = 0, taskevation = 0, reasonEtc = 0;
    private int selfharm = 0, harm = 0, destruction = 0, breakaway = 0, sexual = 0, typeEtc = 0;
    private int home = 0, mart = 0, restaurant = 0, school = 0, locationEtc = 0;
    private int yearNumber = 0, yearIntensity = 0;
    private double yearTime = 0.0;
    private String intensity;

    TextView mYearNumber;
    TextView mYearTime;
    TextView mYearIntensity;

    public static ArrayList<Behavior> behaviorData;
    public static int positionNum;
    private int getCount = ChartYearFragment.adapter.getCount();
    private int diff;

    public TemplateChartYearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment TemplateChartYearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateChartYearFragment newInstance(int position, ArrayList<Behavior> behaviors) {
        TemplateChartYearFragment fragment = new TemplateChartYearFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);

        behaviorData = behaviors;
        positionNum = position;

        fragment.setArguments(args);
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

        v = inflater.inflate(R.layout.fragment_template_chart_year, container, false);

        sdf = new SimpleDateFormat("yyyy년", Locale.KOREAN);
        sdfTime = new SimpleDateFormat("MM월", Locale.KOREAN);
        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1*diff);
        DateandTime = sdf.format(cal.getTime());

        // Behavior ArrayList
        for(int i = 0; i < behaviorData.size(); i++) {
            Behavior behavior =  behaviorData.get(i);

            Date startTime = behavior.start_time;

            if(sdf.format(startTime).equals(DateandTime)){
                // frequency

                String sTime = sdfTime.format(startTime).substring(0,2);

                switch(Integer.parseInt(sTime)) {
                    case 1:
                        january += 1;
                        break;
                    case 2:
                        february += 1;
                        break;
                    case 3:
                        march += 1;
                        break;
                    case 4:
                        afril += 1;
                        break;
                    case 5:
                        may += 1;
                        break;
                    case 6:
                        june += 1;
                        break;
                    case 7:
                        july += 1;
                        break;
                    case 8:
                        august += 1;
                        break;
                    case 9:
                        september += 1;
                        break;
                    case 10:
                        october += 1;
                        break;
                    case 11:
                        november += 1;
                        break;
                    case 12:
                        december += 1;
                        break;
                }

                // number
                yearNumber += 1;

                // time
                long timeDiff = behavior.end_time.getTime() - behavior.start_time.getTime();
                yearTime = yearTime + (timeDiff/(1000*60));

                // intensity
                yearIntensity += behavior.intensity;

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
        chartFrequency = v.findViewById(R.id.chart_year_frequency);
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
        yAxisRightFrequency.setStartAtZero(true);

        xLabelsFrequency = new ArrayList<>();
        xLabelsFrequency.add("1월");
        xLabelsFrequency.add("2월");
        xLabelsFrequency.add("3월");
        xLabelsFrequency.add("4월");
        xLabelsFrequency.add("5월");
        xLabelsFrequency.add("6월");
        xLabelsFrequency.add("7월");
        xLabelsFrequency.add("8월");
        xLabelsFrequency.add("9월");
        xLabelsFrequency.add("10월");
        xLabelsFrequency.add("11월");
        xLabelsFrequency.add("12월");

        yFrequency.add(new BarEntry(0, january));
        yFrequency.add(new BarEntry(1, february));
        yFrequency.add(new BarEntry(2, march));
        yFrequency.add(new BarEntry(3, afril));
        yFrequency.add(new BarEntry(4, may));
        yFrequency.add(new BarEntry(5, june));
        yFrequency.add(new BarEntry(6, july));
        yFrequency.add(new BarEntry(7, august));
        yFrequency.add(new BarEntry(8, september));
        yFrequency.add(new BarEntry(9, october));
        yFrequency.add(new BarEntry(10, november));
        yFrequency.add(new BarEntry(11, december));

        xAxisFrequency.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelsFrequency.get((int) value);
            }

        });

        xAxisFrequency.setLabelCount(12,true);

        BarDataSet setFrequency = new BarDataSet(yFrequency, "");
        setFrequency.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE});
        setFrequency.setDrawValues(false);
        setFrequency.setColors(Color.parseColor("#2dc76d"));
        BarData dataFrequency = new BarData(setFrequency);
        chartFrequency.setData(dataFrequency);

        // Textview
        TextView yearDate = v.findViewById(R.id.chart_year_date);
        yearDate.setText(DateandTime);

        mYearNumber = v.findViewById(R.id.chart_year_number);
        mYearTime = v.findViewById(R.id.chart_year_time);
        mYearIntensity = v.findViewById(R.id.chart_year_intensity);
        mYearNumber.setText(String.valueOf(yearNumber));

        if(yearNumber != 0) {
            mYearTime.setText(String.valueOf(Math.round((yearTime / yearNumber)*10)/10.0));
        } else {
            mYearTime.setText("0");
        }

        if(yearNumber == 0) {
            intensity = "없음";
        } else {
            switch(Math.round(yearIntensity / yearNumber)) {
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
        mYearIntensity.setText(intensity);

        // Reasons
        chartReasons = v.findViewById(R.id.chart_year_reasons);
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
        yAxisLeftReasons.setLabelCount(5, true);
        yAxisLeftReasons.setAxisMaxValue(5);

        YAxis yAxisRightReasons = chartReasons.getAxisRight();
        yAxisRightReasons.setStartAtZero(true);
        yAxisRightReasons.setLabelCount(5, true);
        yAxisRightReasons.setAxisMaxValue(5);

        yReasons.add(new BarEntry(0, reasonEtc));
        yReasons.add(new BarEntry(1, taskevation));
        yReasons.add(new BarEntry(2, selfstimulation));
        yReasons.add(new BarEntry(3, demand));
        yReasons.add(new BarEntry(4, interest));

        BarDataSet setReasons = new BarDataSet(yReasons, "");
        setReasons.setColors(Color.parseColor("#2dc76d"));
        setReasons.setDrawValues(false);
        BarData dataReasons = new BarData(setReasons);
        chartReasons.setData(dataReasons);
        chartReasons.setFitBars(true);

        // Types
        chartTypes = v.findViewById(R.id.chart_year_types);
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
        yAxisLeftTypes.setLabelCount(5, true);
        yAxisLeftTypes.setAxisMaxValue(5);

        YAxis yAxisRightTypes = chartTypes.getAxisRight();
        yAxisRightTypes.setStartAtZero(true);
        yAxisRightTypes.setLabelCount(5, true);
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
        BarData dataTypes = new BarData(setTypes);
        chartTypes.setData(dataTypes);
        chartTypes.setFitBars(true);

        // Locations
        chartLocations = v.findViewById(R.id.chart_year_locations);
        chartLocations.getDescription().setEnabled(false);
        chartLocations.getLegend().setEnabled(false);
        chartLocations.setScaleEnabled(false);
        chartLocations.setTouchEnabled(false);

        xLabelsLocations = new ArrayList<>();
        xLabelsLocations.add("기타");
        xLabelsLocations.add("학교");
        xLabelsLocations.add("식당");
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
        yAxisLeftLocations.setLabelCount(5, true);
        yAxisLeftLocations.setAxisMaxValue(5);

        YAxis yAxisRightLocations = chartLocations.getAxisRight();
        yAxisRightLocations.setStartAtZero(true);
        yAxisRightLocations.setLabelCount(5, true);
        yAxisRightLocations.setAxisMaxValue(5);

        yLocations.add(new BarEntry(0, locationEtc));
        yLocations.add(new BarEntry(1, school));
        yLocations.add(new BarEntry(2, restaurant));
        yLocations.add(new BarEntry(3, mart));
        yLocations.add(new BarEntry(4, home));

        BarDataSet setLocations = new BarDataSet(yLocations, "");
        setLocations.setColors(Color.parseColor("#2dc76d"));
        setLocations.setDrawValues(false);
        BarData dataLocations = new BarData(setLocations);
        chartLocations.setData(dataLocations);
        chartLocations.setFitBars(true);

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            //화면에 실제로 보일때
            int currentItem = ChartYearFragment.viewPager.getCurrentItem();
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
