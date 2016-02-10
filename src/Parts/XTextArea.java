package Parts;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class XTextArea implements XFrame{

	private int width = 0;
	private int height = 0;
	private String text = "";
	private String name = "";
	private boolean hidden = false;
	private boolean writable = false;
	private int x1 = 0;
	private int y1 = 0;
	private int x2 = 0;
	private int y2 = 0;
	private static int size = 24;
	private Color color = Color.black;
	private static UnicodeFont font;

	
	public XTextArea(int x, int y, String text){
		this.x1 = x;
		this.y1 = y;
		loadFonts();
	}

	
	@Override
	public String getType() {
		return "text";
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
		font.drawString(this.x1, this.y1, this.text, color);
		return false;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setSize(int new_size){
		size = new_size;
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
	
	public boolean isWritable(){
		return writable;
	}
	
	public void setWritable(boolean state){
		this.writable = state;
	}
	
	public void write(String letter){
		if(writable){
		text += letter;
		}
	}
	
	public void delete(){
		if(text.length()>0 && writable){
		text = text.substring(0, text.length()-1);
		}
	}
	
	@Override
	public boolean isSelected() {
		if(Mouse.getX()>=this.x1 && Mouse.getX() <=this.x2 && Mouse.getY()>=this.y1 &&Mouse.getY()<=this.y2){
			return true;
		}
		return false;
	}
	
	private static void loadFonts(){
		font = new UnicodeFont(new java.awt.Font("Times New Roman",
				java.awt.Font.PLAIN, size));
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setPosition(int x, int y) {
		this.x1 = x;
		this.y1 = y;
	}

	@Override
	public void setPosition(int x, int y, int x2, int y2) {
		this.x1 = x;
		this.y1 = y;
	}
}
