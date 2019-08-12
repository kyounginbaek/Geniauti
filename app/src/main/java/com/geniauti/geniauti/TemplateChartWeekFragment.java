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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplateChartWeekFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateChartWeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateChartWeekFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    private SimpleDateFormat sdf, sdfDay;
    private Calendar cal, calSunday, calSaturday;
    private String DateandTime;

    private OnFragmentInteractionListener mListener;
    private BarChart chartFrequency;
    private HorizontalBarChart chartReasons, chartTypes, chartLocations;
    private ArrayList<String> xLabelsFrequency, xLabelsReasons, xLabelsTypes, xLabelsLocations;

    private List<BarEntry> yFrequency = new ArrayList<>();
    private List<BarEntry> yReasons = new ArrayList<>();
    private List<BarEntry> yTypes = new ArrayList<>();
    private List<BarEntry> yLocations = new ArrayList<>();

    private int sunday = 0, monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, saturday = 0;
    private int sundayIntensity = 0, mondayIntensity = 0, tuesdayIntensity = 0, wednesdayIntensity = 0, thursdayIntensity = 0, fridayIntensity = 0, saturdayIntensity = 0;
    private int interest = 0, demand = 0, selfstimulation = 0, taskevation = 0;
    private int selfharm = 0, harm = 0, destruction = 0, breakaway = 0, sexual = 0, typeEtc = 0;
    private int home = 0, mart = 0, restaurant = 0, school = 0;
    private HashMap<String, Integer> xLocations = new HashMap<>();
    private int weekNumber = 0, weekIntensity = 0;
    private double weekTime = 0.0;
    private String intensity;

    TextView mWeekNumber;
    TextView mWeekTime;
    TextView mWeekIntensity;

    private int colorIntensity1, colorIntensity2, colorIntensity3, colorIntensity4, colorIntensity5;

    public static ArrayList<BehaviorChart> behaviorData;
    public static int positionNum;
    private int getCount = ChartWeekFragment.adapter.getCount();
    private int diff;

    public TemplateChartWeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     //     * @param param1 Parameter 1.
     //     * @param param2 Parameter 2.
     * @return A new instance of fragment TemplateChartWeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateChartWeekFragment newInstance(int position, ArrayList<BehaviorChart> behaviors) {
        TemplateChartWeekFragment fragment = new TemplateChartWeekFragment();
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

        v = inflater.inflate(R.layout.fragment_template_chart_week, container, false);

        sdf = new SimpleDateFormat("yyyy년 MM월", Locale.KOREAN);
        sdfDay = new SimpleDateFormat("dd", Locale.KOREAN);
        cal = Calendar.getInstance();
        calSunday = Calendar.getInstance();
        calSaturday = Calendar.getInstance();
        cal.add(Calendar.DATE, -7*diff);
        calSunday.add(Calendar.DATE, -7*diff);
        calSaturday.add(Calendar.DATE, -7*diff);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (Calendar.MONDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -1);
            calSaturday.add(Calendar.DATE, +5);
        } else if (Calendar.TUESDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -2);
            calSaturday.add(Calendar.DATE, +4);
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -3);
            calSaturday.add(Calendar.DATE, +3);
        } else if (Calendar.THURSDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -4);
            calSaturday.add(Calendar.DATE, +2);
        } else if (Calendar.FRIDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -5);
            calSaturday.add(Calendar.DATE, +1);
        } else if (Calendar.SATURDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -6);
            calSaturday.add(Calendar.DATE, +0);
        } else if (Calendar.SUNDAY == dayOfWeek) {
            calSunday.add(Calendar.DATE, -0);
            calSaturday.add(Calendar.DATE, +6);
        }

        DateandTime = sdf.format(calSunday.getTime()) + " " + sdfDay.format(calSunday.getTime()) + "일" + " - " + sdf.format(calSaturday.getTime()) + " " + sdfDay.format(calSaturday.getTime()) + "일";

        colorIntensity5 = Color.parseColor("#2dc76d");
        colorIntensity4 = Color.parseColor("#cc2dc76d");
        colorIntensity3 = Color.parseColor("#992dc76d");
        colorIntensity2 = Color.parseColor("#662dc76d");
        colorIntensity1 = Color.parseColor("#332dc76d");

        // Behavior ArrayList
        for(int i = 0; i < behaviorData.size(); i++) {
            BehaviorChart behavior =  behaviorData.get(i);

            Date startTime = behavior.start_time;

            if(sdf.format(startTime).equals(sdf.format(cal.getTime()))) {
                if(Integer.parseInt(sdfDay.format(startTime)) >= Integer.parseInt(sdfDay.format(calSunday.getTime())) && Integer.parseInt(sdfDay.format(startTime)) <= Integer.parseInt(sdfDay.format(calSaturday.getTime()))){

                    // frequency
                    switch(Integer.parseInt(sdfDay.format(calSaturday.getTime())) - Integer.parseInt(sdfDay.format(startTime))) {
                        case 0:
                            saturday += 1;
                            saturdayIntensity += behavior.intensity;
                            break;
                        case 1:
                            friday += 1;
                            fridayIntensity += behavior.intensity;
                            break;
                        case 2:
                            thursday += 1;
                            thursdayIntensity += behavior.intensity;
                            break;
                        case 3:
                            wednesday += 1;
                            wednesdayIntensity += behavior.intensity;
                            break;
                        case 4:
                            tuesday += 1;
                            tuesdayIntensity += behavior.intensity;
                            break;
                        case 5:
                            monday += 1;
                            mondayIntensity += behavior.intensity;
                            break;
                        case 6:
                            sunday += 1;
                            sundayIntensity += behavior.intensity;
                            break;
                    }

                    // number
                    weekNumber += 1;

                    // time
                    long timeDiff = behavior.end_time.getTime() - behavior.start_time.getTime();
                    weekTime = weekTime + (timeDiff/(1000*60));

                    // intensity
                    weekIntensity += behavior.intensity;

                    // Reasons
                    HashMap<String, Object> reason = (HashMap<String, Object>) behavior.reason;
                    HashMap.Entry<String,Object> entryReason = reason.entrySet().iterator().next();

                    // Color Code
                    switch(entryReason.getKey()) {
                        case "attention1":
                        case "attention2":
                        case "attention3":
                        case "attention4":
                            interest += 1;
                            break;
                        case "self-stimulatory behaviour1":
                        case "self-stimulatory behaviour2":
                        case "self-stimulatory behaviour3":
                            selfstimulation += 1;
                            break;
                        case "escape1":
                        case "escape2":
                        case "escape3":
                        case "escape4":
                            taskevation += 1;
                            break;
                        case "tangibles1":
                        case "tangibles2":
                        case "tangibles3":
                        case "tangibles4":
                            demand += 1;
                            break;
                    }

                    // Types
                    HashMap<String, Object> type = (HashMap<String, Object>) behavior.type;
                    HashMap.Entry<String,Object> entryType = type.entrySet().iterator().next();

                    // Color Code
                    switch(entryType.getKey()) {
                        case "self-injury":
                            selfharm += 1;
                            break;
                        case "aggression":
                            harm += 1;
                            break;
                        case "disruption":
                            destruction += 1;
                            break;
                        case "elopement":
                            breakaway += 1;
                            break;
                        case "sexual behaviors":
                            sexual += 1;
                            break;
                        case "other behaviors":
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
                        default:
                            if(xLocations.containsKey(behavior.place)) {
                                int tmpInteger = xLocations.get(behavior.place);
                                xLocations.put(behavior.place, tmpInteger+1);
                            } else {
                                xLocations.put(behavior.place, 1);
                            }
                            break;
                    }

                }
            }
        }

        // Frequency
        chartFrequency = v.findViewById(R.id.chart_week_frequency);
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

        List<Integer> list = Arrays.asList(sunday, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        int maxFrequency = Collections.max(list);
        yAxisLeftFrequency.setAxisMaxValue(maxFrequency);
        yAxisRightFrequency.setAxisMaxValue(maxFrequency);

        if(maxFrequency == 1) {
            yAxisLeftFrequency.setLabelCount(maxFrequency, true);
            yAxisRightFrequency.setLabelCount(maxFrequency, true);
        } else {
            yAxisLeftFrequency.setLabelCount(maxFrequency, false);
            yAxisRightFrequency.setLabelCount(maxFrequency, false);
        }

        xLabelsFrequency = new ArrayList<>();
        xLabelsFrequency.add("일");
        xLabelsFrequency.add("월");
        xLabelsFrequency.add("화");
        xLabelsFrequency.add("수");
        xLabelsFrequency.add("목");
        xLabelsFrequency.add("금");
        xLabelsFrequency.add("토");
        xLabelsFrequency.add("일");

        yFrequency.add(new BarEntry(0, sunday));
        yFrequency.add(new BarEntry(1, monday));
        yFrequency.add(new BarEntry(2, tuesday));
        yFrequency.add(new BarEntry(3, wednesday));
        yFrequency.add(new BarEntry(4, thursday));
        yFrequency.add(new BarEntry(5, friday));
        yFrequency.add(new BarEntry(6, saturday));

        xAxisFrequency.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabelsFrequency.get((int) value);
            }

        });

//        int[] colors = new int[] {colorIntensity(sundayIntensity, sunday), colorIntensity(mondayIntensity, monday), colorIntensity(tuesdayIntensity, tuesday), colorIntensity(wednesdayIntensity, wednesday), colorIntensity(thursdayIntensity, thursday), colorIntensity(fridayIntensity, friday), colorIntensity(saturdayIntensity, saturday)};

        BarDataSet setFrequency = new BarDataSet(yFrequency, "");
        setFrequency.setDrawValues(false);
        setFrequency.setColors(colorIntensity5);
        BarData dataFrequency = new BarData(setFrequency);
        chartFrequency.setData(dataFrequency);

        // Textview
        TextView weekDate = v.findViewById(R.id.chart_week_date);
        weekDate.setText(DateandTime);

        mWeekNumber = v.findViewById(R.id.chart_week_number);
        mWeekTime = v.findViewById(R.id.chart_week_time);
        mWeekIntensity = v.findViewById(R.id.chart_week_intensity);
        mWeekNumber.setText(String.valueOf(weekNumber));

        if(weekNumber != 0) {
            mWeekTime.setText(String.valueOf(Math.round((weekTime / weekNumber)*10)/10.0));
        } else {
            mWeekTime.setText("0");
        }

        if(weekNumber == 0) {
            intensity = "없음";
        } else {
            switch(Math.round(weekIntensity / weekNumber)) {
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
        mWeekIntensity.setText(intensity);

        // Reasons
        chartReasons = v.findViewById(R.id.chart_week_reasons);
        chartReasons.getDescription().setEnabled(false);
        chartReasons.getLegend().setEnabled(false);
        chartReasons.setScaleEnabled(false);
        chartReasons.setTouchEnabled(false);

        xLabelsReasons = new ArrayList<>();
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
        xAxisReasons.setLabelCount(4,false);

        YAxis yAxisLeftReasons = chartReasons.getAxisLeft();
        yAxisLeftReasons.setStartAtZero(true);
        yAxisLeftReasons.setEnabled(false);
        YAxis yAxisRightReasons = chartReasons.getAxisRight();
        yAxisRightReasons.setStartAtZero(true);

        int maxReason = maxNumber4(taskevation, selfstimulation, demand, interest);
        yAxisLeftReasons.setAxisMaxValue(maxReason);
        yAxisRightReasons.setAxisMaxValue(maxReason);

        if(maxReason == 1){
            yAxisLeftReasons.setLabelCount(maxReason, true);
            yAxisRightReasons.setLabelCount(maxReason, true);
        } else {
            yAxisLeftReasons.setLabelCount(maxReason, false);
            yAxisRightReasons.setLabelCount(maxReason, false);
        }

        yReasons.add(new BarEntry(0, taskevation));
        yReasons.add(new BarEntry(1, selfstimulation));
        yReasons.add(new BarEntry(2, demand));
        yReasons.add(new BarEntry(3, interest));

        BarDataSet setReasons = new BarDataSet(yReasons, "");
        setReasons.setColors(Color.parseColor("#2dc76d"));
        setReasons.setDrawValues(false);
        BarData dataReasons = new BarData(setReasons);
        chartReasons.setData(dataReasons);
        chartReasons.setFitBars(true);

        // Types
        chartTypes = v.findViewById(R.id.chart_week_types);
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
        YAxis yAxisRightTypes = chartTypes.getAxisRight();
        yAxisRightTypes.setStartAtZero(true);

        int maxType = maxNumber6(typeEtc, sexual, breakaway, destruction, harm, selfharm);
        yAxisLeftTypes.setAxisMaxValue(maxType);
        yAxisRightTypes.setAxisMaxValue(maxType);

        if(maxType == 1) {
            yAxisLeftTypes.setLabelCount(maxType, true);
            yAxisRightTypes.setLabelCount(maxType, true);
        } else {
            yAxisLeftTypes.setLabelCount(maxType, false);
            yAxisRightTypes.setLabelCount(maxType, false);
        }

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
        chartLocations = v.findViewById(R.id.chart_week_locations);
        chartLocations.getDescription().setEnabled(false);
        chartLocations.getLegend().setEnabled(false);
        chartLocations.setScaleEnabled(false);
        chartLocations.setTouchEnabled(false);

        // for loop
        xLabelsLocations = new ArrayList<>();
        if(xLocations.size() != 0) {
            Iterator it_location = xLocations.entrySet().iterator();
            while (it_location.hasNext()) {
                Map.Entry pair = (Map.Entry)it_location.next();
                if(pair.getKey().toString().length() >= 5) {
                    xLabelsLocations.add(TextEllipse(pair.getKey().toString()));
                } else {
                    xLabelsLocations.add(pair.getKey().toString());
                }
            }
        }
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
        xAxisLocations.setLabelCount(xLabelsLocations.size(),false);

        YAxis yAxisLeftLocations = chartLocations.getAxisLeft();
        yAxisLeftLocations.setStartAtZero(true);
        yAxisLeftLocations.setEnabled(false);
        YAxis yAxisRightLocations = chartLocations.getAxisRight();
        yAxisRightLocations.setStartAtZero(true);

        // for loop
        int maxLocation = maxNumberLocation(xLocations, school, restaurant, mart, home);
        yAxisLeftLocations.setAxisMaxValue(maxLocation);
        yAxisRightLocations.setAxisMaxValue(maxLocation);

        if(maxLocation == 1) {
            yAxisLeftLocations.setLabelCount(maxLocation, true);
            yAxisRightLocations.setLabelCount(maxLocation, true);
        } else {
            yAxisLeftLocations.setLabelCount(maxLocation, false);
            yAxisRightLocations.setLabelCount(maxLocation, false);
        }

        // for loop
        int nLocations = 0;

        if(xLocations.size() != 0) {
            Iterator it_location = xLocations.entrySet().iterator();
            while (it_location.hasNext()) {
                Map.Entry pair = (Map.Entry)it_location.next();
                yLocations.add(new BarEntry(nLocations, Integer.parseInt(pair.getValue().toString())));
                nLocations++;
            }
        }

        yLocations.add(new BarEntry(nLocations, school));
        yLocations.add(new BarEntry(nLocations+1, restaurant));
        yLocations.add(new BarEntry(nLocations+2, mart));
        yLocations.add(new BarEntry(nLocations+3, home));

        BarDataSet setLocations = new BarDataSet(yLocations, "");
        setLocations.setColors(Color.parseColor("#2dc76d"));
        setLocations.setDrawValues(false);
        BarData dataLocations = new BarData(setLocations);
        chartLocations.setData(dataLocations);
        chartLocations.setFitBars(true);

        return v;
    }

    public String TextEllipse(String text){

        return text.substring(0,3) + "..";
    }

    public int maxNumber4(int n1, int n2, int n3, int n4) {
        List<Integer> list = Arrays.asList(n1, n2, n3, n4);
        return Collections.max(list);
    }

    public int maxNumberLocation(HashMap<String, Integer> n, int n2, int n3, int n4, int n5) {

        List<Integer> list;

        if(n.size() != 0) {
            int n1 = 0;
            Iterator it_n = n.entrySet().iterator();
            while (it_n.hasNext()) {
                Map.Entry pair = (Map.Entry)it_n.next();
                if(Integer.parseInt(pair.getValue().toString()) > n1) {
                    n1 = Integer.parseInt(pair.getValue().toString());
                }
            }
            list = Arrays.asList(n1, n2, n3, n4, n5);
        } else {
            list = Arrays.asList(n2, n3, n4, n5);
        }

        return Collections.max(list);
    }

    public int maxNumber6(int n1, int n2, int n3, int n4, int n5, int n6) {
        List<Integer> list = Arrays.asList(n1, n2, n3, n4, n5, n6);
        return Collections.max(list);
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
            int currentItem = ChartWeekFragment.viewPager.getCurrentItem();
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