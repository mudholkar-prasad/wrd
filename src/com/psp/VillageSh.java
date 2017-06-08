package com.psp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

public class VillageSh {

	public static WebDriver driver;
	public WebDriverWait wait;
	public static JFileChooser xlsFile;
	public static File slFile;
	
	public static void main(String[] args) {
		
		JFrame vshFr = new JFrame("Village Schedule Data Entry");
		vshFr.setVisible(true);
		vshFr.setLayout(new FlowLayout());
		vshFr.setLocationRelativeTo(null);
		vshFr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton sltFile = new JButton("Select File");
		vshFr.add(sltFile);
		
		sltFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				xlsFile = new JFileChooser();
				xlsFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
				
				int rst = xlsFile.showOpenDialog(vshFr);
				
				if(rst == JFileChooser.APPROVE_OPTION)
				{
					slFile = xlsFile.getSelectedFile();
					
					if(slFile.exists())
					{
						
						vshFr.setSize(740, 150);
						vshFr.setLocation(300, 250);
						
						JLabel lblBlock = new JLabel("Block Name:");
						vshFr.add(lblBlock);
						
						JTextField txtBlock = new JTextField(10);
						vshFr.add(txtBlock);
						
						JLabel lblVillage = new JLabel("Village Name:");
						vshFr.add(lblVillage);
						
						JTextField txtVillage = new JTextField(10);
						vshFr.add(txtVillage);
						
						JLabel lblDate = new JLabel("Date:");
						vshFr.add(lblDate);
						
						JTextField txtDate = new JTextField(10);
						vshFr.add(txtDate);
						
						JButton btnStrt = new JButton("Start");
						vshFr.add(btnStrt);	
						
						btnStrt.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
						
								try{
									
									System.setProperty("webdriver.chrome.driver","ChromeDriver//chromedriver.exe");
									
									FileInputStream ins = new FileInputStream(slFile);
									
									HSSFWorkbook wb = new HSSFWorkbook(ins);
									
									HSSFSheet sh = wb.getSheetAt(0);
									
									driver = new ChromeDriver();
									
									driver.manage().window().maximize();
									
									driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
									
									driver.get("http://mi5census-data.nic.in/mi_census/login.jsp");
									
									driver.findElement(By.id("edit")).sendKeys("S15D522U07");
									
									driver.findElement(By.id("edit-pass")).sendKeys("Ahmad#*456");
									
									driver.findElement(By.id("edit-submit")).click();
									
									driver.findElement(By.linkText("Village Schedule")).click();									
									
									Select sltBlc = new Select(driver.findElement(By.id("edit-ddl-block")));
									sltBlc.selectByVisibleText(txtBlock.getText());
									
									Thread.sleep(500);
									
									/*Select Village*/
									//WebElement vlg = driver.findElement(By.id("ddl-village"));
									Select sltVlg = new Select(driver.findElement(By.id("ddl-village")));
									sltVlg.selectByVisibleText(txtVillage.getText());
									
									/*Select Date*/
									String dateE = txtDate.getText().toString();						
									driver.findElement(By.id("edit-field-vl-enumeration-date-und-0-value-datepicker-popup-0")).sendKeys(dateE);						
									driver.findElement(By.id("edit-field-vl-enumeration-date-und-0-value-datepicker-popup-0")).sendKeys(Keys.TAB);
									
									/* Tribal / Non-Tribal */
									double _1 = sh.getRow(1).getCell(1).getNumericCellValue();
									Select _1Sl = new Select(driver.findElement(By.id("edit-field-vl-is-village-tribal-und")));
									_1Sl.selectByValue(String.valueOf((int)_1));
									
									/* Major / Minor Scheme */
									if(sh.getRow(1).getCell(2).getStringCellValue().equals(""))
									{
										Select _2Sl = new Select(driver.findElement(By.id("edit-field-vl-village-covered-by-majo-und")));
										_2Sl.selectByValue("2");
										
										double _3 = sh.getRow(1).getCell(3).getNumericCellValue();
										driver.findElement(By.id("edit-field-vl-geographical-area-und-0-value")).sendKeys(String.valueOf((int)_3));
										
										double _4 = sh.getRow(1).getCell(4).getNumericCellValue();
										driver.findElement(By.id("edit-field-vl-cultivable-area-und-0-value")).sendKeys(String.valueOf((int)_4));
										
										double _5 = sh.getRow(1).getCell(5).getNumericCellValue();
										driver.findElement(By.id("edit-field-vl-net-area-sown-und-0-value")).sendKeys(String.valueOf((int)_5));
										
										
									}									
									else if(sh.getRow(1).getCell(2).getStringCellValue().equals("1"))
									{
										
									}
									
									wb.close();
									
								}catch(IOException | NoSuchElementException | InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}		
								
							}
						});
					}
				}
				
			}
		});
		
		vshFr.pack();
		
	}

}