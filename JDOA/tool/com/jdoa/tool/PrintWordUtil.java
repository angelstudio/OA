package com.jdoa.tool;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.graphic.RtfShape;
import com.lowagie.text.rtf.graphic.RtfShapePosition;
import com.lowagie.text.rtf.graphic.RtfShapeProperty;

public class PrintWordUtil {
	
	/**
	 * 打印word文档
	 * @param firstTitle 大标题
	 * @param dispatchNo 发文号
	 * @param secondTitle 二标题
	 * @param paragraphs 每个段落
	 * @param filePath 文件保存路径
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static void printWord(String firstTitle,String dispatchNo,String secondTitle,List<String> paragraphs,String filePath) throws DocumentException, IOException{
		
		//--------------------------初始化文档开始-----------------------------
		 Document document = new Document(PageSize.A4);
		 RtfWriter2.getInstance(document, new FileOutputStream(filePath));
		 document.open();
		 //--------------------------初始化文档结束-----------------------------
		
		 //设置全局字体
		 BaseFont bfChinese = BaseFont.createFont("STSong-Light",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		
		 //---------------------------大标题初始化开始---------------------------
		 Font titleFont = new Font(bfChinese, 48, Font.BOLD);
		 titleFont.setColor(Color.RED);	
		 Paragraph title = new Paragraph(firstTitle);
		 title.setSpacingBefore(60f);
		 title.setAlignment(Element.ALIGN_CENTER);
		 title.setFont(titleFont);
		 document.add(title);
		 //---------------------------大标题初始化结束---------------------------
		 
		 //--------------------------发文号开始---------------------------------
		 Font dispatchFont = new Font(bfChinese, 20, Font.NORMAL);
		 dispatchFont.setColor(Color.BLACK);
		 Paragraph dispatchPar = new Paragraph(dispatchNo);
		 dispatchPar.setLeading(50f);
		 dispatchPar.setAlignment(Element.ALIGN_CENTER);
		 document.add(dispatchPar);
		 //----------------------------发文号结束------------------------------
		 
		 //----------------------------红色下划线开始------------------------
		 RtfShapePosition position  = new RtfShapePosition(150, 0, 10400, 170);
		 position.setXRelativePos(RtfShapePosition.POSITION_X_RELATIVE_MARGIN);
		 position.setYRelativePos(RtfShapePosition.POSITION_Y_RELATIVE_PARAGRAPH);
		 RtfShape shape = new RtfShape(RtfShape.SHAPE_RECTANGLE, position);
		 RtfShapeProperty property = new RtfShapeProperty(RtfShapeProperty.PROPERTY_LINE_COLOR,Color.RED);
		 shape.setProperty(property);
		 Paragraph linePar = new Paragraph(shape);
		 document.add(linePar);
		 //-----------------------------红色下划线结束------------------------
		 
		 //-----------------------------二标题开始-------------------------
		 Font ctitleFont = new Font(bfChinese, 23, Font.BOLD);
		 ctitleFont.setColor(Color.BLACK);	
		 Paragraph ctitle = new Paragraph(secondTitle);
		 ctitle.setLeading(50f);
		 // 设置标题格式对齐方式
		 ctitle.setAlignment(Element.ALIGN_CENTER);
		 ctitle.setFont(ctitleFont);
		 document.add(ctitle);
		 //------------------------------二标题结束--------------------------
		
		 //------------------------------正文开始-----------------------------
		 Font contentFont = new Font(bfChinese, 15, Font.NORMAL);
		 contentFont.setColor(Color.BLACK);	
		 Paragraph content = new Paragraph(paragraphs.get(0));
		 content.setLeading(50f);
		 content.setAlignment(Element.ALIGN_LEFT);
		 content.setFont(contentFont);
		 document.add(content);
		 for(int i = 1; i < paragraphs.size(); i++){
			 content = new Paragraph("     "+paragraphs.get(i));
			 content.setFont(contentFont);
			 content.setLeading(30f);
			 document.add(content);
		 }
		 content = new Paragraph(firstTitle);
		 content.setSpacingBefore(80f);
		 content.setAlignment(Element.ALIGN_RIGHT);
		 document.add(content);
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		 content = new Paragraph(sf.format(new Date()));
		 content.setSpacingBefore(20f);
		 content.setAlignment(Element.ALIGN_RIGHT);
		 document.add(content);
		 //---------------------------------正文结束------------------------------
		 document.close();
	}
}
