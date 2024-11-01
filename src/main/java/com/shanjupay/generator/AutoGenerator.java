package com.shanjupay.generator;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoGenerator {
    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);
    protected ConfigBuilder config;
    protected InjectionConfig injectionConfig;
    private DataSourceConfig dataSource;
    private StrategyConfig strategy;
    private PackageConfig packageInfo;
    private TemplateConfig template;
    private GlobalConfig globalConfig;
    private AbstractTemplateEngine templateEngine;

    public void execute() {
        logger.debug("==========================准备生成文件...==========================");
        if (null == this.config) {
            //this.packageInfo.setModuleName("web");
            this.config = new ConfigBuilder(this.packageInfo, this.dataSource, this.strategy, this.template, this.globalConfig);

            if (null != this.injectionConfig) {
                this.injectionConfig.setConfig(this.config);
            }
        }

        if (null == this.templateEngine) {
            this.templateEngine = new VelocityTemplateEngine();
        }

        this.templateEngine.init(this.pretreatmentConfigBuilder(this.config)).mkdirs().batchOutput().open();
        logger.debug("==========================文件生成完成！！！==========================");
    }

    protected List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
        return config.getTableInfoList();
    }

    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        if (null != this.injectionConfig) {
            this.injectionConfig.initMap();
            config.setInjectionConfig(this.injectionConfig);
        }

        List<TableInfo> tableList = this.getAllTableInfoList(config);

        String entityName = tableList.get(0).getEntityName().substring(0);
        tableList.get(0).setEntityName(entityName);
        String controllerName = tableList.get(0).getControllerName().substring(0);
        tableList.get(0).setControllerName(controllerName);
        String serName = tableList.get(0).getServiceName().substring(0);
        tableList.get(0).setServiceName(serName);
        String serImplName = tableList.get(0).getServiceImplName().substring(0);
        tableList.get(0).setServiceImplName(serImplName);
        String mapperName = tableList.get(0).getMapperName().substring(0);
        tableList.get(0).setMapperName(mapperName);
        //tableList.get(0).setFields(fields);
        Iterator var3 = tableList.iterator();

        while(var3.hasNext()) {
            TableInfo tableInfo = (TableInfo)var3.next();
            if (config.getGlobalConfig().isActiveRecord()) {
                tableInfo.setImportPackages(Model.class.getCanonicalName());
            }

            if (tableInfo.isConvert()) {
                tableInfo.setImportPackages(TableName.class.getCanonicalName());
            }

            if (config.getStrategyConfig().getLogicDeleteFieldName() != null && tableInfo.isLogicDelete(config.getStrategyConfig().getLogicDeleteFieldName())) {
                tableInfo.setImportPackages(TableLogic.class.getCanonicalName());
            }

            if (StringUtils.isNotEmpty(config.getStrategyConfig().getVersionFieldName())) {
                tableInfo.setImportPackages(Version.class.getCanonicalName());
            }

            boolean importSerializable = true;
            if (StringUtils.isNotEmpty(config.getSuperEntityClass())) {
                tableInfo.setImportPackages(config.getSuperEntityClass());
                importSerializable = false;
            }

            if (config.getGlobalConfig().isActiveRecord()) {
                importSerializable = true;
            }

            if (importSerializable) {
                tableInfo.setImportPackages(Serializable.class.getCanonicalName());
            }

            if (config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()) {
                tableInfo.getFields().stream().filter((field) -> {
                    return "boolean".equalsIgnoreCase(field.getPropertyType());
                }).filter((field) -> {
                    return field.getPropertyName().startsWith("is");
                }).forEach((field) -> {
                    field.setConvert(true);
                    field.setPropertyName(StringUtils.removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                });
            }
        }

        return config.setTableInfoList(tableList);
    }

    public InjectionConfig getCfg() {
        return this.injectionConfig;
    }

    public AutoGenerator setCfg(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }

    public AutoGenerator() {
    }

    public ConfigBuilder getConfig() {
        return this.config;
    }

    public DataSourceConfig getDataSource() {
        return this.dataSource;
    }

    public StrategyConfig getStrategy() {
        return this.strategy;
    }

    public PackageConfig getPackageInfo() {
        return this.packageInfo;
    }

    public TemplateConfig getTemplate() {
        return this.template;
    }

    public GlobalConfig getGlobalConfig() {
        return this.globalConfig;
    }

    public AbstractTemplateEngine getTemplateEngine() {
        return this.templateEngine;
    }

    public AutoGenerator setConfig(final ConfigBuilder config) {
        this.config = config;
        return this;
    }

    public AutoGenerator setDataSource(final DataSourceConfig dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public AutoGenerator setStrategy(final StrategyConfig strategy) {
        this.strategy = strategy;
        return this;
    }

    public AutoGenerator setPackageInfo(final PackageConfig packageInfo) {
        this.packageInfo = packageInfo;
        return this;
    }

    public AutoGenerator setTemplate(final TemplateConfig template) {
        this.template = template;
        return this;
    }

    public AutoGenerator setGlobalConfig(final GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public AutoGenerator setTemplateEngine(final AbstractTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AutoGenerator)) {
            return false;
        } else {
            AutoGenerator other = (AutoGenerator)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label107: {
                    Object this$config = this.getConfig();
                    Object other$config = other.getConfig();
                    if (this$config == null) {
                        if (other$config == null) {
                            break label107;
                        }
                    } else if (this$config.equals(other$config)) {
                        break label107;
                    }

                    return false;
                }

                Object this$injectionConfig = this.injectionConfig;
                Object other$injectionConfig = other.injectionConfig;
                if (this$injectionConfig == null) {
                    if (other$injectionConfig != null) {
                        return false;
                    }
                } else if (!this$injectionConfig.equals(other$injectionConfig)) {
                    return false;
                }

                Object this$dataSource = this.getDataSource();
                Object other$dataSource = other.getDataSource();
                if (this$dataSource == null) {
                    if (other$dataSource != null) {
                        return false;
                    }
                } else if (!this$dataSource.equals(other$dataSource)) {
                    return false;
                }

                label86: {
                    Object this$strategy = this.getStrategy();
                    Object other$strategy = other.getStrategy();
                    if (this$strategy == null) {
                        if (other$strategy == null) {
                            break label86;
                        }
                    } else if (this$strategy.equals(other$strategy)) {
                        break label86;
                    }

                    return false;
                }

                label79: {
                    Object this$packageInfo = this.getPackageInfo();
                    Object other$packageInfo = other.getPackageInfo();
                    if (this$packageInfo == null) {
                        if (other$packageInfo == null) {
                            break label79;
                        }
                    } else if (this$packageInfo.equals(other$packageInfo)) {
                        break label79;
                    }

                    return false;
                }

                label72: {
                    Object this$template = this.getTemplate();
                    Object other$template = other.getTemplate();
                    if (this$template == null) {
                        if (other$template == null) {
                            break label72;
                        }
                    } else if (this$template.equals(other$template)) {
                        break label72;
                    }

                    return false;
                }

                Object this$globalConfig = this.getGlobalConfig();
                Object other$globalConfig = other.getGlobalConfig();
                if (this$globalConfig == null) {
                    if (other$globalConfig != null) {
                        return false;
                    }
                } else if (!this$globalConfig.equals(other$globalConfig)) {
                    return false;
                }

                Object this$templateEngine = this.getTemplateEngine();
                Object other$templateEngine = other.getTemplateEngine();
                if (this$templateEngine == null) {
                    if (other$templateEngine != null) {
                        return false;
                    }
                } else if (!this$templateEngine.equals(other$templateEngine)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AutoGenerator;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $config = this.getConfig();
        result = result * 59 + ($config == null ? 43 : $config.hashCode());
        Object $injectionConfig = this.injectionConfig;
        result = result * 59 + ($injectionConfig == null ? 43 : $injectionConfig.hashCode());
        Object $dataSource = this.getDataSource();
        result = result * 59 + ($dataSource == null ? 43 : $dataSource.hashCode());
        Object $strategy = this.getStrategy();
        result = result * 59 + ($strategy == null ? 43 : $strategy.hashCode());
        Object $packageInfo = this.getPackageInfo();
        result = result * 59 + ($packageInfo == null ? 43 : $packageInfo.hashCode());
        Object $template = this.getTemplate();
        result = result * 59 + ($template == null ? 43 : $template.hashCode());
        Object $globalConfig = this.getGlobalConfig();
        result = result * 59 + ($globalConfig == null ? 43 : $globalConfig.hashCode());
        Object $templateEngine = this.getTemplateEngine();
        result = result * 59 + ($templateEngine == null ? 43 : $templateEngine.hashCode());
        return result;
    }

    public String toString() {
        return "AutoGenerator(config=" + this.getConfig() + ", injectionConfig=" + this.injectionConfig + ", dataSource=" + this.getDataSource() + ", strategy=" + this.getStrategy() + ", packageInfo=" + this.getPackageInfo() + ", template=" + this.getTemplate() + ", globalConfig=" + this.getGlobalConfig() + ", templateEngine=" + this.getTemplateEngine() + ")";
    }
}

