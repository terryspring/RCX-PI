import josx.rcxcomm.*;
import java.io.*;

public class rpi {
	public static void main(String[] args) throws Exception {

		RCXPort port = new RCXPort("/dev/ttyUSB0");
		OutputStream out = port.getOutputStream();
		InputStream in = port.getInputStream();
		int commandHandle = 0;
		int commandParameter = 0;

		while(true){
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Command: ");
				commandHandle = Integer.parseInt(br.readLine());
				System.out.println("Param: ");
				commandParameter = Integer.parseInt(br.readLine());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

	        try {
				IRSendInt(out,commandHandle);
				IRSendInt(out,commandParameter);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("Response: " + IRRecieveInt(in) + "angles in " + IRRecieveInt(in) + "ms");
		}
	}

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

