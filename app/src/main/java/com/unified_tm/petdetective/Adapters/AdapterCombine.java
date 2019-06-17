package com.unified_tm.petdetective.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ramotion.foldingcell.FoldingCell;
import com.unified_tm.petdetective.Models.ModelITemMain;
import com.unified_tm.petdetective.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCombine  extends RecyclerView.Adapter<AdapterCombine.MyViewHolder>{

    Context context;
    List<ModelITemMain> postList;

    String image_url = "https://www.unifiedtnc.com/pet_detective_api/images/";

    public AdapterCombine(Context context, List<ModelITemMain> postList) {
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

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

        holder.txtContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupMenu popup = new PopupMenu(context, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_contact, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.call){
                            makePhoneCall(holder.txtContact.getText().toString());
                        }
                        else
                            if(item.getItemId() == R.id.sms){
                            makeSms(holder.txtContact.getText().toString());
                            }

                        return true;
                    }
                });

                popup.show();

            }
        });
        holder.txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(holder.txtEmail.getText().toString());
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

            btnDelet.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
    }



    void sendEmail(String emailAddress){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",emailAddress, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your Message");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private  void makePhoneCall(String contactNo){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +contactNo));
        context.startActivity(intent);
    }
    private   void makeSms(String contactNo){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW,  Uri.parse("smsto: " +contactNo));
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            sendIntent.setType("vnd.android-dir/mms-sms");

        context.startActivity(sendIntent);

    }






}
