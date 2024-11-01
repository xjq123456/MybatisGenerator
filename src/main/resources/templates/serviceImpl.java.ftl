package com.smt.web.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.smt.web.result.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smt.web.vo.${entity};
import com.smt.web.dto.${cfg.paramType}AddDTO;
import com.smt.web.dto.${cfg.paramType}UpdateDTO;
import com.smt.web.dto.${cfg.paramType}PageDTO;
import com.smt.web.dto.CommonIdDetailDTO;
import com.smt.web.entity.${cfg.paramType};
import com.smt.web.mapper.${table.mapperName};
import ${superServiceImplClassPackage};
import com.smt.web.service.${table.serviceName};
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${cfg.paramType}> implements ${table.serviceName} {
    /**
    * <p>
    * ${table.comment!} 新增接口实现层
    * </p>
    *
    * @author ${author}
    * @since ${date}
    */
    @Override
    public void add${cfg.paramType}(${cfg.paramType}AddDTO dto){
            <#assign variable = "${cfg.paramType}">
            <#assign firstLetterLower = variable?substring(0, 1)?lower_case>
            <#assign restOfString = variable?substring(1)>
            <#assign result = firstLetterLower + restOfString>
         try {
             ${cfg.paramType} ${result} = new ${cfg.paramType}();
             BeanUtil.copyProperties(dto, ${result});
             ${result}.setIsDelete(false);
             ${result}.setStatus(1);
             this.save(${result});
             } catch (Exception e) {
             throw new RuntimeException("新增失败");
 }

    }


    /**
    * <p>
    * ${table.comment!} 修改接口实现层
    * </p>
    *
    * @author ${author}
    * @since ${date}
    */
    @Override
    public void update${cfg.paramType}(${cfg.paramType}UpdateDTO dto){
        try {
            ${cfg.paramType} ${result} = new ${cfg.paramType}();
            BeanUtil.copyProperties(dto, ${result});
            this.updateById(${result});
            } catch (Exception e) {
            throw new RuntimeException("修改失败");
            }
    }


    /**
    * <p>
    * ${table.comment!} 删除接口实现层
    * </p>
    *
    * @author ${author}
    * @since ${date}
    */
    @Override
    public void remove${cfg.paramType}(CommonIdDetailDTO dto){
        try {
            LambdaUpdateWrapper<${cfg.paramType}> wrapper = new LambdaUpdateWrapper<${cfg.paramType}>();
                wrapper.eq(${cfg.paramType}::getId, dto.getId())
                    .set(${cfg.paramType}::getIsDelete, true);
                ${cfg.paramType} ${result} = new ${cfg.paramType}();
                BeanUtil.copyProperties(dto, ${result});
                this.update(wrapper);
                } catch (Exception e) {
                throw new RuntimeException("删除失败");
                }
    }

    /**
    * <p>
    * ${table.comment!} 详情接口实现层
    * </p>
    *
    * @author ${author}
    * @since ${date}
    */
    @Override
    public ${entity} get${cfg.paramType}(CommonIdDetailDTO dto){
        LambdaQueryWrapper<${cfg.paramType}> wrapper = new LambdaQueryWrapper<${cfg.paramType}>();
        wrapper.eq(${cfg.paramType}::getId, dto.getId())
        .eq(${cfg.paramType}::getIsDelete, false);
        ${cfg.paramType} rate = this.baseMapper.selectOne(wrapper);
        ${entity} ${result}VO = new ${entity}();
        BeanUtil.copyProperties(rate, ${result}VO);
        return ${result}VO;
    }

    /**
    * <p>
    * ${table.comment!} 分页接口实现层
    * </p>
    *
    * @author ${author}
    * @since ${date}
    */
    @Override
    public PageResult<${entity}> get${cfg.paramType}List(${cfg.paramType}PageDTO dto){
        Page<${cfg.paramType}> page = new Page<>(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<${cfg.paramType}> wrapper = new LambdaQueryWrapper<${cfg.paramType}>();
            wrapper.eq(${cfg.paramType}::getIsDelete, false)
                   .eq(${cfg.paramType}::getStatus,1);
                page = this.baseMapper.selectPage(page, wrapper);
                List<${cfg.paramType}VO> ${result}VOS = new ArrayList<>();
                ${result}VOS = page.getRecords().stream().map(vos -> {
                ${entity} ${result}VO = new ${entity}();
                BeanUtil.copyProperties(vos, ${result}VO);
                return ${result}VO;
                }).toList();
                PageResult<${entity}> rs=new PageResult<>(page.getTotal(),${result}VOS);
                return rs;
    }
}
</#if>
