package boss;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
	
public class NeuralNetwork {
	int numberOfInputs = 2;
	int numberOfHiddenNeurons = 25;
	int numberOfOutputs = 1;
	int expectedOutput = 0;
	int globalI = 0;
	int reps = 0;
	
	double netError = 0;
	double beta = 1;
	double bias = 0;
	double outError = 0;
	double learningRate = 0.3d;
	double IHBias=2;
	double HJBias=10;
	double ILBias = 2;
	double HLBias = 2;
	double biasError=0;
	 
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

	
	public NeuralNetwork(VisualisationPanel vp){
		this.vPanel = vp;
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
		loadData("dupa.txt");
		initialiseWeights(Wih); //1
		initialiseWeights(Whj);//1
		//setWeights();
		while(true){
		getILins();//2
		//propagate forward
		propagateForward();
		//calc errors and backpropagate
		OLError(); //7
		//System.out.println("OLError: " + outError);
		HLError(); //8 
		//System.out.println("HLError: " + OLouts[0][0]);
		//drawProgress();
		//calcNetError();
		changeOLWeights(); //9
		changeHLWeights(); //10
		if(reps%100000 == 0) { drawProgress();calcNetError();};

		//System.out.println("WILBias: "+Arrays.toString(WILBias[0]));
		//System.out.println("WHLBias: "+Arrays.toString(WHLBias[0]));
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
			globalI = 0;
			Collections.shuffle(learningData);
		}


		//System.out.println(ILins[0][0]+" "+ ILins[0][1]+ " "+ expectedOutput);

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
				d[i][j] = -1 + 2*Math.random();
			}
		}
	} //initialiseWeithts
	
	void setWeights(){{
	//neuron 1
		Wih[0][0] = 0.156872;
		Wih[1][0] = 0.885006;
		WILBias[0][0] = 4.35196;
	//neuron 2
		Wih[0][1] = 0.143324;
		Wih[1][1] = 0.831352;
		WILBias[0][1] = 4.393581;
	//neuron 3
		Wih[0][2] = -36.66076;
		Wih[1][2] = -7.943919;
		WILBias[0][2] = 14.954909;
	//neuron 4
		Wih[0][3] = 0.583379;
		Wih[1][3] = 2.346503;
		WILBias[0][3] = 3.6818388;
	//neuron 5
		Wih[0][4] =-14.023706634294228;
		Wih[1][4] = 21.571332373059928;
		WILBias[0][4] = 0.4984156334909183;
	//neuron 6
		Wih[0][5] =0.673539553248472;
		Wih[1][5] = 0.7545379458076115;
		WILBias[0][5] = 4.247775387682993;
	//neuron 7
		Wih[0][6] =-3.3363067459452367;
		Wih[1][6] = 35.08142457669939;
		WILBias[0][6] = -16.883432585582177;
	//neuron 8
		Wih[0][7] = 0.3422204299730121;
		Wih[1][7] = 0.9593119609134101;
		WILBias[0][7] = 4.282071313371813;	
	//neuron 9
		Wih[0][8] = 0.4040080951061729;
		Wih[1][8] = 0.7995314967778571;
		WILBias[0][8] = 4.295825002042163;
		
	//neuron 10
		Wih[0][9] = 0.12314259341314858;
		Wih[1][9] = 0.8364712104240283;
		WILBias[0][9] = 4.385266506852125;
	//neuron 11
		Wih[0][10] = 0.2297150974141932;
		Wih[1][10] = 0.778362292191241;
		WILBias[0][10] = 4.380720183845237;
	//neuron 12
		Wih[0][11] = 0.5399706680584174;
		Wih[1][11] = 1.0561639446086;
		WILBias[0][11] = 4.217407685309505;
	//neuron 13
		Wih[0][12] = -36.666450042861946;
		Wih[1][12] = 5.190600510707538;
		WILBias[0][12] = 3.2792650788578195;
	//neuron 14
		Wih[0][13] = 10.565245670663339;
		Wih[1][13] = 43.1026234250162;
		WILBias[0][13] = -23.34933663400463;
	//neuron 14
		Wih[0][14] = 0.2948744604582052;
		Wih[1][14] = 0.8306337466089092;
		WILBias[0][14] = 4.328266234906378;
	// neuron wyjsciowy
		Whj[0][0]=3.1728557219554734;
		Whj[1][0]=3.2686704889174525;
		Whj[2][0]=-16.72518607531907;
		Whj[3][0]=1.015516483508811;
		Whj[4][0]=-18.91188184884326;
		Whj[5][0]=2.44011664755368;
		Whj[6][0]=19.569388124338474;
		Whj[7][0]=2.76024635787119;
		Whj[8][0]=2.769923057518508;
		Whj[9][0]=3.2730664820691495;
		Whj[10][0]=3.157947600520578;
		Whj[11][0]=2.437139386386023;
		Whj[12][0]=23.217065996724244;
		Whj[13][0]=-27.046349888981744;
		Whj[14][0]=2.9663515325665313;
		WHLBias [0][0]=3.989224299341897;
	}
		
	}
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
			//System.out.println(globalI+":"+outs[0][i]);
		}
	}
	
	void OLError(){
		outError = (expectedOutput - OLouts[0][0]) * derivative(OLstate[0][0]);
		//System.out.println("OLstate:" + OLstate[0][0]);
		//System.out.println("OLstate:" + OLstate[0][0]);

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
		Collections.shuffle(testData);
		//System.out.println("Output: " + OLouts[0][0]);
		System.out.println("Net error: " + netError);
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
			System.out.println(data.size());
			learningData = data.subList(0, 899);
			testData = data.subList(900, 999);
			//learningData = data.subList(0, 89999);
			//testData = data.subList(90000, 99999);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
