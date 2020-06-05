package cn.itchen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class FileInfo {
    private Integer id;
    private String oldFileName;
    private String newFileName;
    private String ext;
    private Integer size;
    private String type;
    private String isImg;
    private Integer downCounts;
    private Date uploadTime;
    private String path;
    private Integer userId;
}
