import josx.rcxcomm.*;
import java.io.*;
import java.net.*;

public class rpi {
	public static void main(String[] args) throws Exception {

        // IR handlers
		RCXPort port = new RCXPort("/dev/ttyUSB0");
        OutputStream out = port.getOutputStream();
		InputStream in = port.getInputStream();

        // network socket
        ServerSocket welcomeSocket = new ServerSocket(3001);

        int commandHandle = 0;
		int commandParameter = 0;

		while(true){
			try {


//             STD I/O method
//  			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//				System.out.println("Command: ");
//				commandHandle = Integer.parseInt(br.readLine());
//				System.out.println("Param: ");
//				commandParameter = Integer.parseInt(br.readLine());

                // read commands for execute
                // network socket
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                clientSentence = inFromClient.readLine();

                // extract parameters
                String[] requestParam = clientSentence.split(" ");
                requestParam = requestParam[1].split("&");
                requestParam[0] = requestParam[0].substring(2); // remove "/?"
                requestParam[0] = requestParam[0].split("=")[1];
                requestParam[1] = requestParam[1].split("=")[1];
                System.out.println("Parameters recieved and extracted: " + requestParam[0] + " " + requestParam[1]);

                // convert params to integer
                int[] commands = { Integer.parseInt(requestParam[0]), Integer.parseInt(requestParam[1]) };

                connectionSocket.close();

			}
			catch (Exception e) {
				e.printStackTrace();
			}

	        try {
                // send command to RCX
				IRSendInt(out,commands[0]);
				IRSendInt(out,commands[1]);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

            // print command
			System.out.println("Response: " + IRRecieveInt(in) + "angles in " + IRRecieveInt(in) + "ms");
		}
	}

        // send methods
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

