package Parts;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public abstract class XItem implements XFrame {

	protected boolean hidden = false;
	protected int width = 0;
	protected int height = 0;
	protected String name = "";
	protected Texture image = null;
	protected int x1 = 0;
	protected int y1 = 0;
	protected int x2 = 0;
	protected int y2 = 0;
	
	
	public XItem(int x, int y){
		this.x1 = x;
		this.y1 = y;
	}

	@Override
	public String getType() {
		return "item";
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
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
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String new_name) {
		this.name = new_name;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public void hidden(boolean state) {
		this.hidden = state;
	}

	@Override
	public boolean isSelected() {
		if (Mouse.getX() >= this.x1 && Mouse.getX() <= this.x2
				&& Mouse.getY() <= Display.getHeight() - this.y1
				&& Mouse.getY() >= Display.getHeight() - this.y2) {
			return true;
		}
		return false;
	}

	@Override
	public void setPosition(int x, int y) {
		this.x1 = x;
		this.y1 = y;
		this.x2 = x + this.width;
		this.y2 = y + this.height;
	}

	@Override
	public void setPosition(int x, int y, int x2, int y2) {
		this.x1 = x;
		this.y1 = y;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	abstract void action();

}
