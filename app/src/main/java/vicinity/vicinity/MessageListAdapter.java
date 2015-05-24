package vicinity.vicinity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vicinity.Controller.MainController;
import vicinity.model.Neighbor;
import vicinity.model.VicinityMessage;

/**
 * This class takes a list of messages and turns it into a list
 * that displays all the current chats the user has
 */
public class MessageListAdapter  extends BaseAdapter {
    ArrayList<VicinityMessage> vicinityMessages;
    private LayoutInflater mInflater;
    private MainController controller;



    public MessageListAdapter(Context context, ArrayList<VicinityMessage> vicinityMessages){
        this.vicinityMessages = vicinityMessages;
        mInflater = LayoutInflater.from(context);
        controller = new MainController(context);
    }

    @Override
    public int getCount() {
        return vicinityMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return vicinityMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null ){
            convertView = mInflater.inflate(R.layout.messages_row_view, null);


            holder = new ViewHolder();
            holder.textDate = (TextView) convertView.findViewById(R.id.date);
            holder.textName = (TextView) convertView.findViewById(R.id.friendName);
            holder.textMessage = (TextView) convertView.findViewById(R.id.latestMessage);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


            holder.textDate.setText(vicinityMessages.get(position).getDate());
            holder.textMessage.setText(vicinityMessages.get(position).getMessageBody());
            holder.textName.setText(controller.getFriend(vicinityMessages.get(position).getFrom()).getInstanceName());




        return convertView;
    }

    static class ViewHolder{
        TextView textDate, textMessage,textName;
    }

    public void clearAdapter(){
        vicinityMessages.clear();
    }
}
