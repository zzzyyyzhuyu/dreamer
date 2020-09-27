package com.wimp.dreamer.base.web.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wimp.dreamer.base.exception.BaseException;
import com.wimp.dreamer.base.exception.enums.ErrorCode;
import com.wimp.dreamer.base.msg.TableResultResponse;
import com.wimp.dreamer.base.utils.EntityUtils;
import com.wimp.dreamer.base.utils.Query;
import com.wimp.dreamer.base.utils.ReflectionUtils;
import com.wimp.dreamer.base.web.constant.WebConstant;
import com.wimp.dreamer.security.auth.domain.User;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zy
 * @date 2020/9/3
 * <p>
 * 通用Biz
 */
public abstract class BaseBiz<M extends Mapper<T>, T>{

    @Resource
    protected M mapper;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }

    @SuppressWarnings("unchecked")
    public T selectById(Object id) {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return mapper.selectByPrimaryKey(EntityUtils.getPKProVal(clazz, id));
    }


    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }


    public List<T> selectListAll() {
        return mapper.selectAll();
    }


    public Long selectCount(T entity) {
        return (long) mapper.selectCount(entity);
    }


    public void insert(T entity) {
        EntityUtils.setDefaultId(entity);
        mapper.insert(entity);
    }

    public void insertSelective(T entity) {
        EntityUtils.setDefaultId(entity);
        EntityUtils.setCreatInfo(entity);
        mapper.insertSelective(entity);
    }

    public void delete(T entity) {
        mapper.delete(entity);
    }

    @SuppressWarnings("unchecked")
    public void deleteById(Object id) {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        mapper.deleteByPrimaryKey(EntityUtils.getPKProVal(clazz, id));
    }


    public void updateById(T entity) {
        mapper.updateByPrimaryKey(entity);
    }


    public void updateSelectiveById(T entity) {
        mapper.updateByPrimaryKey(entity);

    }

    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }

    public TableResultResponse<T> selectByQuery(Query query, Boolean isCriteria) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<T> list = mapper.selectByExample(setExample(query, isCriteria));
        return new TableResultResponse<>(result.getTotal(), list);
    }

    @SuppressWarnings("unchecked")
    public Example setExample(Query query, Boolean isCriteria) {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        if (query.entrySet().size() > 0) {
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if (isCriteria == null || !isCriteria) {
                    criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                } else {
                    String key = entry.getKey();
                    if (key.contains("_")) {
                        String[] strs = key.split("_");
                        assembleParams(clazz, example, criteria, strs[1], strs[0], entry.getValue());
                    } else {
                        assembleParams(clazz, example, criteria, key, "", entry.getValue());
                    }


                }
            }
        }
        if (ReflectionUtils.getAccessibleFieldByClass(clazz, EntityUtils.CREATE_TIME) != null) {
            example.orderBy(EntityUtils.CREATE_TIME).desc();
        }
        return example;
    }

    private void assembleParams(Class<T> clazz, Example example, Example.Criteria criteria, String columnName, String op, Object value) {
        switch (op) {
            case WebConstant.OP.LEFT_LIKE:
                if (value != null) {
                    criteria.andLike(columnName, "%" + value);
                }
                break;
            case WebConstant.OP.RIGHT_LIKE:
                if (value != null) {
                    criteria.andLike(columnName, value + "%");
                }
                break;
            case WebConstant.OP.EQ:
                if (value != null) {
                    criteria.andEqualTo(columnName, value);
                }
                break;
            case WebConstant.OP.NE:
                if (value != null) {
                    criteria.andNotEqualTo(columnName, value);
                }
                break;
            case WebConstant.OP.GT:
                if (value != null) {
                    criteria.andGreaterThan(columnName, value);
                }
                break;
            case WebConstant.OP.NL:
                if (value != null) {
                    criteria.andGreaterThanOrEqualTo(columnName, value);
                }
                break;
            case WebConstant.OP.LT:
                if (value != null) {
                    criteria.andLessThan(columnName, value);
                }
                break;
            case WebConstant.OP.NG:
                if (value != null) {
                    criteria.andLessThanOrEqualTo(columnName, value);
                }
                break;
            case WebConstant.OP.NULL:
                criteria.andIsNull(columnName);
                break;
            case WebConstant.OP.NOTNULL:
                criteria.andIsNotNull(columnName);
                break;
            case WebConstant.OP.IN:
                if (value != null) {
                    if (value instanceof ArrayList) {
                        criteria.andIn(columnName, (ArrayList<?>) value);
                    }
                }
                break;
            case WebConstant.OP.BETWEEN:
                if (value != null) {
                    if (value instanceof ArrayList) {
                        criteria.andBetween(columnName, ((ArrayList<?>) value).get(0),
                                ((ArrayList<?>) value).get(1));
                    }
                }
                break;
            case WebConstant.OP.NOT_BETWEEN:
                if (value != null) {
                    if (value instanceof ArrayList) {
                        criteria.andNotBetween(columnName, ((ArrayList<?>) value).get(0),
                                ((ArrayList<?>) value).get(1));
                    }
                }
                break;
            case WebConstant.OP.NOT_IN:
                if (value != null) {
                    if (value instanceof ArrayList) {
                        criteria.andNotIn(columnName, (ArrayList<?>) value);
                    }
                }
                break;
            case WebConstant.OP.ORDER:
                if (StringUtils.isNoneBlank(columnName)) {
                    Field field = ReflectionUtils.getAccessibleFieldByClass(clazz, columnName);
                    assert field != null;
                    Column column = field.getAnnotation(Column.class);
                    if (column != null) {
                        String colName = column.name();
                        if (StringUtils.isNoneBlank(colName)) {
                            if (value != null) {
                                example.setOrderByClause(colName + " " + value.toString());
                            } else {
                                example.setOrderByClause(colName + " DESC");
                            }
                        }
                    }
                }
                break;
            default:
                if (value != null) {
                    criteria.andLike(columnName, "%" + value + "%");
                }

        }

    }

    /**
     * 判断重复
     *
     * @param params   如果两属性并行判断重复，则用,例如：model,brand
     * @param isUpdate ，false 为添加方法，true为 修改方法
     * @return boolean true 代表重复，false 代表无重复
     * @Param entity
     **/
    @SuppressWarnings("unchecked")
    public boolean isRepeat(T entity, String params, boolean isUpdate) {
        if (StringUtil.isEmpty(params)) {
            return false;
        }
        String[] ps = params.split(",");
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        for (String p : ps) {
            Object fie;
            try {
                fie = getFieldValueByFieldName(p, entity);
            } catch (Exception e) {//如果没有该属性，则返回
                throw new BaseException(ErrorCode.GL99990100);
            }
            if (fie != null) {
                criteria.andEqualTo(p, fie);
            }
        }
        if (isUpdate) {
            EntityColumn pkColumn = EntityHelper.getPKColumns(entity.getClass()).iterator().next();
            Object val = getFieldValueByFieldName(pkColumn.getProperty(), entity);
            criteria.andNotEqualTo(pkColumn.getProperty(), val);
        }
        int count = selectCountByExample(example);
        return count>0;
    }

    /**
     * 根据属性名获取属性值,通过get方法
     *
     * @param fieldName 属性名
     * @param object 参数类型
     * @return 返回属性值
     */
    private Object getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Method method = object.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            return method.invoke(object);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.GL99990100);
        }

    }

}
