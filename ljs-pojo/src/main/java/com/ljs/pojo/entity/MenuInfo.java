package com.ljs.pojo.entity;

import com.ljs.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @ClassName MenuInfo
 * @Description: 菜单实体类
 * @Author 小松
 * @Date 2019/8/5
 **/
@Entity
@Data
@Table(name = "base_menu")
public class MenuInfo extends BaseAuditable {

    @Column(name = "menuName")
    private String menuName;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "leval")
    private int leval;

    @Column(name = "url")
    private String url;

    @Transient
    private List<MenuInfo> menuInfoList;

}
