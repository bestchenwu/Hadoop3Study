package hadoopCommonStudy.unit4_rpc;

import java.io.Serializable;

/**
 * 模拟hdfs对文件的状态进行描述
 *
 * @author chenwu on 2022.5.12
 */
public class RMIFileStatus implements Serializable {

    private String fileCreateTime;//文件创建时间
    private Long fileLength;//文件大小

    public RMIFileStatus(){

    }

    public String getFileCreateTime() {
        return fileCreateTime;
    }

    public void setFileCreateTime(String fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    public String toString() {
        return "RMIFileStatus{" +
                "fileCreateTime='" + fileCreateTime + '\'' +
                ", fileLength=" + fileLength +
                '}';
    }
}
