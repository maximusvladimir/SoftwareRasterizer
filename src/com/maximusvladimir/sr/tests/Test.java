package com.maximusvladimir.sr.tests;

import javax.swing.JFrame;

import com.maximusvladimir.sr.Component3D;

public class Test extends JFrame {
	public static void main(String[] args) {
		new Test();
	}
	
	public Test() {
		setSize(800,600);
		setTitle("Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		Component3D c = new Component3D();
		c.setSize(800, 600);
		getContentPane().add(c);
	}
}
