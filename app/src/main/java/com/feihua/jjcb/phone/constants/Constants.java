package com.feihua.jjcb.phone.constants;

/**
 * Created by wcj on 2016-03-29.
 */
public class Constants
{

//    public static String MAIN_URL = "http://192.168.199.128:8090/fh_jjwater_service/";// 本地环境
//    public static String MAIN_URL = "http://192.168.199.149:8090/fh_jjwater_service/";// 测试环境
//    public static String MAIN_URL = "http://218.85.93.130:8280/fh_jjwater_service/";// 正式环境
//    public static String MAIN_URL = "http://218.207.198.197:1439/fh_jjwater_service/";// 晋江正式环境
    public static String MAIN_URL = "http://218.207.198.160:1439/fh_jjwater_service/";// 晋江测式环境  lihm 1
//    http://218.207.198.160:1439/
    public static final String LOGIN_URL = MAIN_URL + "user/login";//登入URL
    public static final String UPDATE_PHONE = MAIN_URL + "userbase/updatePhone";//修改用户手机号
    public static final String DOWN_TABLES = MAIN_URL + "volume/getVolume";//下载册号数据列表
    public static final String DOWN_TABLES_USERBASE = MAIN_URL + "volume/getUserbase";//下载抄表数据列表
    public static final String UPDATA_CBGL_DETAILSE = MAIN_URL + "volume/addWATERUSED";//抄表管理详情上传数据
    public static final String GET_ABNORMAL_TYPE = MAIN_URL + "volume/cbycTYPE";//异常上报类型获取
    public static final String UPLOAD_ABNORMAL_DATA_IMAGE = MAIN_URL + "UploadServlet";//异常上报图片上传
    public static final String UPLOAD_ABNORMAL_DATA_DETAILS = MAIN_URL + "volume/addExcreport";//异常上报详情数据上传
    public static final String UPDATA_CBGL_DETAILSE_ALL = MAIN_URL + "volume/uploadAll";//表册批量上传数据
    public static final String UPDATA_CBGL_VOLSCBS = MAIN_URL + "volume/updateVOLSCBS";//表册都已上传
    public static final String UPLOAD_LOCATION = MAIN_URL + "zb/zbUpload";//坐标
    public static final String USER_IS_KICK = MAIN_URL + "user/isKickOff";//用户Token是否正确
    public static final String USER_STATE = MAIN_URL + "zb/saveAppNowAndHis";//用户Token是否正确
    public static final String GET_ORG_NO = MAIN_URL + "userbase/getOrgNoAndTypeAndSffs";//获取区域用水性质收费方式
    public static final String GET_BK = MAIN_URL + "volume/getBK";//获取表况数据
    public static final String GET_SUPER_NO = MAIN_URL + "userbase/getOrgNoForSuperNo";//获取片区数据
    public static final String GET_TJCX_LIST = MAIN_URL + "userbase/getUserbaseList";//获取条件查询结果列表
    public static final String GET_TJCX_DETAILS = MAIN_URL + "userbase/getUserbaseForKH";//获取用户详情
    public static final String GET_QYCX_LIST = MAIN_URL + "userbase/getOwe";//获取区域查询列表
    public static final String GET_WATERPAY = MAIN_URL + "userbase/getWaterpay";//获取区域查询列表
    public static final String GET_DEBT = MAIN_URL + "debt/queryDebt";//获取用户欠费查询列表
    public static final String RESET_PASSWORD = MAIN_URL + "user/passwordModify";//更改密码
    public static final String GET_CBLIST = MAIN_URL + "debt/cbsjlist";//抄表情况
    public static final String GET_FEELIST = MAIN_URL + "debt/historylistPage";//水费情况
    public static final String GET_PAYLIST = MAIN_URL + "debt/datalistPagePAYHIS";//欠费情况
    public static final String CHECK_VERSING_CODE = MAIN_URL + "version/isUpdateAndroidApk";//检测是否需要升级
    public static final String GET_LOCATION = MAIN_URL + "userbase/getBz";//获取定位人员信息

    public static final String APK_NAME = "jjcb_phone.apk";
    public static final String SETTING_DB_NAME = "com.feihua.jjcb.phone.sp";
    public static final String LAST_FRAGMENT = "lastfragment";

    public static final String Remember_Pw = "remember_pw";// 记住密码
    public static final String IS_LOGIN = "is_login";// 是否已经登入过
    public static final String USERNAME = "username";// 用户名
    public static final String PASSWORD = "password";// 密码
    public static final String USET_TOKEN = "user_token";// 用户识别码
    public static final String USER_ID = "user_id";// 用户id
    public static final String NAME = "name";// 用户昵称
    public static final String STATUS = "status";// 用户状态
    public static final String DEPT_ID = "dept";// 部门名称
    public static final String ROLE_ID = "role";// 用户角色
    public static final String PHONE = "phone";// 用户手机号
    public static final String ORG_NO = "org_no";// 公司名称
    public static final String POSITION = "position";// 员工职位
    public static final String IS_RELOGIN = "is_relogin";// 是否重新登录
    public static final String LOGIN_TOKENS = "login_tokens";// 用户唯一识别码
    public static final String LOCATION_INFO = "location_info";// 用户定位信息

    public static final String SEARCH_HISTORY = "search_history";//搜索历史

    public static final int CBGL_CHAO_BIAO_LIST_RECEIVER = 0x001;//抄表管理列表请求码
    public static final int CBGL_BIAO_CE_LIST_RECEIVER = 0x002;//抄表管理列表请求码

    public static final String CBGL_CHAO_BIAO_ACTION = "com.feihua.jjcb.phone.cbgl.chaobiao.action";//抄表管理列表广播
    public static final String CBGL_BIAO_CE_ACTION = "com.feihua.jjcb.phone.cbgl.biaoce.action";//表册广播

    public static final String ACTION_MULTIPLE = "WCJ_ACTION_MULTIPLE_PICK";
    public static final String FILEPATH_DIR = "/Jj/Camera/";
    public static final String FILEPATHSMALL_DIR = "/Jj/compressedPic/";
    public static final int UPLOADPHOTO_CAMERA = 0;// 选择相机

    public static final String TARGETSERVICE_DESTORY = "com.feihua.jjcb.service.destroy";
    public static final String TRACESERVICE_DESTORY = "com.feihua.jjcb.traceservice.destroy";
    public static final String LOGINTOKEN_ERROR_SERVICE = "com.feihua.jjcb.service.logintoken";


}
