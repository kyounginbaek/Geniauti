package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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

    public static String hour_start;
    public static String hour_end;
    public static String date_start;
    private RelativeLayout dateLayout ;
    private RelativeLayout startTimeLayout, endTimeLayout;
    private TextView dateText, startTimeText, endTimeText;
    private TimePicker startTimePicker, endTimePicker;
    private LinearLayout startTimeDialogCancel, endTimeDialogCancel;
    private LinearLayout startTimeDialogSubmit, endTimeDialogSubmit;

    private long ONE_MINUTE_IN_MILLIS = 60000; //millisecs
    private SimpleDateFormat formatterDate;
    private SimpleDateFormat formatterHour;
    public String purpose = "";

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

        formatterDate = new SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN);
        formatterHour = new SimpleDateFormat("aa hh:mm", Locale.KOREAN);

        dateLayout = (RelativeLayout) v.findViewById(R.id.start_date_layout);
        dateText = (TextView) v.findViewById(R.id.start_date_txt);
        startTimeLayout = (RelativeLayout) v.findViewById(R.id.start_time_layout);
        startTimeText = (TextView) v.findViewById(R.id.start_time_txt);
        endTimeLayout = (RelativeLayout) v.findViewById(R.id.end_time_layout);
        endTimeText = (TextView) v.findViewById(R.id.end_time_txt);

        if(purpose.equals("editBehavior")) {
            date_start = formatterDate.format(BehaviorActivity.editBehavior.start_time);
            hour_start = formatterHour.format(BehaviorActivity.editBehavior.start_time);
            hour_end = formatterHour.format(BehaviorActivity.editBehavior.end_time);

            dateText.setText(date_start);
            startTimeText.setText(hour_start);
            endTimeText.setText(hour_end);
        } else {

            Date dateToday;
            Date afterAddingMins;

            if(TimerWidget.second != 0) {
                dateToday = TimerWidget.startTime;
                long curTimeInMs = dateToday.getTime();
                afterAddingMins = new Date(curTimeInMs + (TimerWidget.second * 1000));

                TimerWidget.second = 0;
                TimerWidget.startTime = null;
            } else {
                dateToday = new Date();
                long curTimeInMs = dateToday.getTime();
                afterAddingMins = new Date(curTimeInMs + (5 * ONE_MINUTE_IN_MILLIS));
            }

            date_start = formatterDate.format(dateToday);
            dateText.setText(date_start);

            hour_start = formatterHour.format(dateToday);
            hour_end = formatterHour.format(afterAddingMins);

            startTimeText.setText(hour_start);
            endTimeText.setText(hour_end);
        }

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append(year);
                        strBuf.append("년 ");
                        strBuf.append(month + 1);
                        strBuf.append("월 ");
                        strBuf.append(dayOfMonth);
                        strBuf.append("일 ");

                        SimpleDateFormat simpledateformat = new SimpleDateFormat("E요일", Locale.KOREAN);
                        Date date = new Date(year, month, dayOfMonth-1);
                        String dayOfWeek = simpledateformat.format(date);
                        strBuf.append(dayOfWeek);

                        date_start = strBuf.toString();
                        dateText.setText(date_start);
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

        AlertDialog.Builder mStartBuilder = new AlertDialog.Builder(getContext());
        View mStartView = getLayoutInflater().inflate(R.layout.dialog_timepicker_spinner, null);
        mStartBuilder.setView(mStartView);
        final AlertDialog startTimeDialog = mStartBuilder.create();
        startTimeDialog.setTitle("행동 시작 시간");

        startTimePicker = (TimePicker) mStartView.findViewById(R.id.timepicker_spinner);

        startTimeDialogCancel = (LinearLayout) mStartView.findViewById(R.id.timepicker_cancel);
        startTimeDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimeDialog.dismiss();
            }
        });

        startTimeDialogSubmit = (LinearLayout) mStartView.findViewById(R.id.timepicker_submit);
        startTimeDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startTimeAmPm = "";
                String startTimeHour = "";
                String startTimeMinute = "";

                if(startTimePicker.getCurrentHour() < 12){
                    startTimeAmPm = "오전";
                    if(startTimePicker.getCurrentHour() == 0){
                        startTimeHour = String.valueOf(startTimePicker.getCurrentHour()+12);
                    } else if(startTimePicker.getCurrentHour() < 10) {
                        startTimeHour = "0" + startTimePicker.getCurrentHour().toString();
                    } else {
                        startTimeHour = startTimePicker.getCurrentHour().toString();
                    }
                } else {
                    startTimeAmPm = "오후";
                    if(startTimePicker.getCurrentHour() < 22 && startTimePicker.getCurrentHour() != 12) {
                        startTimeHour = "0" + String.valueOf(startTimePicker.getCurrentHour()-12);
                    } else if(startTimePicker.getCurrentHour() == 12) {
                        startTimeHour = String.valueOf(startTimePicker.getCurrentHour());
                    } else {
                        startTimeHour = String.valueOf(startTimePicker.getCurrentHour()-12);
                    }
                }

                if(startTimePicker.getCurrentMinute() < 10){
                    startTimeMinute = "0" + startTimePicker.getCurrentMinute().toString();
                } else {
                    startTimeMinute = startTimePicker.getCurrentMinute().toString();
                }

                hour_start = startTimeAmPm + " " + startTimeHour + ":" + startTimeMinute;
                startTimeText.setText(hour_start);

                try {
                    Date hour_start_compare = formatterHour.parse(hour_start);
                    Date hour_end_compare = formatterHour.parse(hour_end);

                    if(hour_start_compare.compareTo(hour_end_compare) < 0) {

                    } else {
                        Date date = formatterHour.parse(hour_start);
                        long curTimeInMs = date.getTime();
                        Date afterAddingMins = new Date(curTimeInMs + (5 * ONE_MINUTE_IN_MILLIS));
                        hour_end = formatterHour.format(afterAddingMins);
                        endTimeText.setText(hour_end);
                    }

                } catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                startTimeDialog.dismiss();
            }
        });


        AlertDialog.Builder mEndBuilder = new AlertDialog.Builder(getContext());
        View mEndView = getLayoutInflater().inflate(R.layout.dialog_timepicker_spinner, null);
        mEndBuilder.setView(mEndView);
        final AlertDialog endTimeDialog = mEndBuilder.create();
        endTimeDialog.setTitle("행동 종료 시간");

        endTimePicker = (TimePicker) mEndView.findViewById(R.id.timepicker_spinner);

        endTimeDialogCancel = (LinearLayout) mEndView.findViewById(R.id.timepicker_cancel);
        endTimeDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimeDialog.dismiss();
            }
        });

        endTimeDialogSubmit = (LinearLayout) mEndView.findViewById(R.id.timepicker_submit);
        endTimeDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endTimeAmPm = "";
                String endTimeHour = "";
                String endTimeMinute = "";

                if(endTimePicker.getCurrentHour() < 12){
                    endTimeAmPm = "오전";
                    if(endTimePicker.getCurrentHour() == 0){
                        endTimeHour = String.valueOf(endTimePicker.getCurrentHour()+12);
                    } else if(endTimePicker.getCurrentHour() < 10) {
                        endTimeHour = "0" + endTimePicker.getCurrentHour().toString();
                    } else {
                        endTimeHour = endTimePicker.getCurrentHour().toString();
                    }
                } else {
                    endTimeAmPm = "오후";
                    if (endTimePicker.getCurrentHour() < 22 && endTimePicker.getCurrentHour() != 12) {
                        endTimeHour = "0" + String.valueOf(endTimePicker.getCurrentHour() - 12);
                    } else if(endTimePicker.getCurrentHour() == 12) {
                        endTimeHour = String.valueOf(endTimePicker.getCurrentHour());
                    } else {
                        endTimeHour = String.valueOf(endTimePicker.getCurrentHour()-12);
                    }
                }

                if(endTimePicker.getCurrentMinute() < 10){
                    endTimeMinute = "0" + endTimePicker.getCurrentMinute().toString();
                } else {
                    endTimeMinute = endTimePicker.getCurrentMinute().toString();
                }

                hour_end = endTimeAmPm + " " + endTimeHour + ":" + endTimeMinute;
                endTimeText.setText(hour_end);
                endTimeDialog.dismiss();
            }
        });

        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
//                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                        final StringBuffer strBufStart = new StringBuffer();
//                        AM_PM_Start = "";
//                        if(hour < 12) {
//                            AM_PM_Start = "오전";
//
//                            if(hour < 10) {
//                                strBufStart.append("0"+hour);
//                            } else {
//                                strBufStart.append(hour);
//                            }
//                        } else {
//                            AM_PM_Start = "오후";
//
//                            if(hour-12 < 10){
//                                strBufStart.append("0"+(hour-12));
//                            } else {
//                                strBufStart.append(hour-12);
//                            }
//                        }
//                        strBufStart.append(":");
//                        if(minute < 10) {
//                            strBufStart.append("0"+minute);
//                        } else {
//                            strBufStart.append(minute);
//                        }
//
//                        hour_start = AM_PM_Start + " " + strBufStart.toString();
//                        startTimeText.setText(AM_PM_Start + " " + strBufStart);
//                    }
//                };
//
//                Calendar now = Calendar.getInstance();
//                int startHour = now.get(java.util.Calendar.HOUR_OF_DAY);
//                int startMinute = now.get(java.util.Calendar.MINUTE);
//
//                // Whether show time in 24 hour format or not.
//                boolean is24Hour = false;
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, startHour, startMinute, is24Hour);
//
//                timePickerDialog.setTitle("행동 시작 시간");
//                timePickerDialog.show();

                if (hour_start.substring(0, 2).equals("오후") && !hour_start.substring(3, 5).equals("12")) {
                    startTimePicker.setCurrentHour(Integer.parseInt(hour_start.substring(3, 5)) + 12);
                } else if(hour_start.substring(0, 2).equals("오전") && hour_start.substring(3, 5).equals("12")) {
                    startTimePicker.setCurrentHour(Integer.parseInt(hour_start.substring(3, 5)) - 12);
                } else {
                    startTimePicker.setCurrentHour(Integer.parseInt(hour_start.substring(3,5)));
                }
                startTimePicker.setCurrentMinute(Integer.parseInt(hour_start.substring(6, 8)));

                startTimeDialog.show();

            }
        });

        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
//                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                        StringBuffer strBufEnd = new StringBuffer();
//                        String AM_PM_End = "";
//                        if(hour < 12) {
//                            AM_PM_End = "오전";
//
//                            if(hour < 10) {
//                                strBufEnd.append("0"+hour);
//                            } else {
//                                strBufEnd.append(hour);
//                            }
//                        } else {
//                            AM_PM_End = "오후";
//
//                            if(hour-12 < 10){
//                                strBufEnd.append("0"+(hour-12));
//                            } else {
//                                strBufEnd.append(hour-12);
//                            }
//                        }
//                        strBufEnd.append(":");
//                        if(minute < 10){
//                            strBufEnd.append("0"+minute);
//                        } else {
//                            strBufEnd.append(minute);
//                        }
//
//                        hour_end = AM_PM_End + " " + strBufEnd.toString();
//                        endTimeText.setText(AM_PM_End + " " + strBufEnd);
//                    }
//                };
//
//                Calendar now = Calendar.getInstance();
//                int endHour = now.get(java.util.Calendar.HOUR_OF_DAY);
//                int endMinute = now.get(java.util.Calendar.MINUTE);
//
//                // Whether show time in 24 hour format or not.
//                boolean is24Hour = false;
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, endHour, endMinute, is24Hour);
//
//                timePickerDialog.setTitle("행동 종료 시간");
//                timePickerDialog.show();
                if (hour_end.substring(0, 2).equals("오후") && !hour_end.substring(3, 5).equals("12")) {
                    endTimePicker.setCurrentHour(Integer.parseInt(hour_end.substring(3, 5)) + 12);
                } else if(hour_end.substring(0, 2).equals("오전") && hour_end.substring(3, 5).equals("12")) {
                    endTimePicker.setCurrentHour(Integer.parseInt(hour_end.substring(3, 5)) - 12);
                } else {
                    endTimePicker.setCurrentHour(Integer.parseInt(hour_end.substring(3,5)));
                }
                endTimePicker.setCurrentMinute(Integer.parseInt(hour_end.substring(6,8)));

                endTimeDialog.show();
            }
        });

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
