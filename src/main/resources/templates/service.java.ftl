package com.smt.web.service;

import com.smt.web.entity.${cfg.paramType};
import com.smt.web.dto.*;
import com.smt.web.mapper.${table.mapperName};
import com.baomidou.mybatisplus.extension.service.IService;
import com.smt.web.service.${table.serviceName};
import com.smt.web.vo.${entity};
import lombok.extern.slf4j.Slf4j;
import com.smt.web.dto.CommonIdDetailDTO;
import com.smt.web.dto.${cfg.paramType}AddDTO;
import com.smt.web.dto.${cfg.paramType}UpdateDTO;
import com.smt.web.dto.${cfg.paramType}PageDTO;
import com.smt.web.result.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * ${table.comment!} 服务接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */

<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public interface ${table.serviceName} extends IService<${cfg.paramType}>{
      /**
      * <p>
      * ${table.comment!} 新增接口
      * </p>
      *
      * @author ${author}
      * @since ${date}
      */
      void add${cfg.paramType}(${cfg.paramType}AddDTO dto);


        /**
        * <p>
        * ${table.comment!} 修改接口
        * </p>
        *
        * @author ${author}
        * @since ${date}
        */
        void update${cfg.paramType}(${cfg.paramType}UpdateDTO dto);


        /**
        * <p>
        * ${table.comment!} 删除接口
        * </p>
        *
        * @author ${author}
        * @since ${date}
        */
        void remove${cfg.paramType}(CommonIdDetailDTO dto);

        /**
        * <p>
        * ${table.comment!} 详情接口
        * </p>
        *
        * @author ${author}
        * @since ${date}
        */
        ${entity} get${cfg.paramType}(CommonIdDetailDTO dto);

        /**
        * <p>
        * ${table.comment!} 分页接口
        * </p>
        *
        * @author ${author}
        * @since ${date}
        */
        PageResult<${entity}> get${cfg.paramType}List(${cfg.paramType}PageDTO dto);

}
</#if>
