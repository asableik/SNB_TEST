package boss;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class VisualisationPanel extends JPanel{
	int size;
	List<Double> ins;
	double[][] outs;
	public VisualisationPanel(int size){
		this.size = size;
		ins = new ArrayList<Double>();
		outs = new double[size][size];
		setVisible(true);
	}
	
	public void draw(double[][] o){
		outs = o;
		this.repaint();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 400, 400);
		for(int i = 0;i<size;i++){
			for(int j =0;j<size;j++){
					Color col = new Color((int)(Math.abs(outs[i][j])), (int)(Math.abs(outs[i][j])), (int)(Math.abs(outs[i][j])));
					g.setColor(col);
					g.fillRect(i*400/size, j*400/size, 400/size, 400/size);
			}
		}
	}
}
