package Parts;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class XList extends XItem {

	private ArrayList<XItem> items;
	private int first_to_render = 0;	
	private int capacity = 0;
	
	public XList(int x, int y, int height, int width, int capacity, String name) {
		super(x, y);
		items = new ArrayList<XItem>();
		this.height = height;
		this.width = width;
		this.x2 = this.x1 + width;
		this.y2 = this.y1 + height;
		this.capacity = capacity;
		this.name = name;
	}

	@Override
	public String getType() {
		return "list";
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
	
	public XItem getSelectedItem(){
		for(int i = first_to_render; i < items.size() && i < first_to_render+capacity; i++){
			if(items.get(i).isSelected()){
				items.get(i).action();
			}
		}
		return null;
	}
	
	public void addItem(XItem item){
		items.add(item);
	}

	@Override
	void action() {
		
		
	}

}
