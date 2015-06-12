package fragmentest.ctrlz.com.fragmenttest;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Receiver extends Fragment {

    ListView list;
    ArrayAdapter<String> adapter;
    List<String> elements;

    public Receiver() {
        // Required empty public constructor
        elements=new ArrayList<String>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,elements);
        // Inflate the layout for this fragment
        View fragmentView= inflater.inflate(R.layout.fragment_receiver, container, false);
        list=(ListView)fragmentView.findViewById(R.id.listView);
        list.setAdapter(adapter);
        return fragmentView;
    }


    public void receiveData(String content) {
        elements.add(content);
        Toast.makeText(this.getActivity(), "Regla agregada correctamente:\n["+content+"]",Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
    }
}
