package io.github.ses110.dloops.looper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import org.json.JSONException;

import io.github.ses110.dloops.MainActivity;
import io.github.ses110.dloops.R;
import io.github.ses110.dloops.models.Loop;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link io.github.ses110.dloops.looper.LooperFragment.LooperFragmentListener} interface
 * to handle interaction events.
 * Use the {@link LooperFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LooperFragment extends Fragment implements OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String tag = "LOOPER";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //TODO: make this thing restore everything on resume
    private LooperFragmentListener mListener;
    private int channel;
    private int cell;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LooperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LooperFragment newInstance(String param1, String param2) {
        LooperFragment fragment = new LooperFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public LooperFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.v(tag, "onCreate");
    }

    public void setChannel(int channel, int cell)
    {
        this.channel = channel;
        this.cell = cell;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(tag, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(tag, "onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_looper, container, false);
        v.findViewById(R.id.add_button).setOnClickListener(this);
        v.findViewById(R.id.bpm_button).setOnClickListener(this);
        v.findViewById(R.id.save_button).setOnClickListener(this);
        v.findViewById(R.id.play_button).setOnClickListener(this);

//        container.addView(((MainActivity)getActivity()).mProgressBar);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Loop loop) {
        if (mListener != null) {
            mListener.getLoop(loop);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (LooperFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ArrangerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.add_button:
                ((MainActivity)getActivity()).newLoopRow(view);
                break;
            case R.id.bpm_button:
                ((MainActivity)getActivity()).changeBpmDialog(view);
                break;
            case R.id.save_button:
                try {
                    ((MainActivity)getActivity()).saveLoop(channel, cell);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.play_button:
                ((MainActivity)getActivity()).startPlay(view);
                break;
        }
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
    public interface LooperFragmentListener {
        // TODO: Update argument type and name
        public void getLoop(Loop loop);
    }

}
