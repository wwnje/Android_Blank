package orvnge.wwnje.com.fucknews.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//FakeNews
/**
 * 使用正则表达式验证输入格式
 * @author 邱万利
 * @date 2016-09-09
 */
public class myCheckTools {

    /**
     * 检查注册信息
     * name < 10
     * password < 10
     *desc < 40
     */

    /**
     * 检查是否是我们提供的格式
     * @param string
     * @return
     */
    public static boolean CHECK_IF_QRSCAN_CODE(String string){
        boolean if_ = false;

        String sub="MECARD:";
        int a=string.indexOf(sub);
        if(a>=0){
            if_ = true;
        }else{
            if_ = false;
        }
        return if_;
    }

    /**
    * 检查 email输入是否正确 
    * 正确的书写格 式为 username@gmail.com
    * @param  value  待验证邮箱
    * @param  length 邮箱最大长度
    * @return        正确与否
    */
    public static boolean checkEmail(String value, int length) {
        return value
        .matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*") 
        && value.length() <= length;  
    }  

    /**  
    * 检查电话输入 是否正确  
    * 正确格 式 012-87654321、0123-87654321、0123－7654321  
    * @param value  待验证电话号码  
    * @return       正确与否
    */  
    public static boolean checkTel(String value) {
        return value.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)");  
    }  

    /**  
    * 检查手机输入 是否正确  
    * @param value  待验证手机号码
    * @return       正确与否
    */  
    public static boolean checkMobile(String value) {
        return value.matches("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
    }

    /**  
    * 检查手机输入 是否正确  
    * @param value  待验证手机号码
    * @return       正确与否
    */  
    public boolean checkNumber(String value) {  
        return value.matches("^(\\d{9})$");
        //return value.matches("^[1][3,5]+\\d{9}") || value.matches("^[1][7][0,8]+\\d{8}") ||value.matches("^[1][8][0,3,9]+\\d{8}");  
    }


    /**  
    * 检查URL是 否合法  
    * @param link  URL字符串
    * @return       正确与否
    */  
    public static boolean checkURL(String link) {
        boolean if_ = false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
        Pattern patt = Pattern. compile(regex );
        Matcher matcher = patt.matcher(link);
        boolean isMatch = matcher.matches();
        if (!isMatch) {
            if_ = false;
        } else {
            if_ = true;
        }
        return if_;
    }

    /**  
    * 检查输入是否 超出规定长度 
    * @param length  
    * @param value  
    * @return       正确与否
    */  
    public static boolean CheckLength(String value, int length) {
        return ( (value == null || "".equals(value.trim()))
            ? 0 : value.length() ) <= length;  
    }

    /**
     * 检查学号
     * @param length
     * @param value
     * @return       正确与否
     * 9位
     */
    public static boolean checkNameID(String value, int length) {
        return ( (value == null || "".equals(value.trim()))
                ? 0 : value.length() ) == length;
    }

    /**
     * 是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}