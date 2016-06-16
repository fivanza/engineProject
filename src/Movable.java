import java.io.Serializable;

import processing.core.PApplet;


public class Movable implements Component, Serializable {
	
	GameObj gameobject;
	float []speed= new float[2];
	boolean auto;
	boolean keyboardControl;
	boolean moveWithPatter;
	
	boolean leftMove;
	boolean rightMove;
	boolean jumping;
	boolean up;
	
	float jumpPoint;
	int jumpCount=0;
	
	Float[]patterns= new Float[4];
	
	Movable(GameObj a, float xspeed, float yspeed)
	{
		gameobject=a;
		speed[0]=xspeed;
		speed[1]=yspeed;
	}
	public void setVelosity(float vx, float vy)
	{
		speed[0]=vx;
		speed[1]=vy;
	}
	public void EnableAutomaticMove(boolean a)
	{
		auto=a;
	}
	public void EnableKeyboardControl(boolean a)
	{
		keyboardControl=a;
	}
	public void EnableAutomaticPatter(float xo, float yo, float xf, float yf)
	{
		moveWithPatter=true;
		patterns[0]=xo;
		patterns[1]=xf;
		patterns[2]=yo;
		patterns[3]=yf;
	}
	public void moveWithPatternBehavior()
	{
		if(patterns[1]==patterns[0])
		{	
			speed[0]=0;
		}
		else
		{
		if(gameobject.position[0]>patterns[1] )
			speed[0]*=-1;
		if(gameobject.position[0]<patterns[0])
			speed[0]*=-1;
		}
		
		if(patterns[2].equals(patterns[3]))
		{	
			speed[1]=0;
		}
		else
		{
		if(gameobject.position[1]>patterns[3])
			speed[1]*=-1;
		if(gameobject.position[1]<patterns[2])
			speed[1]*=-1;
		}
		gameobject.position[0] = gameobject.position[0] + speed[0];
		gameobject.position[1] = gameobject.position[1] + speed[1];
	}
	public void HIDBehavior()
	{
		if(jumping)
		{
			if(jumpCount==0)
			{	
 				jumpPoint=gameobject.position[1]-gameobject.objectHeight*3;
				jumpCount=1;
				gameobject.position[1]-=5;
			}
			else
			{
			boolean col= gameobject.collidableComponent.isCollisionBtweenAgents();
			
			if(up && col )
			{
				up=false;	
				
			}
			else if(up==false && col)
			{
				jumping=false;
				jumpCount=0;
			}
			else if(gameobject.position[1]<=jumpPoint )
			{
				up=false;
			}
		 if(up==true)
			{
				gameobject.position[1]-=2;
				//jumpPoint=2;
			}
		 else if(up==false)
			{
				if(gameobject.position[1]+gameobject.objectHeight/2<= gameobject.worldHeight)
					gameobject.position[1]+=2;
				else
					{
					jumping=false;
					jumpCount=0;
					}
			
			}
			
			}
		}
		
		
		
		//System.out.println(o.IDclient+" letf y rigjht "+o.leftMove+" "+ o.rightMove);
		if(leftMove==true)
		{
			System.out.println("Move left");
			if(gameobject.position[0]-2>0)
			{
				//System.out.println("left");
				gameobject.position[0]-=2;
				//o.leftMove=false;
			}
			leftMove=false;
		}
	
			
		if(rightMove==true)
		{
			System.out.println("Move right");
			if(gameobject.position[0]+2<gameobject.worldWidth)
			{
				//System.out.println("right");
				gameobject.position[0]+=2;
				//o.rightMove=false;
			}
			rightMove=false;
		}
	}
	public void defineFall()
	{
		if(!gameobject.collidableComponent.isCollisionBtweenAgents() && !jumping && 
				gameobject.position[1]+gameobject.objectHeight/2<= gameobject.worldHeight)
		{
			gameobject.position[1]+=2;
		}
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(auto)
		{
			gameobject.position[0] = gameobject.position[0] + speed[0];
			gameobject.position[1] = gameobject.position[1] + speed[1];
		}
		if(moveWithPatter)
		{
			moveWithPatternBehavior();
		}
		if(keyboardControl)
		{
			HIDBehavior();
			defineFall();
		}
		
		
		
	}

}
