package cc.dkcms.cms.dao.utils;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

public class ReBuild {


    public static void main(String[] args) {

        // cc.dkcms.cms.dao 所使用的包名
        String modelPkg = "cc.dkcms.cms.dao";
// cc.dkcms.cms.dao 文件保存路径
        String modelDir = PathKit.getWebRootPath() + "/src/main/java/cc/dkcms/cms/dao/new/";


        // base cc.dkcms.cms.dao 所使用的包名
        String baseModelPkg = modelPkg + ".base";
// base cc.dkcms.cms.dao 文件保存路径
        String baseModelDir = modelDir + "/base";


        DruidPlugin druidPlugin = new DruidPlugin(
                "jdbc:mysql://127.0.0.1/xuewei",
                "root",
                "123456");
        druidPlugin.start();


        Generator gernerator = new Generator(druidPlugin.getDataSource(), baseModelPkg, baseModelDir, modelPkg, modelDir);
// 在 getter、setter 方法上生成字段备注内容
        gernerator.setGenerateRemarks(true);
        gernerator.generate();

    }
}
