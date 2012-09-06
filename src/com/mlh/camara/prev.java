package com.mlh.camara;

import java.io.IOException;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class prev extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = null;
	private SurfaceHolder mHolder;
	private Camera mcam;
	
	public prev(Context context, Camera camera) {
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		
	}

	public void surfaceCreated (SurfaceHolder holder){
		try{
			mcam.setPreviewDisplay(holder);
			mcam.startPreview();
		}catch(IOException e){
			Log.d(TAG, "Error Error Error"+ e.getMessage());
		}
	}
   
	public void surfaceDestroyed(SurfaceHolder holder){
		
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
		
		if (mHolder.getSurface()== null){
			return;
		}
		
		try{
			mcam.stopPreview();
			
		}catch(Exception e){
			Log.d(TAG, "error error " + e.getMessage());
		}
		
		
	}

	
    
}
