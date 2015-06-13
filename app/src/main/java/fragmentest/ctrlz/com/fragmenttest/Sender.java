package fragmentest.ctrlz.com.fragmenttest;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
        //values from the GUI are retrieved
        EditText ruleName= (EditText)getActivity().findViewById(R.id.ruleNameText);
        EditText ip1= (EditText)getActivity().findViewById(R.id.ip1Text);
        EditText ip2= (EditText)getActivity().findViewById(R.id.ip2Text);
        EditText ipServer= (EditText)getActivity().findViewById(R.id.ipServerText);

        //TODO validate all the information before creating the JSON

        //List of actions associated to the rule we are about to create
        //In this example, all packages will be dropped
        JSONArray actions=new JSONArray();
        actions.put("DROP");

        //We create the JSON with all the information required for creating the rule
        JSONObject postData = new JSONObject();
        postData.put("name",ruleName.getText().toString()); //name of the rule
        postData.put("nwSrc",ip1.getText().toString());     //ip source
        postData.put("nwDst",ip2.getText().toString());     //ip destination
        postData.put("installInHw","true");                 //install the rule in the switch
        postData.put("priority","500");
        postData.put("etherType","0x800");                  //ethernet type = ipv4
        postData.put("actions",actions);                    //list of actions within the rule

        //Node on which this flow should be installed
        JSONObject node = new JSONObject();
        node.put("id","1");             //identifier of the switch in the controller
        node.put("type", "OF13");       //version of OpenFlow: OF13 (v 1.3)
        postData.put("node", node);

        //Actual flow install using user authentication: admin
        boolean result = OpenDayLightHelper.installFlow(postData, "admin", "admin", ipServer.getText().toString()+":8080");

        if(result){ //if it is correctly installed we put it in the list
            communicator.sendContent(ip1.getText().toString()+ " <-> "+ip2.getText().toString() + " -> X");
            System.out.println("Flow allowed Successfully");
        }else{
            System.err.println("Failed to install flow!");
        }

    }
}
