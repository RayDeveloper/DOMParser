/**
 * @(#)DomParser.java
 *
 *
 * @Raydon Davis 813117991
 * @version 1.00 2022/3/12
 */
import java.io.*;
import java.net.*;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.*;
import java.net.*;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import java.sql.*;
import java.sql.PreparedStatement;

public class DomParser {

    public static void main(String[] args){

try{

Class.forName("oracle.jdbc.driver.OracleDriver"); // load the oracle driver class

Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","SYS as sysdba","admin");//unique string to connect to my database

Statement stmt=con.createStatement();//varialbe to send statements to be executed

 //upload inital information into Crop table about crop CASS
PreparedStatement Crop = con.prepareStatement("INSERT INTO Crop VALUES(?,?,?,?,?)");
Crop.setString(1,"100");
Crop.setString(2,"CASS");
Crop.setString(3,"Ixeris");
Crop.setString(4,"long stem");
Crop.setString(5,"2000 per year");
int r=Crop.executeUpdate();
System.out.println(r+" record inserted into Crop table\n");


Node p = null;
String pestid = null;
String PestName = null;
String PestTreatment = null;
Element name= null;
Element pest = null;
NodeList pestnamelist = null;
Element pestNameElement = null;
NodeList pestName=null;
NodeList pestTreatment = null;
Element pestTreamentElement = null;
NodeList pesttreatmentlist=null;

    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	try{
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		Document doc = builder.parse("CASS.xml");
    		NodeList pestlist = doc.getElementsByTagName("Pest");
    		for(int i=0; i<pestlist.getLength(); i++){
    			 p= pestlist.item(i);
    			if(p.getNodeType()== Node.ELEMENT_NODE){
    			//getting pestid # from the attribute
                Element element = (Element) p;
                System.out.println("PestID : " +p.getAttributes().getNamedItem("Pestid").getNodeValue());
				// getting the node value from tag
                 pestnamelist = element.getElementsByTagName("PestName");
                 pestNameElement = (Element) pestnamelist.item(0);
                 pestName = pestNameElement.getChildNodes();
                 System.out.println("PestName : " + (pestName.item(0)).getNodeValue());
				// getting the node value from tag
                 pesttreatmentlist = element.getElementsByTagName("PestTreatment");
                 pestTreamentElement = (Element) pesttreatmentlist.item(0);
                 pestTreatment = pestTreamentElement.getChildNodes();
                 System.out.println("PestTreatment : " + (pestTreatment.item(0)).getNodeValue());

				//upload information to PESTMGMT table
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO PESTMGMT VALUES(?,?,?)");
				pstmt.setString(1,p.getAttributes().getNamedItem("Pestid").getNodeValue());//first column
				pstmt.setString(2,pestName.item(0).getNodeValue());
				pstmt.setString(3,pestTreatment.item(0).getNodeValue());
				int s=pstmt.executeUpdate();
				System.out.println(s+" record inserted into PestMgmt table.");
    			}//end if
    			System.out.print("\n");


    			//upload information to CropDetails table
    			PreparedStatement cropDetails = con.prepareStatement("INSERT INTO CropDetails VALUES(?,?,?,?,?)");
				cropDetails.setString(1,"100");//first column
				cropDetails.setString(2,p.getAttributes().getNamedItem("Pestid").getNodeValue());
				cropDetails.setString(3,"Potassium fertilizers");
				cropDetails.setString(4,"weed plants by hand, no chemicals");
				cropDetails.setString(5,"Pick when ripe");
				int a=cropDetails.executeUpdate();
				System.out.println(a+" record inserted into CropDetails table\n");

    		}//end for


    	} catch (ParserConfigurationException e){
    		e.printStackTrace();
    	} catch (SAXException e){
    		e.printStackTrace();
    	}catch (IOException e){
    		e.printStackTrace();

    	}// end try

//display the current records in the 3 tables.

System.out.println("-------------BEGIN Database output--------------------");
System.out.println("Current records in database\n");
ResultSet rs=stmt.executeQuery("select * from PESTMGMT");
System.out.println("|PestID|  |PestName|      |PestTreatment|");

while(rs.next())
System.out.println(rs.getString(1)+"|  |"+rs.getString(2)+"|     |"+rs.getString(3));
System.out.print("\n");

ResultSet rb=stmt.executeQuery("select * from Crop");
System.out.println("|CropID|    |CropName|      |CropType|  |Variety|   |AnnualProduction|");
while(rb.next())
System.out.println(rb.getString(1)+"|            |"+rb.getString(2)+"|       |"+rb.getString(3)+"|     |"+rb.getString(4)+"|      |"+rb.getString(5));
System.out.print("\n");


ResultSet rt=stmt.executeQuery("select * from CropDetails");
ResultSetMetaData rsmd = rt.getMetaData();
int columnsNumber = rsmd.getColumnCount();

System.out.println("|CropID||PestID||FertilizationMgmt||WeedMgmt||HarvestingMgmt|");
while (rt.next()) {

for(int y = 1 ; y <= columnsNumber; y++){
      System.out.print(rt.getString(y) + "| ");
}//end for
  System.out.println();
    }//end while

System.out.println("-------------END Database output--------------------");



con.close(); //close database connection

}catch(Exception e){ System.out.println(e);}



}//end main

}//end class



