package demo.app;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class sample 
{
	//public static final String fPath = "E:\\test.xls";
	public static void main(String[] args) 
	{	    
		Scanner sc=new Scanner(System.in);
	      System.out.print("1.Upload Excel File\n2.Generate Excel File\nEnter your choice:");
	      int choice=sc.nextInt();
	      switch(choice)
	      {
	      case 1:
	    	  System.out.println("Enter FilePath:");
	    	  String sPath=sc.next();
	    	  ReadExcel(sPath);
	    	  
	    	  break;
	      case 2:Write2Excel();
	    	  break;
	      default:System.out.println("Wrong Input");
	      }
	}
	public static void ReadExcel(String fPath)
	{
		try
		{
			
			Workbook workbook = WorkbookFactory.create(new File(fPath));		
            DataFormatter dataFormatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
           sheet.forEach(row -> {
        	   int i=0;
        	   String[] items=new String[5];
        	   for(Cell cell: row) 
                {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    items[i]=cellValue;
                    //System.out.print(cellValue + "\t");                    
                    i++;
                };
                //System.out.println();
                DBOperations.InsertData(items[0], items[1], items[2], items[3]);
            });
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public static void Write2Excel()
	{
		String[] columns = {"SNO", "Name", "FatherName", "Mobile"};
		try
		{
			ResultSet rs=DBOperations.GetData();
			Workbook workbook = new XSSFWorkbook();
			CreationHelper createHelper = workbook.getCreationHelper();
			Sheet sheet = workbook.createSheet("Employee");
			
			// Create a Font for styling header cells
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        headerFont.setFontHeightInPoints((short) 14);
	        headerFont.setColor(IndexedColors.RED.getIndex());
	        
	     // Create a CellStyle with the font
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFont(headerFont);
	        
	     // Create a Row
	        Row headerRow = sheet.createRow(0);

	        // Create cells
	        for(int i = 0; i < columns.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns[i]);
	            cell.setCellStyle(headerCellStyle);
	        }
	        
	        int rowNum = 1;
	        while(rs.next()) {
	            Row row = sheet.createRow(rowNum++);

	            row.createCell(0)
	                    .setCellValue(rs.getInt(1));

	            row.createCell(1)
	                    .setCellValue(rs.getString(2));

	            row.createCell(2)
                .setCellValue(rs.getString(3));

	            row.createCell(3)
	                    .setCellValue(rs.getString(4));
	        }
	     // Resize all columns to fit the content size
	        for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	        }
	     // Write the output to a file
	        FileOutputStream fileOut = new FileOutputStream("E://poi-generated-file.xlsx");
	        workbook.write(fileOut);
	        fileOut.close();
	        workbook.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
