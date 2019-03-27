package xyz.yaroslav.zttapacs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private List<EmployeeTag> tagList;

    public TagAdapter(List<EmployeeTag> tagList) {
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tag_layout, viewGroup, false);
        return new TagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder viewHolder, int i) {
        EmployeeTag tag = tagList.get(i);
        viewHolder.tag_id.setText(tag.getTagId());
        viewHolder.emp_name.setText(tag.getEmpName());
        viewHolder.emp_dep.setText(tag.getEmpDepartment());
    }

    @Override
    public int getItemCount() {
        return  tagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag_id, emp_name, emp_dep;

        ViewHolder(View view) {
            super(view);

            tag_id = view.findViewById(R.id.itemTagId);
            emp_name = view.findViewById(R.id.itemEmpName);
            emp_dep = view.findViewById(R.id.itemEmpDep);
        }
    }
}


































