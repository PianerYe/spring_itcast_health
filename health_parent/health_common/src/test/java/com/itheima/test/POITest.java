package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class POITest {

    //使用POI读取Excel文件中的数
    @Test
    public void test1() throws Exception {
        //加载指定文件，创建一个Excel对象（工作簿）
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\yxkf\\Desktop\\poi.xlsx")));
        //读取Excle文件中第一个Sheet标签页
        XSSFSheet sheet = excel.getSheetAt(0);
        //遍历Sheet标签页，获得每一行数据
        for (Row row : sheet) {
            //遍历行，获得每个单元格对象
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }

        //关闭资源
        excel.close();
    }

    //使用POI读取Excel文件中的数
    @Test
    public void test2() throws Exception {
        //加载指定文件，创建一个Excel对象（工作簿）
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\yxkf\\Desktop\\poi.xlsx")));
        //读取Excle文件中第一个Sheet标签页
        XSSFSheet sheet = excel.getSheetAt(0);
        //获得当前工作表中最后一个行号，需要注意，行号从0开始
        int lastRowNum = sheet.getLastRowNum();
//        System.out.println("lastRowNum = " + lastRowNum); //从0开始获取行号
        for (int i = 0;i<=lastRowNum;i++){
            XSSFRow row = sheet.getRow(i);//根据行号获取每一行
            //获得当前行中最后一个单元格索引
            short lastCellNum = row.getLastCellNum();
//            System.out.println("lastCellNum =" + lastCellNum);  //从1开始获取单元格序列
            for (int j = 0;j<lastRowNum;j++){
                XSSFCell cell = row.getCell(j);//根据单元格索引获得单元格对象
                System.out.println(cell.getStringCellValue());
            }

        }

        //关闭资源
        excel.close();
    }


}
