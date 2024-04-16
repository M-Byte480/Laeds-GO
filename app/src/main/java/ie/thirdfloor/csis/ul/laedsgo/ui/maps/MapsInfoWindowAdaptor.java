package ie.thirdfloor.csis.ul.laedsgo.ui.maps;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ie.thirdfloor.csis.ul.laedsgo.R;

class MapsInfoWindowAdaptor implements GoogleMap.InfoWindowAdapter {

    private final View view;
    private Context mContext;

    public MapsInfoWindowAdaptor(Context context) {
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.maps_info_window_view, null);
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        setInfoWindowText(marker);
        return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        setInfoWindowText(marker);
        return view;
    }

    private void setInfoWindowText(Marker marker) {
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.infoWindowTitle);
        if (!title.equals("")) {
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.infoWindowSnippet);
        if (!snippet.equals("")) {
            tvSnippet.setText(snippet);
        }

        String buttonText = "Catch!";
        Button button = (Button) view.findViewById(R.id.infoWindowtoARButton);
        button.setText(buttonText);
        button.setBackgroundColor(Color.GREEN);
    }
}
