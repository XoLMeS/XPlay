package Parts;

import java.awt.event.ActionListener;


public class XButton extends XImage{

	ActionListener l;
	
	public XButton(String fileName, String type, int x, int y) {
		super(fileName, type, x, y);
	}
	
	public void addActionListener(ActionListener l){
		this.l = l;
	}
	
	public void action(){
		if(!isHidden()){
		l.actionPerformed(null);
		}
	}

}
