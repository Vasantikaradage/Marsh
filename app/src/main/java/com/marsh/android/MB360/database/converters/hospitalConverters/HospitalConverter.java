package com.marsh.android.MB360.database.converters.hospitalConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsh.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;

import java.lang.reflect.Type;
import java.util.List;

public class HospitalConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<HospitalInformation> stringToHospitalInformation(String hospitalInformation) {
        Type listType = new TypeToken<List<HospitalInformation>>() {
        }.getType();
        return new Gson().fromJson(hospitalInformation, listType);
    }

    @TypeConverter
    public static String hospitalnformatiionToString(List<HospitalInformation> hospitalInformation) {

        return gson.toJson(hospitalInformation);
    }
}
