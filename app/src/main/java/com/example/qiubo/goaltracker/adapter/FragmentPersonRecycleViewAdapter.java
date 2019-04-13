package com.example.qiubo.goaltracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.util.DateUtil;

import org.w3c.dom.Text;

import java.util.List;

public class FragmentPersonRecycleViewAdapter extends RecyclerView.Adapter<FragmentPersonRecycleViewAdapter.FragementPersonViewHolder> {
    private List<Event> datas;
    private LayoutInflater inflater;
    public FragmentPersonRecycleViewAdapter(Context context, List<Event> datas){
       this.datas=datas;
       inflater= LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public FragementPersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FragementPersonViewHolder fragementPersonViewHolder=new FragementPersonViewHolder(inflater.inflate(R.layout.fragment_person_recycle_view_item,viewGroup,false));
        return fragementPersonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FragementPersonViewHolder fragementPersonViewHolder, int i) {
        fragementPersonViewHolder.eventText.setText(datas.get(i).getEvent());
        fragementPersonViewHolder.timeText.setText(DateUtil.getItemTime(datas.get(i).getPlanStartTime(),datas.get(i).getPlanEndTime()));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class FragementPersonViewHolder extends RecyclerView.ViewHolder{
        private TextView eventText;
        private TextView timeText;
        public FragementPersonViewHolder(@NonNull View itemView) {
            super(itemView);
            eventText=itemView.findViewById(R.id.person_item_event_text);
            timeText=itemView.findViewById(R.id.person_item_time);
        }
    }

}
