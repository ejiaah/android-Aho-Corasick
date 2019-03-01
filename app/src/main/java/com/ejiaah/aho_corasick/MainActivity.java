package com.ejiaah.aho_corasick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    EditText insertEditText;
    EditText parseEditText;
    TextView resultTextView;

    CheckBox configCaseInsensitiveCheckBox;
    CheckBox configRemoveOverlapsCheckBox;
    CheckBox configOnlyWholeWordsCheckBox;

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

        insertEditText = (EditText) findViewById(R.id.insert_editText);
        parseEditText = (EditText) findViewById(R.id.parse_editText);
        resultTextView = (TextView) findViewById(R.id.result_textView);

        configCaseInsensitiveCheckBox = (CheckBox) findViewById(R.id.config_case_insensitive_checkBox);
        configRemoveOverlapsCheckBox = (CheckBox) findViewById(R.id.config_remove_overlaps_checkBox);
        configOnlyWholeWordsCheckBox = (CheckBox) findViewById(R.id.config_only_whole_words_checkBox);

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

        insert("he");
        insert("she");
        insert("his");
        insert("hers");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void setCaseInsensitive(boolean isOn);

    public native void setRemoveOverlaps(boolean isOn);

    public native void setOnlyWholeWords(boolean isOn);

    public native void insert(String keyword);

    public native String[] parseText(String text);

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insert_button: {
                String keyword = insertEditText.getText().toString();
                insertEditText.setText("");

                keywordRecyclerAdapter.insertItem(keyword);
                insert(keyword);
                break;
            }
            case R.id.parse_button: {
                String keyword = parseEditText.getText().toString();
                parseEditText.setText("");

                String[] results = parseText(keyword);
                StringBuilder stringBuilder = new StringBuilder();

                if (results == null || results.length == 0) {
                    stringBuilder.append("not matching");
                } else {
                    for (String result : results) {
                        stringBuilder.append("matching: ").append(result).append("\n");
                    }
                }
                resultTextView.setText(stringBuilder.toString());
                break;
            }
            case R.id.config_case_insensitive_checkBox: {
                setCaseInsensitive(configCaseInsensitiveCheckBox.isChecked());
                break;
            }
            case R.id.config_remove_overlaps_checkBox: {
                setRemoveOverlaps(configRemoveOverlapsCheckBox.isChecked());
                break;
            }
            case R.id.config_only_whole_words_checkBox: {
                setOnlyWholeWords(configOnlyWholeWordsCheckBox.isChecked());
                break;
            }
        }
    }


}
