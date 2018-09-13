package com.mulesoft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.rmi.CORBA.Util;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.OutputStream;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class checkURL implements Runnable {

	static Logger logger = Logger.getLogger(checkURL.class);

	private String Env="";
	private String ReportFilePath="";
	private String ReportFileName="";

	public checkURL()
	{
		//default Constructor
	}

	public checkURL(String Env, String ReportFilePath, String ReportFileName){
		this.Env = Env;
		this.ReportFilePath = ReportFilePath;
		this.ReportFileName = ReportFileName;
	}

	public void run()
	{
		logger.info("Starting the Thread checkURL::"+"Environments="+Env+":: ReportFilePath ="+ReportFilePath+":: ReportFileName ="+ReportFileName);
		Properties prop = new Properties();
		InputStream  input  = null;
		String interfaces = "";
		String currIntrfcNme = "";
		String URLIntExt = "";
		String [] arrURLIntExt;
		String url;
		String intExt;
		String envs = "";
		String internalURL = "";
		String externalURL = "";
		String urlTested = "";
		List<String> arrEnvs = new ArrayList<String>();
		List<String> arrInterfaces = new ArrayList<String>();
		Map<String, List<String>> hm = new LinkedHashMap<String, List<String>>();
		int code = 0;
		String HTTPStatus ="";
		String HTTPSuccessCodes = "";
		String HTTPFailureCodes = "";
		List<String> valuesForExcel = new ArrayList<String>();
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
		checkURL objCheckURL = new checkURL();
		Date date = new Date();
		//String filePath = "C:\\property\\";
		String filePath = "";
		String fileNameSuffix = "URL_Report_";
		String fileName = "";
		try 
		{
			input  = new FileInputStream("C:\\property\\URLs.properties");
			prop.load(input);
			interfaces=prop.getProperty("Interfaces");
			//envs= prop.getProperty("Environments"); //To be taken by request URL
			envs=Env;
			HTTPSuccessCodes=prop.getProperty("HTTPSuccessCodes");
			HTTPFailureCodes=prop.getProperty("HTTPFailureCodes");
			arrInterfaces = Arrays.asList(interfaces.split(","));
			arrEnvs= Arrays.asList(envs.split(","));
			URLIntExt = new String();
			filePath= ReportFilePath;
			File filePth = new File (filePath);
			if(!filePth.exists())
			{
				filePth.mkdirs();
			}
			//fileName= filePath+fileNameSuffix+GetCurrentTimeStamp().replace(":"," ").replace("."," ").replace("-", " ");
			fileName=filePath+ReportFileName;
			File file = new File(fileName+".xlsx");
			if(!file.exists())
			{
				file.createNewFile();
			}
			XSSFWorkbook  workbook = new XSSFWorkbook ();
			XSSFSheet  sheet ;
			//Iterate over the Env
			for (int i = 0; i < arrEnvs.size(); i++) 
			{
				sheet = workbook.createSheet(arrEnvs.get(i));
				sheet.setColumnWidth(0, 2000);
				sheet.setColumnWidth(1, 7000);
				sheet.setColumnWidth(2, 20000);
				for(int j=0; j<arrInterfaces.size(); j++)
				{ 
					//Get Current Interface Name
					currIntrfcNme=	arrInterfaces.get(j);

					//Find internal or external URL
					URLIntExt = prop.getProperty(currIntrfcNme);
					arrURLIntExt = URLIntExt.split(",");
					url   = arrURLIntExt[0];
					intExt= arrURLIntExt[1];

					try 
					{
						//Forming the URL to be tested
						urlTested = formURLTested(arrEnvs.get(i),url,intExt,prop,internalURL,externalURL); 

						//Getting the status code of the URL and tagging a PASS FAILED Status
						SSLContext sc = SSLContext.getInstance("SSL");
						sc.init(null, trustAllCerts, new java.security.SecureRandom());
						HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

						URL urlHTTPS = new URL(urlTested);
						HttpURLConnection connection = (HttpURLConnection)urlHTTPS.openConnection();
						//HttpURLConnection.setFollowRedirects(false);
						//connection.setConnectTimeout(10 * 1000);
						connection.setRequestMethod("GET");
						code = connection.getResponseCode();
					}
					catch(Exception e)
					{
						e.printStackTrace();	
						//logger.error("Exception::",e);
					}

					//Get Pass Fail status
					if (Arrays.asList(HTTPSuccessCodes.split(",")).contains(Integer.toString(code)))
					{
						HTTPStatus = "Success";
					}
					else if (Arrays.asList(HTTPFailureCodes.split(",")).contains(Integer.toString(code)))
					{
						HTTPStatus = "Failure";
					}
					else
					{
						HTTPStatus = "Unknown";
					}
					//End Getting the status code of the URL
					try {
						valuesForExcel.add(urlTested);
						valuesForExcel.add(Integer.toString(code));
						valuesForExcel.add(HTTPStatus);
						//Storing URLs for specific env with key as name, value as Arraylist of URL,Code,Status
						hm.put(currIntrfcNme, valuesForExcel) ;		
						valuesForExcel = new ArrayList<String>(); 
						logger.info(hm);
						//System.out.println(hm);
					}
					finally {
						//if(!(arrInterfaces.size()==arrInterfaces.size()-1))
							code=0;
						HTTPStatus="";
						urlTested="";
					}
				}// End of Interface iteration				

				//Write the data in the excel sheet with 4 columns name, url, code and status
				objCheckURL.writeExcel(hm, arrEnvs.get(i),file,workbook,sheet,fileName,true);
				valuesForExcel.clear();

			}// End of Environment Iteration	
			workbook.close();
			System.out.println("File Processed!!");
		}
		catch(Exception e)
		{
			//logger.error("Exception::",e);
			e.printStackTrace();
		}
		finally 
		{

		}
	}

	public static <E> void main (String [] args)
	{
		//Main Flow
		//Thread t = new Thread(new checkURL());
		//t.start();
		//Main Flow End
	}


	public Object performAction(String Env, String ReportFilePath, String ReportFileName) throws Exception {
		try{
			//Main Flow
			//Send Env, FilePath and FileName
			String stEnv = Env;
			String stFilePath = ReportFilePath;
			String stFileName = ReportFileName;
			//Thread t = new Thread(new checkURL(stEnv,stFilePath,stFileName));
			//t.start();
			//Main Flow End
		}
		catch(Exception e)
		{
			logger.error("Exception::",e);;
		}
		return null;
	}

	public static void writeExcel(Map hm, String env, File file, XSSFWorkbook workbook, XSSFSheet sheet, String fileName, boolean getDetldRprt) throws IOException // takes input env, hashmap having all the data
	{
		logger.info("Entry writeExcel_checkURL::");
		FileOutputStream outputStream = null;
		int rowCounter = 0;
		int colCounter =5;
		Map<String, List<String>> nodeHM = new LinkedHashMap<String, List<String>>();
		String HTTPresponseMessage ="";
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
			cell.setCellValue("Interface Name");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(2);
			cell.setCellValue("URL Tested");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Code");
			cell.setCellStyle(cellStyle);

			cell = row.createCell(4);
			cell.setCellValue("HTTPS LB Status");
			cell.setCellStyle(cellStyle);
			
			//For Heading Subjected to Nodes
			if(getDetldRprt) {
				nodeHM=checkURL.loadNodeDetails(env);
				colCounter =5;
				for( Map.Entry<String, List<String>> entry : nodeHM.entrySet()) 
				{
					
						if(entry.getKey() != null && !entry.getKey().isEmpty()) 
						{
							cell= row.createCell(colCounter);
							cell.setCellValue("HTTP_".concat(entry.getKey()));
							cell.setCellStyle(cellStyle);
							colCounter++;
							
							cell= row.createCell(colCounter);
							cell.setCellValue("HTTPS_".concat(entry.getKey()));
							cell.setCellStyle(cellStyle);
						}
						colCounter++;
				}
			}
			//End For Heading Subjected to Nodes

			Map<String, List<String>> hm2 = hm;
			HashSet<String> legendsHttpCode = new HashSet<String>();
			for( Map.Entry<String, List<String>> entry : hm2.entrySet()) 
			{
				String key = entry.getKey();
				List<String> value = entry.getValue();	
				/*
				 * //For Heading Subjected to Nodes
				colCounter =5;
				for(int i=3; i<value.size(); i=i+2) {
					if(value.get(i) != null && !value.get(i).isEmpty()) 
					{
						cell= row.createCell(colCounter);
						cell.setCellValue("HTTP_".concat(Arrays.asList(value.get(i).split("|")).get(0)));
						cell.setCellStyle(cellStyle);
						colCounter++;
						
						cell= row.createCell(colCounter);
						cell.setCellValue("HTTPS_".concat(Arrays.asList(value.get(i+1).split("|")).get(0)));
						cell.setCellStyle(cellStyle);
					}
					colCounter++;
				}
				//End For Heading Subjected to Nodes
				*
				**/				
				row = sheet.createRow(rowCounter);
				cell = row.createCell(0);
				cell.setCellValue(rowCounter);

				cell = row.createCell(1);
				cell.setCellValue(key);

				cell = row.createCell(2);
				cell.setCellValue(value.get(0));

				cell = row.createCell(3);
				cell.setCellValue(value.get(1));
				legendsHttpCode.add(value.get(1));

				cell = row.createCell(4);
				cell.setCellValue(value.get(2));
				if(value.get(2).equalsIgnoreCase(Utility.SUCCESS))
				{
					cell.setCellStyle(cellStyleGreen);
				}
				else {
					cell.setCellStyle(cellStyleRed);
				}
				//Color Code Success from Success and failure

				//if(hm2.keySet().toArray()[0] == null  && hm2.isEmpty())
					//getDetldRprt = false;
				
				if(hm2.keySet().toArray()[0].equals(Utility.ServerDownStatus))
					getDetldRprt = false;
				//Check if the flag set is true, will be present for each interface
				if(getDetldRprt) {
					colCounter =5;
					for(int i=3; i<value.size(); i=i+2) {
						if(value.get(i) != null && !value.get(i).isEmpty()) 
						{
							cell= row.createCell(colCounter);
							cell.setCellValue(value.get(i));
							//cell.setCellValue(Arrays.asList(value.get(i).split("|")).get(1));
							if(value.get(i).equalsIgnoreCase(Utility.SUCCESS))
							{
								cell.setCellStyle(cellStyleGreen);
							}
							else {
								cell.setCellStyle(cellStyleRed);
							}
							colCounter++;
							cell= row.createCell(colCounter);
							cell.setCellValue(value.get(i+1));
							//cell.setCellValue(Arrays.asList(value.get(i).split("|")).get(i+1));
							if(value.get(i+1).equalsIgnoreCase(Utility.SUCCESS))
							{
								cell.setCellStyle(cellStyleGreen);
							}
							else {
								cell.setCellStyle(cellStyleRed);
							}
							//colCounter++;
						}
						colCounter++;
					}
				}
				//End flag is set as true
				rowCounter++;
				hm2.keySet().toArray()[0] = "";
				
			}
			
			rowCounter = rowCounter+4;
			Iterator<String> i = legendsHttpCode.iterator();
	        while (i.hasNext())
	        {
	        	row = sheet.createRow(rowCounter);
				cell = row.createCell(1);
				cell.setCellValue(i.next());
				cell.setCellStyle(stylePBlue);
				
				if (cell.getStringCellValue().equals("0"))
					HTTPresponseMessage ="This site cant be reached";
				else
				HTTPresponseMessage = HttpStatus.getStatusText(Integer.parseInt(cell.getStringCellValue()));
				cell = row.createCell(2);
				cell.setCellValue(HTTPresponseMessage);
				cell.setCellStyle(styleGrey25);
				rowCounter++;
	        }
			outputStream = new FileOutputStream(fileName+Utility.ExcelExtension); 
			workbook.write(outputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//e.printStackTrace();
		}
		finally {
			
			outputStream.flush();
			outputStream.close();
		}
		logger.info("Exit writeExcel_checkURL::");
	}

	public static String formURLTested(String env,String url, String intExt,Properties prop,String internalURL,String externalURL) 
	{
		String urlTested="";
		try 
		{

			//Forming URL for DEV
			if(env.equalsIgnoreCase(Utility.Development))
			{
				internalURL = prop.getProperty("devInternalURL");
				externalURL = prop.getProperty("devExternalURL");
				if(intExt.equals("int"))
				{
					urlTested = internalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
				else 
				{
					urlTested = externalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
			}
			//End Loading URL for DEV

			//Forming URL for QA
			else if(env.equalsIgnoreCase(Utility.QualityAssurance))
			{
				internalURL = prop.getProperty("qaInternalURL");
				externalURL = prop.getProperty("qaExternalURL");
				if(intExt.equals("int"))
				{
					urlTested = internalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
				else 
				{
					urlTested = externalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
			}
			//End Loading URL for QA

			//Forming URL for TRN
			else if(env.equalsIgnoreCase(Utility.Training))
			{
				internalURL = prop.getProperty("trnInternalURL");
				externalURL = prop.getProperty("trnExternalURL");
				if(Utility.onPremFlag)
				{
					if(intExt.equals("int"))
					{
						urlTested = internalURL.concat(url).concat(Utility.trnURLSuffix).concat("?wsdl");
						//System.out.println(urlTested);
					}
					else 
					{
						urlTested = externalURL.concat(url).concat(Utility.trnURLSuffix).concat("?wsdl");
						//System.out.println(urlTested);
					}
				}
				else 
				{
					if(intExt.equals("int"))
					{
						urlTested = internalURL.concat(url).concat("?wsdl");
						//System.out.println(urlTested);
					}
					else 
					{
						urlTested = externalURL.concat(url).concat("?wsdl");
						//System.out.println(urlTested);
					}
				}
				
			}
			//End Loading URL for TRN

			//Forming URL for UAT
			else if(env.equalsIgnoreCase(Utility.UserAcceptanceTesting))
			{
				internalURL = prop.getProperty("uatInternalURL");
				externalURL = prop.getProperty("uatExternalURL");
				if(Utility.onPremFlag)
				{
					if(intExt.equals("int"))
					{
						urlTested = internalURL.concat(url).concat(Utility.uatURLSuffix).concat("?wsdl");
						//System.out.println(urlTested);
					}
					else 
					{
						urlTested = externalURL.concat(url).concat(Utility.uatURLSuffix).concat("?wsdl");
						//System.out.println(urlTested);
					}
				}
				else 
				{
					if(intExt.equals("int"))
					{
						urlTested = internalURL.concat(url).concat("?wsdl");
						//System.out.println(urlTested);
					}
					else 
					{
						urlTested = externalURL.concat(url).concat("?wsdl");
						//System.out.println(urlTested);
					}
				}
				
			}
			//End Loading URL for UAT

			//Forming URL for PPD
			else if(env.equalsIgnoreCase(Utility.PreProduction))
			{
				internalURL = prop.getProperty("ppdInternalURL");
				externalURL = prop.getProperty("ppdExternalURL");
				if(intExt.equals("int"))
				{
					urlTested = internalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
				else 
				{
					urlTested = externalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
			}
			//End Loading URL for PPD

			//Forming URL for PROD
			else if(env.equalsIgnoreCase(Utility.Production))
			{
				internalURL = prop.getProperty("prodInternalURL");
				externalURL = prop.getProperty("prodExternalURL");
				if(intExt.equals("int"))
				{
					urlTested = internalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
				else 
				{
					urlTested = externalURL.concat(url).concat("?wsdl");
					//System.out.println(urlTested);
				}
			}
			//End Loading URL for PROD
		}
		catch(Exception e) 
		{
			logger.error("Exception::",e);
			//e.printStackTrace();
		}

		return urlTested;
	}

	public static String formURLTestedNode(String ip, String env, String url) {
		String urlTested="";
		try {
			if(env.equalsIgnoreCase(Utility.Development))
			{
				urlTested=ip.concat(url).concat("?wsdl");
			}
			else if(env.equalsIgnoreCase(Utility.QualityAssurance))
			{
				urlTested=ip.concat(url).concat("?wsdl");
			}
			else if(env.equalsIgnoreCase(Utility.UserAcceptanceTesting))
			{
				if(Utility.onPremFlag)
				urlTested=ip.concat(url).concat(Utility.uatURLSuffix).concat("?wsdl");
				else
					urlTested=ip.concat(url).concat("?wsdl");
			}
			else if(env.equalsIgnoreCase(Utility.Training))
			{
				if(Utility.onPremFlag)
				urlTested=ip.concat(url).concat(Utility.trnURLSuffix).concat("?wsdl");
				else
					urlTested=ip.concat(url).concat("?wsdl");
			}
			else if(env.equalsIgnoreCase(Utility.PreProduction))
			{
				urlTested=ip.concat(url).concat("?wsdl");
			}
			if(env.equalsIgnoreCase(Utility.Production))
			{
				urlTested=ip.concat(url).concat("?wsdl");
			}
		}
		catch(Exception e)
		{
			logger.error("Exception",e);
		}
		
		return urlTested;
	}
	public static String GetCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"dd-MM-yyyy  HH:mm:ss.SSS");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate.replace(":"," ").replace("."," ").replace("-", " ");
	}
	
	public static String removeLastComma(String str) {
	    final int len = str.length();
	    return len > 0 && str.charAt(len - 1) == ',' ?
		str.substring(0, len - 1) : 
		str;
	  }
	
	public static Map loadNodeDetails(String env ) 
	{
		List<String> nodeValues = new ArrayList<String>();
		Map<String, List<String>> nodeHM = new LinkedHashMap<String, List<String>>();
		TrustManager[] trustAllCerts = new ConnectionFactory().implementTrstMngr();
		File fXmlFile= null;
		
		try {
			
			if(!Utility.pickLocalResourceFlag.equals("Y"))
			fXmlFile = new File(Utility.muleNodeXMLPath);
			else if(Utility.pickLocalResourceFlag.equals("Y"))
			fXmlFile = new File(checkURL.class.getClassLoader().getResource(Utility.muleNodeXMLPath).getPath());
			//fXmlFile = checkURL.getResourceAsFile(Utility.muleNodeXMLPath);
				/*{
				url = new URL(checkURL.class.getResource(Utility.muleNodeXMLPath).getPath());
			fXmlFile = new File(url.getPath());
			}*/
			//fXmlFile = new File(checkURL.class.getResource(Utility.muleNodeXMLPath).getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			//for(int env =0; env < arrEnv.size(); env ++) //Environment loop not required over here
			//{
				NodeList nList = doc.getElementsByTagName(env);//This to be iterated by loop
				for (int temp = 0; temp < nList.getLength(); temp++) //Iterations for Centers in Each Environment
				{
					Node nNode = nList.item(temp);
					//System.out.println("\nCurrent Element :" + nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//System.out.println("Server id : " + eElement.getAttribute("id"));
						NodeList appList = eElement.getElementsByTagName("nd");
						for(int i=0;i< appList.getLength();i++)
						{
							if (nNode.getNodeType() == Node.ELEMENT_NODE) 
							{
								Node appNode = appList.item(i);
								Element element = (Element) appNode;
								
								nodeValues.add(element.getAttribute("env")); 		  //index 0
								nodeValues.add(element.getAttribute("name"));  	      //index 1
								nodeValues.add(element.getAttribute("internalHTTP")); //index 2
								nodeValues.add(element.getAttribute("internalHTTPS"));//index 3
								nodeValues.add(element.getAttribute("externalHTTPS"));//index 4
								//nodeValues=getResponse(element.getAttribute("url")); 
								//nodeValues.add(pingDetls[1]); //code  //index 3
								//nodeValues.add(pingDetls[0]); //Status //index4
								nodeHM.put(element.getAttribute("env"), nodeValues);
								nodeValues = new ArrayList<String>(); 
								//logger.info(nodeHM);
							}
						}
					}
				}
			//}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return nodeHM;
		
	}
	
	//Method to get Pass Fail Status
	public static String getPassFailStatus(int codeHTTPS, String HTTPSuccessCodes, String HTTPFailureCodes)
	{
		String HTTPS_Status="";
		try {
			//Get Pass Fail status
			if (Arrays.asList(HTTPSuccessCodes.split(",")).contains(Integer.toString(codeHTTPS)))
			{
				HTTPS_Status = Utility.SUCCESS;
			}
			else if (Arrays.asList(HTTPFailureCodes.split(",")).contains(Integer.toString(codeHTTPS)))
			{
				HTTPS_Status = Utility.FAILURE;
			}
			else
			{
				HTTPS_Status = Utility.UNKNOWN;
			}
		}
		catch(Exception e)
		{
		  e.printStackTrace();
		}
		return HTTPS_Status;
	}
	
	public static int progressBarTime(int arrEnvsSize)
	{
		int time =0;
		try {
			if(arrEnvsSize==6)
				time=time+16;
			else if(arrEnvsSize==5)
				time=time+20;
			else if(arrEnvsSize==4)
				time=time+25;
			else if(arrEnvsSize==3)
				time=time+33;
			else if(arrEnvsSize==2)
				time=time+50;
			else if(arrEnvsSize==1)
				time=time+85;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean hostAvailabilityCheck(String ip, int port) { 
	    try (Socket s = new Socket(ip, port)) 
	    {
	        return true;
	    } 
	    catch (IOException ex) 
	    {
	        /* ignore */
	    }
	    return false;
	}
	
	public static String getIPParseURL(String URL) 
	{
		URL url = null;
		try 
		{
			url = new URL(URL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return url.getHost();
	}
	
	public static int getPortParseURL(String URL) {
		URL url = null;
		try 
		{
			url = new URL(URL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return url.getPort();
	}
	
	public static void fileDownload ()
	{
		try {
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static File getResourceAsFile(String resourcePath) {
	    try {
	        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
	        if (in == null) {
	            return null;
	        }

	        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
	        tempFile.deleteOnExit();

	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            //copy stream
	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = in.read(buffer)) != -1) {
	                out.write(buffer, 0, bytesRead);
	            }
	        }
	        return tempFile;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
