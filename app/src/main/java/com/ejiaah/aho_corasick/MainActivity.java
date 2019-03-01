package com.ejiaah.aho_corasick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private KeywordRecyclerAdapter keywordRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("ahocorasick-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> keywordList = new ArrayList<>();
        keywordList.add("he");
        keywordList.add("she");
        keywordList.add("his");
        keywordList.add("hers");

        recyclerView = (RecyclerView) findViewById(R.id.keyword_recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        keywordRecyclerAdapter = new KeywordRecyclerAdapter(keywordList);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(keywordRecyclerAdapter);

        setCaseInsensitive();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native void setCaseInsensitive();
    public native void setRemoveOverlaps();
    public native void setOnlyWholeWords();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insert_button: {
                EditText editText = (EditText) findViewById(R.id.insert_editText);
                keywordRecyclerAdapter.insertItem(editText.getText().toString());
                editText.setText("");
                break;
            }
            case R.id.parse_button: {
                break;
            }
        }
    }
}
