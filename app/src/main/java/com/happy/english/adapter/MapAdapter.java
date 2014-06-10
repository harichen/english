package com.happy.english.adapter;

import com.happy.english.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MapAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] mDatas;
    private int currentIndex ;

    public MapAdapter(Context context, String[] datas)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    public void setCurrentMissionIndex(int index){
    	this.currentIndex = index; 
    	this.notifyDataSetChanged();
    }
    @Override
    public int getCount()
    {
        return mDatas.length;
    }

    @Override
    public Object getItem(int arg0)
    {
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView,
            ViewGroup parent)
    {
        MapHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(
                    R.layout.map_item, null);
            holder = new MapHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        }
        else{
            holder = (MapHolder) convertView
                    .getTag(R.id.holder);
        }
        holder.number.setText(mDatas[position]);
        holder.pass.setVisibility(View.GONE);
        holder.lock.setVisibility(View.GONE);
//    	holder.number.setAlpha((float) 1);
        if(position < currentIndex){
            holder.pass.setVisibility(View.VISIBLE);
        }else if(position > currentIndex){
        	holder.lock.setVisibility(View.VISIBLE);
//        	holder.number.setAlpha((float) 0.5);
        }
        return convertView;
    }

    class MapHolder
    {
        private TextView number;
        private ImageView pass;
        private ImageView lock;

        public MapHolder(View view)
        {
            number = (TextView) view
                    .findViewById(R.id.tv_num);
            pass = (ImageView) view.findViewById(R.id.iv_pass);
            lock = (ImageView) view.findViewById(R.id.iv_lock);
        }
    }
}
