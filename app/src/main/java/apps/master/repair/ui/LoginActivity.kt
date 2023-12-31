package apps.master.repair.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import apps.master.repair.R
import apps.master.repair.databinding.ActivityLoginBinding
import apps.master.repair.http.IndexViewModel
import com.app.toast.ToastX
import com.app.toast.expand.dp
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.exts.observeNonNull
import nearby.lib.base.uitl.AppManager
import nearby.lib.base.uitl.SPreUtil
import nearby.lib.mvvm.activity.BaseAppBVMActivity
import nearby.lib.mvvm.activity.BaseAppBindActivity
import nearby.lib.mvvm.activity.BaseBVMActivity


class LoginActivity : BaseAppBVMActivity<ActivityLoginBinding, IndexViewModel>() {


    override fun layoutRes(): Int {
        return R.layout.activity_login
    }


    override fun initialize(savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            go()
        }
        viewModel.login.observeNonNull(this) {
            if (!TextUtils.isEmpty(it.message)) {
                toast(it.message!!)
                return@observeNonNull
            }
            SPreUtil.put(this@LoginActivity,"id",it.id)
            SPreUtil.put(this@LoginActivity,"email",it.email.toString())
            SPreUtil.put(this@LoginActivity,"name",it.name.toString())
            SPreUtil.put(this@LoginActivity,"shoolName",it.shoolName.toString())
            SPreUtil.put(this@LoginActivity,"token",it.token.toString())
            SPreUtil.put(this@LoginActivity,"isLogin",true)
            println("進入主界面")
            navigate(MainActivity::class.java)
            AppManager.getInstance().finishAllActivity()
        }
    }

    private fun go() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        if (TextUtils.isEmpty(email)) {
            toast("請輸入郵件賬號")
            return
        }
        if (TextUtils.isEmpty(password)) {
            toast("請輸入密碼")
            return
        }
        viewModel.login(email, password)
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

    override fun createViewModel(): IndexViewModel {
        TODO("Not yet implemented")
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