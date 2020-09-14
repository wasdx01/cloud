package com.bochao.common.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bochao.common.pojo.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

/**
 * @Author 陈晓峰
 * @Description:
 * @Date:Created in  2020/8/4
 */
@Data
@TableName("p_fileinfo")
public class FileInfo extends CommonEntity implements Serializable, Cloneable {
    @ApiModelProperty("文件唯一标识ID")
    @TableId("file_id")
    private String id;
    private String md5;
    private Integer refCount;
    @ApiModelProperty("文件名称")
    private String fileName;
    @ApiModelProperty("文件后缀")
    private String fileSuffix;
    private Long fileSize;
    @ApiModelProperty("储存地址")
    private String storePath;
    private String fileStoreName;
    private Date createTime;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
