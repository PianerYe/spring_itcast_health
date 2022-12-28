package com.itheima.test;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperReportsTest {
    @Test
    public void test1() throws Exception {
        String jrxmlPath = "D:\\java\\itcast_health\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo.jrxml";
        String jasperPath = "D:\\java\\itcast_health\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo.jasper";

        //编译模板
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //构造数据
        Map paramters = new HashMap();
        paramters.put("reportDate","2019-10-10");
        paramters.put("company","itcast");
        List<Map> list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("name","xiaoming");
        map1.put("address","beijing");
        map1.put("email","xiaoming@itcast.cn");
        Map map2 = new HashMap();
        map2.put("name","xiaoli");
        map2.put("address","nanjing");
        map2.put("email","xiaoli@itcast.cn");
        list.add(map1);
        list.add(map2);
        //填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperPath,
                        paramters,
                        new JRBeanCollectionDataSource(list));

        //输出文件
        String pdfPath = "D:\\test.pdf";
        /*JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);*/
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }

    //基于JDBC数据源来填充数据
    @Test
    public void test2() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/health",
                "root",
                "123456");

        String jrxmlPath = "D:\\java\\itcast_health\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo1.jrxml";
        String jasperPath = "D:\\java\\itcast_health\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo1.jasper";

        //编译模板
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //构造数据
        Map map = new HashMap<>();
        map.put("company","传智播客");

        //填充数据-使用jdbc数据源方式填充
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,
                map,connection);

        //输出文件
        String pdfPath = "D:\\test.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint
        ,pdfPath);


    }

    //基于JavaBean方式填充数据
    @Test
    public void test3() throws Exception {

        String jrxmlPath = "D:\\java\\itcast_health\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo2.jrxml";
        String jasperPath = "D:\\java\\itcast_health\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo2.jasper";

        //编译模板
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //构造数据
        Map map = new HashMap<>();
        map.put("company","传智播客");

        //javaBean数据源填充，用于填充列表数据
        List<Map> list = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("name","测试套餐");
        map1.put("code","9999");
        map1.put("age","0-100");
        map1.put("sex","0");

        Map map2 = new HashMap();
        map2.put("name","测试套餐2");
        map2.put("code","9998");
        map2.put("age","0-100");
        map2.put("sex","0");

        Map map3 = new HashMap();
        map3.put("name","测试套餐3");
        map3.put("code","9996");
        map3.put("age","20-65");
        map3.put("sex","0");

        Map map4 = new HashMap();
        map4.put("name","测试套餐手机");
        map4.put("code","9995");
        map4.put("age","0-80");
        map4.put("sex","0");

        Map map5 = new HashMap();
        map5.put("name","测试套餐改");
        map5.put("code","9988");
        map5.put("age","12-22");
        map5.put("sex","0");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);

        //填充数据-使用jdbc数据源方式填充
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,
                map,new JRBeanCollectionDataSource(list));

        //输出文件
        String pdfPath = "D:\\test1.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint
                ,pdfPath);
    }
}
