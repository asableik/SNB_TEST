package boss;

import info.monitorenter.gui.chart.ITrace2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
	
public class NeuralNetwork {
	int numberOfInputs = 2;
	int numberOfHiddenNeurons = 30;
	int numberOfOutputs = 1;
	int expectedOutput = 0;
	int globalI = 0;
	int reps = 0;
	int iterations = 0;
	int bestIteration =0;
	double bestNetError = 100000;
	double netError = 0;
	double beta = 1;
	double bias = 0;
	double outError = 0;
	double learningRate = 0.3d;
	double ILBias = 1;
	double HLBias = 1;
	double biasError=0;
	
	double []biasErrorr=new double[numberOfHiddenNeurons];
	
	double [][]WILBias = new double [1][numberOfHiddenNeurons];
	double [][]WHLBias = new double [1][1];
	double []hidError = new double [numberOfHiddenNeurons];
	double [][] ILins = new double[1][numberOfInputs];
	double [][] Wih = new double [numberOfInputs][numberOfHiddenNeurons];
	double [][] HLstate = new double [1][numberOfHiddenNeurons];
	double [][] HLouts = new double [1][numberOfHiddenNeurons];
	double [][] Whj = new double [numberOfHiddenNeurons][numberOfOutputs];
	double [][] OLstate = new double [1][1];
	double [][] OLouts = new double [1][1];
	List<double[]>data = new ArrayList<double[]>();// x, y, expected
	List<double[]>learningData = new ArrayList<double[]>();// x, y, expected
	List<double[]>testData = new ArrayList<double[]>();// x, y, expected
	
	VisualisationPanel vPanel;
	ITrace2D trace;
	
	public NeuralNetwork(VisualisationPanel vp,ITrace2D trace){
		this.vPanel = vp;
		this.trace = trace;
	}
	
	void drawProgress(){
		double [][] temp = new double [100][100];
		for(int i = 0;i<100;i++){
			ILins[0][0] = i*0.01;
			for(int j = 0;j<100;j++){
				ILins[0][1] = 1- j*0.01;
				propagateForward();
				temp[i][j] = OLouts[0][0]*255;
			}	
		}
		
		vPanel.draw(temp);
	}
	void calcNet(){
		loadData("mlotek.txt");
		initialiseWeights(Wih); //1
		initialiseWeights(Whj);
		//initialiseWeights(WILBias);
		//initialiseWeights(WHLBias);//1
		while(true){
		getILins();//2
		//propagate forward
		propagateForward();
		//calc errors and backpropagate
		OLError(); //7
		HLError(); //8 
		changeOLWeights(); //9
		changeHLWeights(); //10
		
		reps++;
		}
		
	
	}
	
	double [][] getILins(){
		double [] temp = learningData.get(globalI);
		globalI++;
		ILins[0][0] = temp[0];
		ILins[0][1] = temp[1];
		expectedOutput= (int) temp[2];
		if(globalI == 899){
			if(iterations%10 == 0) { drawProgress();calcNetError();};
			globalI = 0;
			iterations++;
			Collections.shuffle(learningData);
		}

		return this.ILins;
	}
	
	void propagateForward(){
		HLstate = new double [1][numberOfHiddenNeurons];
		OLstate = new double [1][1];
		calcLayerState(ILins,Wih,HLstate,ILBias,WILBias);//3
		calcLayerOuts(HLouts,HLstate);//4
		calcLayerState(HLouts,Whj,OLstate,HLBias,WHLBias);//5
		calcLayerOuts(OLouts,OLstate);//6
		//System.out.println("Output: " + OLouts[0][0]);

	};
	
	void initialiseWeights(double [][] d){
		for(int i = 0;i<d.length;i++){
			for (int j = 0;j<d[0].length;j++){
				d[i][j] = -0.1 + 0.2*Math.random();
			}
		}
	} //initialiseWeithts
	
	void calcLayerState(double[][] ins,double [][] w, double [][] state,double bias, double [][]biasWeight){
		for(int i =0;i<state[0].length;i++){
			for(int j = 0;j<ins[0].length;j++){
			state[0][i] += ins[0][j] * w[j][i];
			}
			state[0][i] += bias*biasWeight[0][i];
		}
	}
	
	void calcLayerOuts(double [][] outs,double [][] state){
		for(int i = 0; i<outs[0].length;i++){
			outs[0][i] = function (state[0][i]);
		}
	}
	
	void OLError(){
		outError = (expectedOutput - OLouts[0][0]) * derivative(OLstate[0][0]);

	}
	
	void HLError(){
		for (int i = 0;i<hidError.length;i++){
			hidError[i] = derivative (HLstate[0][i]) * outError * Whj[i][0];
		}
			biasError = derivative(1) * outError * 1;
		
	}
	
	void changeOLWeights(){
		for (int i = 0 ;i<Whj.length;i++){
			Whj[i][0] += learningRate*outError*HLouts[0][i];
		}
		WHLBias[0][0] +=learningRate* outError * 1;
			
	}
	
	void changeHLWeights(){
		for (int i = 0 ;i<Wih[0].length;i++){
			for( int j = 0;j<Wih.length;j++){
				Wih[j][i] += learningRate*hidError[i]*ILins[0][j];
			}
			WILBias[0][i] += learningRate* biasError * 1;
		}
	}
	
	void calcNetError(){
		netError = 0;
		for(int i = 0 ; i<testData.size();i++){
			double [] temp = testData.get(i);
			ILins[0][0] = temp[0];
			ILins[0][1] = temp[1];
			expectedOutput= (int) temp[2];
			propagateForward();
			netError += Math.abs(expectedOutput - OLouts[0][0]);
			}
		netError = (netError)/(testData.size())*100;
		
		if(netError<bestNetError){
			bestNetError = netError;
			bestIteration = iterations;
			
			
			//WYPISYWANIE WAG
			System.out.println("Wagi input-hidden");
				for (int j = 0;j<Wih[0].length;j++){
					System.out.println("Wagi neuronu "+j+"["+Wih[0][j]+","+Wih[1][j]+"]");
				}
				
				
				for (int j = 0;j<WILBias[0].length;j++){
					System.out.println("Wagi neuronu bias"+j+"["+WILBias[0][j]+"]");
				}
				
				System.out.println("Wagi hidden-output");
				for (int j = 0;j<Whj[0].length;j++){
					System.out.println("Wagi neuronu "+j+"["+Whj[j][0]+"]");
				}
			//USUNAC JAK NIEPOTRZEBNE
			System.out.println("Lowest Net Error: "+netError+" in iteration: "+iterations);
		}
		Collections.shuffle(testData);
		trace.addPoint(iterations,netError);
		}
		
	double function(double x){
//		if(x>1000){
//			return 1;
//		} else if (x<-1000){
//			return 0;
//			}
//		else {
			return 1/(1+Math.exp(-(beta * x)));
		//}
	}
	
	double derivative(double x){
//		if(x > 1000 || x<-1000){
//			return 0;
//		} else
		return  //function(x)*(1-function(x));
				(beta * Math.exp(beta * (x + bias))/(Math.pow(Math.exp(beta * (x + bias)) + 1,2)));
	}

	void loadData(String name){
		try {
			Scanner in = new Scanner(new FileReader(name));
			while(in.hasNextLine()){
				Scanner scanner = new Scanner (in.nextLine());
				scanner.useDelimiter(";");
				while(scanner.hasNext()){
				data.add(new double[]{Double.parseDouble(scanner.next()),
									  Double.parseDouble(scanner.next()),
									  Integer.parseInt(scanner.next()) 
									  });
				}
				//System.out.println(Arrays.toString(data.get(data.size())));
				scanner.close();
			};
			in.close();
			//System.out.println(data.size());
			learningData = data.subList(0, 899);
			testData = data.subList(900, 999);
			learningData = data.subList(0, 89999);
			testData = data.subList(90000, 99999);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
