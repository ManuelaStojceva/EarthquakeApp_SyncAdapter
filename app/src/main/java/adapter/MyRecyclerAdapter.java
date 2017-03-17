package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.earthquakeapp.syncadapter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuela.Stojceva on 3/17/2017.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.SimpleViewHolder> {

    private final Context mContext;
    private List<String> mData;
    private List<String> mDataMag;
    private int lastPosition = -1;


    //add item from recycle view
    public void add(String s,int position) {
        position = position == -1 ? getItemCount()  : position;
        mData.add(position,s);
        notifyItemInserted(position);
    }
    //remove item from recycle view
    public void remove(int position){
        mData.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, mData.size());
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView mag;
        LinearLayout container;
        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtPlace);
            mag = (TextView) view.findViewById(R.id.txtMag);
            container = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

    public MyRecyclerAdapter(Context context, List<String> data, List<String> dataMag) {
        mContext = context;
        if (data != null)
            mData = new ArrayList<String>(data);
        else mData = new ArrayList<String>();
        this.mDataMag = dataMag;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.earthquake_list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.title.setText(mData.get(position));
        holder.mag.setText(mDataMag.get(position));
        setAnimation(holder.container, position);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Place: " + mData.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
}