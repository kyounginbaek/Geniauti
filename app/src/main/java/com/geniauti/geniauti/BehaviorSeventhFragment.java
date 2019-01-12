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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BehaviorSeventhFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BehaviorSeventhFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BehaviorSeventhFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HashMap<String,Boolean> checkbox_type = new HashMap<String,Boolean>();
    private BehaviorSeventhFragment.Type[] types ;
    private ArrayAdapter<BehaviorSeventhFragment.Type> listAdapter ;

    private OnFragmentInteractionListener mListener;

    public BehaviorSeventhFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BehaviorSeventhFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BehaviorSeventhFragment newInstance(String param1, String param2) {
        BehaviorSeventhFragment fragment = new BehaviorSeventhFragment();
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
        View v = inflater.inflate(R.layout.fragment_behavior_seventh, container, false);

        // Find the ListView resource.
        ListView mainListView = (ListView) v.findViewById( R.id.checkbox_listview );

        // When item is tapped, toggle checked properties of CheckBox and type.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View item,
                                     int position, long id) {
                BehaviorSeventhFragment.Type type = listAdapter.getItem( position );
                type.toggleChecked();
                BehaviorSeventhFragment.TypeViewHolder viewHolder = (BehaviorSeventhFragment.TypeViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked( type.isChecked() );
            }
        });


        // Create and populate types.
        types = (BehaviorSeventhFragment.Type[]) onRetainCustomNonConfigurationInstance() ;
        if ( types == null ) {
            types = new BehaviorSeventhFragment.Type[] {
                    new BehaviorSeventhFragment.Type("자해 행동", "selfharm"),
                    new BehaviorSeventhFragment.Type("타해 행동", "harm"),
                    new BehaviorSeventhFragment.Type("파괴 행동", "destruction"),
                    new BehaviorSeventhFragment.Type("이탈 행동", "breakaway"),
                    new BehaviorSeventhFragment.Type("성적 행동", "sexual"),
                    new BehaviorSeventhFragment.Type("기타", "etc"),
            };
        }
        ArrayList<BehaviorSeventhFragment.Type> typeList = new ArrayList<BehaviorSeventhFragment.Type>();
        typeList.addAll( Arrays.asList(types) );

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new BehaviorSeventhFragment.TypeArrayAdapter(this.getContext(), typeList);
        mainListView.setAdapter( listAdapter );

        // Inflate the layout for this fragment
        return v;
    }

    public HashMap<String, Boolean> getResult() {
        checkbox_type.clear();

        for (int i=0; i < listAdapter.getCount(); i++) {
            if (listAdapter.getItem(i).isChecked()) {
                checkbox_type.put(listAdapter.getItem(i).type, true);
            }
        }
        return checkbox_type;
    }

    /** Holds checkbox data. */
    private static class Type {
        private String name = "" ;
        private String type = "";
        private boolean checked = false ;
        public Type( String name, String type ) {
            this.name = name ;
            this.type = type;
        }
        public String getName() {
            return name;
        }
        public String getType() { return type; }
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
    private static class TypeViewHolder {
        private CheckBox checkBox ;
        public TypeViewHolder(CheckBox checkBox) {
            this.checkBox = checkBox ;
//            this.textView = textView ;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    /** Custom adapter for displaying an array of type objects. */
    private static class TypeArrayAdapter extends ArrayAdapter<BehaviorSeventhFragment.Type> {

        private LayoutInflater inflater;

        public TypeArrayAdapter( Context context, List<BehaviorSeventhFragment.Type> typeList ) {
//            super( context, R.layout.list_checkbox, R.id.rowTextView, typeList );
            super( context, R.layout.list_checkbox, typeList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // type to display
            BehaviorSeventhFragment.Type type = this.getItem( position );

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
                convertView.setTag( new BehaviorSeventhFragment.TypeViewHolder(checkBox) );

                if(BehaviorActivity.bookmarkState == true && BehaviorActivity.tmpBookmark != null) {
                    Iterator it = BehaviorActivity.tmpBookmark.type.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        if(type.type.equals(pair.getKey().toString())) {
                            type.setChecked(true);
                        }
                    }
                }

                if(BehaviorActivity.editBehaviorState == true && BehaviorActivity.editBehavior != null) {
                    Iterator it = BehaviorActivity.editBehavior.type.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        if(type.type.equals(pair.getKey().toString())) {
                            type.setChecked(true);
                        }
                    }
                }

                if(BookmarkActivity.editBookmarkState == true && BookmarkActivity.editBookmark != null) {
                    Iterator it = BookmarkActivity.editBookmark.type.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        if(type.type.equals(pair.getKey().toString())) {
                            type.setChecked(true);
                        }
                    }
                }

                // If CheckBox is toggled, update the type it is tagged with.
                checkBox.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        BehaviorSeventhFragment.Type type = (BehaviorSeventhFragment.Type) cb.getTag();
                        type.setChecked( cb.isChecked() );
                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                BehaviorSeventhFragment.TypeViewHolder viewHolder = (BehaviorSeventhFragment.TypeViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
//                textView = viewHolder.getTextView() ;
            }

            // Tag the CheckBox with the type it is displaying, so that we can
            // access the type in onClick() when the CheckBox is toggled.
            checkBox.setTag( type );

            // Display type data


            checkBox.setChecked( type.isChecked() );
            checkBox.setText(type.getName());
//            textView.setText( type.getName() );

            return convertView;
        }

    }

    public Object onRetainCustomNonConfigurationInstance () {
        return types ;
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
