package com.example.bjheggset.buckets;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import android.support.v7.widget.CardView;

public class DetailsBucketCA extends RecyclerView.Adapter<DetailsBucketCA.ViewHolder> {

    private List<Items> mItems;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
    public DetailsBucketCA(List<Items> myItems) {
        mItems = myItems;
    }


    @Override
    public DetailsBucketCA.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(DetailsBucketCA.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = (TextView)cardView.findViewById(R.id.goal_name);
        ImageView itemImg = (ImageView)cardView.findViewById(R.id.item_photo);
        CheckBox checkBox = (CheckBox)cardView.findViewById(R.id.chkAccomplished);
        textView.setText(mItems.get(position).toString());

        checkBox.setChecked(checkAccomplished(mItems.get(position)));
        cardView.setClickable(checkAccomplished(mItems.get(position)));
    }




    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();

    }


    public boolean checkAccomplished(Items item) {
        int itemID = item.getItemID();
        if(mItems.contains(itemID)){
            return true;
        } else {
            return false;
        }
    }

/*
    public View getView(int position, View arg1, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_details_bucket_ca, viewGroup, false);

        TextView txtItems = (TextView) row.findViewById(R.id.txtItem);
        CheckBox chkItems = (CheckBox) row.findViewById(R.id.chkItems);

        txtItems.setText(items.get(position).toString());
        chkItems.setChecked(checkAccomplished(items.get(position)));

        row.setClickable(checkAccomplished(items.get(position)));
        return row;
    }
*/
}
