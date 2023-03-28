package Upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // 获取上传的文件
        Collection<Part> parts = request.getParts();
         int count=parts.size();
//        Part filePart = request.getPart("file");
//        Collections<Part> fileParts= request.getParts("file");
        for(Part filePart : parts){
            // 获取文件名
            String fileName = getFileName(filePart);
            if(fileName==null){
                System.out.println("-----");
                break;}
            System.out.println(fileName);
            String username=request.getParameter("username");
            if (username != null) {
                try {
                    username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // 处理异常
                }
            }
            System.out.println(username);
            System.out.println(".....");
            // 获取上传目录的真实路径
//        String uploadPath = getServletContext().getRealPath("E:\\1 文明督导部\\1 2022-2023\\截图");
//        System.out.println(uploadPath);
            String uploadPath="E:\\1 文明督导部\\1 2022-2023\\截图\\"+username;
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            // 保存上传的文件
            File file = new File(uploadDir, fileName);
            try (InputStream inputStream = filePart.getInputStream();
                 OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
          count--;
            if(count==0) break;
        }


        // 显示上传成功的消息
        response.getWriter().write("文件上传成功！");
    }

    /**
     * 获取上传文件的文件名
     */
    private String getFileName(Part part) throws UnsupportedEncodingException {
        String contentDispositionHeader = part.getHeader("content-disposition");
        String[] elements = contentDispositionHeader.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                int index = element.indexOf("=");
                String filename = element.substring(index + 1).trim();
                filename = URLDecoder.decode(filename, "UTF-8");
                filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
                filename = filename.replace("\"", "");
                return filename;
            }
        }
        return null;
    }


}
