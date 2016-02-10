package Parts;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class XImage implements XFrame {

	private boolean hidden = false;
	private int width = 0;
	private int height = 0;
	private String name = "";
	private Texture image = null;
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;

	public XImage(String fileName, String type, int x, int y, int x2, int y2) {
		switch (type) {
		case "jpg":
			try {
				image = TextureLoader.getTexture("JPG",
						ResourceLoader.getResourceAsStream(fileName));
			} catch (IOException e) {
				System.out.println("Check name of the file and type");
				e.printStackTrace();
			}
			break;
		case "png":
			try {
				image = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream(fileName));
			} catch (IOException e) {
				System.out.println("Check name of the file and type");
				e.printStackTrace();
			}
			break;
		}
		this.width = image.getImageWidth();
		this.height = image.getImageHeight();
		this.x1 = x;
		this.y1 = y;
		this.x2 = x2;
		this.y2 = y2;
	
	}
	
	public XImage(String fileName, String type, int x, int y) {
		switch (type) {
		case "jpg":
			try {
				image = TextureLoader.getTexture("JPG",
						ResourceLoader.getResourceAsStream(fileName));
			} catch (IOException e) {
				System.out.println("Check name of the file and type");
				e.printStackTrace();
			}
			break;
		case "png":
			try {
				image = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream(fileName));
			} catch (IOException e) {
				System.out.println("Check name of the file and type");
				e.printStackTrace();
			}
			break;
		}
		this.width = image.getImageWidth();
		this.height = image.getImageHeight();
		this.x1 = x;
		this.y1 = y;
		this.x2 = x + getWidth();
		this.y2 = y + getHeight();
		
	}

	@Override
	public String getType() {
		return "image";
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
		if(Mouse.getX()>=this.x1 && Mouse.getX() <=this.x2 &&  Mouse.getY()<=Display.getHeight() - this.y1 && Mouse.getY()>= Display.getHeight()-this.y2){
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

}
