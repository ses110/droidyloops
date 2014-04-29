package io.github.ses110.dloops.arranger;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.ses110.dloops.R;
import io.github.ses110.dloops.models.Channel;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link io.github.ses110.dloops.arranger.ArrangerFragment.ArrangerFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ArrangerFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ArrangerFragment extends Fragment implements OnLongClickListener, TextView.OnEditorActionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String TAG = "ARRANGER";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrangerFragmentListener mListener;

    private TrackGrid mTrackGridView;

    private ArrayList<Channel> mChannels;

    private HashMap<TableRow, Channel> mRowToChannel;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArrangerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArrangerFragment newInstance(String param1, String param2) {
        ArrangerFragment fragment = new ArrangerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ArrangerFragment() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_arranger, container, false);
        mTrackGridView = (TrackGrid) v.findViewById(R.id.track_grid);
        ArrayList<TableRow> rows = mTrackGridView.returnRows();

        mChannels = new ArrayList<Channel>();
        mRowToChannel = new HashMap<TableRow, Channel>();

        for(TableRow row : rows) {
            EditText labelCell = (EditText) row.getChildAt(0);

            String label = labelCell.getText().toString();

            Channel chan = new Channel(label);
            Log.v(TAG, "Creating new channel with name  \"" + chan.name + "\" linked to row id: " + row.getId());

            mChannels.add(chan);
            mRowToChannel.put(row, chan);
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.createNewLoop(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ArrangerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ArrangerFragmentListener");
        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mTrackGridView = null;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTrackGridView.destroyDrawingCache();
        mTrackGridView = null;
    }

    @Override
    public boolean onLongClick(View view) {
        mTrackGridView.addNewLoopCell(view);
        return true;
    }

    /*
    *       Handle Channel label edittext changes
    *
    * */

    @Override
    public boolean onEditorAction(TextView textView, int actionID, KeyEvent event) {
        if(event != null && event.getAction() != KeyEvent.ACTION_DOWN)
            return false;
        else if (actionID == EditorInfo.IME_ACTION_SEARCH
                || actionID == EditorInfo.IME_ACTION_DONE
                || event != null
                || event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            TableRow parentRow = (TableRow) textView.getParent();
            Channel thisChannel = mRowToChannel.get(parentRow);
            thisChannel.name = ((EditText) textView).getText().toString();
            Log.v(TAG, "Updated channel name to " + thisChannel.name);

            return false;
        }
        return false;
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
    public interface ArrangerFragmentListener {
        // TODO: Update argument type and name
        public void createNewLoop(Uri uri);
    }

}
