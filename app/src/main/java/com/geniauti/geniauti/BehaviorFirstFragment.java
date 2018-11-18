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

    public Timestamp startTimestamp;
    public Timestamp endTimestamp;

    public Date date_start;
    public Date date_end;
    private TextView startDate;
    private TextView endDate;
    private TextView startTime;
    private TextView endTime;

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

        startDate = v.findViewById(R.id.txt_start_date);
        endDate = v.findViewById(R.id.txt_end_date);

        startTime = v.findViewById(R.id.txt_start_time);
        endTime = v.findViewById(R.id.txt_end_time);

        Button pickStartTime = v.findViewById(R.id.start_time_button);
        pickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(year);
                        strBuf.append(".");
                        strBuf.append(month+1);
                        strBuf.append(".");
                        strBuf.append(dayOfMonth);

                        startDate.setText(strBuf.toString());

                        // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                StringBuffer strBuf = new StringBuffer();
                                strBuf.append(hour);
                                strBuf.append(":");
                                strBuf.append(minute);

                                startTime.setText(strBuf.toString());
                                getStartDate();
                            }
                        };

                        Calendar now = Calendar.getInstance();
                        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                        int minute = now.get(java.util.Calendar.MINUTE);

                        // Whether show time in 24 hour format or not.
                        boolean is24Hour = false;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, is24Hour);

                        timePickerDialog.show();
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

        Button pickEndTime = v.findViewById(R.id.end_time_button);
        pickEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(year);
                        strBuf.append("-");
                        strBuf.append(month+1);
                        strBuf.append("-");
                        strBuf.append(dayOfMonth);

                        endDate.setText(strBuf.toString());

                        // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                StringBuffer strBuf = new StringBuffer();
                                strBuf.append(hour);
                                strBuf.append(":");
                                strBuf.append(minute);

                                endTime.setText(strBuf.toString());
                                getEndDate();
                            }
                        };

                        Calendar now = Calendar.getInstance();
                        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                        int minute = now.get(java.util.Calendar.MINUTE);

                        // Whether show time in 24 hour format or not.
                        boolean is24Hour = false;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, is24Hour);

                        timePickerDialog.show();
                    }
                };

                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(java.util.Calendar.YEAR);
                int month = now.get(java.util.Calendar.MONTH);
                int day = now.get(java.util.Calendar.DAY_OF_MONTH);

                // Create the new DatePickerDialog instance.
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);

                // Popup the dialog.
                datePickerDialog.show();

            }
        });

        getStartDate();
        getEndDate();

        return v;
    }

    public void getStartDate() {
        String str_date_start = startDate.getText().toString() + " " + startTime.getText().toString();
        DateFormat formatter_start = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            //The code you are trying to exception handle
            date_start = formatter_start.parse(str_date_start);
            startTimestamp = new Timestamp(date_start.getTime());
        } catch (Exception e) {
            //The handling for the code
            e.printStackTrace();
        }
    }

    public void getEndDate() {
        String str_date_end = endDate.getText().toString() + " " + endTime.getText().toString();
        DateFormat formatter_end = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            //The code you are trying to exception handle
            date_end = formatter_end.parse(str_date_end);
            endTimestamp = new Timestamp(date_end.getTime());
        } catch (Exception e) {
            //The handling for the code
            e.printStackTrace();
        }
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
