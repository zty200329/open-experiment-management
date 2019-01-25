package com.swpu.uchain.openexperiment.form.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description
 * @Author cby
 * @Date 19-1-25
 **/
@Data
public class UploadFileForm {


    private MultipartFile file;


}
