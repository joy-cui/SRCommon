//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.serenegiant.bluetooth;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.serenegiant.common.R.id;
import java.util.List;

public class BluetoothDeviceInfoAdapter extends ArrayAdapter<BluetoothDeviceInfo> {
    private static final String TAG = BluetoothDeviceInfoAdapter.class.getSimpleName();
    private final LayoutInflater mInflater;
    private final int mLayoutId;

    public BluetoothDeviceInfoAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = resource;
    }

    public BluetoothDeviceInfoAdapter(@NonNull Context context, @LayoutRes int resource, List<BluetoothDeviceInfo> objects) {
        super(context, resource, objects);
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = resource;
    }

    public BluetoothDeviceInfoAdapter(@NonNull Context context, @LayoutRes int resource, BluetoothDeviceInfo[] objects) {
        super(context, resource, objects);
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = resource;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (convertView == null) {
            rootView = this.mInflater.inflate(this.mLayoutId, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.nameTv = (TextView)rootView.findViewById(id.name);
            holder.addressTv = (TextView)rootView.findViewById(id.address);
            holder.icon = (ImageView)rootView.findViewById(id.icon);
            rootView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)rootView.getTag();

        try {
            BluetoothDeviceInfo item = (BluetoothDeviceInfo)this.getItem(position);
            if (item != null) {
                if (holder.nameTv != null) {
                    holder.nameTv.setText(item.name);
                }

                if (holder.addressTv != null) {
                    holder.addressTv.setText(item.address);
                }

                if (holder.icon != null) {
                }
            }
        } catch (Exception var7) {
            Log.w(TAG, var7);
        }

        return rootView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView nameTv;
        TextView addressTv;

        private ViewHolder() {
        }
    }
}
