package com.djh.entity;

import java.sql.Blob;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author djh
 * @since 2021-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Test extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Blob test;


}
