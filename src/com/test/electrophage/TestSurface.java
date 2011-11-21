package com.test.electrophage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class TestSurface extends GLSurfaceView implements Renderer {

	private FloatBuffer triangleVB;
	
	public TestSurface(Context context) {
		super(context);
		setRenderer(this);
	}
	
	private float ratio = 1f;
	private float maxX = 0;
	private float maxY = 0;
	private int numVertices = 20;
	private int numDepths = 15;
	private float[] depths;
	private float depthOffset = 0f;
	
	private void initShapes(){
        float triangleCoords[] = {
            // X, Y, Z
        	  0f, 0f, 0,
        	  0f, 0.5f, 0,
        	  0.5f, 0f, 0,
        	  0.5f, 0.5f, 0
//            -0.5f, -0.25f, 0,
//             0.5f, -0.25f, 0,
//             0.0f,  0.559016994f, 0,
//             0.5f, 0.25f, 0
        }; 
        
        // initialize vertex Buffer for triangle  
        ByteBuffer vbb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                triangleCoords.length * 4); 
        vbb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
        triangleVB = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        triangleVB.put(triangleCoords);    // add the coordinates to the FloatBuffer
        triangleVB.position(0);            // set the buffer to read the first coordinate
        depths = new float[numDepths];
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1f, 0f);

//		gl.glColor4f(0f, 0.5f, 0.3f, 0.5f);
//		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleVB);
//		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);
		updateDepths();
		drawRings(gl);
	}
	
	public void updateDepths(){
		depthOffset += 0.05f;
		depthOffset %= 1.0f;
		for(int i = -1; i < numDepths -1; i++){
        	depths[i+1] = (float)i - depthOffset;
        }
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		
		ratio = (float) width/height;
		maxX = ratio;
		maxY = 1f;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
//		gl.glFrustumf(-(float)width/2f, (float)width/2f, -(float)height/2f, (float)height/2f, 3, 7);
		gl.glFrustumf(-ratio, ratio, -1, 1, 3, 15);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0f, 0f, 0f, 1.0f);
		
		initShapes();
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	private void drawRings(GL10 gl){
		for(int i = 0; i<numDepths; i++){
			float[] ring = genRing(depths[i]);
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(ring.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			FloatBuffer ringBuffer = byteBuf.asFloatBuffer();
			ringBuffer.put(ring);
			ringBuffer.position(0);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, ringBuffer);
			gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, ring.length/3);
		}
	}
	
	private float[] genRing(float depth){
		float ring[] = new float[3*(numVertices+1)]; 
		float vertStep = (float)(Math.PI*2f)/numVertices;
		for(int i=0; i<numVertices + 1; i++){
			ring[i*3] = (float)Math.cos(vertStep*i)*maxX;
			ring[i*3 + 1] = (float)Math.sin(vertStep*i)*maxY;
			ring[i*3 + 2] = depth;
		}
		
		return ring;
	}
}
