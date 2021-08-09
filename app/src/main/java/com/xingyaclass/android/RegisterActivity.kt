package xingya.example.xingya
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.xingyaclass.android.MD5Utils.md5
import com.xingyaclass.android.R


class RegisterActivity : AppCompatActivity() {
    private var tv_main_title //标题
            : TextView? = null
    private var tv_back: TextView? = null
    private var tv_register: TextView? = null
    private var tv_find_psw //返回键,显示的注册，找回密码
            : TextView? = null
    private var btn_login //登录按钮
            : Button? = null
    private var userName: String? = null
    private var psw: String? = null
    private var age: Int? =null //年龄
    private var gender: String? = null //性别
    private var email :String? = null//邮箱
    private var spPsw //获取的用户名，密码，加密密码
            : String? = null
    private var et_user_name: EditText? = null
    private var et_psw: EditText? = null
    private var et_age: EditText? = null
    private var et_gender: EditText? = null
    private var et_email: EditText? = null//编辑框

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //设置此界面为竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        init()
    }

    //获取界面控件
    private fun init() {
        //从main_title_bar中获取的id
        tv_main_title = findViewById(R.id.tv_main_title)
        tv_main_title?.setText("登录")
        tv_back = findViewById(R.id.tv_back)
        //从activity_login.xml中获取的
        tv_register = findViewById(R.id.tv_register)
        tv_find_psw = findViewById(R.id.tv_find_psw)
        btn_login = findViewById(R.id.btn_login)
        et_user_name = findViewById(R.id.et_user_name)
        et_psw = findViewById(R.id.et_psw)
        et_age=findViewById(R.id.et_age)
        et_gender=findViewById(R.id.et_gender)
        et_email=findViewById(R.id.et_email)
        //返回键的点击事件
        tv_back?.setOnClickListener(View.OnClickListener { //登录界面销毁
            finish()
        })
        //立即注册控件的点击事件
        tv_register?.setOnClickListener(View.OnClickListener { //为了跳转到注册界面，并实现注册功能
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivityForResult(intent, 1)
        })
        //找回密码控件的点击事件
        tv_find_psw?.setOnClickListener(View.OnClickListener {
            //跳转到找回密码界面（此页面暂未创建）
        })
        //登录按钮的点击事件
        btn_login?.setOnClickListener(View.OnClickListener {
            //开始登录，获取用户名和密码 getText().toString().trim();
            userName = et_user_name?.getText().toString().trim { it <= ' ' }
            psw = et_psw?.getText().toString().trim { it <= ' ' }
            //对当前用户输入的密码进行MD5加密再进行比对判断, MD5Utils.md5( ); psw 进行加密判断是否一致
            val md5Psw = md5(psw!!)
            // md5Psw ; spPsw 为 根据从SharedPreferences中用户名读取密码
            // 定义方法 readPsw为了读取用户名，得到密码
            spPsw = readPsw(userName!!)
            // TextUtils.isEmpty
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(this@LoginActivity, "请输入用户名", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(psw)) {
                Toast.makeText(this@LoginActivity, "请输入密码", Toast.LENGTH_SHORT).show()
                return@OnClickListener
                // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
            } else if (md5Psw == spPsw) {
                //一致登录成功
                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                saveLoginStatus(true, userName!!)
                //登录成功后关闭此页面进入主页
                val data = Intent()
                //datad.putExtra( ); name , value ;
                data.putExtra("isLogin", true)
                //RESULT_OK为Activity系统常量，状态码为-1
                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                setResult(RESULT_OK, data)
                //销毁登录界面
                finish()
                //跳转到主界面，登录成功的状态传递到 MainActivity 中
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                return@OnClickListener
            } else if (spPsw != null && !TextUtils.isEmpty(spPsw) && md5Psw != spPsw) {
                Toast.makeText(this@LoginActivity, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }else {
                Toast.makeText(this@LoginActivity, "此用户名不存在", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * 从SharedPreferences中根据用户名读取密码
     */
    private fun readPsw(userName: String): String? {
        //getSharedPreferences("loginInfo",MODE_PRIVATE);
        //"loginInfo",mode_private; MODE_PRIVATE表示可以继续写入
        val sp = getSharedPreferences("loginInfo", MODE_PRIVATE)
        //sp.getString() userName, "";
        return sp.getString(userName, "")
    }

    /**
     * 保存登录状态和登录用户名到SharedPreferences中
     */
    private fun saveLoginStatus(status: Boolean, userName: String) {
        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        val sp = getSharedPreferences("loginInfo", MODE_PRIVATE)
        //获取编辑器
        val editor = sp.edit()
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status)
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName)
        //提交修改
        editor.commit()
    }

    /**
     * 注册成功的数据返回至此
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    //显示数据， onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    //int requestCode , int resultCode , Intent data
    // LoginActivity -> startActivityForResult -> onActivityResult();
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            //是获取注册界面回传过来的用户名
            // getExtra().getString("***");
            val userName = data.getStringExtra("userName")
            if (!TextUtils.isEmpty(userName)) {
                //设置用户名到 et_user_name 控件
                et_user_name!!.setText(userName)
                //et_user_name控件的setSelection()方法来设置光标位置
                et_user_name!!.setSelection(userName!!.length)
            }
        }
    }
}