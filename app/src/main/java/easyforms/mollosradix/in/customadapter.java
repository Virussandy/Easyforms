package easyforms.mollosradix.in;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class customadapter extends RecyclerView.Adapter<customadapter.ViewHolder> implements Filterable{

    List<ListItem> listItems;
    List<ListItem> alllistItems;
    private final Context context;

    public customadapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.alllistItems = new ArrayList<>(listItems);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListItem listItem = listItems.get(position);
        holder.textView.setText(listItem.getsl_no());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Display_data.class);
                intent.putExtra("id",listItem.getId());
                intent.putExtra("sl_no",listItem.getsl_no());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final List<ListItem> list = listItems;

//            int count = list.size();
//            final ArrayList<ListItem> nlist = new ArrayList<ListItem>(count);

            ListItem filteredList;
            FilterResults filterResults = new FilterResults();

            if(constraint == null || constraint.length() == 0){
                filterResults.count = listItems.size();
                filterResults.values = alllistItems;

            }else {

                String searchstr = constraint.toString().toLowerCase();

                List<ListItem> nlist = new ArrayList<>();

                for (ListItem listItem:listItems){
                    if (listItem.getsl_no().contains(searchstr) /*|| listItem.getdescription().contains(searchstr)*/){
                        nlist.add(listItem);
                    }
                }

                filterResults.values = nlist;
                filterResults.count = nlist.size();

            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            listItems = (List<ListItem>) results.values;
            notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.folder_name);
            card = itemView.findViewById(R.id.card);
        }
    }
}
