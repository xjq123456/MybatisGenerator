package com.shanjupay.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.fasterxml.jackson.core.JsonProcessingException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MybatisPlusGenerator2Main {

    public static void main(String[] args) throws IOException {
            // 执行生成
            MybatisPlusGeneratorVo.getVo();
            MybatisPlusGeneratorAddDTO.getDto();
            MybatisPlusGeneratorUpdateDTO.getDto();
            MybatisPlusGeneratorPageDTO.getDto();
            Jedis jedis = new Jedis("127.0.0.1",6379);
            jedis.auth("rrdd9999");
            // 连接 Redis 服务器
            /*jedis = new Jedis("127.0.0.1", 6379);
                    jedis.auth("rrdd9999");*/

            // 代码生成器
            AutoGenerator autoGenerator = new AutoGenerator();

            // 全局配置
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
            globalConfig.setAuthor("xiongjunqiao");//
            //globalConfig.setEntityName("%s");
            globalConfig.setFileOverride(false);
            globalConfig.setOpen(true);
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
            TableFill ct = new TableFill("create_time", FieldFill.INSERT);
            TableFill ut = new TableFill("update_time", FieldFill.INSERT_UPDATE);
            TableFill cu = new TableFill("create_user_id", FieldFill.INSERT);
            TableFill uu = new TableFill("update_user_id", FieldFill.INSERT_UPDATE);
            final List<TableFill> tlist = new ArrayList<TableFill>();
            tlist.add(cu);
            tlist.add(uu);
            tlist.add(ct);
            tlist.add(ut);
            strategyConfig.setTableFillList(tlist);
            String tableNames = jedis.get("tableNames");
            /*ObjectMapper objectMapper = new ObjectMapper();
            String [] strings = objectMapper.readValue(tableNames,String[].class);*/
            strategyConfig.setInclude(tableNames.split(",")); // 需要生成的表名
            autoGenerator.setStrategy(strategyConfig);

            // 模板配置：自定义模板路径
            TemplateConfig templateConfig = new TemplateConfig();
            templateConfig.setXml(null); // 不生成 XML 文件

            //templateConfig.setEntity("templates/vo.java"); // 自定义 DTO 模板路径
            //strategyConfig.setEntityLombokModel(true);//实体类使用lombok
            strategyConfig.setEntityBooleanColumnRemoveIsPrefix(false);// Boolean类型字段是否移除is前缀处理
            templateConfig.setService("templates/service.java.ftl");
            autoGenerator.setTemplate(templateConfig);


            InjectionConfig injectionConfig = new InjectionConfig() {
                @Override
                public void initMap() {
                        List<TableInfo> tableInfo = this.getConfig().getTableInfoList();
                        for (TableInfo tableInfo1 : tableInfo) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("idType","ID_WORKER");
                                map.put("paramType",tableInfo1.getEntityName());
                                map.put("cnpkg",jedis.get("userOrAdmin"));
                                map.put("mappingPre",jedis.get("userOrAdmin"));
                                this.setMap(map);
                        }

                }
            };
            autoGenerator.setCfg(injectionConfig);

            // 使用Freemarker引擎生成代码
            autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future futureA = executorService.submit(()->{
                    autoGenerator.execute();
            });
            try {
                    futureA.get(); // 等待任务A完成
            } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
            }
            executorService.submit(()->{
                    Path sourceDir = Paths.get("D:\\person\\generator\\generator\\src\\main\\java\\com\\smt\\"+jedis.get("moudle"));
                    System.out.println(sourceDir);
                    Path destDir = Paths.get("D:\\person\\generator\\generator\\src\\main\\java\\com\\smt\\web");
                try {
                    Files.move(sourceDir,destDir, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            /*jedis.del("userOrAdmin");
            jedis.del("moudle");
            jedis.del("tableNames");*/
            executorService.shutdown();

        }



}
