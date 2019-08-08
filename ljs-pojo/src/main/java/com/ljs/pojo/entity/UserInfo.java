package com.ljs.pojo.entity;

import com.ljs.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserInfo
 * @Description: 用户实体类
 * @Author 小松
 * @Date 2019/8/5
 **/
@Data
@Entity
@Table(name = "base_user")
public class UserInfo extends BaseAuditable {

    @Column(name = "userName")
   private String userName;

    @Column(name = "loginName")
   private String loginName;

    @Column(name = "password")
   private String password;

    @Column(name = "tel")
   private String tel;

    @Column(name = "sex")
   private String sex;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Transient
    private List<MenuInfo> listMenuInfo;

    @Transient
    private RoleInfo roleInfo;

    @Transient
    private List<RoleInfo> roleInfoList;

    @Transient
    private Map<String,String> authmap;

    @Transient
    private String createTimeFormat;

    public String getCreateTimeFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.getCreateTime());
    }
}
