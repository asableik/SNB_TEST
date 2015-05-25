package boss;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.EventQueue;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
		static MainFrame thisMainFrame;
		static VisualisationPanel vPanel;
		static Chart2D chart;
		static ITrace2D trace;
		public MainFrame(){
		thisMainFrame = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600	,700);
		setTitle("frame");
		vPanel = new VisualisationPanel(100);
		this.add(vPanel);
		vPanel.setSize(400,400);

		this.setLayout(null);

		chart = new Chart2D();
		trace = new Trace2DSimple();
		trace.setName("Net error");
		chart.setSize(600,400);
		chart.setLocation(400,0);
		chart.addTrace(trace);
		thisMainFrame.add(chart);
		trace.addPoint(0,0);
		
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
			while (thisMainFrame.isVisible()== false){
				System.out.println("ahoj");	
			}
	
			System.out.println(trace);	
			NeuralNetwork network = new NeuralNetwork(vPanel,trace);
			network.calcNet();
	}
}
