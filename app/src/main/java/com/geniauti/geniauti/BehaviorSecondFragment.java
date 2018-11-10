package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BehaviorSecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BehaviorSecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BehaviorSecondFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HashMap<String,Boolean> checkbox_type = new HashMap<String,Boolean>();

    private CheckBox checkbox_harm;
    private CheckBox checkbox_selfHarm;
    private CheckBox checkbox_destruction;
    private CheckBox checkbox_breakaway;
    private CheckBox checkbox_sexual;
    private CheckBox checkbox_etc;

    private OnFragmentInteractionListener mListener;

    public BehaviorSecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BehaviorSecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BehaviorSecondFragment newInstance(String param1, String param2) {
        BehaviorSecondFragment fragment = new BehaviorSecondFragment();
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
        View v = inflater.inflate(R.layout.fragment_behavior_second, container, false);

        checkbox_harm = (CheckBox) v.findViewById(R.id.harm);
        checkbox_selfHarm = (CheckBox) v.findViewById(R.id.selfHarm);
        checkbox_destruction = (CheckBox) v.findViewById(R.id.destruction);
        checkbox_breakaway = (CheckBox) v.findViewById(R.id.breakaway);
        checkbox_sexual = (CheckBox) v.findViewById(R.id.sexual);
        checkbox_etc = (CheckBox) v.findViewById(R.id.etc);

        checkbox_harm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                checkbox_type.put("harm", true);
                }
            }
        );
        checkbox_selfHarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                         checkbox_type.put("selfHarm", true);
                                                     }
                                                 }
        );
        checkbox_destruction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                         @Override
                                                         public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                             checkbox_type.put("destruction", true);
                                                         }
                                                     }
        );
        checkbox_breakaway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                         checkbox_type.put("breakaway", true);
                                                     }
                                                 }
        );
        checkbox_sexual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                checkbox_type.put("sexual", true);
            }
        });

        checkbox_etc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                checkbox_type.put("etc", true);
            }
        });

        // Inflate the layout for this fragment
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
