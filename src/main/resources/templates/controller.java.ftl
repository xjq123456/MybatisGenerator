package com.smt.web.controller.${cfg.cnpkg};


<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.web.bind.annotation.RestController;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import com.smt.web.service.${table.serviceName};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.smt.web.vo.${cfg.paramType}VO;
import com.smt.web.dto.${cfg.paramType}AddDTO;
import com.smt.web.dto.${cfg.paramType}UpdateDTO;
import com.smt.web.dto.${cfg.paramType}PageDTO;
import com.smt.web.dto.CommonIdDetailDTO;
import com.smt.web.result.PageResult;
import com.smt.web.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
<#if restControllerStyle>
@RestController
<#else>
@RestController
@RequestMapping("/${cfg.mappingPre}/${table.controllerName}")
</#if>
@Api(tags = "${table.comment!}管理")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
<#assign variable = "${cfg.paramType}">
<#assign firstLetterLower = variable?substring(0, 1)?lower_case>
<#assign restOfString = variable?substring(1)>
<#assign result = firstLetterLower + restOfString>

    @Autowired
    private I${cfg.paramType}Service ${cfg.paramType?uncap_first}Service;

    @PostMapping("/v1/get${cfg.paramType}")
    @ApiOperation("${table.comment!}详情")
    public Result<${cfg.paramType}VO> get${cfg.paramType}(@Validated @RequestBody CommonIdDetailDTO dto){
        ${cfg.paramType}VO re = this.${cfg.paramType?uncap_first}Service.get${cfg.paramType}(dto);
            return Result.success(re);
        }


    @PostMapping("/v1/add${cfg.paramType}")
    @ApiOperation("${table.comment!}新增")
    public Result add${cfg.paramType}(@Validated @RequestBody ${cfg.paramType}AddDTO dto){
        this.${cfg.paramType?uncap_first}Service.add${cfg.paramType}(dto);
            return Result.success();
    }



    @PostMapping("/v1/update${cfg.paramType}")
    @ApiOperation("${table.comment!}修改")
    public Result update${cfg.paramType}(@Validated @RequestBody ${cfg.paramType}UpdateDTO dto){
        this.${cfg.paramType?uncap_first}Service.update${cfg.paramType}(dto);
            return Result.success();
    }

    @PostMapping("/v1/remove${cfg.paramType}")
    @ApiOperation("${table.comment!}删除")
    public Result remove${cfg.paramType}(@Validated @RequestBody CommonIdDetailDTO dto){
        this.${cfg.paramType?uncap_first}Service.remove${cfg.paramType}(dto);
            return Result.success();
    }



    <#assign var2 = "<${cfg.paramType}VO>" />
    @PostMapping("/v1/getServiceRateList")
    @ApiOperation("${table.comment!}分页查询")
    public Result<PageResult${var2}> get${cfg.paramType}List(@Validated @RequestBody ${cfg.paramType}PageDTO dto){
        PageResult<${cfg.paramType}VO> result = this.${cfg.paramType?uncap_first}Service.get${cfg.paramType}List(dto);
            return Result.success(result);
    }

}
</#if>
