package edu.hendrix.csci250proj2.network;

public class color{
	double r;
	double g;
	double b;
	double sx;
	double sy;
	double fx;
	double fy;
	
	public color(double r,double g, double b, double sx, double sy, double fx, double fy){
		this.r = r;
		this.g = g;
		this.b = b;
		this.sx = sx;
		this.sy = sy;
		this.fx = fx;
		this.fy = fy;
	}
	
	public double getR(){
		return this.r;
	}
	
	public double getG(){
		return this.g;
	}
	
	public double getB(){
		return this.b;
	}
	
	public double getSX(){
		return this.sx;
	}
	
	public double getSY(){
		return this.sy;
	}
	
	public double getFX(){
		return this.fx;
	}
	
	public double getFY(){
		return this.fy;
	}
	
	public boolean checkIfDoneDrawing(){
		if (this.r==0 && this.g==0 && this.b==0 && this.sx==0 && this.sy==0 && this.fx==0 && this.fy==0){
			return true;
		}else{
			return false;
		}
	}
}
