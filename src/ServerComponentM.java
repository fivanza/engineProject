import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Author: Ivan Zamora
 * Bibliography:
 * [1]socket programming multiple client to one server; http://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
 */
public class ServerComponentM {
ConcurrentHashMap<Integer, GameObj> clientsMap = new ConcurrentHashMap<>();
	
	int IDServer;
	int port;
	ServerSocket socketConnection;
	
	GameObj deadZone;
	GameObj deadZone1;
	
	//game world facts
	float gameWorldHeigh;
	float gameWorldWidth;
	
	boolean busy=false;
	
	ServerComponentM(int i)
	{
		port=i;
		IDServer= this.hashCode();
		try {
			socketConnection = new ServerSocket(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setWolrdSize(float w, float h)
	{
		gameWorldHeigh=w;
		gameWorldHeigh=h;
	}
	public void gameWorldCreation()
	{
		// static platform
		GameObj platform1= new GameObj(gameWorldWidth, gameWorldHeigh, 
				280, (float)(gameWorldHeigh*0.75),80,30);
		platform1.setColor(255, 0, 0);
		platform1.enableDrawable();
		platform1.enableCollidable();
		platform1.collidableComponent.enableCollidableBeweenObjects();
		clientsMap.put(platform1.ID, platform1);
		
		//moving platform
		GameObj Movingplatform= new GameObj(gameWorldWidth, gameWorldHeigh, 
				(float)(gameWorldWidth*0.75), (float)(gameWorldHeigh*0.75),80,30);
		Movingplatform.setColor(0, 255, 0);
		Movingplatform.enableDrawable();
		Movingplatform.enableMovable();
		Movingplatform.moveComponent.EnableAutomaticPatter(0, 0, 300, 0);
		Movingplatform.moveComponent.setVelosity((float)0.5, (float)0.5);
		Movingplatform.enableCollidable();
		Movingplatform.collidableComponent.enableCollidableBeweenObjects();
		Movingplatform.setIdent(34);
		clientsMap.put(Movingplatform.ID, Movingplatform);
		
		//dead zone
		 deadZone= new GameObj(gameWorldWidth, gameWorldHeigh, 
				300, (float)(gameWorldHeigh*0.95),80,30);
		 //deadZone.setColor(0, 0, 255);
		 //deadZone.enableDrawable();
		 deadZone.enableCollidableAsDeadZone();
		 deadZone.collidableasdeadzone.enaleDeadZoneBehavior();
		 
		//dead zone1
		 deadZone1= new GameObj(gameWorldWidth, gameWorldHeigh, 
				200, 150,30,30);
		 //deadZone1.setColor(0, 0, 255);
		 //deadZone1.enableDrawable();
		 deadZone1.enableCollidableAsDeadZone();
		 deadZone1.collidableasdeadzone.enaleDeadZoneBehavior();
		
		//Swap Point
		GameObj swapPoint= new GameObj(gameWorldWidth, gameWorldHeigh, 
				100, 50,10,10);
		//swapPoint.setColor(5, 100, 100);
		//swapPoint.enableDrawable();
		
		deadZone.collidableasdeadzone.addSpawnPoints(swapPoint);
		deadZone1.collidableasdeadzone.addSpawnPoints(swapPoint);
		//Swap Point 1
		GameObj swapPoint1= new GameObj(gameWorldWidth, gameWorldHeigh, 
				200, 50,10,10);
		//swapPoint1.setColor(5, 100, 100);
		//swapPoint1.enableDrawable();
				
				deadZone.collidableasdeadzone.addSpawnPoints(swapPoint1);
				deadZone1.collidableasdeadzone.addSpawnPoints(swapPoint1);
		
		//Swap Point 2
		GameObj swapPoint2= new GameObj(gameWorldWidth, gameWorldHeigh, 
				50, 100,10,10);
		//swapPoint2.setColor(5, 100, 100);
		//swapPoint2.enableDrawable();
		
		deadZone.collidableasdeadzone.addSpawnPoints(swapPoint2);
		deadZone1.collidableasdeadzone.addSpawnPoints(swapPoint2);
		
		clientsMap.put(swapPoint.ID, swapPoint);
		clientsMap.put(swapPoint1.ID, swapPoint1);
		clientsMap.put(swapPoint2.ID, swapPoint2);
		clientsMap.put(deadZone.ID, deadZone);
		clientsMap.put(deadZone1.ID, deadZone1);
		
	}
	
	public void refreshClient()
	{
		while(true)
		{
			
		GameObj Gameclient = null;

	      try {

	        // ServerSocket socketConnection = new ServerSocket(port);

	        // System.out.println("Server Waiting");

	         Socket pipe = socketConnection.accept();

	         ObjectInputStream serverInputStream = new    
	            ObjectInputStream(pipe.getInputStream());

	         ObjectOutputStream serverOutputStream = new 
	            ObjectOutputStream(pipe.getOutputStream());

	         Gameclient = (GameObj)serverInputStream.readObject();
	        if(!clientsMap.containsKey(Gameclient.IDclient))
	        {
	        	
	        	clientsMap.put(Gameclient.IDclient, Gameclient);
	        	
	        }
	        else
	        {
	        	clientsMap.remove(Gameclient.IDclient);
	        	clientsMap.put(Gameclient.IDclient, Gameclient);
	        	
	        }
	        for (Integer key: clientsMap.keySet())
	        {
	        	//keyboardMove(clientsMap.get(key));
	        	//clientsMap.get(key).drive();
	        	//clientsMap.get(key).CollideBoundaries();
	        	
	        }
	       
	        
	       
	         //serverOutputStream.writeObject(clients);
	        serverOutputStream.writeObject(new LinkedList<GameObj>(clientsMap.values()));
	         serverInputStream.close();
	         serverOutputStream.close();
	         //socketConnection.close();


	      }  catch(Exception e) {
	    	 // System.out.println("problem server");
	    	  System.out.println(e); 
	      }
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		int port=2223;
		ServerComponentM application = new ServerComponentM(port); // create server
	   application.setWolrdSize(400, 400);
	   application.gameWorldCreation();
	      //application.refreshClient(); // run server application
	      application.refreshClientByRun();
	}
	
	/*
	 * [1] create a separate thread for every client
	 */
	public void refreshClientByRun()
	{
		while(true)
		{
			Socket S;
			try {
				S = socketConnection.accept();
				SocketHandlerThread t= new SocketHandlerThread(S);
				t.run();
			} 
			catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}

	public class SocketHandlerThread implements Runnable
	{
		Socket so;
		SocketHandlerThread(Socket s)
		{
			so=s;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
				
			GameObj Gameclient = null;

		      try {

		   

		         ObjectInputStream serverInputStream = new    
		            ObjectInputStream(so.getInputStream());

		         ObjectOutputStream serverOutputStream = new 
		            ObjectOutputStream(so.getOutputStream());

		         Gameclient = (GameObj)serverInputStream.readObject();
		         clientsMap.get(deadZone.ID).collidableasdeadzone.addPlayers(Gameclient);
		         clientsMap.get(deadZone1.ID).collidableasdeadzone.addPlayers(Gameclient);
		        if(!clientsMap.containsKey(Gameclient.IDclient))
		        {
		 	
		        	if(Gameclient.collidableComponent!=null && Gameclient.collidableComponent.collidableBetweenObjects==true)
		        	{
		        		for (Integer key: clientsMap.keySet())
				        {
			        		if(clientsMap.get(key).collidableComponent!=null  &&
				        			clientsMap.get(key).collidableComponent.collidableBetweenObjects==true)
			        		{
			        			Gameclient.collidableComponent.addObjectsToInteract(clientsMap.get(key));
			        			clientsMap.get(key).collidableComponent.addObjectsToInteract(clientsMap.get(Gameclient.IDclient));
			        		}
				        }
		        	}
		        	//deadZone.collidableasdeadzone.deadZoneBehavior(Gameclient);
		        	
		        	clientsMap.put(Gameclient.IDclient, Gameclient);
		        	
		        }
		        else
		        {
		        	//deadZone.collidableasdeadzone.deadZoneBehavior(Gameclient);
		        	//clientsMap.get(deadZone.ID).collidableasdeadzone.addObjectsToInteract(Gameclient);
		        	clientsMap.remove(Gameclient.IDclient);
		        	clientsMap.put(Gameclient.IDclient, Gameclient);
		        	
		        }
		        
		       /* if(clientsMap.get(Gameclient.IDclient).collidableComponent!=null &&
	        			clientsMap.get(Gameclient.IDclient).collidableComponent.collidableBetweenObjects==true)
	        	{
		        	for (Integer key: clientsMap.keySet())
			        {
		        		if(clientsMap.get(key).collidableComponent!=null  &&
			        			clientsMap.get(key).collidableComponent.collidableBetweenObjects==true)
		        		{
		        			clientsMap.get(Gameclient.IDclient).collidableComponent.addObjectsToCollide(clientsMap.get(key));
		        			//clientsMap.get(key).collidableComponent.addObjectsToCollide(clientsMap.get(Gameclient.IDclient));
		        		}
		        		clientsMap.get(key).updateInServer();
			        }
	        	}*/
		        
		        for (Integer key: clientsMap.keySet())
		        {
		        	
		        	clientsMap.get(key).updateInServer();
		        	//keyboardMove(clientsMap.get(key));
		        	//clientsMap.get(key).drive();
		        	//clientsMap.get(key).CollideBoundaries();
		        	
		        }
		       
		        
		       
		         //serverOutputStream.writeObject(clients);
		        serverOutputStream.writeObject(new LinkedList<GameObj>(clientsMap.values()));
		         serverInputStream.close();
		         serverOutputStream.close();
		         //socketConnection.close();


		      }  catch(Exception e) {
		    	 // System.out.println("problem server");
		    	 // System.out.println(e); 
		    	  e.printStackTrace();
		      }
			
		}
		
	}

}
