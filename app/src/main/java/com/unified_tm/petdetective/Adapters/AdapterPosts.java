package com.unified_tm.petdetective.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ramotion.foldingcell.FoldingCell;
import com.unified_tm.petdetective.Models.ModelITemMain;
import com.unified_tm.petdetective.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyViewHolder> {

    Context context;
    List<ModelITemMain> postList;

    String image_url = "https://www.unifiedtnc.com/pet_detective_api/images/";

    public AdapterPosts(Context context, List<ModelITemMain> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_post,parent,false);

         final FoldingCell foldingCell = (FoldingCell) view.findViewById(R.id.folding_cell);
          foldingCell.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  foldingCell.toggle(false);
              }
          });


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelITemMain modelITemMain = postList.get(position);

        holder.txtTitle.setText(modelITemMain.getTitle());
        holder.txtDesc.setText(modelITemMain.getDescription());
        holder.txtTitleExpand.setText(modelITemMain.getTitle());
        holder.txtDescExpand.setText(modelITemMain.getDescription());
        holder.txtBreed.setText(modelITemMain.getPetBreed());
        holder.txtContact.setText(modelITemMain.getContact());
        holder.txtEmail.setText(modelITemMain.getEmail());

        String date = modelITemMain.getPostDate();
        String[] parts = date.split(" ");

        holder.txtDate.setText(parts[0]);
        holder.textStatus.setText(modelITemMain.getStatus());
        holder.txtZipCode.setText(modelITemMain.getZipCode());

        Glide.with(context)
                .load(image_url+modelITemMain.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.image_dog_1)
                .into(holder.imagPet);

        Glide.with(context)
                .load(image_url+modelITemMain.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.image_dog_1)
                .into(holder.imagPetExpand);


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.btnDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle,txtTitleExpand,txtDesc,txtDescExpand,textStatus,txtDate,txtBreed,txtLocation,txtContact,txtEmail,txtZipCode;
        ImageView imagPet,imagPetExpand,btnDelet,btnEdit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.pet_title);
            txtTitleExpand = itemView.findViewById(R.id.pet_titleExpand);
            txtDesc  = itemView.findViewById(R.id.pet_descr);
            txtDescExpand = itemView.findViewById(R.id.pet_descrExpand);
            textStatus = itemView.findViewById(R.id.txtStatusExpand);
            txtDate    = itemView.findViewById(R.id.txtDateExpand);
            txtEmail   = itemView.findViewById(R.id.txtEmailExpand);
            txtContact = itemView.findViewById(R.id.txtContactExpand);
            txtBreed   = itemView.findViewById(R.id.txtBreedExpand);
            txtLocation = itemView.findViewById(R.id.txtLocationExpand);
            txtZipCode  = itemView.findViewById(R.id.txtzipCodeExpand);

            imagPet = itemView.findViewById(R.id.image_pet);
            imagPetExpand = itemView.findViewById(R.id.petImageExpand);

            btnDelet = itemView.findViewById(R.id.btnDeletPost);
            btnEdit  = itemView.findViewById(R.id.btnEditPost);





        }
    }
}
