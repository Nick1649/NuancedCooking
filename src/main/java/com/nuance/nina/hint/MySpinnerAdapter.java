package com.nuance.nina.hint;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Spinner adapter to make the spinner show a default value
 */
public class MySpinnerAdapter  extends ArrayAdapter<String> {
    public MySpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        return super.getCount() - 1; // This makes the trick: do not show last item
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
