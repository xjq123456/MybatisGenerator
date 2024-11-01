package com.shanjupay.generator;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.fasterxml.jackson.core.JsonProcessingException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MybatisPlusGeneratorUpdateDTO {

    public static void getDto() throws JsonProcessingException {
        Jedis jedis = new Jedis("175.178.126.194");
        // 连接 Redis 服务器
        jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("rrdd9999");
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        globalConfig.setAuthor("xiongjunqiao");
        globalConfig.setOpen(false);
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setUrl("jdbc:mysql://127.0.0.1:3309/mushroom_smt?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&userAffectedRows=true");
        // dataSourceConfig.setSchemaName("public");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("ll775522");
        autoGenerator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.smt");
        String userOrAdmin = jedis.get("userOrAdmin");

        packageConfig.setController("controller."+userOrAdmin);
        String moudle = jedis.get("moudle");
        packageConfig.setModuleName(moudle);
        autoGenerator.setPackageInfo(packageConfig);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setTablePrefix(packageConfig.getModuleName() + "_");
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);//表名映射到实体策略，带下划线的转成驼峰
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);//列名映射到类型属性策略，带下划线的转成驼峰
        // strategyConfig.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategyConfig.setEntityLombokModel(true);//实体类使用lombok

        String tableNames = jedis.get("tableNames");
            /*ObjectMapper objectMapper = new ObjectMapper();
            String [] strings = objectMapper.readValue(tableNames,String[].class);*/
        strategyConfig.setInclude(tableNames.split(",")); // 需要生成的表名
        autoGenerator.setStrategy(strategyConfig);


        // 模板配置：自定义模板路径
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null); // 不生成 XML 文件
        templateConfig.setEntity("templates/DTO.java"); // 自定义 DTO 模板路径
        packageConfig.setEntity("dto");
        globalConfig.setSwagger2(true);
        //globalConfig.setServiceName("I"+"s%"+"Service");
        globalConfig.setEntityName("%sUpdateDTO");
        strategyConfig.setEntityLombokModel(true);//实体类使用lombok
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(false);// Boolean类型字段是否移除is前缀处理
        autoGenerator.setTemplate(templateConfig);
        ConfigBuilder config = new ConfigBuilder(packageConfig, dataSourceConfig, strategyConfig, templateConfig, globalConfig);
        List<TableInfo> tableList = autoGenerator.getAllTableInfoList(config);
        String s = tableList.get(0).getFieldNames();
        // 要移除的字段
        List<String> fieldsToRemoveUpdate = Arrays.asList("is_delete", "status", "create_time", "update_time", "create_user_id", "update_user_id");;
        Predicate<String> filterPredicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return false;
            }
        };
        String conditionStr = globalConfig.getEntityName() == null?"":globalConfig.getEntityName();
        if(conditionStr.contains("AddDTO")){
            filterPredicate = item -> !fieldsToRemoveUpdate.contains(item);
        }

        // 处理字符串
        List<String> fieldsStr = Arrays.stream(s.split(",\\s*"))
                .map(String::trim)
                .filter(filterPredicate)
                .collect(Collectors.toList());
        List<TableField> tl = tableList.get(0).getFields().stream().filter(field->!fieldsToRemoveUpdate.contains(field.getName())).collect(Collectors.toList());
        config.getTableInfoList().get(0).setFields(tl);
        // 自定义配置：实现自定义代码生成
        Jedis finalJedis = jedis;
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                List<TableInfo> tableInfo = config.getTableInfoList();
                for (TableInfo tableInfo1 : tableInfo) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idType","ID_WORKER");
                    map.put("paramType",tableInfo1.getEntityName());
                    map.put("cnpkg", finalJedis.get("userOrAdmin"));
                    map.put("dto_comment","修改DTO");
                    map.put("mappingPre",finalJedis.get("userOrAdmin"));
                    this.setMap(map);
                }
            }
        };
        autoGenerator.setCfg(injectionConfig);

        autoGenerator.setConfig(config);

        // 使用Freemarker引擎生成代码
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        // 执行生成
        autoGenerator.execute();
    }

}
