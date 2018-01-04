import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
/**
 * Displays Boot.mesh.points Objects
 * 
 * @author Keegan Bruer <br>
 *
 */
@SuppressWarnings("serial")
public class Display extends JFrame implements GLEventListener, MouseListener {
	public boolean saveImage = false;
	DisplayRunner runner;
	double x = 0, y = 0, z = 89;
	GL2 gl2;
	public int width = 0, height = 0, frameX = 0, frameY = 0;
	
	public Display(int width, int height, int x, int y) {
		super("Minimal OpenGL");
		setUpDisplay(width, height, x ,y);
	}
	
	public Display(int width, int height) {
		super("Minimal OpenGL");
		setUpDisplay(width, height, 0 , 0);
	}
	/**
	 * Create a new Display Frame to display Boot.mesh.pointses
	 * @param width
	 * @param height
	 */
	public void setUpDisplay(int width, int height, int frameX, int frameY) {
		this.width = width;
		this.height = height;
		this.frameX = frameX;
		this.frameY = frameY;
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		
		GLCanvas canvas = new GLCanvas(capabilities);
		FPSAnimator animator = new FPSAnimator(canvas, 60);
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		animator.start(); 
		
		this.setName("Minimal OpenGL");
		this.getContentPane().add(canvas);
		
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setLocation(frameX, frameY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setResizable(false);
        canvas.requestFocusInWindow();
        
        canvas.addKeyListener(new KeyListener() {
        	boolean admin = false;
			@Override
			public void keyPressed(KeyEvent arg0) {
				switch(arg0.getKeyChar()) {
				case 'w':
					if (z > 78 || admin) {
						z -= 0.05;
					}
					break;
				case 'a':
					x += 1;
					break;
				case 'd':
					x -= 1;
					break;
				case 's':
					if (z < 89 || admin) {
						z += 0.05;
					}
					break;
				case 'q':
					z = 89;
					x = 0;
					break;
				} 
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			int counter = 0;
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if (counter == 0 && c == 'a') {
					counter++;
				} else if (counter == 1 && c == 'd') {
					counter++;
				} else if (counter == 2 && c == 'm') {
					counter++;
				} else if (counter == 3 && c == 'i') {
					counter++;
				} else if (counter == 4 && c == 'n') {
					admin = !admin;
					counter = 0;
				}
			}
        	
        });
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		render(gl);
	}
	
	private void render(GL2 gl) {
		gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
	    gl.glLoadIdentity(); // Reset The View
	    gl.glTranslated( 0, 0, 0); // Move the triangle down
	    //gl.glRotated( 50, 1, 0, 0 ); //zoom based on w and s
	    gl.glRotated( z, 0, 0, 0 ); //zoom based on w and s
	    gl.glRotated( x, 0, 1, 0 ); // rotate around up/down axis with a/d

		runner.run(gl);
		if (saveImage) {
			saveImage(gl, new File("screenshot.png"));
			saveImage = false;
		}
		gl.glFlush();
	}
	
	/**
	 * Use this to set the DisplayRunner. A interface allowing user to specify which Boot.mesh.pointses to display.
	 * @param run
	 */
	public void setDisplayMethod(DisplayRunner run) {
		runner = run;
	}
	
	

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		//GL2 gl = drawable.getGL().getGL2();
		//gl.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
	}
	
	public boolean saveImage(GL2 gl, File outputFile) {
        ByteBuffer pixelsRGB = Buffers.newDirectByteBuffer(width * height * 3);

		gl.glReadBuffer(GL.GL_BACK);
        gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);

        gl.glReadPixels(0, 0, width, height, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, pixelsRGB);

        int[] pixels = new int[width * height];

        int firstByte = width * height * 3;
        int sourceIndex;
        int targetIndex = 0;
        int rowBytesNumber = width * 3;

        for (int row = 0; row < height; row++) {
            firstByte -= rowBytesNumber;
            sourceIndex = firstByte;
            for (int col = 0; col < width; col++) {
                if (pixelsRGB.get(sourceIndex) != 0) {
                    //System.out.println(sourceIndex);
                }

                int iR = pixelsRGB.get(sourceIndex++);
                int iG = pixelsRGB.get(sourceIndex++);
                int iB = pixelsRGB.get(sourceIndex++);

                pixels[targetIndex++] = 0xFF000000
                        | ((iR & 0x000000FF) << 16)
                        | ((iG & 0x000000FF) << 8)
                        | (iB & 0x000000FF);
            }

        }

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);

        try {
            ImageIO.write(bufferedImage, "PNG", outputFile);
            return true;
        } catch (Exception e) {
        	return false;
        }       

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Boot.mesh.setColor(255, 255, 255);
		double mouseX = e.getX() - (this.getWidth() /2);
		double mouseY = e.getY() - (this.getHeight() /2);
		
		
		System.out.println(mouseX + " " + mouseY);
		double error = 3;
		for (Point p : Boot.mesh.points) {
			if (p.x > mouseX-error && p.x < mouseX+error && p.y > mouseY-error && p.y < mouseY+error) {
				p.setRGB(0, 255, 0);
			}
		}
		
		
		
		Point center = new Point(0, 0, 0);
		CircleGen.nearestNeighbor(center, Boot.mesh.points, 1);
		System.out.println("test");
		CircleGen.CircleGeneration(center, Boot.mesh.points, 360, 5);
		System.out.println("Success");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
