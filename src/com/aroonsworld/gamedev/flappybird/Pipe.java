package com.aroonsworld.gamedev.flappybird;


public class Pipe {
	int x,y;
	int PIPESPEED = 2;
	public Pipe(int x, int y){
		this.x=x;
		this.y=y;
	}
	public void update(){
		x-=PIPESPEED;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}
