package com.test.electrophage;

import android.app.Activity;
import android.os.Bundle;

public class TestBedActivity extends Activity {
	
	private TestSurface mGLView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new TestSurface(this);
        setContentView(mGLView);
    }
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		mGLView.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mGLView.onResume();
	}
}