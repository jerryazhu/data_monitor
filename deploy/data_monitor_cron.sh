#!/usr/bin/env bash
source /etc/profile
curl http://localhost/reg/get_reg_daily_activity
curl http://localhost/reg/get_reg_city/$(date '+%Y-%m')-1---$(date --date='yesterday' '+%Y-%m-%d')
curl http://localhost/reg/get_experience_city/$(date '+%Y-%m')-1---$(date --date='yesterday' '+%Y-%m-%d')---all
curl http://localhost/pay/get_pay_daily_activity
curl http://localhost/get_web_hot_spot
curl http://localhost/class_tools_daily_activity/2
curl http://localhost/class_tools_daily_activity/3
curl http://localhost/class_tools_daily_activity/7
curl http://localhost/class_mode_daily_activity/QQ
curl http://localhost/class_mode_daily_activity/Skype
curl http://localhost/class_mode_daily_activity/ClassPlat
curl http://localhost/student_daily_activity/Android
curl http://localhost/student_daily_activity/Web
curl http://localhost/student_daily_activity/iOS
curl http://localhost/student_daily_activity/ClassPlat
curl http://localhost/get_android_app
curl http://localhost/get_ios_app
curl http://localhost/get_android_system
curl http://localhost/get_ios_system
curl http://localhost/get_pc_app
curl http://localhost/get_pc_system
