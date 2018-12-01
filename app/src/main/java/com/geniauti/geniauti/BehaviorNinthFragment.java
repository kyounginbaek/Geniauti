package com.geniauti.geniauti;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BehaviorNinthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BehaviorNinthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BehaviorNinthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HashMap<String,Boolean> checkbox_reason = new HashMap<String,Boolean>();
    private Reason[] reasons ;
    private ArrayAdapter<Reason> listAdapter ;

    private OnFragmentInteractionListener mListener;

    public BehaviorNinthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BehaviorNinthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BehaviorNinthFragment newInstance(String param1, String param2) {
        BehaviorNinthFragment fragment = new BehaviorNinthFragment();
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
        View v = inflater.inflate(R.layout.fragment_behavior_ninth, container, false);

        // Find the ListView resource.
        ListView mainListView = (ListView) v.findViewById( R.id.checkbox_listview );

        // When item is tapped, toggle checked properties of CheckBox and Reason.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View item,
                                     int position, long id) {
                Reason reason = listAdapter.getItem( position );
                reason.toggleChecked();
                ReasonViewHolder viewHolder = (ReasonViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked( reason.isChecked() );
            }
        });


        // Create and populate reasons.
        reasons = (Reason[]) onRetainCustomNonConfigurationInstance() ;
        if ( reasons == null ) {
            reasons = new Reason[] {
                    new Reason("자신에게 관심을 갖는 것이 좋아서", "관심"),
                    new Reason("타인에게 주목 받는 것을 즐겨서", "관심"),
                    new Reason("관심의 대상이 되고 싶어서", "관심"),
                    new Reason("다른 사람의 관심을 끌려고", "관심"),
                    new Reason("행동을 통해 얻는 감각이 좋아서", "자기자극"),
                    new Reason("행동을 하는 것 자체가 좋아서", "자기자극"),
                    new Reason("행동이 주는 자극을 얻으려고", "자기자극"),
                    new Reason("원하는 것을 즉각 얻지 못해서", "요구"),
                    new Reason("원하는 물건을 가질 수 없어서", "요구"),
                    new Reason("어떤 물건(장난감)을 갖기 위해서", "요구"),
                    new Reason("본인이 갖고 싶은 물건을 얻으려고", "요구"),
                    new Reason("시키는 일을 거부하려고", "과제회피"),
                    new Reason("하려고 한 일이 힘들어서", "과제회피"),
                    new Reason("주어진 일을 하기 싫어서", "과제회피"),
                    new Reason("시키는 일이 어려워 피하려고", "과제회피"),
            };
        }
        ArrayList<Reason> reasonList = new ArrayList<Reason>();
        reasonList.addAll( Arrays.asList(reasons) );

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new ReasonArrayAdapter(this.getContext(), reasonList);
        mainListView.setAdapter( listAdapter );


        // Inflate the layout for this fragment
        return v;
    }

    public HashMap<String, Boolean> getResult() {
        checkbox_reason.clear();

        for (int i=0; i < listAdapter.getCount(); i++) {
            if (listAdapter.getItem(i).isChecked()) {
                if(checkbox_reason.get(listAdapter.getItem(i).reason)!=null){

                } else {
                    checkbox_reason.put(listAdapter.getItem(i).reason, true);
                }
            }
        }
        return checkbox_reason;
    }

    /** Holds checkbox data. */
    private static class Reason {
        private String name = "" ;
        private String reason = "";
        private boolean checked = false ;
        public Reason() {}
        public Reason( String name, String reason ) {
            this.name = name ;
            this.reason = reason;
        }
        public Reason( String name, boolean checked ) {
            this.name = name ;
            this.checked = checked ;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isChecked() {
            return checked;
        }
        public void setChecked(boolean checked) {
            this.checked = checked;
        }
        public String toString() {
            return name ;
        }
        public void toggleChecked() {
            checked = !checked ;
        }
    }

    /** Holds child views for one row. */
    private static class ReasonViewHolder {
        private CheckBox checkBox ;
        private TextView textView ;
        public ReasonViewHolder() {}
        public ReasonViewHolder(CheckBox checkBox ) {
            this.checkBox = checkBox ;
//            this.textView = textView ;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    /** Custom adapter for displaying an array of Reason objects. */
    private static class ReasonArrayAdapter extends ArrayAdapter<Reason> {

        private LayoutInflater inflater;

        public ReasonArrayAdapter( Context context, List<Reason> reasonList ) {
//            super( context, R.layout.list_checkbox, R.id.rowTextView, reasonList );
            super( context, R.layout.list_checkbox, reasonList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Reason to display
            Reason reason = this.getItem( position );

            // The child views in each row.
            CheckBox checkBox ;

            // Create a new row view
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.list_checkbox, null);

                // Find the child views.
//                textView = (TextView) convertView.findViewById( R.id.rowTextView );
                checkBox = (CheckBox) convertView.findViewById( R.id.rowCheckBox );

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag( new ReasonViewHolder(checkBox) );

                // If CheckBox is toggled, update the reason it is tagged with.
                checkBox.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Reason reason = (Reason) cb.getTag();
                        reason.setChecked( cb.isChecked() );
                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                ReasonViewHolder viewHolder = (ReasonViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
//                textView = viewHolder.getTextView() ;
            }

            // Tag the CheckBox with the Reason it is displaying, so that we can
            // access the reason in onClick() when the CheckBox is toggled.
            checkBox.setTag( reason );

            // Display reason data
            checkBox.setChecked( reason.isChecked() );
            checkBox.setText(reason.getName());
//            textView.setText( reason.getName() );

            return convertView;
        }

    }

    public Object onRetainCustomNonConfigurationInstance () {
        return reasons ;
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
