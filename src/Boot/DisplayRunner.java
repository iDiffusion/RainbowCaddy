import com.jogamp.opengl.GL2;

public interface DisplayRunner {
	/**
	 * This method runs every time the draw method is called in the Display Class
	 * @param gl
	 */
	public void run(GL2 gl);
}
