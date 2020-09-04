package com.wimp.dreamer.base.web.controller;

import com.wimp.dreamer.base.msg.ObjectRestResponse;
import com.wimp.dreamer.base.msg.TableResultResponse;
import com.wimp.dreamer.base.utils.Query;
import com.wimp.dreamer.base.web.biz.BaseBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zy
 * @date 2020/9/3
 * <p>
 *  基础controller
 */
public class BaseController<Biz extends BaseBiz<Mapper<Entity>, Entity>, Entity> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected Biz baseBiz;


    /**
     * 通用  单表添加
     *
     * @param judParams 为判重参数 多个参数使用;分号隔开，如两个属性联合判断唯一则用_隔开 ，如： code;model,brand
     * @return ObjectRestResponse<Entity>
     */
    @PostMapping("/add")
    @ResponseBody
    public ObjectRestResponse<Entity> add(@RequestBody Entity entity, String judParams) {
        ObjectRestResponse<Entity> restResponse = new ObjectRestResponse<>();
        if (StringUtil.isNotEmpty(judParams)) {
            String[] pars = judParams.split(";");
            for (String p : pars) {
                boolean repeat = baseBiz.isRepeat(entity, p, false);
                if (repeat) {
                    restResponse.setRel(false);
                    restResponse.setMessage(p);
                    return restResponse;
                }
            }
        }
        baseBiz.insertSelective(entity);
        restResponse.setData(entity);
        return restResponse;
    }

    /**
     * 通用 单表 通过id查询实体
     *
     * @param id 主键
     * @return ObjectRestResponse
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public ObjectRestResponse<Entity> get(@PathVariable Object id) {
        return ObjectRestResponse.ok(baseBiz.selectById(id));
    }

    /**
     * 通用 单表 修改
     *
     * @param entity 集合
     * @return ObjectRestResponse
     * @Param 校验条件 多个分号隔开;联合判重逗号隔开
     */
    @PostMapping("/update")
    @ResponseBody
    public ObjectRestResponse<Entity> update(@RequestBody Entity entity, String judParams) {
        ObjectRestResponse<Entity> restResponse = new ObjectRestResponse<>();
        if (StringUtil.isNotEmpty(judParams)) {
            String[] pars = judParams.split(";");
            for (String p : pars) {
                boolean repeat = baseBiz.isRepeat(entity, judParams, true);
                if (repeat) {
                    restResponse.setRel(false);
                    restResponse.setMessage(p);
                    return restResponse;
                }
            }
        }
        baseBiz.updateSelectiveById(entity);
        restResponse.setData(entity);
        return restResponse;
    }

    /**
     * 通用 单表 通过id删除
     *
     * @param id 主键
     * @return ObjectRestResponse
     */
    @PostMapping("/remove/{id}")
    @ResponseBody
    public ObjectRestResponse<?> remove(@PathVariable Object id) {
        baseBiz.deleteById(id);
        return ObjectRestResponse.ok(baseBiz.selectById(id));
    }

    /**
     * 通用 单表查询实体 list
     *
     * @return List
     */
    @GetMapping("/all")
    @ResponseBody
    public List<Entity> all() {
        return baseBiz.selectListAll();
    }

    /**
     * 单表 查询 基础方法: 查询条件 默认 多个查询条件 都为like
     *
     * @param params 参数
     * @return TableResultResponse
     */
    @GetMapping("/page")
    @ResponseBody
    public TableResultResponse<Entity> pageBase(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        return baseBiz.selectByQuery(query, false);
    }


    /**
     * 单表查询:查询条件多变,等于,大于,小于,like 等等;
     * 按照查询参数前缀区分
     * 例如查询的参数:
     * like_userName : sql为userName like %userName%
     * eq_userName   : sql为userName = userName
     *
     * @param params map参数
     * @return TableResultResponse
     * @see com.wimp.dreamer.base.web.constant.WebConstant
     */
    @GetMapping("/pageCriteria")
    @ResponseBody
    public TableResultResponse<Entity> pageCriteria(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        return baseBiz.selectByQuery(query, true);
    }
}
