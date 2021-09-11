package com.syh.low.checker.utils;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;




public class PringImage{
public void drawImage(String fileName, int count) {
        try {
            DocFlavor dof = null;
            if (fileName.endsWith(".gif")) {
                dof = DocFlavor.INPUT_STREAM.GIF;
            } else if (fileName.endsWith(".jpg")) {
                dof = DocFlavor.INPUT_STREAM.JPEG;
            } else if (fileName.endsWith(".png")) {
                dof = DocFlavor.INPUT_STREAM.PNG;
            }
            PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
 
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(OrientationRequested.PORTRAIT);
 
            pras.add(new Copies(count));
            pras.add(PrintQuality.HIGH);
            DocAttributeSet das = new HashDocAttributeSet();
 
            // 设置打印纸张的大小（以毫米为单位）
            das.add(new MediaPrintableArea(0, 0, 210, 297, MediaPrintableArea.MM));
            FileInputStream fin = new FileInputStream(fileName);
 
            Doc doc = new SimpleDoc(fin, dof, das);
            DocPrintJob job = ps.createPrintJob();
 
            job.print(doc, pras);
            fin.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (PrintException pe) {
            pe.printStackTrace();
        }
}


public static void printPdf() {
    //构造一个文件选择器，默认为当前目录
    File file = new File("C:\\Users\\syh\\Desktop\\33333.pdf");//获取选择的文件
    //构建打印请求属性集
    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
    //设置打印格式，因为未确定文件类型，这里选择AUTOSENSE
    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
    //查找所有的可用打印服务
    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
    //定位默认的打印服务
    PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
    // 显示打印对话框
    PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);
    if (service != null) {

        try {
            DocPrintJob job = service.createPrintJob(); // 创建打印作业
            FileInputStream fis; // 构造待打印的文件流
            fis = new FileInputStream(file);
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(fis, flavor, das);
            job.print(doc, pras);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
public static void  swtPrintImage(String fileName) throws Exception{
	 // Load the image
   ImageLoader loader = new ImageLoader();
   ImageData[] imageData = loader.load(fileName);

   if (imageData.length > 0) {
     PrinterData printerData = Printer.getDefaultPrinterData();

     if (printerData != null) {
       Printer printer = new Printer(printerData);

       // Calculate the scale factor between the screen resolution and printer
       // resolution in order to correctly size the image for the printer
//       Point screenDPI = Toolkit.getDefaultToolkit().
//       ImageInfo imageInfo = Imaging.getImageInfo(new File(fileName));
       ImageInfo imageInfo = Sanselan.getImageInfo(new File(fileName));
       Point printerDPI = printer.getDPI();
       int scaleFactor = printerDPI.x / imageInfo.getPhysicalWidthDpi();
       int scaleFactory = printerDPI.y / imageInfo.getPhysicalHeightDpi();
       System.out.println(scaleFactor+"/"+scaleFactory);
       // Determine the bounds of the entire area of the printer
       Rectangle trim = printer.computeTrim(0, 0, scaleFactor * imageData[0].width, scaleFactor * imageData[0].height);

       // Start the print job
       if (printer.startJob(fileName)) {
         if (printer.startPage()) {
           GC gc = new GC(printer);
           Image printerImage = new Image(printer, imageData[0]);
           
           // Draw the image
           gc.drawImage(printerImage, 0, 0, imageData[0].width,
             imageData[0].height, -trim.x, -trim.y, 
             scaleFactor * imageData[0].width, 
             scaleFactor * imageData[0].height);
           // Clean up
           printerImage.dispose();
           gc.dispose();
           printer.endPage();
         }
       }
       // End the job and dispose the printer
       printer.endJob();
       printer.dispose();
     }
   }
}



public static void main(String[] args) throws Exception {
	PringImage.swtPrintImage("C:\\Users\\syh\\Desktop\\test.pdf-1.jpg");
}
}
