package com.wzx.sqliteandgreendao;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * 描述 TODO 类型转换
 * Created by 王治湘 on 2017/10/23.
 * version 1.0
 */

public class NoteTypeConverter implements PropertyConverter<NoteType, String> {

    @Override
    public NoteType convertToEntityProperty(String databaseValue) {
        /*转换为实体类型*/
        return NoteType.valueOf(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(NoteType entityProperty) {
        /*转换为数据库值*/
        return entityProperty.name();
    }
}
