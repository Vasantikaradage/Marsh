package com.marsh.android.MB360.database.converters.MyClaimsConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.marsh.android.MB360.insurance.myclaims.responseclass.ClaimsInformation;

public class ClaimInformationConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static ClaimsInformation stringToClaimInformation(String claimInformation) {
        return gson.fromJson(claimInformation, ClaimsInformation.class);
    }

    @TypeConverter
    public static String claimInformatiionToString(ClaimsInformation claimsInformation) {

        return gson.toJson(claimsInformation);
    }
}
