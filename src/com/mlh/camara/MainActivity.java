package com.mlh.camara;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public  class MainActivity extends Activity implements SurfaceHolder.Callback{
	private LayoutInflater myInflater = null;
	Camera myCamera;
	byte[] tempdata;
	boolean myPreviewRunning = false;
	private SurfaceHolder mySurfaceHolder;
	private SurfaceView mySurfaceView;
	Button takePicture;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        
        setContentView(R.layout.activity_main);
        mySurfaceView = (SurfaceView) findViewById(R.id.content);
        mySurfaceHolder = mySurfaceView.getHolder();
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        myInflater = LayoutInflater.from(this);
        
        View overView = myInflater.inflate(R.layout.activity_prev,null);
        this.addContentView(overView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
       
        takePicture = (Button) findViewById(R.id.button);
        takePicture.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		myCamera.takePicture(myShutterCallback, myPictureCallback, myJpeg);
        	
        	}
        });   
	}
	
	ShutterCallback myShutterCallback = new ShutterCallback() {
		public void onShutter() {
		}
	};

	PictureCallback myPictureCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera myCamera) {
			
		}
	};
	
	PictureCallback myJpeg = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera myCamera) {
			if(data != null){
				tempdata = data;
				
				File pictureFileDir = getDir();

				if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

					Log.d("ERRORRRRRR", "Can't create directory to save image.");
					Toast.makeText(MainActivity.this, "Can't create directory to save image.", Toast.LENGTH_LONG).show();
					return;
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
				String date = dateFormat.format(new Date());
				String photoFile = "Picture_" + date + ".jpg";

				String filename = pictureFileDir.getPath() + File.separator + photoFile;

				File pictureFile = new File(filename);

				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
					Toast.makeText(MainActivity.this, "New Image saved:" + photoFile, Toast.LENGTH_LONG).show();
				} catch (Exception error) {
					Log.d("ERRORRRRRR", "File" + filename + "not saved: " + error.getMessage());
					Toast.makeText(MainActivity.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
				}
				
			}
		}
		
		private File getDir() {
			File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			return new File(sdDir, "CameraAPIDemo");
		}
	};
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		try{
			if(myPreviewRunning){
				myCamera.stopPreview();
				myPreviewRunning = false;
			}
			Camera.Parameters p = myCamera.getParameters();
			p.setPreviewSize(width,height);

			myCamera.setParameters(p);
			myCamera.setPreviewDisplay(holder);
			myCamera.startPreview();
			myPreviewRunning = true;
		}catch(Exception e){}
		
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		myCamera = Camera.open();
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		myCamera.stopPreview();
		myPreviewRunning = false;
		myCamera.release();
		myCamera = null;
	}
        
	}
