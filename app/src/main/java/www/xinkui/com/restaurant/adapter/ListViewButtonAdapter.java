package www.xinkui.com.restaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import www.xinkui.com.restaurant.R;

public class ListViewButtonAdapter extends BaseAdapter {

    private class buttonViewHolder {
        TextView deskNumber;
        TextView deskState;
    }

    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private buttonViewHolder holder;

    public ListViewButtonAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource,
                                 String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position) {
        mAppList.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (buttonViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.main_list, null);
            holder = new buttonViewHolder();
            holder.deskNumber = (TextView) convertView.findViewById(valueViewID[0]);
            holder.deskState = (TextView) convertView.findViewById(valueViewID[1]);
            convertView.setTag(holder);
        }

        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String aname = (String) appInfo.get(keyString[0]);
            String aname1 = (String) appInfo.get(keyString[1]);
            holder.deskNumber.setText(aname);
            holder.deskState.setText(aname1);
        }
        return convertView;
    }

    //添加按钮调用接口
    class lvButtonListener implements View.OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            int vid = v.getId();
            //removeItem(position);
        }
    }
}
