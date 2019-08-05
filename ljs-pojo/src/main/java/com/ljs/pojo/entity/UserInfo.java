package com.ljs.pojo.entity;

import com.ljs.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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
   private int sex;

    @Column(name = "parentId")
    private Long parentId;

    @Transient
    private List<MenuInfo> listMenuInfo;

    @Transient
    private RoleInfo roleInfo;

    @Transient
    private Map<String,String> authmap;

}
