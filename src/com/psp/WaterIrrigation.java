package com.psp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaterIrrigation {
	
	public File sltFile;
	public WebDriver driver;
	public WebDriverWait wait;
	public int srn;
	public URL ico = WaterIrrigation.class.getResource("/resources/logo.png");
	public String nashikId = "S15U516U06", ahmadId = "S15U522U07";
	
	public void GetData()
	{
		
		JFrame wiFrm = new JFrame("Water Irrigation - GetData");
		wiFrm.setVisible(true);
		wiFrm.setIconImage(new ImageIcon(ico).getImage());
		wiFrm.setLayout(new FlowLayout());
		wiFrm.setLocationRelativeTo(null);
		wiFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnSltFile = new JButton("Select File");
		wiFrm.add(btnSltFile);
		
		btnSltFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser xlsFile = new JFileChooser();
				xlsFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
				
				int rst = xlsFile.showOpenDialog(wiFrm);
				
				if(rst == JFileChooser.APPROVE_OPTION)
				{
					sltFile = xlsFile.getSelectedFile();
					
					if(sltFile.exists())
					{
						wiFrm.dispose();
						
						startDataEntry();
					}
				}
			}
		});
		
		wiFrm.pack();
	}
	
	public void startDataEntry()
	{
		JFrame sFrm = new JFrame("Start Data Entry");
		sFrm.setVisible(true);
		sFrm.setIconImage(new ImageIcon(ico).getImage());
		sFrm.setSize(500, 150);
		sFrm.setLayout(new FlowLayout());
		sFrm.setLocationRelativeTo(null);
		sFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JCheckBox chNashik = new JCheckBox("Nashik");
		sFrm.add(chNashik);
		
		JCheckBox chAhmad = new JCheckBox("Ahmadnagar");
		sFrm.add(chAhmad);
		
		JLabel lblSrnNo = new JLabel("Serial No :");
		sFrm.add(lblSrnNo);
		
		JLabel lblSrn = new JLabel();
		sFrm.add(lblSrn);

		JTextField txtSrn = new JTextField(2);
		sFrm.add(txtSrn);
		
		JLabel lblBlock = new JLabel("Block Name:");
		sFrm.add(lblBlock);
		
		JTextField txtBlock = new JTextField(10);
		sFrm.add(txtBlock);
		
		JLabel lblVillage = new JLabel("Village Name:");
		sFrm.add(lblVillage);
		
		JTextField txtVillage = new JTextField(10);
		sFrm.add(txtVillage);
		
		JLabel lblDate = new JLabel("Date:");
		sFrm.add(lblDate);
		
		JTextField txtDate = new JTextField(10);
		sFrm.add(txtDate);
		
		JButton btnStrt = new JButton("Start");
		sFrm.add(btnStrt);
		
		JButton btnslt = new JButton("Select File");
		sFrm.add(btnslt);
		
		btnslt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				sFrm.dispose();
				GetData();
				
			}
		});
		
			    
		/* Click Event on Start Button */
		btnStrt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
								
				
				
				try {
						
					System.setProperty("webdriver.chrome.driver","ChromeDriver//chromedriver.exe");
					
					//SimpleDateFormat date = new SimpleDateFormat("dd/mm/yyyy");
					
					FileInputStream ins = new FileInputStream(sltFile);
					
					HSSFWorkbook wb = new HSSFWorkbook(ins);
					
					HSSFSheet sht = wb.getSheetAt(0);
					
					int rowCount = sht.getLastRowNum()-sht.getFirstRowNum()-1;
					
					//String user = sht.getRow(0).getCell(0).getStringCellValue();
					//String psswd = sht.getRow(0).getCell(1).getStringCellValue();
				
					driver = new ChromeDriver();
					
					driver.manage().window().maximize();
					
					driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
					
					driver.get("http://mi5census-data.nic.in/mi_census/login.jsp");
					
					if(chNashik.isSelected()){
						driver.findElement(By.id("edit")).sendKeys("S15D516U06");
					}else if(chAhmad.isSelected())
					{
						driver.findElement(By.id("edit")).sendKeys("S15D522U07");
					}
					
					driver.findElement(By.id("edit-pass")).sendKeys("nic123");
					
					driver.findElement(By.id("edit-submit")).click();
					
					driver.findElement(By.linkText("Ground Water Schemes")).click();
					
					
					//driver.wait(500);
					
					//String val = sht.getRow(srn).getCell(2).getStringCellValue().substring(0,1);
					
				
							for(srn=Integer.parseInt(txtSrn.getText());srn<=rowCount;srn++)
							{
								if(srn == sht.getLastRowNum()-1){
									
									//lblSrn.setText("Village Done");
									JOptionPane.showMessageDialog(null, "Village done");
									
								}
								else{
						
										lblSrn.setText(String.valueOf(srn));
										
										/*Select Block*/										
										WebElement blc = driver.findElement(By.id("edit-ddl-block"));
										Select sltBlc = new Select(blc);
										sltBlc.selectByVisibleText(txtBlock.getText());
										
										Thread.sleep(100);
										
										/*Select Village*/										
										WebElement vlg = driver.findElement(By.id("ddl-village"));
										Select sltVlg = new Select(vlg);
										sltVlg.selectByVisibleText(txtVillage.getText());
										
										/*Select Date*/
										String dateE = txtDate.getText().toString();						
										driver.findElement(By.id("edit-field-gw-enumeration-date-und-0-value-datepicker-popup-0")).sendKeys(dateE);						
										driver.findElement(By.id("edit-field-gw-enumeration-date-und-0-value-datepicker-popup-0")).sendKeys(Keys.TAB);
										
										/*Serial No. Entry*/
										double srn_no = sht.getRow(srn).getCell(0).getNumericCellValue();
										driver.findElement(By.id("edit-field-gw-serial-no-und-0-value")).sendKeys(String.valueOf((int)srn_no));
																				
										Thread.sleep(100);
										/*Select Type*/
										double _2 = sht.getRow(srn).getCell(1).getNumericCellValue();
										WebElement typeE = driver.findElement(By.id("edit-field-gw-2-type-und")); 
										Select sltType = new Select(typeE);
										sltType.selectByIndex((int)_2);
										
										/*Select Owner*/
										String _3 = sht.getRow(srn).getCell(2).getStringCellValue().substring(0,1);
										//System.out.println(_3);
										
										if(_3.equals("5"))
										{
												
											WebElement ownerE = driver.findElement(By.id("edit-field-gw-3-owner-und"));
											Select sltOwner = new Select(ownerE);
											sltOwner.selectByValue(_3);
											
											/*Owner Name Entry*/
											String ownName = sht.getRow(srn).getCell(2).getStringCellValue();
											if(ownName.length() > 2){
											driver.findElement(By.id("edit-field-gw-owner-name-und-0-value")).sendKeys(ownName.substring(3,ownName.length()));
											}else
											{
											driver.findElement(By.id("edit-field-gw-owner-name-und-0-value")).sendKeys("---");
											}
											
											/* 4a) Khasra No. Entry*/
											String _4a = sht.getRow(srn).getCell(3).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-a-khasra-no-und-0-value")).sendKeys(_4a);
											
											/* 4b) Location Particulars Entry*/
											//String _4b = sht.getRow(srn).getCell(4).getStringCellValue();
											int _4b = 0;
											driver.findElement(By.id("edit-field-gw-b-location-particulars-und-0-value")).sendKeys(String.valueOf(_4b));
											
											/* 5a) Total Ownership Holding of Owner Entry */
											String _5a = sht.getRow(srn).getCell(4).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-a-total-ownership-hold-und-0-value")).sendKeys(_5a);
											
											/* 5b) Social Status Of Owner Entry*/
											String _5b = sht.getRow(srn).getCell(5).getStringCellValue().toString();
											WebElement elm5B = driver.findElement(By.id("edit-field-gw-5-b-social-status-owner-und"));
											Select slt5B = new Select(elm5B);
											slt5B.selectByValue(_5b);
											
											/* 6) Year of Commission Entry */
											double _6 = sht.getRow(srn).getCell(6).getNumericCellValue();
											WebElement elm6 = driver.findElement(By.id("edit-field-gw-year-of-commissioning-und"));
											Select slt6 = new Select(elm6);
											slt6.selectByIndex((int)_6);
											
											/* 7) Nature of Scheme in case of Dugwell Entry */
											double _7 = sht.getRow(srn).getCell(7).getNumericCellValue();
											WebElement eml7 = driver.findElement(By.id("edit-field-gw-nature-of-scheme-dugwel-und"));
											Select slt7 = new Select(eml7);
											slt7.selectByIndex((int)_7);
											
											/* 8a) Depth of Well/Tube Well in Meters Entry */
											if((int)_2 == 2){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 3){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 4){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else{
												double _8a = sht.getRow(srn).getCell(8).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(String.valueOf(_8a));
											}
											
											
											/* 8b) Diameter in meters Entry */
											String _8b = sht.getRow(srn).getCell(9).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-b-diameter-und-0-value")).sendKeys(_8b);
										 
																	
											
											if((int)_2 == 1 && (int)_7 == 1){
											/* 8c) Depth of Bore Entry */
											//String _8c = sht.getRow(srn).getCell(15).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-c-depth-of-bore-und-0-value")).sendKeys("1");
											}						
											
											/* 8d) Distance of nearest well Entry */
											double _8d = sht.getRow(srn).getCell(11).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-d-distance-nearest-well-und-0-value")).sendKeys(String.valueOf(_8d));
											
											/* 9a) Cost of Construction Entry */
											double _9a = sht.getRow(srn).getCell(12).getNumericCellValue();
											//System.out.println(_9a);
											driver.findElement(By.id("edit-field-gw-9-cost-of-construction-und-0-value")).sendKeys(String.valueOf((int)_9a));
											
											/* 9b) Cost of Machinery Entry */
											double _9b = sht.getRow(srn).getCell(13).getNumericCellValue();
											//System.out.println(_9b);
											driver.findElement(By.id("edit-field-gw-9-cost-of-machinery-und-0-value")).sendKeys(String.valueOf((int)_9b));
											
											/* 9c) Cost of Maintenance during 2013-2014 */
											double _9c = sht.getRow(srn).getCell(14).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-9-cost-of-maintenance-und-0-value")).sendKeys(String.valueOf((int)_9c));
											
											/* Select 10a)i Source of Fianace */
											double _10ai = sht.getRow(srn).getCell(20).getNumericCellValue();
											WebElement eml10ai = driver.findElement(By.id("edit-field-gw-10-a-und"));
											Select slt10ai = new Select(eml10ai);
											slt10ai.selectByIndex((int)_10ai);
											
											/* Select 10a)ii */
											/*String _10aii = sht.getRow(srn).getCell(21).getStringCellValue();
											WebElement elm10aii = driver.findElement(By.id("edit-field-gw-source-finance-2-und"));
											Select slt10aii = new Select(elm10aii);
											slt10aii.selectByValue(_10aii);*/
																	
											driver.findElement(By.id("edit-field-gw-source-finance-2-und")).sendKeys(Keys.TAB);;
											
											/* 10b)i Entry */
											//String _10bi = sht.getRow(srn).getCell(22).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-construction-of-drillin-und-0-value")).sendKeys("0");
											
											/* 10b)ii Entry */
											//String _10bii = sht.getRow(srn).getCell(23).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-10-cost-of-machinery-und-0-value")).sendKeys("0");
											
											/* 11a) In Use */
											double _11a = sht.getRow(srn).getCell(16).getNumericCellValue();
											
											if((String.valueOf((int)_11a)).equals("1"))
											{
												
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
												
											}else if((String.valueOf((int)_11a)).equals("2"))
											{
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
											
											}else if((String.valueOf((int)_11a)).equals("3"))
											{
												
											}
											
										}
									
										if(_3.equals("4"))
										{
											WebElement ownerE = driver.findElement(By.id("edit-field-gw-3-owner-und"));
											Select sltOwner = new Select(ownerE);
											sltOwner.selectByValue(_3);
											
											/*Owner Name Entry*/
											//String ownName = sht.getRow(srn).getCell(2).getStringCellValue();
											//driver.findElement(By.id("edit-field-gw-owner-name-und-0-value")).sendKeys(ownName.substring(3,ownName.length()));
											
											/* 4a) Khasra No. Entry*/
											String _4a = sht.getRow(srn).getCell(3).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-a-khasra-no-und-0-value")).sendKeys(_4a);
											
											/* 4b) Location Particulars Entry*/
											//String _4b = sht.getRow(srn).getCell(4).getStringCellValue();
											int _4b = 0;
											driver.findElement(By.id("edit-field-gw-b-location-particulars-und-0-value")).sendKeys(String.valueOf(_4b));
											
											/* 5a) Total Ownership Holding of Owner Entry */
											/*String _5a = sht.getRow(srn).getCell(4).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-a-total-ownership-hold-und-0-value")).sendKeys(_5a);*/
											
											/* 5b) Social Status Of Owner Entry*/
											/*String _5b = sht.getRow(srn).getCell(5).getStringCellValue().toString();
											WebElement elm5B = driver.findElement(By.id("edit-field-gw-5-b-social-status-owner-und"));
											Select slt5B = new Select(elm5B);
											slt5B.selectByValue(_5b);*/
											
											/* 6) Year of Commission Entry */
											double _6 = sht.getRow(srn).getCell(6).getNumericCellValue();
											WebElement elm6 = driver.findElement(By.id("edit-field-gw-year-of-commissioning-und"));
											Select slt6 = new Select(elm6);
											slt6.selectByIndex((int)_6);
											
											/* 7) Nature of Scheme in case of Dugwell Entry */
											double _7 = sht.getRow(srn).getCell(7).getNumericCellValue();
											WebElement eml7 = driver.findElement(By.id("edit-field-gw-nature-of-scheme-dugwel-und"));
											Select slt7 = new Select(eml7);
											slt7.selectByIndex((int)_7);
											
											/* 8a) Depth of Well/Tube Well in Meters Entry */
											if((int)_2 == 2){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 3){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 4){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else{
												double _8a = sht.getRow(srn).getCell(8).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(String.valueOf(_8a));
											}										
											
											/* 8b) Diameter in meters Entry */
											String _8b = sht.getRow(srn).getCell(9).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-b-diameter-und-0-value")).sendKeys(_8b);
											
											if((int)_2 == 1 && (int)_7 == 1){
												/* 8c) Depth of Bore Entry */
												//String _8c = sht.getRow(srn).getCell(15).getStringCellValue().toString();
												driver.findElement(By.id("edit-field-gw-c-depth-of-bore-und-0-value")).sendKeys("1");
												}
											
											/* 8d) Distance of nearest well Entry */
											double _8d = sht.getRow(srn).getCell(11).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-d-distance-nearest-well-und-0-value")).sendKeys(String.valueOf(_8d));
											
											/* 9a) Cost of Construction Entry */
											double _9a = sht.getRow(srn).getCell(12).getNumericCellValue();
											//System.out.println(_9a);
											driver.findElement(By.id("edit-field-gw-9-cost-of-construction-und-0-value")).sendKeys(String.valueOf((int)_9a));
											
											/* 9b) Cost of Machinery Entry */
											double _9b = sht.getRow(srn).getCell(13).getNumericCellValue();
											//System.out.println(_9b);
											driver.findElement(By.id("edit-field-gw-9-cost-of-machinery-und-0-value")).sendKeys(String.valueOf((int)_9b));
											
											/* 9c) Cost of Maintenance during 2013-2014 */
											double _9c = sht.getRow(srn).getCell(14).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-9-cost-of-maintenance-und-0-value")).sendKeys(String.valueOf((int)_9c));
											
											/* Select 10a)i Source of Fianace */
											double _10ai = sht.getRow(srn).getCell(20).getNumericCellValue();
											WebElement eml10ai = driver.findElement(By.id("edit-field-gw-10-a-und"));
											Select slt10ai = new Select(eml10ai);
											slt10ai.selectByIndex((int)_10ai);
											
											/* Select 10a)ii */
											/*String _10aii = sht.getRow(srn).getCell(21).getStringCellValue();
											WebElement elm10aii = driver.findElement(By.id("edit-field-gw-source-finance-2-und"));
											Select slt10aii = new Select(elm10aii);
											slt10aii.selectByValue(_10aii);*/
																	
											driver.findElement(By.id("edit-field-gw-source-finance-2-und")).sendKeys(Keys.TAB);;
											
											/* 10b)i Entry */
											//String _10bi = sht.getRow(srn).getCell(22).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-construction-of-drillin-und-0-value")).sendKeys("0");
											
											/* 10b)ii Entry */
											//String _10bii = sht.getRow(srn).getCell(23).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-10-cost-of-machinery-und-0-value")).sendKeys("0");
											
											/* 11a) In Use */
											double _11a = sht.getRow(srn).getCell(16).getNumericCellValue();
											
											if((String.valueOf((int)_11a)).equals("1"))
											{
												
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
												
											}else if((String.valueOf((int)_11a)).equals("2"))
											{
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
											
											}else if((String.valueOf((int)_11a)).equals("3"))
											{
												
											}
											
										}
									
										if(_3.equals("3"))
										{
											WebElement ownerE = driver.findElement(By.id("edit-field-gw-3-owner-und"));
											Select sltOwner = new Select(ownerE);
											sltOwner.selectByValue(_3);
											
											/*Owner Name Entry*/
											//String ownName = sht.getRow(srn).getCell(2).getStringCellValue();
											//driver.findElement(By.id("edit-field-gw-owner-name-und-0-value")).sendKeys(ownName.substring(3,ownName.length()));
											
											/* 4a) Khasra No. Entry*/
											String _4a = sht.getRow(srn).getCell(3).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-a-khasra-no-und-0-value")).sendKeys(_4a);
											
											/* 4b) Location Particulars Entry*/
											//String _4b = sht.getRow(srn).getCell(4).getStringCellValue();
											int _4b = 0;
											driver.findElement(By.id("edit-field-gw-b-location-particulars-und-0-value")).sendKeys(String.valueOf(_4b));
											
											/* 5a) Total Ownership Holding of Owner Entry */
											/*String _5a = sht.getRow(srn).getCell(4).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-a-total-ownership-hold-und-0-value")).sendKeys(_5a);*/
											
											/* 5b) Social Status Of Owner Entry*/
											/*String _5b = sht.getRow(srn).getCell(5).getStringCellValue().toString();
											WebElement elm5B = driver.findElement(By.id("edit-field-gw-5-b-social-status-owner-und"));
											Select slt5B = new Select(elm5B);
											slt5B.selectByValue(_5b);*/
											
											/* 6) Year of Commission Entry */
											double _6 = sht.getRow(srn).getCell(6).getNumericCellValue();
											WebElement elm6 = driver.findElement(By.id("edit-field-gw-year-of-commissioning-und"));
											Select slt6 = new Select(elm6);
											slt6.selectByIndex((int)_6);
											
											/* 7) Nature of Scheme in case of Dugwell Entry */
											double _7 = sht.getRow(srn).getCell(7).getNumericCellValue();
											WebElement eml7 = driver.findElement(By.id("edit-field-gw-nature-of-scheme-dugwel-und"));
											Select slt7 = new Select(eml7);
											slt7.selectByIndex((int)_7);
											
											/* 8a) Depth of Well/Tube Well in Meters Entry */
											if((int)_2 == 2){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 3){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 4){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else{
												double _8a = sht.getRow(srn).getCell(8).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(String.valueOf(_8a));
											}
											
											if((int)_2 == 1 && (int)_7 == 1){
												/* 8c) Depth of Bore Entry */
												//String _8c = sht.getRow(srn).getCell(15).getStringCellValue().toString();
												driver.findElement(By.id("edit-field-gw-c-depth-of-bore-und-0-value")).sendKeys("1");
												}
											
											/* 8d) Distance of nearest well Entry */
											double _8d = sht.getRow(srn).getCell(11).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-d-distance-nearest-well-und-0-value")).sendKeys(String.valueOf(_8d));
											
											/* 9a) Cost of Construction Entry */
											double _9a = sht.getRow(srn).getCell(12).getNumericCellValue();
											//System.out.println(_9a);
											driver.findElement(By.id("edit-field-gw-9-cost-of-construction-und-0-value")).sendKeys(String.valueOf((int)_9a));
											
											/* 9b) Cost of Machinery Entry */
											double _9b = sht.getRow(srn).getCell(13).getNumericCellValue();
											//System.out.println(_9b);
											driver.findElement(By.id("edit-field-gw-9-cost-of-machinery-und-0-value")).sendKeys(String.valueOf((int)_9b));
											
											/* 9c) Cost of Maintenance during 2013-2014 */
											double _9c = sht.getRow(srn).getCell(14).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-9-cost-of-maintenance-und-0-value")).sendKeys(String.valueOf((int)_9c));
											
											/* Select 10a)i Source of Fianace */
											double _10ai = sht.getRow(srn).getCell(20).getNumericCellValue();
											WebElement eml10ai = driver.findElement(By.id("edit-field-gw-10-a-und"));
											Select slt10ai = new Select(eml10ai);
											slt10ai.selectByIndex((int)_10ai);
											
											/* Select 10a)ii */
											/*String _10aii = sht.getRow(srn).getCell(21).getStringCellValue();
											WebElement elm10aii = driver.findElement(By.id("edit-field-gw-source-finance-2-und"));
											Select slt10aii = new Select(elm10aii);
											slt10aii.selectByValue(_10aii);*/
																	
											driver.findElement(By.id("edit-field-gw-source-finance-2-und")).sendKeys(Keys.TAB);;
											
											/* 10b)i Entry */
											//String _10bi = sht.getRow(srn).getCell(22).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-construction-of-drillin-und-0-value")).sendKeys("0");
											
											/* 10b)ii Entry */
											//String _10bii = sht.getRow(srn).getCell(23).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-10-cost-of-machinery-und-0-value")).sendKeys("0");
											
											/* 11a) In Use */
											double _11a = sht.getRow(srn).getCell(16).getNumericCellValue();
											
											if((String.valueOf((int)_11a)).equals("1"))
											{
												
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
												
											}else if((String.valueOf((int)_11a)).equals("2"))
											{
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
											
											}else if((String.valueOf((int)_11a)).equals("3"))
											{
												
											}	
										}
									
										if(_3.equals("2"))
										{
											WebElement ownerE = driver.findElement(By.id("edit-field-gw-3-owner-und"));
											Select sltOwner = new Select(ownerE);
											sltOwner.selectByValue(_3);
											
											/*Owner Name Entry*/
											//String ownName = sht.getRow(srn).getCell(2).getStringCellValue();
											//driver.findElement(By.id("edit-field-gw-owner-name-und-0-value")).sendKeys(ownName.substring(3,ownName.length()));
											
											/* 4a) Khasra No. Entry*/
											String _4a = sht.getRow(srn).getCell(3).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-a-khasra-no-und-0-value")).sendKeys(_4a);
											
											/* 4b) Location Particulars Entry*/
											//String _4b = sht.getRow(srn).getCell(4).getStringCellValue();
											int _4b = 0;
											driver.findElement(By.id("edit-field-gw-b-location-particulars-und-0-value")).sendKeys(String.valueOf(_4b));
											
											/* 5a) Total Ownership Holding of Owner Entry */
											/*String _5a = sht.getRow(srn).getCell(4).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-a-total-ownership-hold-und-0-value")).sendKeys(_5a);*/
											
											/* 5b) Social Status Of Owner Entry*/
											/*String _5b = sht.getRow(srn).getCell(5).getStringCellValue().toString();
											WebElement elm5B = driver.findElement(By.id("edit-field-gw-5-b-social-status-owner-und"));
											Select slt5B = new Select(elm5B);
											slt5B.selectByValue(_5b);*/
											
											/* 6) Year of Commission Entry */
											double _6 = sht.getRow(srn).getCell(6).getNumericCellValue();
											WebElement elm6 = driver.findElement(By.id("edit-field-gw-year-of-commissioning-und"));
											Select slt6 = new Select(elm6);
											slt6.selectByIndex((int)_6);
											
											/* 7) Nature of Scheme in case of Dugwell Entry */
											double _7 = sht.getRow(srn).getCell(7).getNumericCellValue();
											WebElement eml7 = driver.findElement(By.id("edit-field-gw-nature-of-scheme-dugwel-und"));
											Select slt7 = new Select(eml7);
											slt7.selectByIndex((int)_7);
											
											/* 8a) Depth of Well/Tube Well in Meters Entry */
											if((int)_2 == 2){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 3){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 4){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else{
												double _8a = sht.getRow(srn).getCell(8).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(String.valueOf(_8a));
											}
											
											/* 8b) Diameter in meters Entry */
											String _8b = sht.getRow(srn).getCell(9).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-b-diameter-und-0-value")).sendKeys(_8b);
											
											if((int)_2 == 1 && (int)_7 == 1){
												/* 8c) Depth of Bore Entry */
												//String _8c = sht.getRow(srn).getCell(15).getStringCellValue().toString();
												driver.findElement(By.id("edit-field-gw-c-depth-of-bore-und-0-value")).sendKeys("1");
												}
											/* 8d) Distance of nearest well Entry */
											double _8d = sht.getRow(srn).getCell(11).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-d-distance-nearest-well-und-0-value")).sendKeys(String.valueOf(_8d));
											
											/* 9a) Cost of Construction Entry */
											double _9a = sht.getRow(srn).getCell(12).getNumericCellValue();
											System.out.println(_9a);
											driver.findElement(By.id("edit-field-gw-9-cost-of-construction-und-0-value")).sendKeys(String.valueOf((int)_9a));
											
											/* 9b) Cost of Machinery Entry */
											double _9b = sht.getRow(srn).getCell(13).getNumericCellValue();
											System.out.println(_9b);
											driver.findElement(By.id("edit-field-gw-9-cost-of-machinery-und-0-value")).sendKeys(String.valueOf((int)_9b));
											
											/* 9c) Cost of Maintenance during 2013-2014 */
											double _9c = sht.getRow(srn).getCell(14).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-9-cost-of-maintenance-und-0-value")).sendKeys(String.valueOf((int)_9c));
											
											/* Select 10a)i Source of Fianace */
											double _10ai = sht.getRow(srn).getCell(20).getNumericCellValue();
											WebElement eml10ai = driver.findElement(By.id("edit-field-gw-10-a-und"));
											Select slt10ai = new Select(eml10ai);
											slt10ai.selectByIndex((int)_10ai);
											
											/* Select 10a)ii */
											/*String _10aii = sht.getRow(srn).getCell(21).getStringCellValue();
											WebElement elm10aii = driver.findElement(By.id("edit-field-gw-source-finance-2-und"));
											Select slt10aii = new Select(elm10aii);
											slt10aii.selectByValue(_10aii);*/
																	
											driver.findElement(By.id("edit-field-gw-source-finance-2-und")).sendKeys(Keys.TAB);;
											
											/* 10b)i Entry */
											//String _10bi = sht.getRow(srn).getCell(22).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-construction-of-drillin-und-0-value")).sendKeys("0");
											
											/* 10b)ii Entry */
											//String _10bii = sht.getRow(srn).getCell(23).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-10-cost-of-machinery-und-0-value")).sendKeys("0");
											
											/* 11a) In Use */
											double _11a = sht.getRow(srn).getCell(16).getNumericCellValue();
											
											if((String.valueOf((int)_11a)).equals("1"))
											{
												
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
												
											}else if((String.valueOf((int)_11a)).equals("2"))
											{
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
											
											}else if((String.valueOf((int)_11a)).equals("3"))
											{
												
											}
										}
									
										if(_3.equals("1"))
										{
											WebElement ownerE = driver.findElement(By.id("edit-field-gw-3-owner-und"));
											Select sltOwner = new Select(ownerE);
											sltOwner.selectByValue(_3);
											
											/*Owner Name Entry*/
											//String ownName = sht.getRow(srn).getCell(2).getStringCellValue();
											//driver.findElement(By.id("edit-field-gw-owner-name-und-0-value")).sendKeys(ownName.substring(3,ownName.length()));
											
											/* 4a) Khasra No. Entry*/
											String _4a = sht.getRow(srn).getCell(3).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-a-khasra-no-und-0-value")).sendKeys(_4a);
											
											/* 4b) Location Particulars Entry*/
											//String _4b = sht.getRow(srn).getCell(4).getStringCellValue();
											int _4b = 0;
											driver.findElement(By.id("edit-field-gw-b-location-particulars-und-0-value")).sendKeys(String.valueOf(_4b));
											
											/* 5a) Total Ownership Holding of Owner Entry */
											/*String _5a = sht.getRow(srn).getCell(4).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-a-total-ownership-hold-und-0-value")).sendKeys(_5a);*/
											
											/* 5b) Social Status Of Owner Entry*/
											/*String _5b = sht.getRow(srn).getCell(5).getStringCellValue().toString();
											WebElement elm5B = driver.findElement(By.id("edit-field-gw-5-b-social-status-owner-und"));
											Select slt5B = new Select(elm5B);
											slt5B.selectByValue(_5b);*/
											
											/* 6) Year of Commission Entry */
											double _6 = sht.getRow(srn).getCell(6).getNumericCellValue();
											WebElement elm6 = driver.findElement(By.id("edit-field-gw-year-of-commissioning-und"));
											Select slt6 = new Select(elm6);
											slt6.selectByIndex((int)_6);
											
											/* 7) Nature of Scheme in case of Dugwell Entry */
											double _7 = sht.getRow(srn).getCell(7).getNumericCellValue();
											WebElement eml7 = driver.findElement(By.id("edit-field-gw-nature-of-scheme-dugwel-und"));
											Select slt7 = new Select(eml7);
											slt7.selectByIndex((int)_7);
											
											/* 8a) Depth of Well/Tube Well in Meters Entry */
											if((int)_2 == 2){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 3){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 4){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else{
												double _8a = sht.getRow(srn).getCell(8).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(String.valueOf(_8a));
											}		
																			
											
											/* 8b) Diameter in meters Entry */
											String _8b = sht.getRow(srn).getCell(9).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-b-diameter-und-0-value")).sendKeys(_8b);
											
											if((int)_2 == 1 && (int)_7 == 1){
												/* 8c) Depth of Bore Entry */
												//String _8c = sht.getRow(srn).getCell(15).getStringCellValue().toString();
												driver.findElement(By.id("edit-field-gw-c-depth-of-bore-und-0-value")).sendKeys("1");
												}
											/* 8d) Distance of nearest well Entry */
											double _8d = sht.getRow(srn).getCell(11).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-d-distance-nearest-well-und-0-value")).sendKeys(String.valueOf(_8d));
											
											/* 9a) Cost of Construction Entry */
											double _9a = sht.getRow(srn).getCell(12).getNumericCellValue();
											//System.out.println(_9a);
											driver.findElement(By.id("edit-field-gw-9-cost-of-construction-und-0-value")).sendKeys(String.valueOf((int)_9a));
											
											/* 9b) Cost of Machinery Entry */
											double _9b = sht.getRow(srn).getCell(13).getNumericCellValue();
											//System.out.println(_9b);
											driver.findElement(By.id("edit-field-gw-9-cost-of-machinery-und-0-value")).sendKeys(String.valueOf((int)_9b));
											
											/* 9c) Cost of Maintenance during 2013-2014 */
											double _9c = sht.getRow(srn).getCell(14).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-9-cost-of-maintenance-und-0-value")).sendKeys(String.valueOf((int)_9c));
											
											/* Select 10a)i Source of Fianace */
											double _10ai = sht.getRow(srn).getCell(20).getNumericCellValue();
											WebElement eml10ai = driver.findElement(By.id("edit-field-gw-10-a-und"));
											Select slt10ai = new Select(eml10ai);
											slt10ai.selectByIndex((int)_10ai);
											
											/* Select 10a)ii */
											/*String _10aii = sht.getRow(srn).getCell(21).getStringCellValue();
											WebElement elm10aii = driver.findElement(By.id("edit-field-gw-source-finance-2-und"));
											Select slt10aii = new Select(elm10aii);
											slt10aii.selectByValue(_10aii);*/
																	
											driver.findElement(By.id("edit-field-gw-source-finance-2-und")).sendKeys(Keys.TAB);;
											
											/* 10b)i Entry */
											//String _10bi = sht.getRow(srn).getCell(22).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-construction-of-drillin-und-0-value")).sendKeys("0");
											
											/* 10b)ii Entry */
											//String _10bii = sht.getRow(srn).getCell(23).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-10-cost-of-machinery-und-0-value")).sendKeys("0");
											
											/* 11a) In Use */
											double _11a = sht.getRow(srn).getCell(16).getNumericCellValue();
											
											if((String.valueOf((int)_11a)).equals("1"))
											{
												
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
												
											}else if((String.valueOf((int)_11a)).equals("2"))
											{
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
											
											}else if((String.valueOf((int)_11a)).equals("3"))
											{
												
											}
										}
										
										if(_3.equals("6"))
										{
											WebElement ownerE = driver.findElement(By.id("edit-field-gw-3-owner-und"));
											Select sltOwner = new Select(ownerE);
											sltOwner.selectByValue(_3);
											
											/*Owner Name Entry*/
											//String ownName = sht.getRow(srn).getCell(2).getStringCellValue();
											//driver.findElement(By.id("edit-field-gw-owner-name-und-0-value")).sendKeys(ownName.substring(3,ownName.length()));
											
											/* 4a) Khasra No. Entry*/
											String _4a = sht.getRow(srn).getCell(3).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-a-khasra-no-und-0-value")).sendKeys(_4a);
											
											/* 4b) Location Particulars Entry*/
											//String _4b = sht.getRow(srn).getCell(4).getStringCellValue();
											int _4b = 0;
											driver.findElement(By.id("edit-field-gw-b-location-particulars-und-0-value")).sendKeys(String.valueOf(_4b));
											
											/* 5a) Total Ownership Holding of Owner Entry */
											/*String _5a = sht.getRow(srn).getCell(4).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-a-total-ownership-hold-und-0-value")).sendKeys(_5a);*/
											
											/* 5b) Social Status Of Owner Entry*/
											/*String _5b = sht.getRow(srn).getCell(5).getStringCellValue().toString();
											WebElement elm5B = driver.findElement(By.id("edit-field-gw-5-b-social-status-owner-und"));
											Select slt5B = new Select(elm5B);
											slt5B.selectByValue(_5b);*/
											
											/* 6) Year of Commission Entry */
											double _6 = sht.getRow(srn).getCell(6).getNumericCellValue();
											WebElement elm6 = driver.findElement(By.id("edit-field-gw-year-of-commissioning-und"));
											Select slt6 = new Select(elm6);
											slt6.selectByIndex((int)_6);
											
											/* 7) Nature of Scheme in case of Dugwell Entry */
											double _7 = sht.getRow(srn).getCell(7).getNumericCellValue();
											WebElement eml7 = driver.findElement(By.id("edit-field-gw-nature-of-scheme-dugwel-und"));
											Select slt7 = new Select(eml7);
											slt7.selectByIndex((int)_7);
											
											/* 8a) Depth of Well/Tube Well in Meters Entry */
											if((int)_2 == 2){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 3){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else if((int)_2 == 4){
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(Keys.TAB);
											}
											else{
												double _8a = sht.getRow(srn).getCell(8).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-depth-of-the-well-tube-und-0-value")).sendKeys(String.valueOf(_8a));
											}		
																			
											
											/* 8b) Diameter in meters Entry */
											String _8b = sht.getRow(srn).getCell(9).getStringCellValue().toString();
											driver.findElement(By.id("edit-field-gw-b-diameter-und-0-value")).sendKeys(_8b);
											
											if((int)_2 == 1 && (int)_7 == 1){
												/* 8c) Depth of Bore Entry */
												//String _8c = sht.getRow(srn).getCell(15).getStringCellValue().toString();
												driver.findElement(By.id("edit-field-gw-c-depth-of-bore-und-0-value")).sendKeys("1");
												}
											/* 8d) Distance of nearest well Entry */
											double _8d = sht.getRow(srn).getCell(11).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-d-distance-nearest-well-und-0-value")).sendKeys(String.valueOf(_8d));
											
											/* 9a) Cost of Construction Entry */
											double _9a = sht.getRow(srn).getCell(12).getNumericCellValue();
											//System.out.println(_9a);
											driver.findElement(By.id("edit-field-gw-9-cost-of-construction-und-0-value")).sendKeys(String.valueOf((int)_9a));
											
											/* 9b) Cost of Machinery Entry */
											double _9b = sht.getRow(srn).getCell(13).getNumericCellValue();
											//System.out.println(_9b);
											driver.findElement(By.id("edit-field-gw-9-cost-of-machinery-und-0-value")).sendKeys(String.valueOf((int)_9b));
											
											/* 9c) Cost of Maintenance during 2013-2014 */
											double _9c = sht.getRow(srn).getCell(14).getNumericCellValue();
											driver.findElement(By.id("edit-field-gw-9-cost-of-maintenance-und-0-value")).sendKeys(String.valueOf((int)_9c));
											
											/* Select 10a)i Source of Fianace */
											double _10ai = sht.getRow(srn).getCell(20).getNumericCellValue();
											WebElement eml10ai = driver.findElement(By.id("edit-field-gw-10-a-und"));
											Select slt10ai = new Select(eml10ai);
											slt10ai.selectByIndex((int)_10ai);
											
											/* Select 10a)ii */
											/*String _10aii = sht.getRow(srn).getCell(21).getStringCellValue();
											WebElement elm10aii = driver.findElement(By.id("edit-field-gw-source-finance-2-und"));
											Select slt10aii = new Select(elm10aii);
											slt10aii.selectByValue(_10aii);*/
																	
											driver.findElement(By.id("edit-field-gw-source-finance-2-und")).sendKeys(Keys.TAB);;
											
											/* 10b)i Entry */
											//String _10bi = sht.getRow(srn).getCell(22).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-construction-of-drillin-und-0-value")).sendKeys("0");
											
											/* 10b)ii Entry */
											//String _10bii = sht.getRow(srn).getCell(23).getStringCellValue();
											driver.findElement(By.id("edit-field-gw-10-cost-of-machinery-und-0-value")).sendKeys("0");
											
											/* 11a) In Use */
											double _11a = sht.getRow(srn).getCell(16).getNumericCellValue();
											
											if((String.valueOf((int)_11a)).equals("1"))
											{
												
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
												
											}else if((String.valueOf((int)_11a)).equals("2"))
											{
												WebElement elm11a = driver.findElement(By.id("edit-field-gw-11-a-und"));
												Select slt11a = new Select(elm11a);
												slt11a.selectByIndex((int)_11a);
												
												/* Select 14 Water Distribution */
												double _14 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm14 = driver.findElement(By.id("edit-field-gw-14-water-distribution-und"));
												Select slt14 = new Select(elm14);
												slt14.selectByIndex((int)_14);
												
												//String _15a = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-15-a-lifting-device-und-0-value")).sendKeys("1");
												
												double _15bi = sht.getRow(srn).getCell(20).getNumericCellValue();
												WebElement elm15bi = driver.findElement(By.id("edit-field-gw-15-b-types-of-device-und"));
												Select slt15bi = new Select(elm15bi);
												slt15bi.selectByIndex((int)_15bi);
												
												/*String _15bii = sht.getRow(srn).getCell(31).getStringCellValue();
												WebElement elm15bii = driver.findElement(By.id("edit-field-gw-types-of-device2-und"));
												Select slt15bii = new Select(elm15bii);
												slt15bii.selectByValue(_15bii);*/
												
												double _16i = sht.getRow(srn).getCell(21).getNumericCellValue();
												WebElement elm16i = driver.findElement(By.id("edit-field-gw-16-source-energy-lift-und"));
												Select slt16i = new Select(elm16i);
												slt16i.selectByIndex((int)_16i);
												
												String _17 = sht.getRow(srn).getCell(22).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-17-horse-power-und-0-value")).sendKeys(_17);
												
												String _18i = sht.getRow(srn).getCell(23).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-kharif-season-und-0-value")).sendKeys(_18i);
												
												//String _18ii = sht.getRow(srn).getCell(23).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-rabi-season-und-0-value")).sendKeys("0");
												
												String _18iii = sht.getRow(srn).getCell(24).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-18-perennia-und-0-value")).sendKeys(_18iii);
												
												//String _18iv = sht.getRow(srn).getCell(24).getStringCellValue().substring(2,1);
												driver.findElement(By.id("edit-field-gw-18-other-season-und-0-value")).sendKeys("0");
												
												String _19i = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-kharif-season-und-0-value")).sendKeys(_19i);
												
												//String _19ii = sht.getRow(srn).getCell(25).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-rabi-season-und-0-value")).sendKeys("0");
												
												String _19iii = sht.getRow(srn).getCell(26).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-19-perennia-und-0-value")).sendKeys(_19iii);
												
												//String _19iv = sht.getRow(srn).getCell(26).getStringCellValue().substring(2);
												driver.findElement(By.id("edit-field-gw-19-other-season-und-0-value")).sendKeys("0");
												
												double _20 = sht.getRow(srn).getCell(27).getNumericCellValue();
												driver.findElement(By.id("edit-field-gw-20-culturable-command-und-0-value")).sendKeys(Double.toString(_20));
												
												double _21 = sht.getRow(srn).getCell(28).getNumericCellValue();
												WebElement elm21 = driver.findElement(By.id("edit-field-gw-21-major-medium-scheme-und"));
												Select slt21 = new Select(elm21);
												slt21.selectByIndex((int)_21);
												
												String _22 = sht.getRow(srn).getCell(29).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-22-kharif-und-0-value")).sendKeys(_22);
												
												String _23 = sht.getRow(srn).getCell(30).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-23-rabi-und-0-value")).sendKeys(_23);
												
												String _24 = sht.getRow(srn).getCell(31).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-24-perennial-und-0-value")).sendKeys(_24);
												
												String _25 = sht.getRow(srn).getCell(32).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-25-other-und-0-value")).sendKeys(_25);
												
												String _26 = sht.getRow(srn).getCell(33).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-26-total-und-0-value")).sendKeys(_26);
												
												if((int)_21 == 2){
													
													String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
													if(_32.equals("Y")){
														_32 = "1";
													}
													else if(_32.equals("N")){
														_32 = "2";
													}
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _34 */
													String _34 = sht.getRow(srn).getCell(41).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-34-kharif-und-0-value")).sendKeys(_34);
													
													/* _35 */
													String _35 = sht.getRow(srn).getCell(42).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-35-rabi-und-0-value")).sendKeys(_35);
													
													/* _36 */
													String _36 = sht.getRow(srn).getCell(43).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-36-perennial-und-0-value")).sendKeys(_36);
													
													/* _37 */
													String _37 = sht.getRow(srn).getCell(44).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-37-other-und-0-value")).sendKeys(_37);
													
													/* _38 */
													String _38 = sht.getRow(srn).getCell(45).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-38-total-und-0-value")).sendKeys(_38);
												}
												
												else if((int)_21 == 1){
												
												String _27 = sht.getRow(srn).getCell(34).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-27-kharif-und-0-value")).sendKeys(_27);
													
												String _28 = sht.getRow(srn).getCell(35).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-28-rabi-und-0-value")).sendKeys(_28);
												
												String _29 = sht.getRow(srn).getCell(36).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-29-perennial-und-0-value")).sendKeys(_29);
												
												String _30 = sht.getRow(srn).getCell(37).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-30-other-und-0-value")).sendKeys(_30);
												
												String _31 = sht.getRow(srn).getCell(38).getStringCellValue();
												driver.findElement(By.id("edit-field-gw-31-total-und-0-value")).sendKeys(_31);
												
												String _32 = sht.getRow(srn).getCell(39).getStringCellValue();
												if(_32.equals("Y")){
													_32 = "1";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* _39i */												
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
														
												}
												else if(_32.equals("N")){
													_32 = "2";
													
													/* 32 */
													WebElement elm32 = driver.findElement(By.id("edit-field-gw-fuctioning-commisioning-und"));
													Select slt32 = new Select(elm32);
													slt32.selectByValue(_32);
													
													/* 33 */
													String _33 = sht.getRow(srn).getCell(40).getStringCellValue();
													driver.findElement(By.id("edit-field-gw-33-maximum-potential-und-0-value")).sendKeys(_33);
													
													/* 39i */
													WebElement elm39i = driver.findElement(By.id("edit-field-gw-utilization-schemes-und"));
													Select slt39i = new Select(elm39i);
													slt39i.selectByValue("1");
													
													/* 39ii */
													WebElement elm39ii = driver.findElement(By.id("edit-field-gw-reason-under-utilizatio-und"));
													Select slt39ii = new Select(elm39ii);
													slt39ii.selectByValue("5");
												}										
												
												
												}												
																								
												/* Remarks */
												driver.findElement(By.id("edit-field-gw-remarks-und-0-value")).sendKeys("NIL");
												
												/* Submit Form */
												driver.findElement(By.id("edit-submit")).click();
												
												/* Accept Alert */
												driver.switchTo().alert().accept();
												
												Thread.sleep(100);
												
												driver.switchTo().alert().accept();
												
												Thread.sleep(200);
											
											}else if((String.valueOf((int)_11a)).equals("3"))
											{
												
											}
										}
										
										
									wb.close();	
								}		
						}
					
		
				} catch (IOException | InterruptedException | NoSuchElementException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				
			}
			
		});
		
				
		//sFrm.pack();
	}

	public static void main(String[] args) {
		
		WaterIrrigation wi = new WaterIrrigation();
		wi.GetData();
	}

}
			