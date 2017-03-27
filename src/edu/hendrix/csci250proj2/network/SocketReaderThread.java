package edu.hendrix.csci250proj2.network;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hendrix.csci250proj2.User;
import edu.hendrix.csci250proj2.gui.*;



public class SocketReaderThread extends Thread {
	
	//Networking
    private boolean isHost = false;
	private socketHelper player2;
    private String currentPrompt;
    private PaintingGameController controller;
    private boolean player2Done;
    private int finalScore;
   
    
	private List<color> sendStack = Collections.synchronizedList(new ArrayList<color>());//Stack of colors to send over the network
	
	public SocketReaderThread(String result,String name, String currentPrompt, PaintingGameController controller) throws IOException{
		isHost = false;
		this.controller = controller;
		this.currentPrompt = currentPrompt;
		player2 = new socketHelper(result,3002);
		player2.writeString(name);
	}
	
	public SocketReaderThread(String name, String currentPrompt,PaintingGameController controller)throws IOException{
		isHost=true;
		this.controller = controller;
		this.currentPrompt = currentPrompt;
		player2 = new socketHelper(3002);
		player2.writeString(name);
	}
	
    @Override
	public void run() {
    	/////THREE SOCKET STATES
    	while(true)
    	{
        	if(player2.getState() == socketState.USERNAME)
        	{
        		getUsername();
        	}else if(player2.getState() == socketState.DRAWING){
        		getPrompt();
        	}else if(player2.getState() == socketState.COLOR){
        		getColor();
        	}else if(player2.getState() == socketState.COLOR){
        		getScore();
        	}
        	//WAIT
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
        
       }
    	
    }
    
    
    //USERNAME STATE
    private void getUsername(){
    	try {
			player2.setUsername(player2.readNextString());
			controller.setUserArea(player2.getUsername());
			player2.setState(socketState.DRAWING);
			if(isHost){
				player2.writeString(currentPrompt);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    	
    }
    
    //DRAWING STATE
    private void getPrompt(){
    	try {
			if(!isHost){
				currentPrompt = player2.readNextString();
				controller.promptReady(currentPrompt);
				System.out.println("User: " + currentPrompt);
				player2.setState(socketState.COLOR);
			}else{
				System.out.println("Host:" + currentPrompt);
				player2.setState(socketState.COLOR);
				
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }
    
    //COLOR STATE
    private void getColor(){
    	
		if(!this.sendStack.isEmpty()){
			try {
				color sendCol = this.sendStack.remove(0);
				player2.writeColor(sendCol);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				color drawCol = player2.readColor();
				if(drawCol.checkIfDoneDrawing()){
					player2.setState(socketState.SCORE);
					controller.setFinalRating(player2.readInt());
				}else{
					controller.drawFX(drawCol);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
		
	private void getScore(){
		controller.setUserArea("-done drawing");
		if(!this.sendStack.isEmpty()){
			try {
				color sendCol = this.sendStack.remove(0);
				player2.writeColor(sendCol);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    }
	
	public void sendFinalScore(int score){
		try {
			this.player2.writeInt(score);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    //Called from Java FX thread
    //Adds drawings to drawing stack
    public synchronized void addColToSendStack(color Col){
			this.sendStack.add(new color(Col.getR(),Col.getG(),Col.getB(),Col.getSX(),Col.getSY(),Col.getFX(),Col.getFY()));
			//System.out.println("Hello Stack");
    }
    
}


