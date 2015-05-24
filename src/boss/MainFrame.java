package boss;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
		static MainFrame thisMainFrame;
		static VisualisationPanel vPanel;
		public MainFrame(){
		thisMainFrame = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600	,700);
		setTitle("frame");
		//v = new Vector<Integer>();
		vPanel = new VisualisationPanel(100);
		this.add(vPanel);
	}
	
	
	public static void main(String [] args){
		
		EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
		
		
			while (thisMainFrame==null){
				System.out.println("ahoj");	
			}
			NeuralNetwork network = new NeuralNetwork(vPanel);
			network.calcNet();
	}
}
