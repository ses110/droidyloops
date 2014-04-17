package io.github.ses110.dloops.picker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ses110.dloops.R;

import io.github.ses110.dloops.models.Loop;
import io.github.ses110.dloops.models.Sample;
import io.github.ses110.dloops.models.Song;
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
public class FileFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILETYPE = "fileType";

    private FileType fileType;

    private PickerFragmentListener mListener;

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

    // TODO: Rename and change types of parameters
    public static FileFragment newInstance(FileType fileType) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        args.putSerializable("fileType", fileType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fileType = (FileType) getArguments().getSerializable("fileType");

            switch (fileType)
            {
                case SAMPLES:
                    samples = Sample.loadSamples(null);
                    mAdapter = new ArrayAdapter<Sample>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, samples);
                    break;
                case LOOPS:
                    loops = Loop.loadLoops();
                    mAdapter = new ArrayAdapter<Loop>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, loops);
                    break;
                case SONGS:
                    songs = Song.loadSongs();
                    mAdapter = new ArrayAdapter<Song>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, songs);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
