package com.ljs.commen;


import lombok.Data;

import java.io.Serializable;

@Data
public class IdEntity implements Serializable {

    private Long id;

    private int version;

}
