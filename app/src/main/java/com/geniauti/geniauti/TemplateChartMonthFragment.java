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
 * {@link TemplateChartMonthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateChartMonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateChartMonthFragment extends Fragment {
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

    private int day1 = 0, day2 = 0, day3 = 0, day4 = 0, day5 = 0, day6 = 0, day7 = 0, day8 = 0, day9 = 0, day10 = 0, day11 = 0, day12 = 0, day13 = 0, day14 = 0, day15 = 0, day16 = 0, day17 = 0, day18 = 0, day19 = 0, day20 = 0, day21 = 0, day22 = 0, day23 = 0, day24 = 0, day25 = 0, day26 = 0, day27 = 0, day28 = 0, day29 = 0, day30 = 0, day31 = 0;
    private int intensity1 = 0, intensity2 = 0, intensity3 = 0, intensity4 = 0, intensity5 = 0, intensity6 = 0, intensity7 = 0, intensity8 = 0, intensity9 = 0, intensity10 = 0, intensity11 = 0, intensity12 = 0, intensity13 = 0, intensity14 = 0, intensity15 = 0, intensity16 = 0, intensity17 = 0, intensity18 = 0, intensity19 = 0, intensity20 = 0, intensity21 = 0, intensity22 = 0, intensity23 = 0, intensity24 = 0, intensity25 = 0, intensity26 = 0, intensity27 = 0, intensity28 = 0, intensity29 = 0, intensity30 = 0, intensity31 = 0;
    private int interest = 0, demand = 0, selfstimulation = 0, taskevation = 0;
    private int selfharm = 0, harm = 0, destruction = 0, breakaway = 0, sexual = 0, typeEtc = 0;
    private int home = 0, mart = 0, restaurant = 0, school = 0;
    private HashMap<String, Integer> xLocations = new HashMap<>();
    private int monthNumber = 0, monthIntensity = 0;
    private double monthTime = 0.0;
    private String intensity;

    TextView mMonthNumber;
    TextView mMonthTime;
    TextView mMonthIntensity;

    private int colorIntensity1, colorIntensity2, colorIntensity3, colorIntensity4, colorIntensity5;

    public static ArrayList<BehaviorChart> behaviorData;
    public static int positionNum;
    private int getCount = ChartMonthFragment.adapter.getCount();

    private int diff;

    public TemplateChartMonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment TemplateChartMonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateChartMonthFragment newInstance(int position, ArrayList<BehaviorChart> behaviors) {
        TemplateChartMonthFragment fragment = new TemplateChartMonthFragment();
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
        v = inflater.inflate(R.layout.fragment_template_chart_month, container, false);

        chart();

        return v;
    }

    public void chart() {
        sdf = new SimpleDateFormat("yyyy년 MM월", Locale.KOREAN);
        sdfTime = new SimpleDateFormat("dd", Locale.KOREAN);
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1 * diff);
        DateandTime = sdf.format(cal.getTime());

        colorIntensity5 = Color.parseColor("#2dc76d");
        colorIntensity4 = Color.parseColor("#cc2dc76d");
        colorIntensity3 = Color.parseColor("#992dc76d");
        colorIntensity2 = Color.parseColor("#662dc76d");
        colorIntensity1 = Color.parseColor("#332dc76d");

        // Behavior ArrayList
        for (int i = 0; i < behaviorData.size(); i++) {
            BehaviorChart behavior = behaviorData.get(i);

            Date startTime = behavior.start_time;

            if (sdf.format(startTime).equals(DateandTime)) {
                // frequency

                String sTime = sdfTime.format(startTime);

                switch (Integer.parseInt(sTime)) {
                    case 1:
                        day1 += 1;
                        intensity1 += behavior.intensity;
                        break;
                    case 2:
                        day2 += 1;
                        intensity2 += behavior.intensity;
                        break;
                    case 3:
                        day3 += 1;
                        intensity3 += behavior.intensity;
                        break;
                    case 4:
                        day4 += 1;
                        intensity4 += behavior.intensity;
                        break;
                    case 5:
                        day5 += 1;
                        intensity5 += behavior.intensity;
                        break;
                    case 6:
                        day6 += 1;
                        intensity6 += behavior.intensity;
                        break;
                    case 7:
                        day7 += 1;
                        intensity7 += behavior.intensity;
                        break;
                    case 8:
                        day8 += 1;
                        intensity8 += behavior.intensity;
                        break;
                    case 9:
                        day9 += 1;
                        intensity9 += behavior.intensity;
                        break;
                    case 10:
                        day10 += 1;
                        intensity10 += behavior.intensity;
                        break;
                    case 11:
                        day11 += 1;
                        intensity11 += behavior.intensity;
                        break;
                    case 12:
                        day12 += 1;
                        intensity12 += behavior.intensity;
                        break;
                    case 13:
                        day13 += 1;
                        intensity13 += behavior.intensity;
                        break;
                    case 14:
                        day14 += 1;
                        intensity14 += behavior.intensity;
                        break;
                    case 15:
                        day15 += 1;
                        intensity15 += behavior.intensity;
                        break;
                    case 16:
                        day16 += 1;
                        intensity16 += behavior.intensity;
                        break;
                    case 17:
                        day17 += 1;
                        intensity17 += behavior.intensity;
                        break;
                    case 18:
                        day18 += 1;
                        intensity18 += behavior.intensity;
                        break;
                    case 19:
                        day19 += 1;
                        intensity19 += behavior.intensity;
                        break;
                    case 20:
                        day20 += 1;
                        intensity20 += behavior.intensity;
                        break;
                    case 21:
                        day21 += 1;
                        intensity21 += behavior.intensity;
                        break;
                    case 22:
                        day22 += 1;
                        intensity22 += behavior.intensity;
                        break;
                    case 23:
                        day23 += 1;
                        intensity23 += behavior.intensity;
                        break;
                    case 24:
                        day24 += 1;
                        intensity24 += behavior.intensity;
                        break;
                    case 25:
                        day25 += 1;
                        intensity25 += behavior.intensity;
                        break;
                    case 26:
                        day26 += 1;
                        intensity26 += behavior.intensity;
                        break;
                    case 27:
                        day27 += 1;
                        intensity27 += behavior.intensity;
                        break;
                    case 28:
                        day28 += 1;
                        intensity28 += behavior.intensity;
                        break;
                    case 29:
                        day29 += 1;
                        intensity29 += behavior.intensity;
                        break;
                    case 30:
                        day30 += 1;
                        intensity30 += behavior.intensity;
                        break;
                    case 31:
                        day31 += 1;
                        intensity31 += behavior.intensity;
                        break;
                }

                // number
                monthNumber += 1;

                // time
                long timeDiff = behavior.end_time.getTime() - behavior.start_time.getTime();
                monthTime = monthTime + (timeDiff / (1000 * 60));

                // intensity
                monthIntensity += behavior.intensity;

                // Reasons
                HashMap<String, Object> reason = (HashMap<String, Object>) behavior.reason;
                HashMap.Entry<String, Object> entryReason = reason.entrySet().iterator().next();

                // Color Code
                switch (entryReason.getKey()) {
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
                HashMap.Entry<String, Object> entryType = type.entrySet().iterator().next();

                // Color Code
                switch (entryType.getKey()) {
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
                switch (behavior.place) {
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

        // Frequency
        chartFrequency = v.findViewById(R.id.chart_month_frequency);
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

        List<Integer> list = Arrays.asList(day1, day2, day3, day4, day5, day6, day7, day8, day9, day10,
                day11, day12, day13, day14, day15, day16, day17, day18, day19, day20,
                day21, day22, day23, day24, day25, day26, day27, day28, day29, day30, day31);
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

        yFrequency.add(new BarEntry(1, day1));
        yFrequency.add(new BarEntry(2, day2));
        yFrequency.add(new BarEntry(3, day3));
        yFrequency.add(new BarEntry(4, day4));
        yFrequency.add(new BarEntry(5, day5));
        yFrequency.add(new BarEntry(6, day6));
        yFrequency.add(new BarEntry(7, day7));
        yFrequency.add(new BarEntry(8, day8));
        yFrequency.add(new BarEntry(9, day9));
        yFrequency.add(new BarEntry(10, day10));
        yFrequency.add(new BarEntry(11, day11));
        yFrequency.add(new BarEntry(12, day12));
        yFrequency.add(new BarEntry(13, day13));
        yFrequency.add(new BarEntry(14, day14));
        yFrequency.add(new BarEntry(15, day15));
        yFrequency.add(new BarEntry(16, day16));
        yFrequency.add(new BarEntry(17, day17));
        yFrequency.add(new BarEntry(18, day18));
        yFrequency.add(new BarEntry(19, day19));
        yFrequency.add(new BarEntry(20, day20));
        yFrequency.add(new BarEntry(21, day21));
        yFrequency.add(new BarEntry(22, day22));
        yFrequency.add(new BarEntry(23, day23));
        yFrequency.add(new BarEntry(24, day24));
        yFrequency.add(new BarEntry(25, day25));
        yFrequency.add(new BarEntry(26, day26));
        yFrequency.add(new BarEntry(27, day27));
        yFrequency.add(new BarEntry(28, day28));
        yFrequency.add(new BarEntry(29, day29));
        yFrequency.add(new BarEntry(30, day30));
        yFrequency.add(new BarEntry(31, day31));

//        int[] colors = new int[]{colorIntensity(intensity1, day1), colorIntensity(intensity2, day2), colorIntensity(intensity3, day3), colorIntensity(intensity4, day4), colorIntensity(intensity5, day5), colorIntensity(intensity6, day6), colorIntensity(intensity7, day7), colorIntensity(intensity8, day8), colorIntensity(intensity9, day9), colorIntensity(intensity10, day10),
//                colorIntensity(intensity11, day11), colorIntensity(intensity12, day12), colorIntensity(intensity13, day13), colorIntensity(intensity14, day14), colorIntensity(intensity15, day15), colorIntensity(intensity16, day16), colorIntensity(intensity17, day17), colorIntensity(intensity18, day18), colorIntensity(intensity19, day19), colorIntensity(intensity20, day20),
//                colorIntensity(intensity21, day21), colorIntensity(intensity22, day22), colorIntensity(intensity23, day23), colorIntensity(intensity24, day24), colorIntensity(intensity25, day25), colorIntensity(intensity26, day26), colorIntensity(intensity27, day27), colorIntensity(intensity28, day28), colorIntensity(intensity29, day29), colorIntensity(intensity30, day30),
//                colorIntensity(intensity31, day31)};

        BarDataSet setFrequency = new BarDataSet(yFrequency, "");
        setFrequency.setDrawValues(false);
        setFrequency.setColors(colorIntensity5);
        BarData dataFrequency = new BarData(setFrequency);
        chartFrequency.setData(dataFrequency);

        // Textview
        TextView monthDate = v.findViewById(R.id.chart_month_date);
        monthDate.setText(DateandTime);

        mMonthNumber = v.findViewById(R.id.chart_month_number);
        mMonthTime = v.findViewById(R.id.chart_month_time);
        mMonthIntensity = v.findViewById(R.id.chart_month_intensity);
        mMonthNumber.setText(String.valueOf(monthNumber));

        if (monthNumber != 0) {
            mMonthTime.setText(String.valueOf(Math.round((monthTime / monthNumber) * 10) / 10.0));
        } else {
            mMonthTime.setText("0");
        }

        if (monthNumber == 0) {
            intensity = "없음";
        } else {
            switch (Math.round(monthIntensity / monthNumber)) {
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
        mMonthIntensity.setText(intensity);

        // Reasons
        chartReasons = v.findViewById(R.id.chart_month_reasons);
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

        if(maxReason == 1) {
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
        chartTypes = v.findViewById(R.id.chart_month_types);
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
        chartLocations = v.findViewById(R.id.chart_month_locations);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //화면에 실제로 보일때
            int currentItem = ChartMonthFragment.viewPager.getCurrentItem();
            if (currentItem + 1 == getCount) {
                diff = 0;
            }
        } else {
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