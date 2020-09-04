package com.wimp.dreamer.base.utils;


import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Set;

/**
 * 	EntityUtils
 *  实体类相关工具类(Tk mapper用)
 * @author zy
 */

public class EntityUtils {

	public static final String  CREATE_TIME = "createTime";

	/**
	 * 快速将bean的createTime附上相关值
	 * 
	 * @param entity 实体bean
	 *
	 */
	public static <T> void setCreatInfo(T entity) {
		setCreateInfo(entity);
	}
	public static <T> void setDefaultId(T entity){
		if(!ReflectionUtils.hasField(entity, "id")) {
			return ;
		}
		String type = ReflectionUtils.getFieldType(entity, "id");
		if(StringUtils.isNotBlank(type) && type.contains("java.lang.String")){
            Object value =  ReflectionUtils.getFieldValue(entity, "id");
            if(value == null || StringUtils.isBlank((String)value)){
                ReflectionUtils.setFieldValue(entity,"id",UUIDUtils.generate32UUidString());
            }
		}
	}
	/**
	 * 快速将bean的crtUser、crtHost、crtTime附上相关值
	 *
	 * @param entity 实体bean
	 *
	 */
	public static <T> void setCreateInfo(T entity){
		// 默认属性
		String[] fields = {CREATE_TIME};
		Field field = ReflectionUtils.getAccessibleField(entity, CREATE_TIME);
		// 默认值
		Object [] value = null;
		if(field!=null&&field.getType().equals(Date.class)){
			value = new Object []{new Date()};
		}
		// 填充默认属性值
		setDefaultValues(entity, fields, value);
	}


	/**
	 * 依据对象的属性数组和值数组对对象的属性进行赋值
	 * 
	 * @param entity 对象
	 * @param fields 属性数组
	 * @param value 值数组
	 *
	 */
	private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
		for(int i=0;i<fields.length;i++){
			String field = fields[i];
			if(ReflectionUtils.hasField(entity, field)){
				ReflectionUtils.invokeSetter(entity, field, value[i]);
			}
		}
	}
	/**
	 * 根据主键属性，判断主键是否值为空
	 * 
	 * @param entity
	 * @param field
	 * @return 主键为空，则返回false；主键有值，返回true
	 */
	public static <T> boolean isPKNotNull(T entity,String field){
		if(!ReflectionUtils.hasField(entity, field)) {
			return false;
		}
		Object value = ReflectionUtils.getFieldValue(entity, field);
		return value!=null&&!"".equals(value);
	}

	/**
	 * 主键类型转换为实体类类型
	 * @param clazz
	 * @param idVal
	 * @return
	 */
	public  static Object getPKProVal(Class clazz,Object idVal){
		Set<EntityColumn> columns = EntityHelper.getPKColumns(clazz);
		StringBuffer columnName = new StringBuffer();
		EntityColumn column = columns.iterator().next();
		Class colClass = column.getJavaType();
		if(colClass.equals(Integer.class)){
			return Integer.parseInt(idVal.toString());
		}
		return idVal;
	}
}
