package use.app2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapterAuthor extends ArrayAdapter<Author> {

    Activity context;
    int resource;
    ArrayList<Author> authors;

    public CustomAdapterAuthor(Activity context, int resource, ArrayList<Author> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.authors = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(resource, null);

        if (authors.size() > 0 && position >= 0) {
            Author author = authors.get(position);

            TextView stt, id, ten, sdt;
            stt = convertView.findViewById(R.id.tv_stt);
            id = convertView.findViewById(R.id.tv_id);
            ten = convertView.findViewById(R.id.tv_ten);
            sdt = convertView.findViewById(R.id.tv_sdt);

            stt.setText((position + 1) + "");
            id.setText(author.getId() + "");
            ten.setText(author.getTen());
            sdt.setText(author.getSdt());
        }
        return convertView;
    }
}
