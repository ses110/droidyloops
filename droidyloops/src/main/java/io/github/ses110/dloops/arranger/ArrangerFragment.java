package io.github.ses110.dloops.arranger;

import android.app.Activity;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.targets.ActionViewTarget;
import com.espian.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.ses110.dloops.MainActivity;
import io.github.ses110.dloops.R;
import io.github.ses110.dloops.models.Channel;
import io.github.ses110.dloops.models.Loop;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link io.github.ses110.dloops.arranger.ArrangerFragment.ArrangerFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ArrangerFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ArrangerFragment extends Fragment implements View.OnClickListener, OnLongClickListener, TextView.OnEditorActionListener, View.OnDragListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String TAG = "ARRANGER";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrangerFragmentListener mListener;

    public TrackGrid mTrackGridView;

    private ArrayList<Channel> mChannels;

    private HashMap<TableRow, Channel> mRowToChannel;
    public Menu mMenu;
    View curView;

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

        setHasOptionsMenu(true);

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

        //      Associate row(s) with channels
        for(TableRow row : rows) {
            createNewChannel(row);
        }


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedStateInstance) {
        super.onActivityCreated(savedStateInstance);

        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        isFirstRun = true;
        if (isFirstRun)
        {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();

            ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
            co.hideOnClickOutside = true;

            Button getStarted = new Button(getActivity());
            ActionViewTarget target = new ActionViewTarget(getActivity(), ActionViewTarget.Type.TITLE);
            ShowcaseView.insertShowcaseView(target, getActivity(), "Welcome to DroidyLoops!", "Touch button to begin", co);

            View someColumn = mTrackGridView.getRow(100).getChildAt(2);
            ViewTarget colTarget = new ViewTarget(someColumn);
            ShowcaseView.insertShowcaseView(colTarget, getActivity(), "Press and hold on a column", "Press and hold one of these cells to create loops.");

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.arranger, menu);
        mMenu = menu;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addChannel:
                createNewChannel(mTrackGridView.createNewRow());
                return true;
            case R.id.playSong:
                ((MainActivity)getActivity()).onPlayArranger();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewChannel(TableRow row) {
        EditText labelCell = (EditText) row.getChildAt(0);

        String label = labelCell.getText().toString();

        Channel chan = new Channel(label, mTrackGridView.getColSpan());
        Log.v(TAG, "Creating new channel with name  \"" + chan.name + "\" linked to row id: " + row.getId());

        mChannels.add(chan);
        mRowToChannel.put(row, chan);

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "onDetach");
        mListener = null;
        mTrackGridView = null;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView");
        mTrackGridView.destroyDrawingCache();
        mTrackGridView = null;
    }


    @Override
    public boolean onLongClick(View view) {
        if(view instanceof ImageView) {
            //Clicked on an empty cell
            curView = view;
            TableRow parentRow = (TableRow)view.getParent();
            Channel getChannel = mRowToChannel.get(parentRow);
            int index = mTrackGridView.getCellIdForChannel(view);

            //TODO: call Looper fragment. On return, get a loop and assign it to the channel
            Log.v(TAG, "ID normalized: " + index);

            Log.v(TAG, "Channel's name is  " + getChannel.name);

            ((MainActivity)this.getActivity()).newLoopClick(mChannels.indexOf(getChannel), index);
            return true;
        }
        else
        if (view instanceof TrackView) {
            //Clicked on a loop
            Log.v("Arranger", "Clicked on a trackview");
            final ClipData data = ClipData.newPlainText("cellID", view.getId() + "");
            View.DragShadowBuilder pieceDragShadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, pieceDragShadowBuilder, view, 0);
            return true;
        }
        return false;
    }

    public void addLoop(int channel, int cell, Loop loop)
    {
        Channel curChannel = mChannels.get(channel);
        curChannel.addLoop(cell, loop);
        mTrackGridView.createLoopCell(curView);
        curView = null;
    }


    @Override
    public void onClick(View view) {
        Log.v("Arranger", "Detected a click");
        TableRow parentRow = (TableRow)view.getParent();
        Channel getChannel = mRowToChannel.get(parentRow);
        int index = mTrackGridView.getCellIdForChannel(view);
        ((MainActivity) getActivity()).loadLoopClick(getChannel.loops.get(index));
    }


    /***
     *   Handle onDrag changes for TrackViews
     */
    @Override
    public boolean onDrag(View dropOnCell, DragEvent event) {
        //TODO: Finish handling drag-drop
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.v("DragDrop","Drag Started");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.v("DragDrop","Drag ENTERED");
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.v("DragDrop","Drag EXITED");
                break;
            case DragEvent.ACTION_DROP:
                //TODO: Handle three cases:
                //              It was dropped on itself, do nothing.
                //              It was dropped on another TrackView: do nothing? or replace?
                //              It was dropped on an empty cell: replace empty cell with new TrackView, and delete the old trackView and replace with empty cell
                Log.v("DragDrop","Drag ACTION DROP");
                View originalCell = (View)event.getLocalState();

                TableRow originalRow = (TableRow) originalCell.getParent();
                TableRow dropOnRow = (TableRow) dropOnCell.getParent();

                //Update the view.
                mTrackGridView.createLoopCell(dropOnCell);

                //Update the model (change their channels)
                Channel originalChannel = mRowToChannel.get(originalRow);
                Channel dropOnChannel = mRowToChannel.get(dropOnRow);

                int IndexOriginal = mTrackGridView.getCellIdForChannel(originalCell);
                int IndexDropCell = mTrackGridView.getCellIdForChannel(dropOnCell);

                Log.v("Arranger DRAG", "Reference to original loop: " + originalChannel.getLoop(IndexOriginal));
                Log.v("Arranger DRAG", "Reference to dropOn loop: " + dropOnChannel.getLoop(IndexDropCell));
                Loop LoopOriginal = originalChannel.getLoop(IndexOriginal);
                Loop LoopDropCell = dropOnChannel.getLoop(IndexDropCell);

                //originalChannel.setLoop(IndexOriginal, LoopDropCell);
                dropOnChannel.setLoop(IndexDropCell, LoopOriginal);

                Log.v("Arranger DRAG", "Reference to original loop: " + originalChannel.getLoop(IndexOriginal));
                Log.v("Arranger DRAG", "Reference to dropOn loop: " + dropOnChannel.getLoop(IndexDropCell));


                break;
        }
        return true;
    }

    /*
    *       Handle Channel label EditText changes
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


    public int length()
    {
        int max = 0;
        for (int i = 0; i < mChannels.size(); i++)
        {
            int cur = mChannels.get(i).length();
            if (cur > max)
                max = cur;
        }
        return max;
    }

    public ArrayList<Integer> curSamples(int colOffset, int loopOffset)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Channel cur : mChannels)
        {
            int[] temp = cur.curSamples(colOffset, loopOffset);
            if(temp != null) {
                for (int i : temp)
                    result.add(i);
            }
        }
        return result;
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
