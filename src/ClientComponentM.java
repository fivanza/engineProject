import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

import processing.core.PApplet;

/*
 * Author: Ivan Zamora
 * Bibliography:
 * [1] Fig. 27.11: Client.java and Fig. 27.9: Server.java , Java How to Program Early Objects 9th edition, Deitel.
 * [2] GreetingClient.java and  GreetingServer.java , http://www.tutorialspoint.com/java/java_networking.htm
 */
public class ClientComponentM extends PApplet {

	GameObj c;
	String serverName = "localhost";
    int port = 2223;
    LinkedList<GameObj>retrieveList= new LinkedList<>();
	
	
	public void setup() 
	{
		this.size(400, 400);
		Random rand = new Random();
		c= new GameObj(this,25,this.height-50,50,50);
		c.setColor(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		c.setIDclient(this.hashCode());
		
		c.enableDrawable();
		c.enableMovable();
		c.moveComponent.EnableKeyboardControl(true);
		c.enableCollidable();
		c.collidableComponent.enableCollidableBeweenObjects();
		
	
		c.enableCollidableWithBoundaries();
	}
	 public void draw() 
	 {
		 background(24,20,100);
		 fill(255,0,0);
		 driveKeyboard();
		 makeConectiontoSever();
		 if(!retrieveList.isEmpty())
		 {
			 for(GameObj a: retrieveList)
			 { 
				 //System.out.println("posx "+a.posX+" posy "+a.posY);
				 if(a.IDclient== c.IDclient)
					 c=a;
				 if(a.drawableComponent!=null)
					 a.updateInClient(this);
				// a.drawObject(this);
			 }
		 }
		 
		
		 
	 }
	 public void driveKeyboard()
	 {
		 if(this.keyPressed)
		 {
			 if(keyCode == LEFT)
			 {
			 c.moveComponent.leftMove=true;
			 //System.out.println("Move left");
			 }
			 
			 if(keyCode == RIGHT)
			 {
				 c.moveComponent.rightMove=true;
				// System.out.println("Move right");
			 }
			 if(this.key == ' ' && c.moveComponent.jumping != true)
				{
				 c.moveComponent.jumping=true;
				 c.moveComponent.up=true;
					//c.posY-=2;
					
				}
		
		 }
	 }
	 
	 @SuppressWarnings("unchecked")
	public void makeConectiontoSever()
	 {
		      
			try
		      {
				//[2]
				/*
				 * This part of code is based on GreetingClient.java
				 */
				

		         Socket socketConnection = new Socket(serverName, port);
		         

		         ObjectOutputStream clientOutputStream = new
		            ObjectOutputStream(socketConnection.getOutputStream());
		         ObjectInputStream clientInputStream = new 
		            ObjectInputStream(socketConnection.getInputStream());

		         clientOutputStream.writeObject(c);

		        // c= (GameObject)clientInputStream.readObject();
		         try{
		        	 retrieveList.clear();
		        	 retrieveList.addAll((LinkedList<GameObj>)clientInputStream.readObject());
		       // retrieveList=(LinkedList<GameObject>)clientInputStream.readObject();
		         }
		         catch(Exception e){
		        	 System.out.println(e);
		         }

		         clientOutputStream.close();
		         clientInputStream.close();
				
		        
		      }catch(Exception e)
		      {
		    	
		    	  
		      }

	 }
}
