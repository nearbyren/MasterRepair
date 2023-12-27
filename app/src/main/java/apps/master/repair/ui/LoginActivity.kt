package apps.master.repair.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import apps.master.repair.R
import apps.master.repair.databinding.ActivityLoginBinding
import com.app.toast.ToastX
import com.app.toast.expand.dp
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.mvvm.activity.BaseAppBindActivity


class LoginActivity : BaseAppBindActivity<ActivityLoginBinding>() {


    override fun layoutRes(): Int {
        return R.layout.activity_login
    }


    override fun initialize(savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            go()
        }
    }

    private fun go() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
//            if (TextUtils.isEmpty(email)) {
//                toast("請輸入郵件賬號")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(password)) {
//                toast("請輸入密碼")
//                return@setOnClickListener
//            }

//                val getEmail = SPreUtil[this@SignInLoginActivity, "email", ""]
//                val getPassword = SPreUtil[this@SignInLoginActivity, "password", ""]
//                if (email== getEmail && password == getPassword) {
        navigate(MainActivity::class.java)
        finishPage(SignInLoginActivity@ this)


    }


    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_back_left)
            .setBgColor(nearby.lib.base.R.color.white).setTitle(title = "").build()
    }

    private fun toast(text: String) {
        ToastX.with(this)
            .text(text) //文字
            .backgroundColor(nearby.lib.base.R.color.white) //背景
            .animationMode(ToastX.ANIM_MODEL_SLIDE) //动画模式 弹出或者渐变
            .textColor(nearby.lib.uikit.R.color.black) //文字颜色
            .position(ToastX.POSITION_TOP) //显示的位置 顶部或者底部
            .textGravity(Gravity.CENTER) //文字的位置
            .duration(1000) //显示的时间 单位ms
            .textSize(14f) //文字大小 单位sp
            .padding(10.dp, 10.dp) //左右内边距
            .margin(15.dp, 15.dp) //左右外边距
            .radius(10f.dp) //圆角半径
            .offset(44.dp) //距离顶部或者底部的偏移量
            .duration(2000)
            .show() //显示
    }

    override fun onDestroy() {
        hideSoftKeyboard()
        clearFocusFromEditText()
        super.onDestroy()
    }

    private fun clearFocusFromEditText() {
        val emailEt = binding.emailEt
        val passwordEt = binding.passwordEt
        emailEt.clearFocus()
        passwordEt.clearFocus()
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}