package com.coderstory.purify.module;

import android.content.ComponentName;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.coderstory.purify.utils.ConfigPreferences.getInstance;

public class HideApp extends XposedHelper implements IModule {
    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {

    }

    /**
     * bl.add(new ComponentName("com.android.vending", "com.android.vending.MarketWidgetProvider"));
     * @param loadPackageParam
     */
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equals("com.meizu.flyme.launcher")) {
            // bl.add(new ComponentName("com.android.vending", "com.android.vending.MarketWidgetProvider"));
            final String value = getInstance().getString("Hide_App_List", "");
            if (!value.equals("")) {
                final List<String> hideAppList = Arrays.asList(value.split(":"));
                XposedBridge.log("load config" + value);
                findAndHookMethod("com.meizu.flyme.launcher.cb", loadPackageParam.classLoader, "a", ComponentName.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        ComponentName componentName = (ComponentName) param.args[0];
                        if (hideAppList.contains(componentName.getPackageName())) {
                            param.setResult(true);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }
}
