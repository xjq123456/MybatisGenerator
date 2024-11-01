package com.shanjupay.generator;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import redis.clients.jedis.Jedis;

import java.util.*;

public class MybatisPlusGeneratorVo {

        public static void getVo() {
            Jedis jedis = new Jedis("175.178.126.194");
            // 连接 Redis 服务器
            jedis = new Jedis("127.0.0.1", 6379);
            jedis.auth("rrdd9999");
            System.out.println("连接成功");
            // 代码生成器
            AutoGenerator autoGenerator = new AutoGenerator();

            // 全局配置
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
            globalConfig.setAuthor("xiongjunqiao");
            globalConfig.setEntityName("%s");
            globalConfig.setFileOverride(false);
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
            Scanner scanner = new Scanner(System.in);
            System.out.println("controller需要包，请输入admin或者user：");

            String userOrAdmin = scanner.next();
            jedis.set("userOrAdmin",userOrAdmin);
            packageConfig.setController("controller."+userOrAdmin);
            System.out.println("生成模块，请输入模块名或者数据库表的第一个前缀：");
            String moudle = scanner.next();
            jedis.set("moudle",moudle);
            packageConfig.setModuleName(moudle);
            autoGenerator.setPackageInfo(packageConfig);

            // 策略配置
            StrategyConfig strategyConfig = new StrategyConfig();
            strategyConfig.setTablePrefix(packageConfig.getModuleName() + "_");
            strategyConfig.setNaming(NamingStrategy.underline_to_camel);//表名映射到实体策略，带下划线的转成驼峰
            strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);//列名映射到类型属性策略，带下划线的转成驼峰
            // strategyConfig.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
            strategyConfig.setEntityLombokModel(true);//实体类使用lombok

            System.out.println("请输入表名,如需多个表同时生成，请用英文逗号分隔两个表名：");
            String input = scanner.next();
            String[] inputs = input.split(",");
            List<String> tableNames = new ArrayList<>();
            for (String tableName : inputs) {
                tableNames.add(tableName.trim()); // 去除每个表名前后的空格
            }
            System.out.println("ssssssssssssssssssssssssssssss:"+tableNames.toString());
            jedis.set("tableNames", String.join(",", tableNames));
            strategyConfig.setInclude(tableNames.toArray(new String[0])); // 需要生成的表名
            autoGenerator.setStrategy(strategyConfig);
            // 自定义配置：实现自定义代码生成
            Jedis finalJedis = jedis;
            InjectionConfig injectionConfig = new InjectionConfig() {
                @Override
                public void initMap() {
                    List<TableInfo> tableInfo = getConfig().getTableInfoList();
                    for (TableInfo tableInfo1 : tableInfo) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("idType","ID_WORKER");
                        map.put("cnpkg", finalJedis.get("userOrAdmin"));
                        map.put("paramType",tableInfo1.getEntityName().split("VO")[0]);
                        map.put("mappingPre",finalJedis.get("userOrAdmin"));
                        this.setMap(map);
                    }
                    // 自定义配置，可以在模板中使用
                }
            };
            // 模板配置：自定义模板路径
            TemplateConfig templateConfig = new TemplateConfig();
            templateConfig.setXml(null); // 不生成 XML 文件
            //templateConfig.setEntity("templates/vo.java"); // 自定义 DTO 模板路径
            strategyConfig.setEntityLombokModel(true);//实体类使用lombok
            templateConfig.setEntity("templates/vo.java"); // 自定义 DTO 模板路径
            packageConfig.setEntity("vo");
            globalConfig.setSwagger2(true);
            globalConfig.setEntityName("%sVO");
            strategyConfig.setEntityBooleanColumnRemoveIsPrefix(false);// Boolean类型字段是否移除is前缀处理
            autoGenerator.setTemplate(templateConfig);


            autoGenerator.setCfg(injectionConfig);

            // 使用Freemarker引擎生成代码
            autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

            // 执行生成
            autoGenerator.execute();
            globalConfig.setSwagger2(true);
            globalConfig.setEntityName("%s");
            //packageConfig.setEntity("vo");
            globalConfig.setEntityName("%s");
        }

}
