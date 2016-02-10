package Parts;

public interface XFrame {

	public String getType();
	public int getWidth();
	public int getHeight();
	public boolean render();
	public String getName();
	public void setName(String new_name);
	public boolean isHidden();
	public void hidden(boolean state);
	public boolean isSelected();
	public void setPosition(int x, int y);
	public void setPosition(int x, int y, int x2, int y2);
}
