
import java.io.*;
import josx.rcxcomm.*;
import josx.platform.rcx.*;


public class rcxcommand{

	private static RCXInputStream IRLinkIN = new RCXInputStream();
	private static RCXOutputStream IRLinkOUT = new RCXOutputStream();

	public static void main(String []arg) throws Exception{
		Sensor.S1.setTypeAndMode(4,0xE0);
		Sensor.S1.activate();

		while(true){
			int[] commandData = commandGet();
	
			/*if(commandData[0] == -1){
				commandReturn([-1,-1]);
				break;
			}*/
			//else {
				commandReturn(commandRun(commandData));
			//}
		}

	}

	private static int[] commandGet() throws IOException{
		int[] command = new int[2];

		command[0] = IRRecieveInt(IRLinkIN);
		command[1] = IRRecieveInt(IRLinkIN);

		return command;
	}

	private static void commandReturn(int[] data) throws IOException{
		IRSendInt(IRLinkOUT, data[0]);
		IRSendInt(IRLinkOUT, data[1]);
	}

	private static int[] commandRun(int[] command){
		int commandIndex = command[0]%10;
			command[0] = command[0]/10;
		int commandArgType = command[0]%10;

		int[] response = new int[2];
		if(commandIndex == 1 || commandIndex == 2)
			response = Move(commandIndex,commandArgType,command[1]);

		return response;
	}

	/*
	public static int runtimeCheck(){
		try{
			IRSendInt(IRLinkOUT,1);
			return IRRecieveInt(IRLinkIN);
		} catch(IOException e) {}
	}
	*/

        public static int[] Move(int moveType, int argType, int duration){
		Sensor.S1.setPreviousValue(0);
		int starttime = (int)System.currentTimeMillis();
		int[] runData = new int[2];

		if(duration >= 0){
			if(moveType == 1)
	                       	bMotorFWD();
			else if(moveType == 2)
				bMotorROTL();
		}
                else if(duration < 0){
			if(moveType == 1)
	                       	bMotorBKW();
			else if(moveType == 2)
				bMotorROTR();
		}



		if(argType == 1){
			while(Math.abs(((int)System.currentTimeMillis())-starttime) < Math.abs(duration)) {}
		}

		else if (argType == 2){
			while(Math.abs(Sensor.S1.readValue()) <= Math.abs(duration)) {}
		}

       		bMotorSTOP();

		runData[0] = Math.abs(Sensor.S1.readValue());
		runData[1] = Math.abs((int)System.currentTimeMillis() - starttime);

		return runData;
        }

	// BASIC/PRIMITIVE METHODS

        public static void bMotorFWD(){
                Motor.A.forward();
                Motor.C.forward();
        }

        public static void bMotorBKW(){
                Motor.A.backward();
                Motor.C.backward();
        }

        public static void bMotorSTOP(){
                Motor.A.stop();
                Motor.C.stop();
        }

        public static void bMotorROTL(){
                Motor.A.backward();
                Motor.C.forward();
        }

        public static void bMotorROTR(){
                Motor.A.forward();
                Motor.C.backward();
        }

        public static void bMotorPOW(int POW){
                Motor.A.setPower(POW);
                Motor.B.setPower(POW);
        }

        public static void bSleep(int ms){
                try{
                        Thread.sleep(ms);
                }
                catch(Exception e){}
        }

	// IR COMM METHODS

        public static void IRSendInt(OutputStream IRLink, int x) throws IOException{
                if(x<0) IRLink.write(1); // sign 1 = negative 0 = positive
                else IRLink.write(0);
                IRLink.write(Math.abs(x)/250); // times 250 in x
                IRLink.write(Math.abs(x)%250); // rest till 250
        }

        public static int IRRecieveInt(InputStream IRLink) throws IOException{
                int xSign = IRLink.read();
                int xTimes = IRLink.read();
                int xMod = IRLink.read();
                if(xSign == 1) return -1*((xTimes*250)+xMod);
                else return ((xTimes*250)+xMod);
        }


}


