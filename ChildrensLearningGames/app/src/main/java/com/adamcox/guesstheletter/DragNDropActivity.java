package com.adamcox.guesstheletter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DragNDropActivity extends Activity {

    private static final String L1_TAG = "L1",
                        		L2_TAG = "L2",
    				            L3_TAG = "L3",
    						    L4_TAG = "L4",
    							L5_TAG = "L5";

    Bitmap l1Bitmap,
		   l2Bitmap,
		   l3Bitmap,
		   l4Bitmap,
		   l5Bitmap;
    
    private static ImageView leftImg1, rightImg1, 
				    		 leftImg2, rightImg2, 
				    		 leftImg3, rightImg3, 
				    		 leftImg4, rightImg4, 
				    		 leftImg5, rightImg5;

    private static DragNDropActivity me;

    private static myDragEventListener l1DragListen = new myDragEventListener(1);
    private static myDragEventListener l2DragListen = new myDragEventListener(2);
    private static myDragEventListener l3DragListen = new myDragEventListener(3);
    private static myDragEventListener l4DragListen = new myDragEventListener(4);
    private static myDragEventListener l5DragListen = new myDragEventListener(5);
    
    static int[] icons = new int[5];

    private static TextView DoneField;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dragndrop);

        Toast.makeText(this,"Drag N Drop to Matching Images", Toast.LENGTH_LONG).show();
        
    	DoneField = (TextView) findViewById(R.id.yae);
    	DoneField.setVisibility(View.GONE);
        
        icons[0] = R.drawable.icon_shapes;
        icons[1] = R.drawable.icon_spelling;
        icons[2] = R.drawable.icon_rate;
        icons[3] = R.drawable.icon_shapes;
        icons[4] = R.drawable.icon_sounds;
        
        me = this;

        leftImg1 = (ImageView) findViewById(R.id.leftImg1);
        rightImg1 = (ImageView) findViewById(R.id.rightImg1);
        leftImg2 = (ImageView) findViewById(R.id.leftImg2);
        rightImg2 = (ImageView) findViewById(R.id.rightImg2);
        leftImg3 = (ImageView) findViewById(R.id.leftImg3);
        rightImg3 = (ImageView) findViewById(R.id.rightImg3);
        leftImg4 = (ImageView) findViewById(R.id.leftImg4);
        rightImg4 = (ImageView) findViewById(R.id.rightImg4);
        leftImg5 = (ImageView) findViewById(R.id.leftImg5);
        rightImg5 = (ImageView) findViewById(R.id.rightImg5);

        l1Bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_shapes);
        l2Bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_spelling);
        l3Bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_rate);
        l4Bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_colors);
        l5Bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_sounds);
        
        leftImg1.setImageBitmap(l1Bitmap);
        leftImg1.setTag(L1_TAG);
        leftImg2.setImageBitmap(l2Bitmap);
        leftImg2.setTag(L2_TAG);
        leftImg3.setImageBitmap(l3Bitmap);
        leftImg3.setTag(L3_TAG);
        leftImg4.setImageBitmap(l4Bitmap);
        leftImg4.setTag(L4_TAG);
        leftImg5.setImageBitmap(l5Bitmap);
        leftImg5.setTag(L5_TAG);

        rightImg1.setOnDragListener(l1DragListen);
        rightImg2.setOnDragListener(l2DragListen);
        rightImg3.setOnDragListener(l3DragListen);
        rightImg4.setOnDragListener(l4DragListen);
        rightImg5.setOnDragListener(l5DragListen);
        
        leftImg1.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent arg1) {
            	ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            	String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            	ClipData dragData = new ClipData((CharSequence)"Test",clipDescription,item);
            	MyDragShadowBuilder myShadow = new MyDragShadowBuilder(leftImg1,0);
            	v.startDrag(dragData, myShadow, null, 0);
            	v.performClick();
            	return false;
			}
        });
        leftImg2.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent arg1) {
            	ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            	String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            	ClipData dragData = new ClipData((CharSequence)"Test",clipDescription,item);
            	MyDragShadowBuilder myShadow = new MyDragShadowBuilder(leftImg2,1);
            	v.startDrag(dragData, myShadow, null, 0);
            	v.performClick();
            	return false;
			}
        });
        leftImg3.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent arg1) {
            	ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            	String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            	ClipData dragData = new ClipData((CharSequence)"Test",clipDescription,item);
            	MyDragShadowBuilder myShadow = new MyDragShadowBuilder(leftImg3,2);
            	v.startDrag(dragData, myShadow, null, 0);
            	v.performClick();
            	return false;
			}
        });
        leftImg4.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent arg1) {
            	ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            	String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            	ClipData dragData = new ClipData((CharSequence)"Test",clipDescription,item);
            	MyDragShadowBuilder myShadow = new MyDragShadowBuilder(leftImg4,3);
            	v.startDrag(dragData, myShadow, null, 0);
            	v.performClick();
            	return false;
			}
        });
        leftImg5.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent arg1) {
            	ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            	String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            	ClipData dragData = new ClipData((CharSequence)"Test",clipDescription,item);
            	MyDragShadowBuilder myShadow = new MyDragShadowBuilder(leftImg5,4);
            	v.startDrag(dragData, myShadow, null, 0);
            	v.performClick();
            	return false;
			}
        });
    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

	    private static Drawable shadow;

        public MyDragShadowBuilder(View v, int num) {
            super(v);
        	Resources res = me.getResources();
        	shadow = res.getDrawable( icons[num] );
        }

        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {
            int width, height;
            width = getView().getWidth();
            height = getView().getHeight();
            shadow.setBounds(0, 0, width/2, height/2);
            size.set(width, height);
            touch.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }
    

    private static class myDragEventListener implements View.OnDragListener {
    	private int num;
    	
    	public myDragEventListener(int num) {
    		this.num=num;
    	}
    	
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Determines if this View can accept the dragged data
                    //if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    //    ((ImageView) v).setColorFilter(Color.BLUE);
                    //    v.invalidate();
                        return true;
                    //}
                    //return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    ((ImageView) v).setColorFilter(Color.GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    // Ignore the event
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    ((ImageView) v).clearColorFilter();
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    CharSequence dragData = item.getText();

                    // Displays a message containing the dragged data.
                    Log.d( "DragNDrop-dragged data", String.valueOf(dragData) + String.valueOf(this.num));

                    if( String.valueOf(dragData).equals("L1") && this.num == 1 ) {
                    	leftImg1.setVisibility(View.INVISIBLE);
                    	rightImg1.setVisibility(View.INVISIBLE);
                    }
                    if( String.valueOf(dragData).equals("L2") && this.num == 2 ) {
                    	leftImg2.setVisibility(View.INVISIBLE);
                    	rightImg2.setVisibility(View.INVISIBLE);
                    }
                    if( String.valueOf(dragData).equals("L3") && this.num == 3 ) {
                    	leftImg3.setVisibility(View.INVISIBLE);
                    	rightImg3.setVisibility(View.INVISIBLE);
                    }
                    if( String.valueOf(dragData).equals("L4") && this.num == 4 ) {
                    	leftImg4.setVisibility(View.INVISIBLE);
                    	rightImg4.setVisibility(View.INVISIBLE);
                    }
                    if( String.valueOf(dragData).equals("L5") && this.num == 5 ) {
                    	leftImg5.setVisibility(View.INVISIBLE);
                    	rightImg5.setVisibility(View.INVISIBLE);
                    }
                    
                    if( leftImg1.getVisibility() == View.INVISIBLE && 
                    	leftImg2.getVisibility() == View.INVISIBLE && 
                    	leftImg3.getVisibility() == View.INVISIBLE && 
                    	leftImg4.getVisibility() == View.INVISIBLE && 
                    	leftImg5.getVisibility() == View.INVISIBLE ) {
                    	DoneField.setVisibility(View.VISIBLE);
                    	DoneField.setText("You Won!");
                    }
                    
                    // Turns off any color tints
                    ((ImageView) v).clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting
                    ((ImageView) v).clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        Log.d( "DragNDrop", "handled");
                    } else {
                        Log.d( "DragNDrop", "not handled");
                    }

                    return true;
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }
            
            return false;
        }
    };
    
}