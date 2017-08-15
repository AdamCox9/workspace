package com.adamcox.followme;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class TextEntryActivity extends Activity {
    /** Called when the activity is first created. */
	static final private int CLEAR_ID = Menu.FIRST;

	private EditText mEditor;
	private TextView mView;
	private HttpService httpService;
	private TextEntryActivity me = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doTextEntry();
    }

    private void doTextEntry() {
        setContentView(R.layout.text_entry);
        mEditor = (EditText) findViewById(R.id.editor);
        mView = (TextView) findViewById(R.id.success_text);
        
        //Make sure there is a keyboard: only will trigger it if no physical keyboard is open
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(mEditor, InputMethodManager.SHOW_IMPLICIT);
     
        ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        ((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        ((Button) findViewById(R.id.submit)).setOnClickListener(mSubmitListener);
        ((Button) findViewById(R.id.ok)).setVisibility(View.GONE);
        mView.setVisibility(View.GONE);
        
        mEditor.setText(getText(R.string.main_label));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	menu.findItem(CLEAR_ID).setVisible(mEditor.getText().length() > 0);
    	return true;
    }

	OnClickListener mBackListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};

	OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View v) {
			mEditor.setText("");
		}
	};

	OnClickListener mOkListener = new OnClickListener() {
		public void onClick(View v) {
            me.finish();
		}
	};

	OnClickListener mSubmitListener = new OnClickListener() {
		public void onClick(View v) {
            httpService = new HttpService();
            String text;
			try {
				text = httpService.postText(mEditor.getText().toString());
                mEditor.setText(text);
                mEditor.setVisibility(View.GONE);
                
                /*
                 * @Todo make a success/failure message and force a user to click OK
                 */
                if( true ) {
                	
                    mView.setVisibility(View.VISIBLE);
               	
                    ((Button) findViewById(R.id.submit)).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.clear)).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.back)).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.ok)).setVisibility(View.VISIBLE);
                    ((Button) findViewById(R.id.ok)).setOnClickListener(mOkListener);
                 }
                
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	};
    
}