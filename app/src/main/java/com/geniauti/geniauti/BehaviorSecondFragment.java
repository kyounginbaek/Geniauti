package com.geniauti.geniauti;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


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

    private View v;
    private GridListAdapter adapter;
    private ArrayList<String> arrayList;
    public ListView listView;

    public String location;
    private EditText txtLocation;

    private LinearLayout locationDialog;
    private LinearLayout locationCancel;
    private LinearLayout locationAdd;

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

        if (v != null) {
            if ((ViewGroup) v.getParent() != null)
                ((ViewGroup) v.getParent()).removeView(v);
            return v;
        }
        v = inflater.inflate(R.layout.fragment_behavior_second, container, false);

        listView = (ListView) v.findViewById(R.id.radio_listview_first);
        arrayList = new ArrayList<>();

        arrayList.add("집");
        arrayList.add("마트");
        arrayList.add("식당");
        arrayList.add("학교");
        arrayList.add("장소 추가하기");

        adapter = new BehaviorSecondFragment.GridListAdapter(this.getContext(), arrayList, true);
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

    public String getResult() {
        int i = adapter.selectedPosition;

        if(i == -1){
            return "";
        } else {
            return adapter.getItem(i).toString();
        }
    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.rowRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected position
//                adapter.getSelectedItem();
            }
        });
//        view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Delete the selected position
//                adapter.deleteSelectedPosition();
//            }
//        });
    }

    public class GridListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> arrayList;
        private LayoutInflater inflater;
        private boolean isListView;
        private int selectedPosition = -1;

        public GridListAdapter(Context context, ArrayList<String> arrayList, boolean isListView) {
            this.context = context;
            this.arrayList = arrayList;
            this.isListView = isListView;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if(i == arrayList.size()-1){
                viewHolder = new ViewHolder();
                view = inflater.inflate(R.layout.list_addlist, viewGroup, false);
                viewHolder.addListText = (TextView) view.findViewById(R.id.add_list_txt);
                viewHolder.addListLayout = (LinearLayout) view.findViewById(R.id.add_list_layout);

                view.setTag(viewHolder);
            } else {
                if (view == null) {
                    viewHolder = new ViewHolder();

                    view = inflater.inflate(R.layout.list_radio, viewGroup, false);
                    viewHolder.radioButton = (RadioButton) view.findViewById(R.id.rowRadioButton);
                    if(i > 3) {
                        viewHolder.radioDelete = (ImageView) view.findViewById(R.id.rowRadioDelete);
                        viewHolder.radioDelete.setVisibility(View.VISIBLE);
                    }

                    view.setTag(viewHolder);
                } else
                    viewHolder = (ViewHolder) view.getTag();
            }

            if (i == arrayList.size()-1) {
                viewHolder.addListText.setText("장소 추가하기");
                viewHolder.addListLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        itemCheckChanged(v);

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                        View mView = getLayoutInflater().inflate(R.layout.dialog_location_add, null);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                        dialog.show();

                        txtLocation = (EditText) mView.findViewById(R.id.txt_location);

                        locationCancel = (LinearLayout) mView.findViewById(R.id.location_cancel);
                        locationCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        locationAdd = (LinearLayout) mView.findViewById(R.id.location_add);
                        locationAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean cancel = false;
                                String location = txtLocation.getText().toString().trim();

                                if(location.equals("집") || location.equals("마트") || location.equals("식당") || location.equals("학교")) {
                                    Toast.makeText(getActivity(), "이미 등록되어 있는 장소입니다.", Toast.LENGTH_SHORT).show();
                                    cancel = true;
                                }

                                if(location.equals("")) {
                                    Toast.makeText(getActivity(), "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    cancel = true;
                                }

                                if(cancel){

                                } else {
                                    adapter.selectedPosition = arrayList.size() - 1;
                                    arrayList.add(arrayList.size() - 1, txtLocation.getText().toString());
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
            } else {
                //check the radio button if both position and selectedPosition matches
                viewHolder.radioButton.setChecked(i == selectedPosition);
                viewHolder.radioButton.setText(arrayList.get(i));

                //Set the position tag to both radio button and label
                viewHolder.radioButton.setTag(i);

                viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemCheckChanged(v);
                    }
                });

                if(i > 3) {
                    viewHolder.radioDelete.setTag(i);
                    viewHolder.radioDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(adapter.selectedPosition == i){
                                adapter.selectedPosition = -1;
                            } else if(adapter.selectedPosition > i) {
                                adapter.selectedPosition += -1;
                            }
                            arrayList.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            return view;
        }

        //On selecting any view set the current position to selectedPositon and notify adapter
        private void itemCheckChanged(View v) {
            selectedPosition = (Integer) v.getTag();
            notifyDataSetChanged();
        }

        private class ViewHolder {
            private TextView addListText;
            private LinearLayout addListLayout;
            private RadioButton radioButton;
            private ImageView radioDelete;
        }

        //Return the selectedPosition item
//        public String getSelectedItem() {
//            if (selectedPosition != -1) {
//                Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition), Toast.LENGTH_SHORT).show();
//                return arrayList.get(selectedPosition);
//            }
//            return "";
//        }

        //Delete the selected position from the arrayList
//        public void deleteSelectedPosition() {
//            if (selectedPosition != -1) {
//                arrayList.remove(selectedPosition);
//                selectedPosition = -1;//after removing selectedPosition set it back to -1
//                notifyDataSetChanged();
//            }
//        }
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
