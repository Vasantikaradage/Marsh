package com.marsh.android.MB360.database.converters.claimProcedureConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsh.android.MB360.insurance.claimsprocedure.repository.responseclass.EmergencyContactNo;
import com.marsh.android.MB360.insurance.claimsprocedure.repository.responseclass.Message;

import java.lang.reflect.Type;
import java.util.List;

public class EmergencyContactNoConverter {
    private static final Gson gson = new Gson();
    @TypeConverter
    public static List<EmergencyContactNo> toList(String value) {
        Type listType = new TypeToken<List<EmergencyContactNo>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<EmergencyContactNo> list) {
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
