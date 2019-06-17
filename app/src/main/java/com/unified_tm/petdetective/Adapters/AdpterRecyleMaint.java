package com.unified_tm.petdetective.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unified_tm.petdetective.MainActivity;
import com.unified_tm.petdetective.Models.ModelITemMain;
import com.unified_tm.petdetective.R;

import java.util.List;

public class AdpterRecyleMaint extends RecyclerView.Adapter<AdpterRecyleMaint.MyViewHOlder> {
    Context context;
    List<ModelITemMain> iTemMainList ;

    public AdpterRecyleMaint(Context context, List<ModelITemMain> iTemMainList) {
        this.context = context;
        this.iTemMainList = iTemMainList;
    }

    @NonNull
    @Override
    public MyViewHOlder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycle_main,null,false);

        return new MyViewHOlder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHOlder myViewHOlder, int i) {
        ModelITemMain iTemMain = iTemMainList.get(i);
        myViewHOlder.imagePet.setImageResource(iTemMain.getImage());
      //  myViewHOlder.txtTitle.setText(iTemMain.getTitle());
       // myViewHOlder.txtDescrption.setText(iTemMain.getDescription());

        if(MainActivity.contextFrom.equals("found")){
            myViewHOlder.txtTitle.setTextColor(context.getResources().getColor(R.color.color_green));
            myViewHOlder.deviderView.setBackgroundColor(context.getResources().getColor(R.color.color_green));
        }

    }

    @Override
    public int getItemCount() {
        return iTemMainList.size();
    }

    public class MyViewHOlder extends RecyclerView.ViewHolder{
        ImageView imagePet;
        TextView txtTitle,txtDescrption;
        View deviderView;

        public MyViewHOlder(@NonNull View itemView) {
            super(itemView);

            imagePet = itemView.findViewById(R.id.imageV);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescrption = itemView.findViewById(R.id.txtDes);
            deviderView = itemView.findViewById(R.id.view);
        }
    }
}
