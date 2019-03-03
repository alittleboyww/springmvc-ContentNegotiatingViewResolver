1. 新建一个Maven+web项目
2. 引入依赖如下
```
<!--pom.xml-->
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.7</maven.compiler.source>
    <maven.compiler.target>1.7</maven.compiler.target>
    <springframework.version>4.0.6.RELEASE</springframework.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>${springframework.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>${springframework.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${springframework.version}</version>
    </dependency>

    <!-- Needed for XML View (with JAXB2) -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-oxm</artifactId>
      <version>4.3.5.RELEASE</version>
    </dependency>

    <!-- Needed for JSON View -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.4.1.3</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>2.4.1</version>
    </dependency>

    <!-- Needed for PDF View -->
    <dependency>
      <groupId>com.lowagie</groupId>
      <artifactId>itext</artifactId>
      <version>2.1.7</version>
      <scope>compile</scope>
    </dependency>

    <!-- Needed for XLS View -->
    <dependency>
      <groupId>org.apache.poi</groupId>
      <artifactId>poi</artifactId>
      <version>3.10-beta2</version>
    </dependency>

    <!-- Servlet dependencies -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>javax.servlet.jsp-api</artifactId>
      <version>2.3.1</version>
    </dependency>

  </dependencies>

  <build>
    <finalName>mutilViewResolver</finalName>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-war-plugin</artifactId>
          <version>2.4</version>
          <configuration>
            <warSourceDirectory>src/main/webapp</warSourceDirectory>
            <warName>mutilViewResolver</warName>
            <failOnMissingWebXml>false</failOnMissingWebXml>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
```
3. 创建pdf和excel对应的视图配置
```
//PdfView.java
package com.liu.springmvc.viewresolver;

import com.liu.springmvc.Model.Pizza;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

public class PdfView extends AbstractPdfView{
    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Pizza pizza = (Pizza)map.get("pizza");

        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setBackgroundColor(Color.lightGray);

        table.addCell("Name");
        table.addCell("Flavor");
        table.addCell("Toppings");

        table.addCell(pizza.getName());
        table.addCell(pizza.getFlavor());
        StringBuffer toppings = new StringBuffer();
        for (String topping: pizza.getToppings()){
            toppings.append(topping);
            toppings.append(" ");
        }
        table.addCell(toppings.toString());
        document.add(table);
    }
}


//ExcelView.java
package com.liu.springmvc.viewresolver;

import com.liu.springmvc.Model.Pizza;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ExcelView extends AbstractExcelView{
    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Pizza pizza = (Pizza)map.get("pizza");

        Sheet sheet = hssfWorkbook.createSheet("sheet 1");
        CellStyle style = hssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);

        Row row = null;
        Cell cell = null;
        int rowCount = 0;
        int colCount = 0;

        //Create header cells
        row = sheet.createRow(rowCount++);

        cell = row.createCell(colCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Name");

        cell = row.createCell(colCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Flavor");

        cell = row.createCell(colCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Toppings");

        //Create data cells
        row = sheet.createRow(rowCount++);
        colCount = 0;
        row.createCell(colCount++).setCellValue(pizza.getName());
        row.createCell(colCount++).setCellValue(pizza.getFlavor());

        StringBuffer toppings = new StringBuffer("");
        for (String topping: pizza.getToppings()){
            toppings.append(topping);
            toppings.append(" ");
        }
        row.createCell(colCount++).setCellValue(toppings.toString());

        for(int i = 0; i < 3; i++){
            sheet.autoSizeColumn(i,true);
        }
    }
}

```
4. 为各种文件方式创建ViewResolver
```
//=========================================================
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class ExcelViewResolver implements ViewResolver{
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        ExcelView view = new ExcelView();
        return view;
    }
}

//=========================================================
package com.liu.springmvc.viewresolver;

import org.springframework.cglib.core.Local;
import org.springframework.oxm.Marshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.Locale;

public class Jaxb2MarshallingXmlViewResolver implements ViewResolver{
    private Marshaller marshaller;

    public Jaxb2MarshallingXmlViewResolver(Marshaller marshaller){
        this.marshaller = marshaller;
    }

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        MarshallingView view = new MarshallingView();
        view.setMarshaller(marshaller);
        return view;
    }
}


//=========================================================
package com.liu.springmvc.viewresolver;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;

public class JsonViewResolver implements ViewResolver{
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setPrettyPrint(true);
        return view;
    }
}



//=========================================================
package com.liu.springmvc.viewresolver;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class PdfViewResolve implements ViewResolver{
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        PdfView view = new PdfView();
        return view;
    }
}

```
5. 创建配置类
```
package com.liu.springmvc.config;

import com.liu.springmvc.Model.Pizza;
import com.liu.springmvc.viewresolver.ExcelViewResolver;
import com.liu.springmvc.viewresolver.Jaxb2MarshallingXmlViewResolver;
import com.liu.springmvc.viewresolver.JsonViewResolver;
import com.liu.springmvc.viewresolver.PdfViewResolve;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.XmlViewResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.liu.springmvc")
public class AppConfig extends WebMvcConfigurerAdapter{
    /**
     * configure ContentNegotiationManager
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
        configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML);
    }

    /**
     * Configure ContentNegotiatingViewResolver
     */
    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager){
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);

        List<ViewResolver> resolvers = new ArrayList<>();
        resolvers.add(jaxb2MarshallingXmlViewResolver());
        resolvers.add(jsonViewResolver());
        resolvers.add(jspViewResolver());
        resolvers.add(excelViewResolver());
        resolvers.add(pdfViewResolver());

        resolver.setViewResolvers(resolvers);
        return  resolver;
    }

    @Bean
    public ViewResolver jaxb2MarshallingXmlViewResolver(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Pizza.class);
        return new Jaxb2MarshallingXmlViewResolver(marshaller);
    }

    @Bean
    public ViewResolver jsonViewResolver(){
        return new JsonViewResolver();
    }

    @Bean
    public ViewResolver pdfViewResolver(){
        return new PdfViewResolve();
    }

    @Bean
    public ViewResolver excelViewResolver(){
        return new ExcelViewResolver();
    }

    @Bean
    public ViewResolver jspViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}

```

6. 创建应用初始化类
```
package com.liu.springmvc.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInit implements WebApplicationInitializer{
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.setServletContext(servletContext);

        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("dispatcher",new DispatcherServlet(context));

        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");
    }
}
//一般常用下面这种方式
/*
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}

 */
```
7. 创建jsp
```
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/3 0003
  Time: 20:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Pizza JSP View</title>
</head>
<body>
<table border="1">
    <tr>
        <td>
            NAME
        </td>
        <td>
            Flavor
        </td>
        <td>
            Toppings
        </td>
    </tr>
    <tr>
        <td>${pizza.name}</td>
        <td>${pizza.flavor}</td>
        <td>
            <c:forEach var="item" items="${pizza.toppings}">
                <c:out value="${item}"/>&nbsp;

            </c:forEach>
        </td>
    </tr>
</table>
</body>
</html>
```