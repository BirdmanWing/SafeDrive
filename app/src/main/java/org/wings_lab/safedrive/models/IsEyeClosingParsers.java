package org.wings_lab.safedrive.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wings_lab.safedrive.parsers.M2XValueObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rAYMOND on 2/20/2016.
 */
public class IsEyeClosingParsers {
    public static M2XValueObject[] parse(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("values");
            M2XValueObject objs[] = new M2XValueObject[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
                objs[i] = new M2XValueObject();
                objs[i].setValue(jsonArray.getJSONObject(i).getInt("value"));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",new Locale("en"));
                Date date = format.parse(jsonArray.getJSONObject(i).getString("timestamp"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, 8);
                objs[i].setTimestamp(calendar.getTime());
            }
            return objs;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}


