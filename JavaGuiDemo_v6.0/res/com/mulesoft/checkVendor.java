package com.mulesoft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

import org.apache.commons.httpclient.HttpStatus;
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

public class checkVendor {

	static Logger logger = Logger.getLogger(checkVendor.class);
	static Properties prop;
	//public static void loadProperties2(Map hm, List arrEnv, Properties prop) //pass hm, env
		public static void loadProperties( List arrEnv, Properties prop) //pass hm, env
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
				File file = new File(stFileName+ Utility.ExcelExtension);
				if(!file.exists())
				{
					file.createNewFile();
				}
				XSSFWorkbook  workbook = new XSSFWorkbook ();
				XSSFSheet  sheet ;
				
				//File fXmlFile = new File("res/resources/Vendor.xml");
				File fXmlFile = new File(checkVendor.class.getResource(Utility.URLxmlFilePathVndr).getPath());
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
						System.out.println("\nCurrent Element :" + nNode.getNodeName());
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
									valuesForExcel.add(element.getAttribute("name"));  //index 1
									valuesForExcel.add(element.getAttribute("env")); //index 2
									valuesForExcel.add(Integer.toString(getResponse(element.getAttribute("url")))); //code  //index 3
									valuesForExcel.add(getVndrStatus(getResponse(element.getAttribute("url")),prop)); //index 4
									hm.put(element.getAttribute("name"), valuesForExcel);
									valuesForExcel = new ArrayList<String>(); 
								}
							}
						}
					}
					
					//Write the data in the excel sheet with 4 columns name, url, code and status
					checkVendor.writeExcel(hm, arrEnv.get(env).toString(),file,workbook,sheet,stFileName);
					valuesForExcel.clear();
					hm.clear();
					hm= new LinkedHashMap<String, List<String>>();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		public static int getResponse (String stURL) //pass url
		{
			String urlTested = "";
			int code = 0;
			char chResp= '\u0000' ;
			String[] pingDetls= new String[3];
			String stGWStatus="";
			boolean connectionException= false;
			HttpURLConnection connection = null;
			TrustManager[] trustAllCerts = ConnectionFactory.implementTrstMngr2();
			try {
				URL urlHTTPS = new URL(stURL);
				connection= ConnectionFactory.retrnConectnFrURL(urlHTTPS);
				code = connection.getResponseCode();
				System.out.println("Code"+code);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally {
			}
			return code;
		}

		
		public static String GetCurrentTimeStamp() {
			SimpleDateFormat sdfDate = new SimpleDateFormat(
					"dd-MM-yyyy  HH:mm:ss.SSS");// dd/MM/yyyy
			Date now = new Date();
			String strDate = sdfDate.format(now);
			return strDate;
		}
		
		public static void main(String [] args)
		{
			prop = new Properties();
			InputStream  input  = null;
			String Env="";
			Map<String, List<String>> hm = new LinkedHashMap<String, List<String>>();
			List envList = new ArrayList<>();
			try {
				//Main logic goes over here
				input  = new FileInputStream("C:\\property\\Vendor_URLs.properties");
				prop.load(input);
				Env=prop.getProperty("Environments");
				checkCenter objCheckCenter = new checkCenter();
				envList = Arrays.asList(Env.split(","));
				loadProperties(envList,prop);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {

			}
		}
		
		public static void writeExcel(Map hm, String env, File file, XSSFWorkbook workbook, XSSFSheet sheet, String fileName) throws IOException // takes input env, hashmap having all the data
		{
			logger.info("Entry writeExcel_checkURL::");
			FileOutputStream outputStream = null;
			int rowCounter = 0;
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
				
				XSSFCellStyle cellStyleGreen = workbook.createCellStyle();
				CellStyle styleGreen = workbook.createCellStyle();
				cellStyleGreen.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				cellStyleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				XSSFCellStyle cellStyleRed = workbook.createCellStyle();
				CellStyle styleRed = workbook.createCellStyle();
				cellStyleRed.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
				cellStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				XSSFCellStyle cellStylePBlue = workbook.createCellStyle();
				CellStyle stylePBlue = workbook.createCellStyle();
				stylePBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
				stylePBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				XSSFCellStyle cellStyleGrey25 = workbook.createCellStyle();
				CellStyle styleGrey25 = workbook.createCellStyle();
				styleGrey25.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				styleGrey25.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				font.setColor(HSSFColor.WHITE.index);
				cellStyle.setFont(font);

				XSSFCell cell = row.createCell(0);
				cell.setCellValue("Serial No.");
				cell.setCellStyle(cellStyle);

				cell = row.createCell(1);
				cell.setCellValue("Vendor Name");
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
				HashSet<String> legendsHttpCode = new HashSet<String>();
				for( Map.Entry<String, List<String>> entry : hm2.entrySet()) 
				{
					String key = entry.getKey();
					List<String> value = entry.getValue();
					row = sheet.createRow(rowCounter);
					cell = row.createCell(0);
					cell.setCellValue(rowCounter);

					cell = row.createCell(1);
					cell.setCellValue(value.get(1));

					cell = row.createCell(2);
					cell.setCellValue(value.get(0));

					cell = row.createCell(3);
					cell.setCellValue(value.get(3));
					legendsHttpCode.add(value.get(3));

					cell = row.createCell(4);
					cell.setCellValue(value.get(4));
					if(value.get(4).equalsIgnoreCase(Utility.SUCCESS))
					{
						cell.setCellStyle(cellStyleGreen);
					}
					else {
						cell.setCellStyle(cellStyleRed);
					}
					//Color Code Success from Success and failure

					rowCounter++;
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
		        
				outputStream = new FileOutputStream(fileName+ Utility.ExcelExtension); 
				workbook.write(outputStream);
			}
			catch (Exception e)
			{
				//logger.error("Exception::",e);
				e.printStackTrace();
			}
			finally {
				outputStream.flush();
				outputStream.close();
			}
			logger.info("Exit writeExcel_checkURL::");
		}

		public static String getVndrStatus (int code, Properties prop)
		{
			String stVendrStatus="";
			String HTTPSuccessCodes=prop.getProperty(Utility.HTTPSuccessCodes);
			String HTTPFailureCodes=prop.getProperty(Utility.HTTPFailureCodes);
			try {
				if (Arrays.asList(HTTPSuccessCodes.split(",")).contains(Integer.toString(code)))
				{
					stVendrStatus = Utility.SUCCESS;
				}
				else if (Arrays.asList(HTTPFailureCodes.split(",")).contains(Integer.toString(code)))
				{
					stVendrStatus = Utility.FAILURE;
				}
				else
				{
					stVendrStatus = Utility.UNKNOWN ;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return stVendrStatus;
		}
}
