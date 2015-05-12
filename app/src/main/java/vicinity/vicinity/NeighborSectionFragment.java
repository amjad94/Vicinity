package vicinity.vicinity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Iterator;

import vicinity.ConnectionManager.ConnectAndDiscoverService;
import vicinity.model.Neighbor;
import vicinity.Controller.MainController;

/**
 * Neighbors tab
 */
public class NeighborSectionFragment extends Fragment {

    public final String TAG = "Neighbors";

    private Context ctx;
    private static ArrayList<Neighbor> listOfServices;
    private static ArrayList<Neighbor> friendServices;
    private MainController controller;
    private ListView lvn;
    private ListView lvf;
    private static NeighborListAdapter neighborListAdapter;
    private static FriendListAdapter friendListAdapter;



    public interface DeviceClickListener {
        public void connectP2p(Neighbor wifiP2pService);
        public void chatWithFriend(Neighbor neighbor);

    }

    public NeighborSectionFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            Log.i(TAG, "OnAttach");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimelineInterface");
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "OnDetach");
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState");
        if(listOfServices.size()!=0){
            //outState.putParcelableArrayList("Neighbors",listOfServices);
        }

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"onActivityCreated");
        setRetainInstance(true);
        if (savedInstanceState != null) {
            Log.i(TAG,"SavedInstance!=null");
           //Restore state here
            //listOfServices= savedInstanceState.getParcelableArrayList("Neighbors");
            neighborListAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_neighbor, container, false);


        controller = new MainController(getActivity());
        listOfServices = new ArrayList<Neighbor>();
        friendServices = new ArrayList<Neighbor>();
        ctx = this.getActivity();

        lvn = (ListView) rootView.findViewById(R.id.listNeighbors);
        lvf = (ListView) rootView.findViewById(R.id.listFriends);

        neighborListAdapter = new NeighborListAdapter(ctx, listOfServices);
        friendListAdapter = new FriendListAdapter(ctx, friendServices);


        ConnectAndDiscoverService.setNAdapter(neighborListAdapter);
        ConnectAndDiscoverService.setFAdapter(friendListAdapter);
        View nHeader = inflater.inflate(R.layout.neighbor_header, null);
        View fHeader = inflater.inflate(R.layout.friend_header, null);

        lvn.addHeaderView(nHeader);
        lvf.addHeaderView(fHeader);

        lvn.setAdapter(neighborListAdapter);
        lvf.setAdapter(friendListAdapter);
        lvn.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"Clicked: "+neighborListAdapter.getItem(position).toString()) ;
                final Neighbor neighbor = (Neighbor) neighborListAdapter.getItem(position);
                ((DeviceClickListener) ConnectAndDiscoverService.ctx).connectP2p(neighbor);
            }
        });



        return rootView;
    }

    public static void updateDeletedFriend(Neighbor deletedFriend){
                friendServices.remove(deletedFriend);
                listOfServices.add(deletedFriend);
                friendListAdapter.notifyDataSetChanged();
                neighborListAdapter.notifyDataSetChanged();
            }

    /**
     * This method is used after sending/accepting a friend request
     * it updates friends list with the new friend
     * and removes the peer from the neighbors list
     * @param neighbor A Neighbor object
     */
    public static void updateAddedFriend(Neighbor neighbor){
            friendServices.add(neighbor);
            friendListAdapter.notifyDataSetChanged();
             Iterator<Neighbor> it = listOfServices.iterator();
             while (it.hasNext()) {
             Neighbor user = it.next();
             if (user.getDeviceAddress().equals(neighbor.getDeviceAddress())) {
                it.remove();
             }
             }
            neighborListAdapter.notifyDataSetChanged();
        }

    /**
     * This method is used to update friends list
     * from the service dynamically
     * @param friend A Neighbor object that contains an online friend
     */
    public static void addToFriendsList(Neighbor friend){
        friendServices.add(friend);
        friendListAdapter.notifyDataSetChanged();
    }

    /**
     * This method is used to update neighbors list
     * from the service dynamically
     * @param neighbor A Neighbor object that contains an online peer
     */
    public static void addToNeighborsList(Neighbor neighbor){
        listOfServices.add(neighbor);
        neighborListAdapter.notifyDataSetChanged();
    }

}