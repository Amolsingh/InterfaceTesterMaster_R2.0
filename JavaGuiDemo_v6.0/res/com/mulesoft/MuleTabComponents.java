package com.mulesoft;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MuleTabComponents {

	private static JCheckBox chckbxNewCheckBox;
	private static JCheckBox chckbxNewCheckBox_1;
	private static JCheckBox chckbxNewCheckBox_2;
	private static JCheckBox chckbxNewCheckBox_3;
	private static JCheckBox chckbxNewCheckBox_4;
	private static JCheckBox chckbxNewCheckBox_5;
	private static JLabel lblCopyright;
	
	public MuleTabComponents(JPanel panel) {
		
		//All the Components required for the Mule Tab Pane
		chckbxNewCheckBox = new JCheckBox(Utility.development);
		chckbxNewCheckBox.setBackground(new Color(143, 188, 143));
		chckbxNewCheckBox.setBounds(90, 111, 97, 23);
		panel.add(chckbxNewCheckBox);

		chckbxNewCheckBox_1 = new JCheckBox(Utility.QualityAssurance);
		chckbxNewCheckBox_1.setBackground(new Color(143, 188, 143));
		chckbxNewCheckBox_1.setBounds(90, 152, 97, 23);
		panel.add(chckbxNewCheckBox_1);

		chckbxNewCheckBox_2 = new JCheckBox(Utility.UserAcceptanceTesting);
		chckbxNewCheckBox_2.setBackground(new Color(143, 188, 143));
		chckbxNewCheckBox_2.setBounds(90, 194, 97, 23);
		panel.add(chckbxNewCheckBox_2);

		chckbxNewCheckBox_3 = new JCheckBox(Utility.Training);
		chckbxNewCheckBox_3.setBackground(new Color(143, 188, 143));
		chckbxNewCheckBox_3.setBounds(276, 111, 97, 23);
		//panel.add(chckbxNewCheckBox_3);

		chckbxNewCheckBox_4 = new JCheckBox(Utility.preProduction);
		chckbxNewCheckBox_4.setBackground(new Color(143, 188, 143));
		chckbxNewCheckBox_4.setBounds(276, 152, 97, 23);
		//panel.add(chckbxNewCheckBox_4);

		chckbxNewCheckBox_5 = new JCheckBox(Utility.production);
		chckbxNewCheckBox_5.setBackground(new Color(143, 188, 143));
		chckbxNewCheckBox_5.setBounds(276, 194, 97, 23);
		//panel.add(chckbxNewCheckBox_5);
		
		lblCopyright = new JLabel(Utility.CopyrightDeloitte);
		lblCopyright.setBounds(0, 393, 452, 9);
		lblCopyright.setForeground(new Color(128, 0, 128));
		lblCopyright.setFont(new Font("sansserif", Font.PLAIN, 8));
		panel.add(lblCopyright);
		
	}
	
	public static void selectAll()
	{
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox_1.setSelected(true);
		chckbxNewCheckBox_2.setSelected(true);
		//chckbxNewCheckBox_3.setSelected(true);
		//chckbxNewCheckBox_4.setSelected(true);
		//chckbxNewCheckBox_5.setSelected(true);
	}
	
	public static void deSelectAll()
	{
		chckbxNewCheckBox.setSelected(false);
		chckbxNewCheckBox_1.setSelected(false);
		chckbxNewCheckBox_2.setSelected(false);
		chckbxNewCheckBox_3.setSelected(false);
		chckbxNewCheckBox_4.setSelected(false);
		chckbxNewCheckBox_5.setSelected(false);
	}
	
	public static String getEnvironment(String Env) {
		try {
			if(chckbxNewCheckBox.isSelected())
				Env=Env.concat(Utility.Development.concat(","));
			if(chckbxNewCheckBox_1.isSelected())
				Env=Env.concat(Utility.QualityAssurance.concat(","));
			if(chckbxNewCheckBox_2.isSelected())
				Env=Env.concat(Utility.UserAcceptanceTesting.concat(","));
			if(chckbxNewCheckBox_3.isSelected())
				Env=Env.concat(Utility.Training.concat(","));
			if(chckbxNewCheckBox_4.isSelected())
				Env=Env.concat(Utility.PreProduction.concat(","));
			if(chckbxNewCheckBox_5.isSelected())
				Env=Env.concat(Utility.Production.concat(","));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return Env;
	}

	public static int getTimeMule(int time, List arrEnvs) {
		
		try {
			if(arrEnvs.size()==6)
				time=time+16;
			else if(arrEnvs.size()==5)
				time=time+20;
			else if(arrEnvs.size()==4)
				time=time+25;
			else if(arrEnvs.size()==3)
				time=time+33;
			else if(arrEnvs.size()==2)
				time=time+50;
			else if(arrEnvs.size()==1)
				time=time+85;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return time;
	} 
	
	public static boolean validationCheck()
	{
		boolean validationFlag = false;
		try {
			if(chckbxNewCheckBox.isSelected() || chckbxNewCheckBox_1.isSelected() || chckbxNewCheckBox_2.isSelected() || chckbxNewCheckBox_3.isSelected() || chckbxNewCheckBox_4.isSelected() || chckbxNewCheckBox_5.isSelected())
				validationFlag = true;
			else 
				validationFlag = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return validationFlag;
	}
	
}
