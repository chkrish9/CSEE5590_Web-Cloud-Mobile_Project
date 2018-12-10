package com.umkc.smartqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<UserDetailsModel> contacts;
    ArrayList<UserDetailsModel> contactList;

    ContactListAdapter(Context c, List<UserDetailsModel> contacts){
        this.context =c;
        this.contacts = contacts;
        this.layoutInflater = LayoutInflater.from(c);
        this.contactList = new ArrayList<UserDetailsModel>();
        this.contactList.addAll(contacts);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.contactlist,null);
        TextView name = (TextView) convertView.findViewById(R.id.txtDisName);
        TextView phone = (TextView) convertView.findViewById(R.id.txtDisPhone);
        name.setText(((Map) contacts.get(position)).get("name").toString());
        phone.setText(((Map) contacts.get(position)).get("phone").toString());
        return convertView;
    }
}
