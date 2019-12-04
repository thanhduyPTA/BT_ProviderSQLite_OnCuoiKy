package use.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String AUTHORITY = "content://use.app.AuthorProvider/author";
    static final Uri CONTENT_URI = Uri.parse(AUTHORITY);

    EditText ed_id, ed_name, ed_sdt;
    Button btn_select, btn_save, btn_delete, btn_edit;

    ListView list_authors;
    ArrayAdapter<Author> adapterAuthors;
    ArrayList<Author> listAuthors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_qlAuthors:
                showDialogAuthor();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void catchEvent_Button(Dialog dialog) {
        btn_select = dialog.findViewById(R.id.btn_select);
        btn_save = dialog.findViewById(R.id.btn_save);
        btn_delete = dialog.findViewById(R.id.btn_delete);
        btn_edit = dialog.findViewById(R.id.btn_edit);
    }
    public void setAdapterAuthors() {
        if (adapterAuthors == null) {
            adapterAuthors = new CustomAdapterAuthor(MainActivity.this, R.layout.layout_customer_author, listAuthors);
            list_authors.setAdapter(adapterAuthors);
        }
        else
            adapterAuthors.notifyDataSetChanged();
    }
    public void showDialogAuthor() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.setTitle("Quản lý tác giả");
        dialog.setContentView(R.layout.dialog_author);

        ed_id = dialog.findViewById(R.id.ed_id);
        ed_name = dialog.findViewById(R.id.ed_name);
        ed_sdt = dialog.findViewById(R.id.ed_sdt);
        list_authors = dialog.findViewById(R.id.list_author);

        catchEvent_Button(dialog);

        listAuthors = selectAll();

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapterAuthors();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_id.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Id không được rỗng", Toast.LENGTH_SHORT).show();
                    ed_id.requestFocus();
                    return;
                }
                if (!ed_id.getText().toString().matches("[0-9]{1,10}")) {
                    Toast.makeText(MainActivity.this, "Id phải là số và không quá 10 ký tự", Toast.LENGTH_SHORT).show();
                    ed_id.selectAll();
                    ed_id.requestFocus();
                    return;
                }
                String name = ed_name.getText().toString(), sdt = ed_sdt.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Tên không được rỗng", Toast.LENGTH_SHORT).show();
                    ed_name.requestFocus();
                    return;
                }
                if (!sdt.equals("") && !sdt.matches("^0[0-9]{9}")) {
                    Toast.makeText(MainActivity.this, "Số điện thoại phải bắt đầu là 0 và đủ 10 số", Toast.LENGTH_SHORT).show();
                    ed_sdt.selectAll();
                    ed_sdt.requestFocus();
                    return;
                }
                Author aut = new Author(Integer.parseInt(ed_id.getText().toString()), name, sdt);
                addAuthor(aut);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_id.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Id không được rỗng", Toast.LENGTH_SHORT).show();
                    ed_id.requestFocus();
                    return;
                }
                if (!ed_id.getText().toString().matches("[0-9]{1,10}")) {
                    Toast.makeText(MainActivity.this, "Id phải là số và không quá 10 ký tự", Toast.LENGTH_SHORT).show();
                    ed_id.selectAll();
                    ed_id.requestFocus();
                    return;
                }
                String name = ed_name.getText().toString(), sdt = ed_sdt.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Tên không được rỗng", Toast.LENGTH_SHORT).show();
                    ed_name.requestFocus();
                    return;
                }
                if (!sdt.equals("") && !sdt.matches("^0[0-9]{9}")) {
                    Toast.makeText(MainActivity.this, "Số điện thoại phải bắt đầu là 0 và đủ 10 số", Toast.LENGTH_SHORT).show();
                    ed_sdt.selectAll();
                    ed_sdt.requestFocus();
                    return;
                }
                Author aut = new Author(Integer.parseInt(ed_id.getText().toString()), name, sdt);
                editAuthor(aut);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAuthor(ed_id.getText().toString());
            }
        });

        list_authors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Author author = listAuthors.get(i);
                ed_id.setText(author.getId() + "");
                ed_name.setText(author.getTen());
                ed_sdt.setText(author.getSdt());
            }
        });

        dialog.show();
    }

    public ArrayList<Author> selectAll() {
        ArrayList<Author> ds = new ArrayList<>();
        Cursor cursor = getContentResolver().query(CONTENT_URI, null, "Select * from author", null, null);
        if (cursor != null)
            cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Author au = new Author(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            Log.d(au.toString(), "----");
            ds.add(au);

            cursor.moveToNext();
        }
        cursor.close();
        return ds;
    }

    public void addAuthor(Author author) {
        if (listAuthors.contains(author)) {
            Toast.makeText(this, "Trùng mã tác giả", Toast.LENGTH_SHORT).show();
            ed_id.selectAll();
            ed_id.requestFocus();
        }
        else {
            ContentValues values = new ContentValues();
            values.put("id", author.getId());
            values.put("name", author.getTen());
            values.put("sdt", author.getSdt());

            getContentResolver().insert(CONTENT_URI, values);

            listAuthors.add(author);
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public void editAuthor(Author author) {
        ContentValues values = new ContentValues();
        values.put("name", author.getTen());
        values.put("sdt", author.getSdt());

        int kt = getContentResolver().update(CONTENT_URI, values, author.getId() + "", null);
        if (kt > 0) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            int vt = listAuthors.indexOf(author);
            listAuthors.get(vt).setTen(author.getTen());
            listAuthors.get(vt).setSdt(author.getSdt());
            
            setAdapterAuthors();
        }
        else
            Toast.makeText(this, "Không có tác giả này", Toast.LENGTH_SHORT).show();
    }

    public void deleteAuthor(String id) {
        if (id.equals(""))
            Toast.makeText(this, "Id không được rỗng", Toast.LENGTH_SHORT).show();
        else {
            int xoa = getContentResolver().delete(CONTENT_URI, id, null);
            if (xoa > 0) {
                Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                Author author = new Author(Integer.parseInt(id), "", "");
                listAuthors.remove(author);

                setAdapterAuthors();
            }
            else
                Toast.makeText(this, "Không có tác giả này", Toast.LENGTH_SHORT).show();
        }
    }
}
