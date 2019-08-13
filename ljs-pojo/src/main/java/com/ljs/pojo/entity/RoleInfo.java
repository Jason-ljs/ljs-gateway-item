package com.ljs.pojo.entity;

import com.ljs.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @ClassName RoleInfo
 * @Description: 权限实体类
 * @Author 小松
 * @Date 2019/8/5
 **/
@Entity
@Data
@Table(name = "base_role")
public class RoleInfo extends BaseAuditable {

    @Column(name = "roleName")
    private String roleName;

    @Column(name = "miaoShu")
    private String miaoShu;

    @Column(name = "leval")
    private Integer leval;

    @Transient
    private String userNames;

    @Transient
    private List<Long> menuList;

}
