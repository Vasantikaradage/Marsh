package com.marsh.android.MB360.database.converters.coverageConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsh.android.MB360.insurance.coverages.repository.responseclass.CoveragePolicyDatum;
import com.marsh.android.MB360.insurance.coverages.repository.responseclass.Message;

import java.lang.reflect.Type;
import java.util.List;

public class CoverageResponseConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<CoveragePolicyDatum> toList(String value) {
        Type listType = new TypeToken<List<CoveragePolicyDatum>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<CoveragePolicyDatum> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static Message stringToMessage(String message) {
        return gson.fromJson(message, Message.class);
    }

    @TypeConverter
    public static String messageToString(Message message) {

        return gson.toJson(message);
    }
}