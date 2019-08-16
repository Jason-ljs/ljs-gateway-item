package com.ljs.config;

import com.ljs.pojo.entity.UserInfo;
import com.ljs.utils.MD5;
import com.ljs.utils.UID;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName EcxcelUtils
 * @Description: Excel导入导出数据工具类
 * @Author 小松
 * @Date 2019/8/12
 **/
public class UserEcxcelUtils {

    /**
     * 上传文件
     */
    public static List<UserInfo> importExcel(InputStream fileInputStream) throws IOException {

        List<UserInfo> list = new ArrayList<>();

        //获取文件输入流
//        FileInputStream fileInputStream = new FileInputStream(file);

        //获取Excel工作簿对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);

        //得到Excel工作表对象
        HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);

        //循环读取表格数据
        for (Row row : sheetAt) {
            //首行（即表头）不读取
            if(row.getRowNum() == 0){
                continue;
            }
            //读取当前行中单元格数据，索引从0开始
            String userName = row.getCell(0).getStringCellValue();
            String loginName = row.getCell(1).getStringCellValue();
            String password = row.getCell(2).getStringCellValue();
            String sex = row.getCell(3).getStringCellValue();
            String tel = row.getCell(4).getStringCellValue();

            //创建对象
            UserInfo userInfo = new UserInfo();
            userInfo.setId(UID.next());
            userInfo.setUserName(userName);
            userInfo.setLoginName(loginName);
            userInfo.setPassword(MD5.encryptPassword(password, "ljs"));
            userInfo.setSex(sex);
            userInfo.setTel(tel);
            userInfo.setCreateTime(new Date());

            //加入到集合
            list.add(userInfo);
        }

        //关闭流
        hssfWorkbook.close();

        return list;
    }


    /**
     * 导出数据到Excel表格
     */
    /*public static void exportExcel(List<UserInfo> userInfoList) throws IOException {

        //在内存中创建excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        //创建工作簿
        HSSFSheet sheet = hssfWorkbook.createSheet();

        //创建标题行
        HSSFRow title = sheet.createRow(0);
        title.createCell(0).setCellValue("用户姓名");
        title.createCell(1).setCellValue("登录名");
        title.createCell(2).setCellValue("加密后的密码");
        title.createCell(3).setCellValue("性别");
        title.createCell(4).setCellValue("电话");
        title.createCell(5).setCellValue("创建时间");
        title.createCell(6).setCellValue("角色");

        //遍历数据，创建数据行
        for (UserInfo userInfo: userInfoList) {
            //获取最后一行的行号
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow hssfRow = sheet.createRow(lastRowNum + 1);
            hssfRow.createCell(0).setCellValue(userInfo.getUserName());
            hssfRow.createCell(1).setCellValue(userInfo.getLoginName());
            hssfRow.createCell(2).setCellValue(userInfo.getPassword());
            hssfRow.createCell(3).setCellValue(userInfo.getSex());
            hssfRow.createCell(4).setCellValue(userInfo.getTel());
            hssfRow.createCell(5).setCellValue(userInfo.getCreateTimeFormat());
            hssfRow.createCell(6).setCellValue(userInfo.getRoleInfo().getRoleName());
        }

        //创建文件名
        String fileName = "用户数据下载.xls";

        //获取配置文件中保存对应excel文件的路径  文件目录
        File file = new File("E:\\biao\\"+fileName);
        if(!file.exists()){
            file.mkdirs();
        }

       FileOutputStream fileOutputStream = new FileOutputStream(file);

        //写出文件
        hssfWorkbook.write(fileOutputStream);

        //关闭流
        hssfWorkbook.close();

    }*/

}
