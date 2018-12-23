package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BehaviorFirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BehaviorFirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BehaviorFirstFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public String hour_start;
    public String hour_end;
    public String date_start;
    private Button dateBtn ;
    private Button hourBtn;

    private String AM_PM_Start;
    private String enOrKor;

    private OnFragmentInteractionListener mListener;

    public BehaviorFirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BehaviorFirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BehaviorFirstFragment newInstance(String param1, String param2) {
        BehaviorFirstFragment fragment = new BehaviorFirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View v = inflater.inflate(R.layout.fragment_behavior_first, container, false);

        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat formatterHour = new SimpleDateFormat("aa hh:mm");

        final long ONE_MINUTE_IN_MILLIS = 60000; //millisecs
        Date dateToday = new Date();
        date_start = formatterDate.format(dateToday);
        long curTimeInMs = dateToday.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (5 * ONE_MINUTE_IN_MILLIS));

        dateBtn = (Button) v.findViewById(R.id.start_date_button);
        dateBtn.setText(date_start);

        hourBtn = (Button) v.findViewById(R.id.start_end_time_button);
        hour_start = formatterHour.format(dateToday);
        hour_end = formatterHour.format(afterAddingMins);

        if(hour_start.substring(0,2).equals("AM") || hour_start.substring(0,2).equals("PM")){
            enOrKor = "english";
        } else {
            enOrKor = "korea";
        }

        hourBtn.setText(hour_start+" ~ "+hour_end);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(year);
                        strBuf.append("년 ");
                        strBuf.append(month+1);
                        strBuf.append("월 ");
                        strBuf.append(dayOfMonth);
                        strBuf.append("일");

                        date_start = strBuf.toString();
                        dateBtn.setText(date_start);
                    }
                };

                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(java.util.Calendar.YEAR);
                int month = now.get(java.util.Calendar.MONTH);
                int day = now.get(java.util.Calendar.DAY_OF_MONTH);

                // Create the new DatePickerDialog instance.
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);

                // Popup the dialog.
                datePickerDialog.show();

            }
        });

        hourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        final StringBuffer strBufStart = new StringBuffer();
                        AM_PM_Start = "";
                        if(hour < 12) {
                            if(enOrKor.equals("english")){
                                AM_PM_Start = "AM";
                            } else {
                                AM_PM_Start = "오전";
                            }
                            if(hour < 10) {
                                strBufStart.append("0"+hour);
                            } else {
                                strBufStart.append(hour);
                            }
                        } else {
                            if(enOrKor.equals("english")){
                                AM_PM_Start = "PM";
                            } else {
                                AM_PM_Start = "오후";
                            }
                            if(hour-12 < 10){
                                strBufStart.append("0"+(hour-12));
                            } else {
                                strBufStart.append(hour-12);
                            }
                        }
                        strBufStart.append(":");
                        if(minute < 10) {
                            strBufStart.append("0"+minute);
                        } else {
                            strBufStart.append(minute);
                        }

                        // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                StringBuffer strBufEnd = new StringBuffer();
                                String AM_PM_End = "";
                                if(hour < 12) {
                                    if(enOrKor.equals("english")){
                                        AM_PM_End = "AM";
                                    } else {
                                        AM_PM_End = "오전";
                                    }

                                    if(hour < 10) {
                                        strBufEnd.append("0"+hour);
                                    } else {
                                        strBufEnd.append(hour);
                                    }
                                } else {
                                    if(enOrKor.equals("english")){
                                        AM_PM_End = "PM";
                                    } else {
                                        AM_PM_End = "오후";
                                    }

                                    if(hour-12 < 10){
                                        strBufEnd.append("0"+(hour-12));
                                    } else {
                                        strBufEnd.append(hour-12);
                                    }
                                }
                                strBufEnd.append(":");
                                if(minute < 10){
                                    strBufEnd.append("0"+minute);
                                } else {
                                    strBufEnd.append(minute);
                                }

                                hour_start = AM_PM_Start + " " + strBufStart.toString();
                                hour_end = AM_PM_End + " " + strBufEnd.toString();
                                hourBtn.setText(AM_PM_Start + " " + strBufStart+" ~ "+ AM_PM_End + " " + strBufEnd);
                            }
                        };

                        Calendar now = Calendar.getInstance();
                        int endHour = now.get(java.util.Calendar.HOUR_OF_DAY);
                        int endMinute = now.get(java.util.Calendar.MINUTE);

                        // Whether show time in 24 hour format or not.
                        boolean is24Hour = false;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, endHour, endMinute, is24Hour);

                        timePickerDialog.setTitle("행동 종료 시간");
                        timePickerDialog.show();
                    }
                };

                Calendar now = Calendar.getInstance();
                int startHour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int startMinute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = false;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, startHour, startMinute, is24Hour);

                timePickerDialog.setTitle("행동 시작 시간");
                timePickerDialog.show();
            }
        });

//        getStartDate();
//        getEndDate();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
