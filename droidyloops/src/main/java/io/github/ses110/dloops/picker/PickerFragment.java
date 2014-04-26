package io.github.ses110.dloops.picker;

import android.app.Activity;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import io.github.ses110.dloops.MainActivity;
import io.github.ses110.dloops.R;
import io.github.ses110.dloops.models.Loop;
import io.github.ses110.dloops.models.Sample;
import io.github.ses110.dloops.models.Song;
import io.github.ses110.dloops.utils.FileHandler;
import io.github.ses110.dloops.utils.FileHandler.FileType;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {Callbacks}
 * interface.
 */
public class PickerFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILETYPE = "fileType";
    private static final String TAG = "PICKER";

    private FileType fileType;

    private PickerFragmentListener mListener;

    private SoundPool mSP;
    public boolean mSoundsLoaded;
    private int[] mSounds;

    private ArrayList<Loop> loops;
    private ArrayList<Song> songs;
    private ArrayList<Sample> samples;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    public static PickerFragment newInstance(FileType fileType) {
        PickerFragment fragment = new PickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("fileType", fileType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PickerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileType = (FileType) getArguments().getSerializable("fileType");

            try {
                switch (fileType) {
                    case SAMPLES:
                        samples = new Sample("", "").loadList(new FileHandler(getActivity()));
                        Log.v("Got list", Integer.toString(samples.size()));
                        mAdapter = new ArrayAdapter<Sample>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, samples);
                        break;
                    case LOOPS:
                        // TODO:
                        loops = new ArrayList<Loop>();
                        mAdapter = new ArrayAdapter<Loop>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, loops);
                        break;
                    case SONGS:
                        //TODO:
                        songs = new ArrayList<Song>();
                        mAdapter = new ArrayAdapter<Song>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, songs);
                        break;
                }
            }
            catch (JSONException e)
            {
                Log.e("LOAD LIST", "JSONException!");
            }
            catch (FileNotFoundException e)
            {
                Log.e("LOAD LIST", "File not Found!");
            }
            catch (ParseException e)
            {
                Log.e("LOAD LIST", "Malformed file!");
            }

            mSP.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                    Log.v("PickerFragment", "Sound loaded.");
                    ((MainActivity)getActivity()).closeDialog(i);
                }
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        switch(fileType)
        {
            case SAMPLES:
                ((TextView) view.findViewById(R.id.listText1)).setText("Select a sample");
                break;
            case LOOPS:
                ((TextView) view.findViewById(R.id.listText1)).setText("Select a loop");
                break;
            case SONGS:
                ((TextView) view.findViewById(R.id.listText1)).setText("Select a song");
                break;
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PickerFragmentListener) activity;
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
    public void attachSoundPool(SoundPool _sp) {
        this.mSP = _sp;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            switch (fileType)
            {
                case SAMPLES:
                    mListener.onPickerSelection(samples.get(position));
                    break;
                case LOOPS:
                    mListener.onPickerSelection(loops.get(position));
                    break;
                case SONGS:
                    mListener.onPickerSelection(songs.get(position));
                    break;
            }
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
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
    public interface PickerFragmentListener {
        // TODO: Update argument type and name
        public void onPickerSelection(Sample sample);

        public void onPickerSelection(Loop loop);

        public void onPickerSelection(Song song);
    }

}
