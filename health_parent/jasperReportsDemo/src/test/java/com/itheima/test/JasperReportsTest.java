package com.itheima.test;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperReportsTest {
    @Test
    public void test1() throws Exception {
        String jrxmlPath = "D:\\springproject\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo.jrxml";
        String jasperPath = "D:\\springproject\\health_parent\\jasperReportsDemo\\src\\main\\resources\\demo.jasper";

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

}
