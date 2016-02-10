package Parts;

import kz.kuzhagaliyev.TimboKZ.ActiveVisualiser.Core;

import org.lwjgl.opengl.GL11;

public class XSong extends XItem{

	private String path = "";
	
	public XSong(int x, int y, String path) {
		
		super(x, y);
		this.path = path;
	}
	
	@Override
	public String getType() {
		return "song";
	}
	
	@Override
	public boolean render() {
		if (!hidden) {
			image.bind();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x1, y1);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(x2, y1);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(x2, y2);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(x1, y2);
			GL11.glEnd();
			return true;
		}
		return false;
	}
	
	@Override
	void action() {
		Core.play_the_song(path);
	}

}
