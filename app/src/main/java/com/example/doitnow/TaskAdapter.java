package com.example.doitnow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> items = new ArrayList<>();

    public void setItems(List<Task> tasks) {
        items.clear();
        List<Task> focus = new ArrayList<>(), later = new ArrayList<>();
        for (Task t : tasks) { if (t.isFocus()) focus.add(t); else later.add(t); }
        if (!focus.isEmpty()) { items.add("FOCUS"); items.addAll(focus); }
        if (!later.isEmpty()) { items.add("LATER"); items.addAll(later); }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int pos) { return items.get(pos) instanceof String ? 0 : 1; }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int type) {
        View v = LayoutInflater.from(p.getContext()).inflate(type == 0 ? R.layout.item_header : R.layout.item_task, p, false);
        return type == 0 ? new HeaderVH(v) : new TaskVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int pos) {
        if (h instanceof HeaderVH) ((HeaderVH) h).tv.setText((String) items.get(pos));
        else {
            Task t = (Task) items.get(pos);
            TaskVH vh = (TaskVH) h;
            vh.name.setText(t.getName());
            vh.meta.setText(t.getTimeFormatted() + " • " + t.getCategory());
            vh.cb.setOnCheckedChangeListener(null);
            vh.cb.setChecked(t.isDone());
            vh.cb.setOnCheckedChangeListener((b, checked) -> {
                t.setDone(checked);
                if (b.getContext() instanceof MainActivity) ((MainActivity) b.getContext()).refreshData();
            });
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class TaskVH extends RecyclerView.ViewHolder {
        TextView name, meta; CheckBox cb;
        TaskVH(View v) { super(v); name = v.findViewById(R.id.tvTaskName); meta = v.findViewById(R.id.tvTaskMeta); cb = v.findViewById(R.id.cbTask); }
    }

    static class HeaderVH extends RecyclerView.ViewHolder {
        TextView tv; HeaderVH(View v) { super(v); tv = v.findViewById(R.id.tvHeader); }
    }
}