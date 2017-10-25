package com.pixcomp.sisei.siseiapp.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.pixcomp.sisei.siseiapp.R;
import com.pixcomp.sisei.siseiapp.data.dto.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos Plata on 24/10/2017.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private List<Message> messages;
    Context mContext;

    public MessageAdapter(Context context, int resource, List<Message> data) {
        super(context, resource, data);
        this.messages = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.message_layout,null);
        }

        Message p = getItem(position);

        if (p != null) {
            TextView txtMessage = (TextView) v.findViewById(R.id.row_text);
            txtMessage.setText(p.getMessage());
        }

        return v;
    }
}
