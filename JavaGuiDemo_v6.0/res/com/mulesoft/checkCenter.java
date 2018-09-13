package com.mulesoft;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class checkCenter {
	static Logger logger = Logger.getLogger(checkCenter.class);
	static Properties prop;

	public static void main(String [] args)
	{
		prop = new Properties();
		InputStream  input  = null;
		String Env="";
		String readyStateResponse="";
		Map<String, List<String>> hm = new LinkedHashMap<String, List<String>>();
		List envList = new ArrayList<>();
		
		try {
			//Main logic goes over here
			input  = new FileInputStream("C:\\property\\URL_GW.properties");
			prop.load(input);
			Env=prop.getProperty("Environments");
			readyStateResponse=prop.getProperty("HTTP_ReadyState_Response");
			checkCenter objCheckCenter = new checkCenter();
			envList = Arrays.asList(Env.split(","));
			//loadProperties(prop,hm,envList);
			
			//loadProperties2(hm,envList,prop);
			loadProperties2(envList,prop);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {

		}
	}



	//public static void loadProperties2(Map hm, List arrEnv, Properties prop) //pass hm, env
	public static void loadProperties2( List arrEnv, Properties prop) //pass hm, env
	{
		List<String> valuesForExcel = new ArrayList<String>();
		String[] pingDetls= new String[3];
		String stReportFilePath = "";
		String stReportFileNamePattern ="";
		String stFileName ="";
		Map<String, List<String>> hm = new LinkedHashMap<String, List<String>>();
		try {
			//needed for excel generation
			stReportFilePath = prop.getProperty("ReportFilePath");
			stReportFileNamePattern = prop.getProperty("ReportFileNamePattern");
			stFileName = stReportFilePath+stReportFileNamePattern+GetCurrentTimeStamp().replace(":"," ").replace("."," ").replace("-", " ");
			
			File filePth = new File (stReportFilePath);
			if(!filePth.exists())
			{
				filePth.mkdirs();
			}
			File file = new File(stFileName+Utility.ExcelExtension);
			if(!file.exists())
			{
				file.createNewFile();
			}
			XSSFWorkbook  workbook = new XSSFWorkbook ();
			XSSFSheet  sheet ;
			
			//File fXmlFile = new File("src/resources/Url_Xml.xml");
			File fXmlFile = new File(checkCenter.class.getResource(Utility.URLxmlFilePathGW).getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			for(int env =0; env < arrEnv.size(); env ++)
			{
				sheet = workbook.createSheet(arrEnv.get(env).toString());
				sheet.setColumnWidth(0, 2000);
				sheet.setColumnWidth(1, 7500);
				sheet.setColumnWidth(2, 15000);
				sheet.setColumnWidth(4, 7000);
				
				NodeList nList = doc.getElementsByTagName(arrEnv.get(env).toString());//This to be iterated by loop
				for (int temp = 0; temp < nList.getLength(); temp++) //Iterations for Centers in Each Environment
				{
					Node nNode = nList.item(temp);
					System.out.println("Current Element :" + nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						System.out.println("Server id : " + eElement.getAttribute("id"));
						NodeList appList = eElement.getElementsByTagName("app");
						for(int i=0;i< appList.getLength();i++)
						{
							if (nNode.getNodeType() == Node.ELEMENT_NODE) 
							{
								Node appNode = appList.item(i);
								Element element = (Element) appNode;
								valuesForExcel.add(element.getAttribute("url")); //index 0
								valuesForExcel.add(element.getAttribute("id"));  //index 1
								valuesForExcel.add(element.getAttribute("env")); //index 2
								pingDetls=getResponse(element.getAttribute("url")); 
								valuesForExcel.add(pingDetls[1]); //code  //index 3
								valuesForExcel.add(pingDetls[0]); //Status //index4
								hm.put(element.getAttribute("name"), valuesForExcel);
								valuesForExcel = new ArrayList<String>(); 
								logger.info(hm);
							}
						}
					}
				}
				
				//Write the data in the excel sheet with 4 columns name, url, code and status
				checkCenter.writeExcel(hm, arrEnv.get(env).toString(),file,workbook,sheet,stFileName);
				valuesForExcel.clear();
				hm.clear();
				hm= new LinkedHashMap<String, List<String>>();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void writeExcel(Map hm, String env, File file, XSSFWorkbook workbook, XSSFSheet sheet, String fileName) throws IOException // takes input env, hashmap having all the data
	{
		logger.info("Entry writeExcel_checkURL::");
		FileOutputStream outputStream = null;
		int rowCounter = 0;
		try 
		{
			//Loop for environment
			XSSFRow row = sheet.createRow(rowCounter);
			rowCounter++;
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			CellStyle style = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.TEAL.getIndex());
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
			XSSFCellStyle cellStylePBlue = workbook.createCellStyle();
			CellStyle stylePBlue = workbook.createCellStyle();
			stylePBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			stylePBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFCellStyle cellStyleGrey25 = workbook.createCellStyle();
			CellStyle styleGrey25 = workbook.createCellStyle();
			styleGrey25.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			styleGrey25.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFCellStyle cellStyleGreen = workbook.createCellStyle();
			CellStyle styleGreen = workbook.createCellStyle();
			cellStyleGreen.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			cellStyleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFCellStyle cellStyleRed = workbook.createCellStyle();
			CellStyle styleRed = workbook.createCellStyle();
			cellStyleRed.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
			cellStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setColor(HSSFColor.WHITE.index);
			cellStyle.setFont(font);

			XSSFCell cell = row.createCell(0);
			cell.setCellValue("Serial No.");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Guidewire Center");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(2);
			cell.setCellValue("URL Tested");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Code");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Status");
			cell.setCellStyle(cellStyle);

			Map<String, List<String>> hm2 = hm;
			for( Map.Entry<String, List<String>> entry : hm2.entrySet()) 
			{
				String key = entry.getKey();
				List<String> value = entry.getValue();
				row = sheet.createRow(rowCounter);
				cell = row.createCell(0);
				cell.setCellValue(rowCounter);

				cell = row.createCell(1);
				cell.setCellValue(key+" -   "+value.get(1).toUpperCase());

				cell = row.createCell(2);
				cell.setCellValue(value.get(0).toLowerCase());

				cell = row.createCell(3);
				cell.setCellValue(value.get(3));

				cell = row.createCell(4);
				
				if(value.get(4).equalsIgnoreCase(Utility.MULTIUSER)|| value.get(4).equalsIgnoreCase(Utility.MAINTENANCE) || value.get(4).equalsIgnoreCase(Utility.DB_MAINTENANCE))
				{
					cell.setCellStyle(cellStyleGreen);
					//cell.setCellValue(value.get(4));
					cell.setCellValue(Utility.UpRunningStatus.concat(" (").concat(value.get(4).concat(" )")));
				}
				else {
					cell.setCellStyle(cellStyleRed);
					cell.setCellValue(value.get(4));
				}
				//Color Code Success from Success and failure

				rowCounter++;
				
				
			}
			
			rowCounter = rowCounter+4;
			row = sheet.createRow(rowCounter);
			cell = row.createCell(1);
			cell.setCellValue(Utility.MULTIUSER);
			cell.setCellStyle(stylePBlue);
			cell = row.createCell(2);
			cell.setCellValue(Utility.MultiuserMsg);
			cell.setCellStyle(styleGrey25);
			
			rowCounter++;
			row = sheet.createRow(rowCounter);
			cell = row.createCell(1);
			cell.setCellValue(Utility.DB_MAINTENANCE);
			cell.setCellStyle(stylePBlue);
			cell = row.createCell(2);
			cell.setCellValue(Utility.DBMaintenanceMsg);
			cell.setCellStyle(styleGrey25);
			
			rowCounter++;
			row = sheet.createRow(rowCounter);
			cell = row.createCell(1);
			cell.setCellValue(Utility.MAINTENANCE);
			cell.setCellStyle(stylePBlue);
			cell = row.createCell(2);
			cell.setCellValue(Utility.MaintenanceMsg);
			cell.setCellStyle(styleGrey25);
			
			rowCounter++;
			row = sheet.createRow(rowCounter);
			cell = row.createCell(1);
			cell.setCellValue(Utility.GW_STARTING);
			cell.setCellStyle(stylePBlue);
			cell = row.createCell(2);
			cell.setCellValue(Utility.GWStartingMsg);
			cell.setCellStyle(styleGrey25);
			
			outputStream = new FileOutputStream(fileName+Utility.ExcelExtension); 
			workbook.write(outputStream);
		}
		catch (Exception e)
		{
			logger.error("Exception::",e);
			//e.printStackTrace();
		}
		finally {
			outputStream.flush();
			outputStream.close();
		}
		logger.info("Exit writeExcel_checkURL::");
	}

	public static String[] getResponse (String stURL) //pass url
	{
		String urlTested = "";
		int code = 0;
		char chResp= '\u0000' ;
		String[] pingDetls= new String[3];
		String stGWStatus="";
		boolean connectionException= false;
		HttpURLConnection connection = null;
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {

					public java.security.cert.X509Certificate[] getAcceptedIssuers()
					{
						return null;
					}
					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
					{
						//No need to implement.
					}
					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
					{
						//No need to implement.
					}
				}
		};

		try {
			urlTested=formURLTested(stURL); //call formURLTested()
			//Getting the status code of the URL and tagging a PASS FAILED Status
			SSLContext sc = SSLContext.getInstance(Utility.SSLContext);
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };//For Guidewire Pre Prod and Prod
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			URL urlHTTPS = new URL(urlTested);
			connection = (HttpURLConnection)urlHTTPS.openConnection();
			connection.setRequestMethod(Utility.GETRequest);
			connection.setRequestProperty("charset", "utf-8");
			
			BufferedReader br = null;
			if (!(connection.getResponseCode() == 400)) {
			     br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
			     String output;
			     StringBuilder builder = new StringBuilder();
			     while ((output = br.readLine()) != null) 
			          builder.append(output);
			     System.out.println(builder.toString()); 
			     chResp=builder.toString().charAt(0);
			     System.out.println("ch resp: " + chResp);
			}
			
			code = connection.getResponseCode();
			stGWStatus= getGWStatus(chResp);
			System.out.println("Response"+stGWStatus);
			System.out.println("Code"+code);
			pingDetls[0] =stGWStatus;
			pingDetls[1]=Integer.toString(code);
		}
		catch(IOException exception)
		{
			pingDetls[0] ="Cant Connect";
			pingDetls[1]= Integer.toString(code);
		}
		catch(Exception e)
		{
			if(e.getMessage().equals("Connection refused: connect")|| e.getMessage().contains("java.io.FileNotFoundException")) {
				pingDetls[0] ="Cant Connect";
				pingDetls[1]= Integer.toString(code);
			}
			
			e.printStackTrace();
		}
		finally {
			//connection.disconnect();
		}
		return pingDetls;
	}

	public static String getGWStatus (Character c)
	{
		String stGWStatus="";
		try {
			if((CharToASCII(c))==50)
			{
				stGWStatus= Utility.MULTIUSER;
			}
			else if((CharToASCII(c))==40){
				stGWStatus= Utility.MAINTENANCE;
			}
			else if((CharToASCII(c))==30) {
				stGWStatus= Utility.DB_MAINTENANCE;
			}
			else if((CharToASCII(c))==0){
				stGWStatus= Utility.GW_STARTING;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return stGWStatus;
	}
	public static int CharToASCII(final char character){
		return (int)character;
	}
	public static String formURLTested(String stURL) {
		return stURL.concat(Utility.GWPingURI);
	}
	public  static void loadProperties(Properties prop, Map hm, List EnvLst) {

		List centerDetls =  new ArrayList<>();
		try {

			for (int i = 0; i < EnvLst.size(); i++) 
			{
				if((EnvLst.get(i)) =="TDEV")
				{
					hm.put("TDEV_PC", prop.getProperty("TDEV_PC"));
					hm.put("TDEV_BC", prop.getProperty("TDEV_BC"));
					hm.put("TDEV_CC", prop.getProperty("TDEV_CC"));
					hm.put("TDEV_CM", prop.getProperty("TDEV_CM"));
				}
				if((EnvLst.get(i)) =="DEV")
				{
					hm.put("DEV_PC", prop.getProperty("DEV_PC"));
					hm.put("DEV_BC", prop.getProperty("DEV_BC"));
					hm.put("DEV_CC", prop.getProperty("DEV_CC"));
					hm.put("DEV_CM", prop.getProperty("DEV_CM"));
				}
				if((EnvLst.get(i)) =="QA")
				{
					hm.put("QA_PC", prop.getProperty("QA_PC"));
					hm.put("QA_BC", prop.getProperty("QA_BC"));
					hm.put("QA_CC", prop.getProperty("QA_CC"));
					hm.put("QA_CM", prop.getProperty("QA_CM"));
				}
				if((EnvLst.get(i)) =="TQA1")
				{
					hm.put("TQA1_PC", prop.getProperty("TQA1_PC"));
					hm.put("TQA1_BC", prop.getProperty("TQA1_BC"));
					hm.put("TQA1_CC", prop.getProperty("TQA1_CC"));
					hm.put("TQA1_CM", prop.getProperty("TQA1_CM"));
				}
				if((EnvLst.get(i)) =="TQA2")
				{
					hm.put("TQA2_PC", prop.getProperty("TQA2_PC"));
					hm.put("TQA2_BC", prop.getProperty("TQA2_BC"));
					hm.put("TQA2_CC", prop.getProperty("TQA2_CC"));
					hm.put("TQA2_CM", prop.getProperty("TQA2_CM"));
				}
				if((EnvLst.get(i)) =="TQA3")
				{
					hm.put("TQA3_PC", prop.getProperty("TQA3_PC"));
					hm.put("TQA3_BC", prop.getProperty("TQA3_BC"));
					hm.put("TQA3_CC", prop.getProperty("TQA3_CC"));
					hm.put("TQA3_CM", prop.getProperty("TQA3_CM"));
				}
				if((EnvLst.get(i)) =="TRN")
				{
					hm.put("TRN_PC", prop.getProperty("TRN_PC"));
					hm.put("TRN_BC", prop.getProperty("TRN_BC"));
					hm.put("TRN_CC", prop.getProperty("TRN_CC"));
					hm.put("TRN_CM", prop.getProperty("TRN_CM"));
				}
				if((EnvLst.get(i)) =="UAT")
				{
					hm.put("UAT_PC", prop.getProperty("UAT_PC"));
					hm.put("UAT_BC", prop.getProperty("UAT_BC"));
					hm.put("UAT_CC", prop.getProperty("UAT_CC"));
					hm.put("UAT_CM", prop.getProperty("UAT_CM"));
				}
				if((EnvLst.get(i)) =="PPD")
				{
					hm.put("UAT_PC", prop.getProperty("UAT_PC"));
					hm.put("UAT_BC", prop.getProperty("UAT_BC"));
					hm.put("UAT_CC", prop.getProperty("UAT_CC"));
					hm.put("UAT_CM", prop.getProperty("UAT_CM"));
				}
				if((EnvLst.get(i)) =="PRD")
				{

				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String GetCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"dd-MM-yyyy  HH:mm:ss.SSS");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}
	
	public static int getGWTimeCompletionStatus (int timeGW, int size) throws Exception
	{
		if(size==10)
			timeGW=timeGW+10;
		else if(size==9)
			timeGW=timeGW+11;
		else if(size==8)
			timeGW=timeGW+12;
		else if(size==7)
			timeGW=timeGW+14;
		else if(size==6)
			timeGW=timeGW+16;
		else if(size==5)
			timeGW=timeGW+20;
		else if(size==4)
			timeGW=timeGW+25;
		else if(size==3)
			timeGW=timeGW+33;
		else if(size==2)
			timeGW=timeGW+50;
		else if(size==1)
			timeGW=timeGW+60;
		
		return timeGW;
	}
	
	
}
