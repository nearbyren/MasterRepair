package apps.master.repair.ui

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import apps.master.repair.R
import apps.master.repair.databinding.ActivityMainBinding
import apps.master.repair.fragment.IndexFragment1
import apps.master.repair.fragment.IndexFragment2
import apps.master.repair.http.IndexViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.navigation.NavigationBarView
import nearby.lib.base.uitl.AppManager
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.activity.BaseAppBVMActivity
import nearby.lib.mvvm.activity.BaseAppBindActivity
import nearby.lib.signal.livebus.LiveBus


class MainActivity : BaseAppBVMActivity<ActivityMainBinding, IndexViewModel>() {

    private val index1 by lazy { IndexFragment1() }
    private val index2 by lazy { IndexFragment2() }
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }


    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    private val onNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_item1 -> {
                    switchFragment(index1)
                    return@OnItemSelectedListener true
                }

                R.id.navigation_item2 -> {
                    switchFragment(index2)
                    return@OnItemSelectedListener true
                }
            }
            false
        }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        LiveBus.get("tab").observeForever {
            binding.bottomNavigation.menu.getItem(0).isChecked = true
            switchFragment(index1)
        }
        binding.bottomNavigation.background = ContextCompat.getDrawable(
            this,
            R.drawable.index_index_bg_white_main
        )
        binding.bottomNavigation.itemIconTintList = null

        binding.bottomNavigation.setOnItemSelectedListener(onNavigationItemSelectedListener)
        // 初始显示第一个 Fragment
        switchFragment(index1)
        initBadge()

    }

    private fun initBadge() {
        //获取底部菜单view
        val menuView = binding.bottomNavigation.getChildAt(0) as BottomNavigationMenuView
        //获取第2个itemView
        menuView?.forEach {
            if (it.id == R.id.navigation_item1) {
                val itemView = it as BottomNavigationItemView
                //引入badgeView
                val badgeView = layoutInflater.inflate(R.layout.menu_badge, menuView, false)
                val ivBadge = badgeView.findViewById<ImageView>(R.id.iv_badge)
                //把badgeView添加到itemView中
                itemView.addView(badgeView)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                this.finish()
                AppManager.getInstance().finishAllActivity()
            } else {
                ToastUtils.showToast("再按一次退出！")
                isExit = true
                Handler().postDelayed({
                    isExit = false
                }, 2000)

            }
            return true;
        }
        return super.onKeyDown(keyCode, event)
    }

    var isExit: Boolean = false
}