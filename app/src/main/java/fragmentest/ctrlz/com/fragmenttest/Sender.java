package fragmentest.ctrlz.com.fragmenttest;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ctrlz.opendaylight.test.OpenDayLightHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class Sender extends Fragment {

    Communicator communicator;

    public Sender() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator=(Communicator)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sender, container, false);
    }


    public void processClick() throws JSONException {
        EditText ip1= (EditText)getActivity().findViewById(R.id.ip1Text);
        EditText ip2= (EditText)getActivity().findViewById(R.id.ip2Text);
        EditText ipServer= (EditText)getActivity().findViewById(R.id.ipServerText);
        EditText ruleName= (EditText)getActivity().findViewById(R.id.ruleNameText);

        //Sample post data.
        JSONArray actions=new JSONArray();
        actions.put("DROP");

        JSONObject postData = new JSONObject();
        postData.put("name",ruleName.getText().toString());
        postData.put("nwSrc",ip1.getText().toString());
        postData.put("nwDst",ip2.getText().toString());
        postData.put("installInHw","true");
        postData.put("priority","500");
        postData.put("etherType","0x800");
        postData.put("actions",actions);

        //Node on which this flow should be installed
        JSONObject node = new JSONObject();
        node.put("id","1");
        node.put("type", "OF13");
        postData.put("node", node);

        //Actual flow install
        boolean result = OpenDayLightHelper.installFlow(postData, "admin", "admin", ipServer.getText().toString()+":8080");

        if(result){
            communicator.sendContent(ip1.getText().toString()+ " <-> "+ip2.getText().toString() + " -> X");
            System.out.println("Flow allowed Successfully");
        }else{
            System.err.println("Failed install flow!");
        }

    }
}
