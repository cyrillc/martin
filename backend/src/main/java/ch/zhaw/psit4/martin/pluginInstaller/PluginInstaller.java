package ch.zhaw.psit4.martin.pluginInstaller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class PluginInstaller {
    
    
    
    public String installPlugin(String name, MultipartFile plugin){
        try {
            if(isFileSupported(plugin)){
                upload(name, plugin);
            } else {
                return "format not supported";
            }
            
        } catch (FileUploadException e) {
            return "upload plugin failed";
        }
        //TODO install etc..
        return "plugin installed correctly";
    }
    
    private void upload(String name, MultipartFile plugin) throws FileUploadException{
        if (!plugin.isEmpty()) {
            try {
                File fileJava = new File("uploads/" + name + ".jar");
                if (!fileJava.getParentFile().exists()) {
                    fileJava.getParentFile().mkdirs();
                }
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(fileJava));
                FileCopyUtils.copy(plugin.getInputStream(), stream);
                stream.close();
            } catch (Exception e) {
                throw new FileUploadException(e.getMessage());
            }
        } else {
            throw new FileUploadException("file does not exist");
        }
    }
    
    public boolean isFileSupported(MultipartFile file){
        String origFileName = file.getOriginalFilename();
        boolean isJar = FilenameUtils.getExtension(origFileName).equals("jar");
        return isJar ? true : false;
    }

}
