package com.qa.data.visualization.controllers;

import com.qa.data.visualization.auth.entities.User;
import com.qa.data.visualization.auth.repositories.UserRepository;
import com.qa.data.visualization.entities.jishu.*;
import com.qa.data.visualization.repositories.jishu.*;
import com.qa.data.visualization.services.jishu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    private ClassToolsDailyActivityService classToolsDailyActivityService;
    @Autowired
    private ClassModeDailyActivityRepository classModeDailyActivityRepository;
    @Autowired
    private LastErrorRepository lastErrorRepository;
    @Autowired
    private LastDebugRepository lastDebugRepository;
    @Autowired
    private StuWebDailyActivityRepository stuWebDailyActiityRepository;
    @Autowired
    private StuPCDailyActivityService stuPCDailyActivityService;
    @Autowired
    private StuMobileDailyActivityRepository stuMobileDailyActivityRepository;
    @Autowired
    private StuTerminalRepository stuTerminalRepository;
    @Autowired
    private StuBrowserRepository stuBrowserRepository;
    @Autowired
    private WebActionService lastWebStuActionService;
    @Autowired
    private AndroidService androidService;
    @Autowired
    private IosService iosService;
    @Autowired
    private PCService pcService;
    @Autowired
    private StudentActivityAllRepository studentActivityAllRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/")
    String index(Model model) {
        long lastError = lastErrorRepository.count();
        model.addAttribute("lastError", lastError);
        long lastDebug = lastDebugRepository.count();
        model.addAttribute("lastDebug", lastDebug);
        model.addAttribute("templateName", "indexContent");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        model.addAttribute("username", name);
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            model.addAttribute(grantedAuthority.getAuthority(), true);
        }
        return "index";
    }

    @RequestMapping("overview/{templateName}")
    String overview(@PathVariable String templateName, Model model) {
        //return "fragments/"+templateName+" :: "+templateName; see https://github.com/dandelion/dandelion/issues/28
        if (templateName.contains("---")) {
            templateName = templateName.replaceAll("---", "/");
        }
        return templateName;
    }

    @RequestMapping("table/{templateName}")
    String index(@PathVariable String templateName, Model model) {
        //return "fragments/"+templateName+" :: "+templateName; see https://github.com/dandelion/dandelion/issues/28
        if (templateName.contains("---")) {
            templateName = templateName.replaceAll("---", "/");
        }
        return templateName;
    }

    @RequestMapping("/mainViews/settingsView")
    public String intoSettingsView(){
        return "fragments/settingsView";
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public boolean resetPassword(@RequestParam(value = "password", required = true) String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByUsername(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    @RequestMapping("class_tools_daily_activity/{type}")
    @ResponseBody
    @Cacheable(value = "class_tool_cache", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getClassToolDailyActivityByType(@PathVariable String type) {
        ArrayList<Object> list = new ArrayList<Object>();
        List<ClassToolsDailyActivity> classToolsDailyActivityList = classToolsDailyActivityService.getDailyActivitiesByType(type);
        for (ClassToolsDailyActivity da : classToolsDailyActivityList) {
            Object[] array = new Object[2];
            array[0] = Long.parseLong(da.getTime()) * 1000;
            array[1] = da.getCount();
            list.add(array);
        }
        return list;
    }

    @RequestMapping("class_mode_daily_activity/{mode}")
    @ResponseBody
    @Cacheable(value = "class_mode_cache", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getClassModeDailyActivityByType(@PathVariable String mode) throws ParseException {
        ArrayList<Object> list = new ArrayList<Object>();
        Iterable<ClassModeDailyActivity> classModeDailyActivityList = classModeDailyActivityRepository.findAll();
        for (ClassModeDailyActivity da : classModeDailyActivityList) {
            if (mode.equals("QQ")) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(da.getTime()).getTime();
                array[1] = da.getQQ();
                list.add(array);
            }
            if (mode.equals("Skype")) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(da.getTime()).getTime();
                array[1] = da.getSkype();
                list.add(array);
            }
            if (mode.equals("ClassPlat")) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(da.getTime()).getTime();
                array[1] = da.getClassPlat();
                list.add(array);
            }
            if (mode.equals("Total")) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(da.getTime()).getTime();
                array[1] = da.getTotal();
                list.add(array);
            }
        }
        return list;
    }

    @RequestMapping("student_daily_activity/{type}")
    @ResponseBody
    @Cacheable(value = "web_daily_activity_cache", keyGenerator = "wiselyKeyGenerator")
    public ArrayList getStuWebDailyActivityByType(@PathVariable String type) throws ParseException {
        ArrayList<Object> list = new ArrayList<Object>();
        if (type.equals("Web")) {
            List<StuWebDailyActivity> stuWebDailyActivityList = stuWebDailyActiityRepository.findWithMatchTime();
            for (StuWebDailyActivity da : stuWebDailyActivityList) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(da.getDay()).getTime();
                array[1] = da.getCount();
                list.add(array);
            }
        } else if (type.equals("ClassPlat")) {
            LinkedHashMap<String, String> stuPCDailyActivityMap = stuPCDailyActivityService.getDailyActivityMap();
            for (Map.Entry<String, String> entry : stuPCDailyActivityMap.entrySet()) {
                Object[] array = new Object[2];
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
                array[0] = dfm.parse(entry.getKey()).getTime();
                if (Long.parseLong(array[0].toString()) < System.currentTimeMillis()) {
                    array[1] = Integer.parseInt(entry.getValue());
                    list.add(array);
                }
            }
        } else if (type.equals("Android")) {
            List<StuMobileDailyActivity> stuMoblieDailyActivityList = stuMobileDailyActivityRepository.findByType(2);
            for (StuMobileDailyActivity da : stuMoblieDailyActivityList) {
                Object[] array = new Object[2];
                array[0] = da.getDay() * 1000;
                array[1] = da.getCount();
                list.add(array);
            }
        } else {
            List<StuMobileDailyActivity> stuMoblieDailyActivityList = stuMobileDailyActivityRepository.findByType(3);
            for (StuMobileDailyActivity da : stuMoblieDailyActivityList) {
                Object[] array = new Object[2];
                array[0] = da.getDay() * 1000;
                array[1] = da.getCount();
                list.add(array);
            }
        }
        return list;
    }

    @RequestMapping("student_daily_activity_all")
    @ResponseBody
    public ArrayList getStudentDailyActivityAll() throws ParseException {
        ArrayList<Object> list = new ArrayList<Object>();
        Iterable<StudentActivityAll> studentActivityAll = studentActivityAllRepository.findAll();
        for (StudentActivityAll saa : studentActivityAll) {
            Object[] array = new Object[2];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(saa.getDay());
            array[0] = date.getTime();
            array[1] = saa.getCount();
            list.add(array);
        }
        return list;
    }

    @RequestMapping("get_student_terminal")
    @ResponseBody
    public ArrayList getStuTerminal() {
        ArrayList<Object> list = new ArrayList<Object>();
        Iterable<StuTerminal> stuTerminalList = stuTerminalRepository.findAll();
        for (StuTerminal st : stuTerminalList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", st.getTerminal());
            map.put("count", st.getCnt());
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_student_browser")
    @ResponseBody
    public ArrayList getStuBrowser() {
        ArrayList<Object> list = new ArrayList<Object>();
        Iterable<StuBrowser> stuBrowserList = stuBrowserRepository.findAll();
        for (StuBrowser sb : stuBrowserList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", sb.getTerminal() + "___" + sb.getBrower());
            map.put("count", sb.getCnt());
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_web_hot_spot")
    @ResponseBody
    public ArrayList getWebHotSpot() throws ParseException {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> astWebStuActionMap = lastWebStuActionService.getHotSpot();
        for (Map.Entry<String, String> entry : astWebStuActionMap.entrySet()) {
            Object[] array = new Object[2];
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            array[0] = dfm.parse(entry.getKey()).getTime();
            if (Long.parseLong(array[0].toString()) < System.currentTimeMillis()) {
                array[1] = Integer.parseInt(entry.getValue());
                list.add(array);
            }
        }
        return list;
    }

    @RequestMapping("get_android_system")
    @ResponseBody
    public ArrayList getAndroidSystem() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> androidSystem = androidService.getAndroidSystem();
        for (Map.Entry<String, String> entry : androidSystem.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            String relSystem;
            if(entry.getKey().contains("-")){
                String[]cutData=entry.getKey().split("-");
                relSystem=cutData[0];
            }else {
                relSystem=entry.getKey();
            }
            map.put("name", entry.getKey().substring(0, 1) + relSystem);
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_android_app")
    @ResponseBody
    public ArrayList getAndroidApp() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> androidApp = androidService.getAndroidApp();
        for (Map.Entry<String, String> entry : androidApp.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey().substring(0, 1) + entry.getKey());
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_ios_system")
    @ResponseBody
    public ArrayList getIosSystem() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> iosSystem = iosService.getIosSystem();
        for (Map.Entry<String, String> entry : iosSystem.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            String[] bigSystemAll = entry.getKey().split(" ");
            String system = bigSystemAll[bigSystemAll.length - 1];
            String[] bigSystem = system.split("\\.");
            if (bigSystemAll.length > 2) {
                map.put("name", bigSystemAll[0] + " " + bigSystemAll[1] + " " + bigSystem[0] + "::" + entry.getKey());
            } else {
                map.put("name", bigSystemAll[0] + " " + bigSystem[0] + "::" + entry.getKey());
            }
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_ios_app")
    @ResponseBody
    public ArrayList getIosApp() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> iosApp = iosService.getIosApp();
        for (Map.Entry<String, String> entry : iosApp.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_pc_system")
    @ResponseBody
    public ArrayList getPcSystem() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> pcSystem = pcService.getPCSystem();
        for (Map.Entry<String, String> entry : pcSystem.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            String[] a = entry.getKey().split(" ");
            String b = a[1];
            for (int i = 2; i < a.length; i++) {
                b = b + a[i];
            }
            map.put("name", b);
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_pc_app")
    @ResponseBody
    public ArrayList getPcApp() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> pcApp = pcService.getPCApp();
        for (Map.Entry<String, String> entry : pcApp.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey().substring(0, 1) + entry.getKey());
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_tch_pc_system")
    @ResponseBody
    public ArrayList getTchPcSystem() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> tchPCSystem = pcService.getTchPCSystem();
        for (Map.Entry<String, String> entry : tchPCSystem.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            String[] a = entry.getKey().split(" ");
            String b = a[1];
            for (int i = 2; i < a.length; i++) {
                b = b + a[i];
            }
            map.put("name", b);
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }

    @RequestMapping("get_tch_pc_app")
    @ResponseBody
    public ArrayList getTchPcApp() {
        ArrayList<Object> list = new ArrayList<Object>();
        LinkedHashMap<String, String> tchPcApp = pcService.getTchPCApp();
        for (Map.Entry<String, String> entry : tchPcApp.entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey().substring(0, 1) + entry.getKey());
            map.put("count", Integer.parseInt(entry.getValue()));
            list.add(map);
        }
        return list;
    }
}
