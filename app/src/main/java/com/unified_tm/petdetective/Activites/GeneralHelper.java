package com.unified_tm.petdetective.Activites;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class GeneralHelper {


    public static String getImage64String(Bitmap bitmap){

        String image = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return image;

    }
}
