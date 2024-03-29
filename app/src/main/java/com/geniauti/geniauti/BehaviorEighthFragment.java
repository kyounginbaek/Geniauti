package com.geniauti.geniauti;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BehaviorEighthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BehaviorEighthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BehaviorEighthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static int seekbarValue;
    private SeekBar seek;
    private TextView intensityOne;
    private TextView intensityTwo;
    private TextView intensityThree;
    private TextView intensityFour;
    private TextView intensityFive;

    public String purpose = "";

    private OnFragmentInteractionListener mListener;

    public BehaviorEighthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BehaviorEighthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BehaviorEighthFragment newInstance(String param1, String param2) {
        BehaviorEighthFragment fragment = new BehaviorEighthFragment();
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

        View v = inflater.inflate(R.layout.fragment_behavior_eighth, container, false);

        // Inflate the layout for this fragment
        seek = (SeekBar) v.findViewById(R.id.seekBar);

        seek.setOnSeekBarChangeListener(new yourListener());

        intensityOne = (TextView) v.findViewById(R.id.txt_behavior_intensity_one);
        intensityOne.setTextColor(Color.parseColor("#2dc76d"));
        intensityOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seek.setProgress(0);
            }
        });

        intensityTwo = (TextView) v.findViewById(R.id.txt_behavior_intensity_two);
        intensityTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seek.setProgress(1);
            }
        });
        intensityThree = (TextView) v.findViewById(R.id.txt_behavior_intensity_three);
        intensityThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seek.setProgress(2);
            }
        });
        intensityFour = (TextView) v.findViewById(R.id.txt_behavior_intensity_four);
        intensityFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seek.setProgress(3);
            }
        });
        intensityFive = (TextView) v.findViewById(R.id.txt_behavior_intensity_five);
        intensityFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seek.setProgress(4);
            }
        });

        if(purpose.equals("tmpBookmark")) {
            seek.setProgress(BehaviorActivity.tmpBookmark.intensity-1);
        }

        if(purpose.equals("editBehavior")) {
            seek.setProgress(BehaviorActivity.editBehavior.intensity-1);
        }

        if(purpose.equals("editBookmark")) {
            seek.setProgress(BookmarkActivity.editBookmark.intensity-1);
        }

        return v;
    }

    private class yourListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // Log the progress
            //set textView's text
            seekbarValue = seek.getProgress();

            intensityOne.setTextColor(Color.parseColor("#ababab"));
            intensityTwo.setTextColor(Color.parseColor("#ababab"));
            intensityThree.setTextColor(Color.parseColor("#ababab"));
            intensityFour.setTextColor(Color.parseColor("#ababab"));
            intensityFive.setTextColor(Color.parseColor("#ababab"));

            switch(seekbarValue+1){
                case 1:
                    intensityOne.setTextColor(Color.parseColor("#2dc76d"));
                    break;
                case 2:
                    intensityTwo.setTextColor(Color.parseColor("#2dc76d"));
                    break;
                case 3:
                    intensityThree.setTextColor(Color.parseColor("#2dc76d"));
                    break;
                case 4:
                    intensityFour.setTextColor(Color.parseColor("#2dc76d"));
                    break;
                case 5:
                    intensityFive.setTextColor(Color.parseColor("#2dc76d"));
                    break;
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

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
