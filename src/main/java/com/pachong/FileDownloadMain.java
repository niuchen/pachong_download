package com.pachong;

import au.com.bytecode.opencsv.CSVReader;
import com.pachong.uilt.JpgDownloadUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class FileDownloadMain {
    public static Log logger = LogFactory.getLog(FileDownloadMain.class);  //日志
    public static void main(String[] d) {
//String html="http://dtol-member-images.s3-website-us-east-1.amazonaws.com/1143/Images/12476.JPG";
//        if(html.toLowerCase().indexOf(".jpg")!=-1){
//            String html2=html.replace(":","_");
//            html2=html2.replace("/","_");
//            html2=html2.replace("\\","_");
//            html2=html2.replace(".","_");
//            html2=html2+".jpg";
//       //     JpgDownloadUtil.download(html,"");
//            System.out.println(html2);
//        }else{
//            System.out.println("没有找到jpg后缀的url:"+html);
//        }
//if(1==1)return;
        String filepath =initAPI.filepath; // 路径
        String csvpath =initAPI.csvpath; // 路径
        System.out.println("222"+filepath);
        //创建下载目录
        File file = new File(filepath);
        if (!file.exists()) {
            boolean result = file.mkdirs();
            if (!result) {
                logger.error("创建失败");
            }else{
                logger.error("创建成功目录"+filepath);
            }
        }

/***下面是下载**/
        File csvfile = new File(csvpath);//读取目录下的全部csv
        if (!csvfile.exists()) {
            logger.error("下载所需的CSV路径错误!"+csvpath);
            return;
        }
         CsvMysqlMain.getFileList(csvpath);
         List<File> filepathlsit = CsvMysqlMain.filelist;
            logger.error("文件数" + filepathlsit.size());
        for (int i = 0; i < filepathlsit.size(); i++) {//循环全部的csv文件
            // File file = new File("D:\\spaga\\ctl00_cphMainContent_repGrids_ctl00_gvGrid\\ctl00_cphMainContent_repGrids_ctl00_gvGrid_ctl02_lnk_IF_Value@9,650 -52%\\1.csv");
            //  FileReader fReader = new FileReader(file);
            //    reader = new CSVReader(new FileReader(file));
            try {
                InputStreamReader filereader2 = new InputStreamReader(new FileInputStream(filepathlsit.get(i)), Charset.forName("GBK"));
                CSVReader csvReader = new CSVReader(filereader2);
                List<String[]> list = csvReader.readAll();
                int shu=1;
                for(String[] ss : list){
                    String img=ss[3];//图片
                    String ptf=ss[4];//pdf
                    int jishu =1;
                    if(img.toLowerCase().indexOf(".jpg")!=-1){
                        String html2=img.replace(":","_");
                        html2=html2.replace("/","_");
                        html2=html2.replace("\\","_");
                        html2=html2.replace(".","_");
                        html2=html2+".jpg";//生成文件名
                        boolean isr=false;
                       while (isr==false){
                           isr= JpgDownloadUtil.download(img,filepath+"/"+html2);
                           if(isr==true){
                               System.out.println("img下载完成:"+img+"||||||"+filepath+"/"+html2);
                               break;
                           }
                           if(jishu++==3){
                               logger.error("img下载失败"+jishu+",取消重拾:"+img);
                               break;
                           }
                           logger.error("img下载错误重拾"+jishu+":"+img);
                        }//下载
                     }else{
                        System.out.println("img没有找到jpg后缀的url:"+img);
                    }

                    if(ptf.toLowerCase().indexOf(".pdf")!=-1){
                        String html2=ptf.replace(":","_");
                        html2=html2.replace("/","_");
                        html2=html2.replace("\\","_");
                        html2=html2.replace(".","_");
                        html2=html2+".pdf";//生成文件名
                           boolean isr=false;
                        while (isr==false){
                            isr= JpgDownloadUtil.download(ptf,filepath+"/"+html2);
                            if(isr==true){
                                System.out.println("ptf下载完成:"+ptf+"||||||"+filepath+"/"+html2);
                                break;
                            }
                            if(jishu++==3){
                                logger.error("ptf下载失败"+jishu+",取消重拾:"+ptf);
                                break;
                            }
                            logger.error("ptf下载错误重拾"+jishu+":"+ptf);
                        }//下载
                    }else{
                        System.out.println("ptf没有找到pdf后缀的url:"+ptf);
                    }
                }
              }catch (Exception e){
                logger.error(e);
            }

            break;
        }





    }

}
